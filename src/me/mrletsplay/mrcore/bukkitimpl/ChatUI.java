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
		 * Adds a "next page" element to the UI<br>
		 * This will have the default action of switching to the page after the current one<br>
		 * If there is no page before the current one, the click will be ignored
		 * @param key The element key
		 * @param layout The layout string to use
		 * @return This UIBuilderMultiPage instance
		 * @see {@link UIBuilder#addElement(String, UIElement)}
		 */
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
		 * This will have the default action of switching to the page before the current one<br>
		 * If there is no page before the current one, the click will be ignored
		 * @param key The element key
		 * @param layout The layout string to use
		 * @return This UIBuilderMultiPage instance
		 * @see {@link UIBuilder#addElement(String, UIElement)}
		 */
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
	
	public static class UILayout {
		
		public List<UILayoutElement> elements;
		
		public UILayout() {
			this.elements = new ArrayList<>();
		}
		
		public UILayout addText(String text) {
			elements.add(new UILayoutElement(UILayoutElementType.TEXT).setProperty("text", text));
			return this;
		}
		
		public UILayout addElement(String element) {
			elements.add(new UILayoutElement(UILayoutElementType.ELEMENT).setProperty("element", element));
			return this;
		}
		
		public UILayout newLine() {
			elements.add(new UILayoutElement(UILayoutElementType.NEWLINE));
			return this;
		}
		
	}
	
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
		
		public UILayoutMultiPage addPageElements(int count, boolean includeNewLines) {
			nItems += count;
			for(int i = 0; i < count; i++) {
				elements.add(new UILayoutElement(UILayoutElementType.MULTIPAGE_PAGE_ELEMENT).setProperty("newline", includeNewLines));
			}
			return this;
		}
		
	}
	
	public static class UILayoutElement {
		
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
	
	public static enum UILayoutElementType {
		
		TEXT,
		NEWLINE,
		ELEMENT,
		MULTIPAGE_PAGE_ELEMENT;
		
	}
	
	public static abstract class UIElement {
		
		private UIElementAction action;
		private String hoverText;
		
		public abstract String getLayout(Player p);
		
		public UIElement setAction(UIElementAction action) {
			this.action = action;
			return this;
		}
		
		public UIElement setHoverText(String hoverText) {
			this.hoverText = hoverText;
			return this;
		}
		
	}
	
	public static class StaticUIElement extends UIElement {
		
		private String layout;
		
		public StaticUIElement(String layout) {
			this.layout = layout;
		}
		
		@Override
		public String getLayout(Player p) {
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
	
	public static abstract class UIElementAction {
		
		public abstract void action(UIElementActionEvent event);
		
	}
	
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
	
	public static class UIInstance {
		
		private String id;
		private HashMap<String, Object> properties;
		private UI ui;
		
		public UIInstance(String id, UI ui, HashMap<String, Object> properties) {
			this.id = id;
			this.ui = ui;
			this.properties = properties;
		}
		
		public String getID() {
			return id;
		}
		
		public HashMap<String, Object> getProperties() {
			return properties;
		}
		
		public UI getUI() {
			return ui;
		}
		
	}
	
	public static class UI {

		private UIBuilder builder;
		public String id;
		private long lInsID = 0;
		
		public UI(UIBuilder builder) {
			this.builder = builder;
			this.id = ""+(lastID++);
			UIListener.uis.add(this);
		}
		
		public UIMessage getForPlayer(Player p) {
			HashMap<String, UIElement> elements = new HashMap<>();
			builder.elements.forEach(elements::put);
			String instanceID = id+"_"+(lInsID++);
			HashMap<String, Object> props = new HashMap<>(builder.properties);
			props.put("elements", elements);
			UIInstance instance = new UIInstance(instanceID, this, props);
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
		
		public String getID() {
			return id;
		}
		
	}
	
	public static class UIMultiPage<T> extends UI {

		private UIBuilderMultiPage<T> builder;
		
		public UIMultiPage(UIBuilderMultiPage<T> builder) {
			super(builder);
			this.builder = builder;
		}
		
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
		
		public void sendToPlayer(Player p) {
			UIMessage uim = getForPlayer(p);
			if(uim==null) return;
			uim.sendToPlayer(p);
		}
		
	}
	
	public static class UILayoutParser {
		
		public static TextComponent parseElement(UIElement element, Player p, String instanceID, String elementID) {
			TextComponent tc = new TextComponent(element.getLayout(p));
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
		
		public UIMessage(UIInstance instance) {
			this.messageElements = new ArrayList<>();
			this.instance = instance;
		}
		
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
		
		public void sendToPlayer(Player p) {
			buildMessage().forEach(p.spigot()::sendMessage);
		}
		
	}
	
	public static class UIListener implements CommandExecutor {
		
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
