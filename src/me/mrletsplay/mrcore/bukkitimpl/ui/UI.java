package me.mrletsplay.mrcore.bukkitimpl.ui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import me.mrletsplay.mrcore.bukkitimpl.ui.event.UIBuildEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * This class represents a UI which can be built into a chat message for specific players
 * @author MrLetsplay2003
 */
public class UI {
	
	private static long lastID = 0;

	private UIBuilder builder;
	public String id;
	private long lInsID = 0;
	
	public UI(UIBuilder builder) {
		this.builder = builder;
		this.id = ""+(lastID++);
		UIListener.uis.add(this);
	}
	
	/**
	 * Creates the UI for a specific player and parses it into a chat message
	 * @param p The player to get this UI for/to be passed to all underlying functions
	 * @return An {@link UIMessage} instance which can be sent to a player
	 */
	public UIMessage getForPlayer(Player p) {
		Map<String, UIElement> elements = new HashMap<>();
		builder.getElements().forEach(elements::put);
		String instanceID = id+"_"+(lInsID++);
		Map<String, Object> props = new HashMap<>(builder.getProperties());
		props.put("elements", elements);
		UIInstance instance = new UIInstance(instanceID, this, props, p);
		UIMessage uiMessage = new UIMessage(instance);
		UIBuildEvent e = new UIBuildEvent(instance, p);
		for(UILayoutElement el : builder.getLayout().getElements()) {
			switch(el.getType()) {
				case TEXT:
					uiMessage.getMessageElements().add(new TextComponent((String) el.getProperty("text")));
					break;
				case ELEMENT:
					String elementID = (String) el.getProperty("element");
					UIElement element = builder.getElements().get(elementID);
					if(element == null) break;
					uiMessage.getMessageElements().add(UILayoutParser.parseElement(e, element, instanceID, elementID));
					break;
				case NEWLINE:
					uiMessage.getMessageElements().add(el);
					break;
				default:
					uiMessage.getMessageElements().add(el);
					break;
			}
		}
		UIListener.instances.put(instanceID, instance);
		return uiMessage;
	}
	
	/**
	 * @return This UI's (unique) id
	 */
	public String getID() {
		return id;
	}
	
	/**
	 * @return This UI's builder
	 */
	public UIBuilder getBuilder() {
		return builder;
	}
	
	/**
	 * Builds the UI into a chat message using {@link #getForPlayer(Player)} and sends it if the return result isn't null
	 * @param p The player to build the UI with
	 * @return The UIMessage instance returned by {@link #getForPlayer(Player)}
	 * @see UI#getForPlayer(Player)
	 */
	public UIMessage sendToPlayer(Player p) {
		UIMessage uim = getForPlayer(p);
		if(uim==null) return null;
		uim.sendToPlayer(p);
		return uim;
	}
	
}
