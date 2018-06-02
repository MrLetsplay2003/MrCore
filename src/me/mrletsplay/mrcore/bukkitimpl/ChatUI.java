package me.mrletsplay.mrcore.bukkitimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatUI {
	
	private static long lastID = 0;

	public static class UIBuilder {
		
		public HashMap<String, UIElement> elements;
		public UILayout layout;
		private HashMap<String, Object> properties;
		
		/**
		 * Constructs a UIBuilder
		 * @see <a href="https://github.com/MrLetsplay2003/MrCore/wiki/ChatUI">ChatUI wiki</a>
		 */
		public UIBuilder() {
			elements = new HashMap<>();
			properties = new HashMap<>();
			layout = new UILayout();
		}

		/**
		 * Adds an element to the UI
		 * @param key The key of the element. This should be a unique value
		 * @param element The {@link UIElement} to add under that key
		 * @return This UIBuilder instance
		 */
		public UIBuilder addElement(String key, UIElement element) {
			elements.put(key, element);
			return this;
		}
		
		/**
		 * Sets the UILayout for the UI
		 * @param layout The {@link UILayout} to use
		 * @return This UIBuilder instance
		 */
		public UIBuilder setLayout(UILayout layout) {
			this.layout = layout;
			return this;
		}
		
		/**
		 * Sets the default properties for this UI<br>
		 * For more info, please see the <a href="https://github.com/MrLetsplay2003/MrCore/wiki/ChatUI">ChatUI wiki</a>
		 * @param properties The property map to use
		 * @return This UIBuilder instance
		 */
		public UIBuilder setProperties(HashMap<String, Object> properties) {
			this.properties = properties;
			return this;
		}
		
		/**
		 * Builds this UIBuilder into a {@link UI} instance
		 * @return
		 */
		public UI build() {
			return new UI(this);
		}
		
	}

	public static class UIBuilderMultiPage<T> extends UIBuilder{
		
		private ItemSupplier<T> supplier;
		
		/**
		 * Constructs a UIBuilderMultiPage instance
		 * @see <a href="https://github.com/MrLetsplay2003/MrCore/wiki/ChatUI">ChatUI wiki</a>
		 */
		public UIBuilderMultiPage() {
			super();
		}
		
		@Override
		public UIBuilderMultiPage<T> addElement(String key, UIElement element) {
			super.addElement(key, element);
			return this;
		}
		
		/**
		 * @deprecated Use {@link #addNextPageElement(String, UIElement)} instead
		 */
		@Deprecated
		public UIBuilderMultiPage<T> addNextPageElement(String key, String layout){
			return addElement(key, new StaticUIElement(layout).setAction(new UIElementAction() {
				
				@Override
				public void action(UIElementActionEvent event) {
					int page = (int) event.getUIInstance().getProperties().get("page");
					((UIMultiPage<?>) event.getUIInstance().getUI()).getForPlayer(event.getPlayer(), page-1);
				}
			}));
		}
		
		/**
		 * Adds a "next page" element to the UI<br>
		 * This will have the default action of switching to the page after the current one<br>
		 * If there is no page before the current one, the click will be ignored
		 * @param key The element key
		 * @param layout The layout string to use
		 * @return This UIBuilderMultiPage instance
		 * @see {@link UIBuilder#addElement(String, UIElement)}
		 */
		public UIBuilderMultiPage<T> addNextPageElement(String key, UIElement layout){
			return addElement(key, layout.setAction(new UIElementAction() {
				
				@Override
				public void action(UIElementActionEvent event) {
					int page = (int) event.getUIInstance().getProperties().get("page");
					((UIMultiPage<?>) event.getUIInstance().getUI()).getForPlayer(event.getPlayer(), page-1);
				}
			}));
		}

		/**
		 * @deprecated Use {@link #addPreviousPageElement(String, UIElement)} instead
		 */
		@Deprecated
		public UIBuilderMultiPage<T> addPreviousPageElement(String key, String layout){
			return addElement(key, new StaticUIElement(layout).setAction(new UIElementAction() {
				
				@Override
				public void action(UIElementActionEvent event) {
					int page = (int) event.getUIInstance().getProperties().get("page");
					((UIMultiPage<?>) event.getUIInstance().getUI()).getForPlayer(event.getPlayer(), page-1);
				}
			}));
		}

		/**
		 * Adds a "next page" element to the UI<br>
		 * This will have the default action of switching to the page before the current one<br>
		 * If there is no page before the current one, the click will be ignored
		 * @param key The element key
		 * @param layout The layout to use
		 * @return This UIBuilderMultiPage instance
		 * @see {@link UIBuilder#addElement(String, UIElement)}
		 */
		public UIBuilderMultiPage<T> addPreviousPageElement(String key, UIElement layout){
			return addElement(key, layout.setAction(new UIElementAction() {
				
				@Override
				public void action(UIElementActionEvent event) {
					int page = (int) event.getUIInstance().getProperties().get("page");
					((UIMultiPage<?>) event.getUIInstance().getUI()).getForPlayer(event.getPlayer(), page-1);
				}
			}));
		}
		
		/**
		 * This method will not work with UIBuilderMultiPage instances and will throw an exception<br>
		 * Use {@link UIBuilderMultiPage#setLayout(UILayoutMultiPage)} instead
		 */
		@Deprecated
		@Override
		public UIBuilder setLayout(UILayout layout) {
			throw new UnsupportedOperationException("Use setLayout(UILayoutMultiPage) instead");
		}

		/**
		 * Sets the UILayoutMultiPage for the UI
		 * @param layout The {@link UILayoutMultiPage} to use
		 * @return This UIBuilderMultiPage instance
		 */
		public UIBuilderMultiPage<T> setLayout(UILayoutMultiPage layout) {
			this.layout = layout;
			return this;
		}
		
		/**
		 * Sets the supplier to use for gathering multipage items
		 * @param supplier The supplier to use
		 * @return This UIBuilderMultiPage instance
		 */
		public UIBuilderMultiPage<T> setSupplier(ItemSupplier<T> supplier) {
			this.supplier = supplier;
			return this;
		}

		@Override
		public UIBuilderMultiPage<T> setProperties(HashMap<String, Object> properties) {
			super.setProperties(properties);
			return this;
		}
		
		@Override
		public UIMultiPage<T> build() {
			return new UIMultiPage<T>(this);
		}
		
	}
	
	public static abstract class ItemSupplier<T> {
		
		public abstract List<T> getItems();
		
		public abstract UIElement toUIElement(Player p, T item);
		
	}
	
	/**
	 * This class is used by UIs to define a basic, flexible layout which will then be parsed into the final chat message<br>
	 * This class is only available to the UIBuilder/UI (single page)
	 * @author MrLetsplay2003
	 */
	public static class UILayout {
		
		public List<UILayoutElement> elements;
		
		public UILayout() {
			this.elements = new ArrayList<>();
		}
		
		/**
		 * Adds plain text to the layout<br>
		 * This will not be altered in the final message
		 * @param text The text to add
		 * @return This UILayout instance
		 */
		public UILayout addText(String text) {
			elements.add(new UILayoutElement(UILayoutElementType.TEXT).setProperty("text", text));
			return this;
		}
		
		/**
		 * Adds an element to the layout<br>
		 * This will be replaced with the layout of the element defined by the key<br>
		 * If no element with that key is defined, it will not be replaced
		 * @param element The element's key
		 * @return This UILayout instance
		 */
		public UILayout addElement(String element) {
			elements.add(new UILayoutElement(UILayoutElementType.ELEMENT).setProperty("element", element));
			return this;
		}
		
		/**
		 * Adds a new line to the message
		 * @return This UILayout instance
		 */
		public UILayout newLine() {
			elements.add(new UILayoutElement(UILayoutElementType.NEWLINE));
			return this;
		}
		
	}

	/**
	 * This class is used by UIMultiPages to define a basic, flexible layout which will then be parsed into the final chat message<br>
	 * This class is only available to the UIBuilderMultiPage/UIMultiPage
	 * @author MrLetsplay2003
	 */
	public static class UILayoutMultiPage extends UILayout {
		
		private int nItems = 0;
		
		@Override
		public UILayoutMultiPage addText(String text) {
			super.addText(text);
			return this;
		}
		
		@Override
		public UILayoutMultiPage addElement(String element) {
			super.addElement(element);
			return this;
		}
		
		@Override
		public UILayoutMultiPage newLine() {
			super.newLine();
			return this;
		}
		
		/**
		 * Adds multi page elements<br>
		 * These will later be replaced by the multi page element's layouts
		 * @param count The amount of elements to add
		 * @param includeNewLines Whether a {@link UILayoutMultiPage#newLine()} should be added before each element
		 * @return This UILayoutMultiPage instance
		 */
		public UILayoutMultiPage addPageElements(int count, boolean includeNewLines) {
			nItems += count;
			for(int i = 0; i < count; i++) {
				elements.add(new UILayoutElement(UILayoutElementType.MULTIPAGE_PAGE_ELEMENT).setProperty("newline", includeNewLines));
			}
			return this;
		}
		
	}
	
	private static class UILayoutElement {
		
		private UILayoutElementType type;
		private HashMap<String, Object> properties;
		
		public UILayoutElement(UILayoutElementType type) {
			this.type = type;
			this.properties = new HashMap<>();
		}
		
		public UILayoutElement setProperty(String key, Object value) {
			properties.put(key, value);
			return this;
		}
		
		public Object getProperty(String key) {
			return properties.get(key);
		}
		
	}
	
	private static enum UILayoutElementType {
		
		TEXT,
		NEWLINE,
		ELEMENT,
		MULTIPAGE_PAGE_ELEMENT;
		
	}
	
	/**
	 * This class represents a flexible, interactive UI element whose layout is dependent on the player (or any other own factors)
	 * @author MrLetsplay2003
	 */
	public static abstract class UIElement {
		
		private UIElementAction action;
		private String hoverText;
		
		public abstract BaseComponent getLayout(Player p);
		
		/**
		 * Sets the action for this UIElement<br>
		 * The action will be called when this element is clicked in chat
		 * @param action The action to use
		 * @return This UIElement instance
		 */
		public UIElement setAction(UIElementAction action) {
			this.action = action;
			return this;
		}
		
		/**
		 * Sets the hover text to be displayed when the player hovers over it in chat
		 * @param hoverText The hover text to use
		 * @return This UIElement instance
		 */
		public UIElement setHoverText(String hoverText) {
			this.hoverText = hoverText;
			return this;
		}
		
	}
	
	/**
	 * This class represents an interactive UI element whose layout is fixed and will not change
	 * @author MrLetsplay2003
	 */
	public static class StaticUIElement extends UIElement {
		
		private BaseComponent layout;
		
		/**
		 * Constructs a UI element
		 * @param text The static text layout to use
		 * @see {@link StaticUIElement}
		 */
		public StaticUIElement(String text) {
			this.layout = new TextComponent(text);
		}
		
		/**
		 * Constructs a UI element
		 * @param layout The static layout to use
		 * @see {@link StaticUIElement}
		 */
		public StaticUIElement(BaseComponent layout) {
			this.layout = layout;
		}
		
		@Override
		public BaseComponent getLayout(Player p) {
			return layout;
		}
		
		@Override
		public StaticUIElement setAction(UIElementAction action) {
			super.setAction(action);
			return this;
		}
		
		@Override
		public StaticUIElement setHoverText(String hoverText) {
			super.setHoverText(hoverText);
			return this;
		}
		
	}
	
	/**
	 * This class is used to add actions to ui elements when you click on them
	 * @author MrLetsplay2003
	 */
	public static abstract class UIElementAction {
		
		public abstract void action(UIElementActionEvent event);
		
	}
	
	/**
	 * This is the event fired to the {@link UIElementAction#action(UIElementActionEvent)} when the corresponding element is clicked
	 * @author MrLetsplay2003
	 */
	public static class UIElementActionEvent {
		
		private UIInstance ui;
		private Player p;
		
		public UIElementActionEvent(UIInstance ui, Player p) {
			this.ui = ui;
			this.p = p;
		}
		
		public UIInstance getUIInstance() {
			return ui;
		}
		
		public Player getPlayer() {
			return p;
		}
		
	}
	
	/**
	 * This class represents one instance (one chat message) of an UI for a specific player<br>
	 * It also contains an instance-specific property HashMap containing the UI elements, which can also be used to store extra data
	 * @author MrLetsplay2003
	 */
	public static class UIInstance {
		
		private String id;
		private Player player;
		private HashMap<String, Object> properties;
		private UI ui;
		
		public UIInstance(String id, UI ui, HashMap<String, Object> properties, Player player) {
			this.id = id;
			this.ui = ui;
			this.properties = properties;
			this.player = player;
		}
		
		/**
		 * @return This instance's (unique) instance id
		 */
		public String getID() {
			return id;
		}
		
		/**
		 * @return This instance's property HashMap
		 */
		public HashMap<String, Object> getProperties() {
			return properties;
		}
		
		/**
		 * @return The UI this is an instance of
		 */
		public UI getUI() {
			return ui;
		}
		
		/**
		 * @return The player this instance was created for/who was passed in the {@link UI#getForPlayer(Player)} function
		 */
		public Player getPlayer() {
			return player;
		}
		
	}
	
	/**
	 * This class represents a UI which can be built into a chat message for specific players
	 * @author MrLetsplay2003
	 */
	public static class UI {

		private UIBuilder builder;
		public String id;
		private long lInsID = 0;
		
		private UI(UIBuilder builder) {
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
			HashMap<String, UIElement> elements = new HashMap<>();
			builder.elements.forEach(elements::put);
			String instanceID = id+"_"+(lInsID++);
			HashMap<String, Object> props = new HashMap<>(builder.properties);
			props.put("elements", elements);
			UIInstance instance = new UIInstance(instanceID, this, props, p);
			UIMessage uiMessage = new UIMessage(instance);
			for(UILayoutElement el : builder.layout.elements) {
				switch(el.type) {
					case TEXT:
						uiMessage.messageElements.add(new TextComponent((String) el.getProperty("text")));
						break;
					case ELEMENT:
						String elementID = (String) el.getProperty("element");
						UIElement element = builder.elements.get(elementID);
						if(element == null) break;
						uiMessage.messageElements.add(UILayoutParser.parseElement(element, p, instanceID, elementID));
						break;
					case NEWLINE:
						uiMessage.messageElements.add(el);
						break;
					default:
						uiMessage.messageElements.add(el);
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
		 * @see {@link UI#getForPlayer(Player)}
		 */
		public UIMessage sendToPlayer(Player p) {
			UIMessage uim = getForPlayer(p);
			if(uim==null) return null;
			uim.sendToPlayer(p);
			return uim;
		}
		
	}
	
	public static class UIMultiPage<T> extends UI {

		private UIBuilderMultiPage<T> builder;
		
		private UIMultiPage(UIBuilderMultiPage<T> builder) {
			super(builder);
			this.builder = builder;
		}
		
		/**
		 * Returns the first page (page 0) of this UI using {@link #getForPlayer(Player, int)}
		 */
		@Override
		public UIMessage getForPlayer(Player p) {
			return getForPlayer(p, 0);
		}
		
		@SuppressWarnings("unchecked")
		public UIMessage getForPlayer(Player p, int page) {
			long lElID = 0;
			UIMessage uiMessage = super.getForPlayer(p);
			String instanceID = uiMessage.instance.id;
			List<T> items = builder.supplier.getItems();
			UILayoutMultiPage lmp = (UILayoutMultiPage) builder.layout;
			int nPg = items.size()/lmp.nItems;
			if(page > nPg || page < 0) return null;
			HashMap<String, Object> props = uiMessage.instance.properties;
			props.put("page", page);
			HashMap<String, UIElement> elements = (HashMap<String, UIElement>) props.get("elements");
			int index = page * lmp.nItems;
			for(int i = 0; i < uiMessage.messageElements.size();  i++) {
				Object msgEl = uiMessage.messageElements.get(i);
				if(msgEl instanceof UILayoutElement) {
					UILayoutElement lEl = (UILayoutElement) msgEl;
					switch(lEl.type) {
						case MULTIPAGE_PAGE_ELEMENT:
							if(index >= items.size()) {
								uiMessage.messageElements.remove(i);
								continue;
							}
							UIElement el = builder.supplier.toUIElement(p, items.get(index++));
							String elID = "multi_"+(lElID++);
							elements.put(elID, el);
							uiMessage.messageElements.set(i, UILayoutParser.parseElement(el, p, instanceID, elID));
							boolean nL = (boolean) lEl.getProperty("newline");
							if(nL) uiMessage.messageElements.add(i, new UILayoutElement(UILayoutElementType.NEWLINE));
						default:
							break;
					}
				}
			}
			uiMessage.instance.properties.put("elements", elements);
			return uiMessage;
		}
		
		@Override
		public UIBuilderMultiPage<T> getBuilder() {
			return builder;
		}
		
	}
	
	private static class UILayoutParser {
		
		public static BaseComponent parseElement(UIElement element, Player p, String instanceID, String elementID) {
			BaseComponent tc = element.getLayout(p);
			if(element.hoverText != null)
				tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(element.hoverText).create()));
			if(element.action != null)
				tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mrcoreui "+instanceID+" "+elementID));
			return tc;
		}
		
	}
	
	public static class UIMessage {
		
		private List<Object> messageElements;
		private UIInstance instance;
		
		private UIMessage(UIInstance instance) {
			this.messageElements = new ArrayList<>();
			this.instance = instance;
		}
		
		/**
		 * Builds this message into a list of {@link BaseComponent} arrays
		 * @return The built message
		 */
		public List<BaseComponent[]> buildMessage() {
			List<BaseComponent[]> lines = new ArrayList<>();
			ComponentBuilder cb = new ComponentBuilder("");
			for(Object o : messageElements) {
				if(o instanceof BaseComponent) {
					cb.append((BaseComponent) o);
				}else if(o instanceof UILayoutElement) {
					UILayoutElement el = (UILayoutElement) o;
					if(el.type.equals(UILayoutElementType.NEWLINE)) {
						lines.add(cb.create());
						cb = new ComponentBuilder("");
					}
				}
			}
			lines.add(cb.create());
			return lines;
		}
		
		/**
		 * Builds this message usigng {@link #buildMessage()} and sends it to the specified player
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
	
	protected static class UIListener implements CommandExecutor {
		
		private static List<UI> uis = new ArrayList<>();
		private static HashMap<String, UIInstance> instances = new HashMap<>();
		
		@SuppressWarnings("unchecked")
		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			if(!command.getName().equals("mrcoreui")) return true;
			if(!(sender instanceof Player)) {
				sender.sendMessage("UIs are only available to players");
				return true;
			}
			Player p = (Player) sender;
			if(!requireArgs(p, args, 2)) return true;
			String insID = args[0];
			String elID = args[1];
			UIInstance instance = UIListener.instances.get(insID);
			if(instance == null) {
				p.sendMessage("Invalid instance");
				return true;
			}
			HashMap<String, UIElement> elements = (HashMap<String, UIElement>) instance.properties.get("elements");
			UIElement el = elements.get(elID);
			if(el == null) {
				p.sendMessage("Invalid element");
				return true;
			}
			if(el.action == null) {
				p.sendMessage("Invalid action");
				return true;
			}
			el.action.action(new UIElementActionEvent(instance, p));
			return true;
		}
		
		private boolean requireArgs(Player p, String[] args, int n) {
			if(args.length >= n) return true;
			p.sendMessage("Invalid syntax");
			return false;
		}
		
	}
	
}
