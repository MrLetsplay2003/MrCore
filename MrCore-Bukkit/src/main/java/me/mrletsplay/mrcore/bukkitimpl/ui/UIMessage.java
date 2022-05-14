package me.mrletsplay.mrcore.bukkitimpl.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.BaseComponent;

public class UIMessage {
	
	private List<Object> messageElements;
	private UIInstance instance;
	
	public UIMessage(UIInstance instance) {
		this.messageElements = new ArrayList<>();
		this.instance = instance;
	}
	
	/**
	 * Builds this message into a list of {@link BaseComponent} arrays
	 * @return The built message
	 */
	public List<BaseComponent[]> buildMessage() {
		List<BaseComponent[]> lines = new ArrayList<>();
		List<BaseComponent> line = new ArrayList<>();
		for(Object o : messageElements) {
			if(o instanceof BaseComponent) {
				line.add((BaseComponent) o);
			}else if(o instanceof BaseComponent[]) {
				line.addAll(Arrays.asList((BaseComponent[]) o));
			}else if(o instanceof UILayoutElement) {
				UILayoutElement el = (UILayoutElement) o;
				if(el.getType().equals(UILayoutElementType.NEWLINE)) {
					lines.add(line.toArray(new BaseComponent[line.size()]));
					line = new ArrayList<>();
				}
			}
		}
		lines.add(line.toArray(new BaseComponent[line.size()]));
		return lines;
	}
	
	public List<Object> getMessageElements() {
		return messageElements;
	}
	
	/**
	 * Builds this message using {@link #buildMessage()} and sends it to the specified player
	 * @param p The player to send the message to
	 */
	public void sendToPlayer(Player p) {
		buildMessage().forEach(p.spigot()::sendMessage);
	}
	
	/**
	 * @return The {@link UIInstance} object this message corresponds to
	 */
	public UIInstance getInstance() {
		return instance;
	}
	
}
