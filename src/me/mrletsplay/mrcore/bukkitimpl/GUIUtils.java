package me.mrletsplay.mrcore.bukkitimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class GUIUtils {
	
	public static class GUIBuilder {
		
		private String title;
		private int size;
		private InventoryType invType;
		private boolean isCustomType;
		private Map<Integer, GUIElement> elements;
		private GUIDragDropListener dragDrop;
		private GUIAction action;
		private Map<String, Object> properties = new HashMap<>();
		GUIBuildAction buildAction;
		
		/**
		 * Creates a GUI builder for a single page GUI
		 * @see <a href="https://github.com/MrLetsplay2003/MrCore/wiki/GUIUtils">GUIUtils wiki</a>
		 * @param title The title for the GUI (inventory)
		 * @param rows The amount of rows of the GUI (inventory). Should not be more than 6
		 */
		public GUIBuilder(String title, int rows) {
			this.title = title;
			this.size = rows * 9;
			this.isCustomType = false;
			this.elements = new HashMap<>();
			this.properties = new HashMap<>();
		}

		/**
		 * Creates a GUI builder for a single page GUI
		 * @see <a href="https://github.com/MrLetsplay2003/MrCore/wiki/GUIUtils">GUIUtils wiki</a>
		 * @param title The title for the GUI (inventory)
		 * @param type The inventory type for this GUI
		 */
		public GUIBuilder(String title, InventoryType type) {
			this.title = title;
			this.invType = type;
			this.isCustomType = true;
			this.elements = new HashMap<>();
			this.properties = new HashMap<>();
		}
		
		/**
		 * Adds an element to the GUI<br>
		 * If there's already an element in this slot, it will be overridden
		 * @param slot The slot to add the element to
		 * @param e The element to add
		 * @return This GUIBuilder instance
		 */
		public GUIBuilder addElement(int slot, GUIElement e) {
			if(e != null) {
				elements.put(slot, e);
			}else {
				elements.remove(slot);
			}
			return this;
		}
		
//		/**
//		 * Convenience version of {@link #addElement(int, GUIElement)}
//		 * @param slot The slot to add the element to
//		 * @param e The element to add
//		 * @return This GUIBuilder instance
//		 */
//		public GUIBuilder addElement(int slot, Function<GUIBuildEvent, ItemStack> e) {
//			elements.put(slot, new GUIElement() {
//				
//				@Override
//				public ItemStack getItem(GUIBuildEvent event) {
//					return e.apply(event);
//				}
//			});
//			return this;
//		}
		
		/**
		 * Sets the {@link GUIAction} listener for this GUI<br>
		 * If there's already a listener registered, it will be overridden
		 * @param a The action listener to use
		 * @return This GUIBuilder instance
		 */
		public GUIBuilder setActionListener(GUIAction a) {
			this.action = a;
			return this;
		}
		
		/**
		 * Sets the {@link GUIDragDropListener} for this GUI<br>
		 * If there's already a listener registered, it will be overridden
		 * @param dragDrop The listener to use
		 * @return This GUIBuilder instance
		 */
		public GUIBuilder setDragDropListener(GUIDragDropListener dragDrop) {
			this.dragDrop = dragDrop;
			return this;
		}
		
		/**
		 * Sets the default properties for this GUIBuilder
		 * @param properties The properties to use
		 */
		public void setProperties(Map<String, Object> properties) {
			this.properties = properties;
		}
		
		/**
		 * Adds a default property for this GUIBuilder
		 * @param key The key of the property
		 * @param value the value of the property
		 */
		public void addProperty(String key, Object value) {
			properties.put(key, value);
		}
		
		/**
		 * Sets the build action/build listener for this instance<br>
		 * This can be used to add instance-specific items to the GUI when the GUI is built or refreshed<br>
		 * Note: This is not called when the {@link #build()} method is used but rather when the {@link GUI#getForPlayer(Player)} method is called
		 * @param buildAction The buid action listener to use
		 */
		public GUIBuilder setBuildAction(GUIBuildAction buildAction) {
			this.buildAction = buildAction;
			return this;
		}
		
		/**
		 * Builds this GUIBuilder into a functioning GUI
		 * @return The GUI that was built
		 */
		public GUI build() {
			return new GUI(this);
		}
		
	}
	
	/**
	 * @see <a href="https://github.com/MrLetsplay2003/MrCore/wiki/GUIUtils">GUIUtils wiki</a>
	 * @author MrLetsplay2003
	 */
	public static class GUIBuilderMultiPage<T> extends GUIBuilder {

		private List<Integer> customItemSlots;
		private ItemSupplier<T> supplier;
		
		/**
		 * Creates a GUI builder for a multi page GUI
		 * @see <a href="https://github.com/MrLetsplay2003/MrCore/wiki/GUIUtils">GUIUtils wiki</a>
		 * @param title The title for this inventory
		 * @param rows The amount of rows (Should not be more than 6)
		 */
		public GUIBuilderMultiPage(String title, int rows) {
			super(title, rows);
			this.customItemSlots = new ArrayList<>();
		}
		/**
		 * Creates a GUI builder for a multi page GUI
		 * @see <a href="https://github.com/MrLetsplay2003/MrCore/wiki/GUIUtils">GUIUtils wiki</a>
		 * @param title The title for this inventory
		 * @param type The type for this inventory
		 */
		public GUIBuilderMultiPage(String title, InventoryType type) {
			super(title, type);
			this.customItemSlots = new ArrayList<>();
		}
		
		/**
		 * Adds the specified page slots to the GUI builder
		 * @param slots The specific slots to add
		 * @return This GUIBuilderMultiPage instance
		 */
		public GUIBuilderMultiPage<T> addPageSlots(int... slots) {
			Arrays.stream(slots).forEach(customItemSlots::add);
			return this;
		}
		
		/**
		 * Adds the specified page slots to the GUI builder (by range)<br>
		 * Both from and to are inclusive<br>
		 * E.g.: {@code addPageSlotsInRange(5, 8);} is basically equivalent to {@code addPageSlots(5, 6, 7, 8);}
		 * @param from The start of the range (inclusive)
		 * @param to The end of the range (inclusive)
		 * @return This GUIBuilderMultiPage instance
		 */
		public GUIBuilderMultiPage<T> addPageSlotsInRange(int from, int to) {
			List<Integer> slots = new ArrayList<>();
			while(from <= to) {
				slots.add(from);
				from++;
			}
			slots.forEach(customItemSlots::add);
			return this;
		}
		
		/**
		 * Sets the {@link ItemSupplier} for this multi page GUI
		 * @param supplier The supplier to use
		 * @return This GUIBuilderMultiPage instance
		 */
		public GUIBuilderMultiPage<T> setSupplier(ItemSupplier<T> supplier){
			this.supplier = supplier;
			return this;
		}
		
		@Override
		public GUIBuilderMultiPage<T> addElement(int slot, GUIElement e) {
			super.addElement(slot, e);
			return this;
		}
		
//		@Override
//		public GUIBuilderMultiPage<T> addElement(int slot, Function<GUIBuildEvent, ItemStack> e) {
//			super.addElement(slot, e);
//			return this;
//		}
		
		@Override
		public GUIBuilderMultiPage<T> setActionListener(GUIAction a) {
			super.setActionListener(a);
			return this;
		}
		
		@Override
		public GUIBuilderMultiPage<T> setDragDropListener(GUIDragDropListener dragDrop) {
			super.setDragDropListener(dragDrop);
			return this;
		}
		
		/**
		 * Adds a "previous page" item to the GUI<br>
		 * This will have the default action of switching to the page before the current one<br>
		 * If there is no page before the current one, the click will be ignored
		 * @param slot The slot to add the item to
		 * @param it The {@link ItemStack} to represent this element
		 * @return This GUIBuilderMultiPage instance
		 */
		public GUIBuilderMultiPage<T> addNextPageItem(int slot, ItemStack it){
			return addElement(slot, changePageItem(it, 1));
		}

		/**
		 * Adds a "next page" item to the GUI<br>
		 * This will have the default action of switching to the page before the current one<br>
		 * If there is no page before the current one, the click will be ignored
		 * @param slot The slot to add the item to
		 * @param it The {@link ItemStack} to represent this element
		 * @return This GUIBuilderMultiPage instance
		 */
		public GUIBuilderMultiPage<T> addPreviousPageItem(int slot, ItemStack it){
			return addElement(slot, changePageItem(it, -1));
		}
		
		private static GUIElement changePageItem(ItemStack it, int diff) {
			return new StaticGUIElement(it)
				.setAction(new GUIElementAction() {
					
					@Override
					public void onAction(GUIElementActionEvent event) {
						int pg = GUIMultiPage.getPage(event.getInventory());
						GUIMultiPage<?> guiMP = (GUIMultiPage<?>) event.getGUI();
						Inventory nPage = guiMP.getForPlayer(event.getPlayer(), pg+diff);
						if(nPage!=null) event.getPlayer().openInventory(nPage);
						event.setCancelled(true);
					}
				});
		}
		
		@Override
		public GUIBuilderMultiPage<T> setBuildAction(GUIBuildAction buildAction) {
			super.setBuildAction(buildAction);
			return this;
		}
		
		@Override
		public GUIMultiPage<T> build() {
			return new GUIMultiPage<T>(this);
		}
		
	}
	
	public static abstract class ItemSupplier<T> {
		
		public abstract List<T> getItems(GUIBuildEvent event);
		
		public abstract GUIElement toGUIElement(GUIBuildEvent event, T item);
		
	}
	
	public static abstract class GUIElement {

		private GUIElementAction action;
		
		public abstract ItemStack getItem(GUIBuildEvent event);
		
		/**
		 * Sets the {@link GUIElementAction} for this element
		 * @param a The action to be called
		 * @return This GUIElement instance
		 */
		public GUIElement setAction(GUIElementAction a) {
			this.action = a;
			return this;
		}
		
//		/**
//		 * Convenience version of {@link #setAction(GUIElementAction)}
//		 * @param a The action to be called
//		 * @return This GUIElement instance
//		 */
//		public GUIElement setAction(Consumer<GUIElementActionEvent> a) {
//			this.action = new GUIElementAction() {
//				
//				@Override
//				public void onAction(GUIElementActionEvent event) {
//					a.accept(event);
//				}
//			};
//			return this;
//		}
		
	}
	
	public static class StaticGUIElement extends GUIElement{
		
		private ItemStack it;
		
		public StaticGUIElement(ItemStack it) {
			this.it = it;
		}

		@Override
		public ItemStack getItem(GUIBuildEvent event) {
			return it;
		}
		
		@Override
		public StaticGUIElement setAction(GUIElementAction a) {
			super.setAction(a);
			return this;
		}
		
//		@Override
//		public StaticGUIElement setAction(Consumer<GUIElementActionEvent> a) {
//			super.setAction(a);
//			return this;
//		}
		
	}

	@FunctionalInterface
	public static interface GUIElementAction {
		
		public void onAction(GUIElementActionEvent event);
		
	}
	
	public static class GUIElementActionEvent {
		
		private Player p;
		private ClickAction button;
		private Inventory inv;
		private GUI gui;
		private GUIHolder guiHolder;
		private InventoryClickEvent event;
		private boolean cancelled;
		
		public GUIElementActionEvent(Player p, ClickAction button, Inventory inv, GUI gui, GUIHolder holder, InventoryClickEvent event) {
			this.p = p;
			this.button = button;
			this.inv = inv;
			this.gui = gui;
			this.guiHolder = holder;
			this.event = event;
			this.cancelled = true;
		}
		
		public Player getPlayer() {
			return p;
		}
		
		public ClickAction getButton() {
			return button;
		}
		
		public ClickType getClickType() {
			return event.getClick();
		}
		
		public ItemStack getItemClickedWith() {
			return event.getCursor();
		}
		
		public ItemStack getItemClicked() {
			return event.getCurrentItem();
		}
		
		public Inventory getInventory() {
			return inv;
		}
		
		public GUI getGUI() {
			return gui;
		}
		
		public GUIHolder getGUIHolder() {
			return guiHolder;
		}
		
		public InventoryClickEvent getEvent() {
			return event;
		}
		
		public void setCancelled(boolean cancelled) {
			this.cancelled = cancelled;
		}
		
		public boolean isCancelled() {
			return cancelled;
		}
		
	}
	
	@FunctionalInterface
	public static interface GUIDragDropListener {
		
		public void onDragDrop(GUIDragDropEvent event);
		
	}
	
	public static class GUIDragDropEvent {
		
		private Player player;
		private ItemStack clicked;
		private Inventory inv;
		private GUI gui;
		private GUIHolder guiHolder;
		private InventoryClickEvent event;
		private boolean cancelled;
		
		public GUIDragDropEvent(Player p, ItemStack clicked, Inventory inv, GUI gui, GUIHolder holder, InventoryClickEvent event) {
			this.player = p;
			this.clicked = clicked;
			this.inv = inv;
			this.gui = gui;
			this.event = event;
			this.guiHolder = holder;
			this.cancelled = false;
		}
		
		public Player getPlayer() {
			return player;
		}
		
		public ItemStack getItemClicked() {
			return clicked;
		}
		
		public Inventory getInventory() {
			return inv;
		}
		
		public GUI getGUI() {
			return gui;
		}
		
		public InventoryClickEvent getEvent() {
			return event;
		}
		
		public GUIHolder getGUIHolder() {
			return guiHolder;
		}
		
		public void setCancelled(boolean cancelled) {
			this.cancelled = cancelled;
		}
		
		public boolean isCancelled() {
			return cancelled;
		}
		
	}
	
	@FunctionalInterface
	public static interface GUIAction {

		public void onAction(GUIActionEvent event);
		
	}
	
	public static class GUIActionEvent {
		
		private Player p;
		private ClickAction action;
		private GUIElement elClicked;
		private Inventory inv;
		private GUI gui;
		private GUIHolder guiHolder;
		private InventoryClickEvent event;
		private boolean cancelled;
		
		public GUIActionEvent(Player p, ClickAction action, GUIElement elClicked, Inventory inv, GUI gui, GUIHolder holder, InventoryClickEvent event) {
			this.p = p;
			this.action = action;
			this.elClicked = elClicked;
			this.inv = inv;
			this.gui = gui;
			this.event = event;
			this.guiHolder = holder;
			this.cancelled = true;
		}
		
		public Player getPlayer() {
			return p;
		}
		
		public ClickAction getAction() {
			return action;
		}
		
		public ItemStack getItemClicked() {
			return event.getCurrentItem();
		}
		
		public ItemStack getItemClickedWith() {
			return event.getCursor();
		}
		
		public GUIElement getElementClicked() {
			return elClicked;
		}
		
		public Inventory getInventory() {
			return inv;
		}
		
		public GUI getGUI() {
			return gui;
		}
		
		public GUIHolder getGUIHolder() {
			return guiHolder;
		}
		
		public InventoryClickEvent getEvent() {
			return event;
		}
		
		public void setCancelled(boolean cancelled) {
			this.cancelled = cancelled;
		}
		
		public boolean isCancelled() {
			return cancelled;
		}
		
	}
	
	@FunctionalInterface
	public static interface GUIBuildAction {
		
		public void onBuild(GUIBuildEvent event);
		
	}
	
	public static class GUIBuildEvent {
		
		private GUIHolder holder;
		private Player player;
		private Inventory inv;
		
		public GUIBuildEvent(GUIHolder holder, Player player, Inventory inv) {
			this.holder = holder;
			this.player = player;
			this.inv = inv;
		}
		
		public GUIHolder getHolder() {
			return holder;
		}
		
		public Player getPlayer() {
			return player;
		}
		
		public Inventory getInventory() {
			return inv;
		}
		
		public GUI getGUI() {
			return holder.gui;
		}
		
	}
	
	public static enum ClickAction {
		
		LEFT_CLICK,
		SHIFT_LEFT_CLICK,
		RIGHT_CLICK,
		SHIFT_RIGHT_CLICK;
		
		public static ClickAction getFromEvent(InventoryClickEvent e) {
			if(e.isLeftClick()) {
				if(e.isShiftClick()) {
					return SHIFT_LEFT_CLICK;
				}else {
					return LEFT_CLICK;
				}
			}else if(e.isRightClick()) {
				if(e.isShiftClick()) {
					return SHIFT_RIGHT_CLICK;
				}else {
					return RIGHT_CLICK;
				}
			}
			return null;
		}
		
	}
	
	public static class GUI {
		
		private GUIBuilder builder;
		
		protected GUI(GUIBuilder builder) {
			this.builder = builder;
		}

		/**
		 * Returns this GUI represented as an inventory for the specified player
		 * @param p The player this inventory is for (Used in all method calls to GUIElements and similar)
		 * @return An inventory representation of this GUI
		 */
		public Inventory getForPlayer(Player p) {
			Inventory inv = buildInternally(p, new HashMap<>());
			if(builder.buildAction != null && !(this instanceof GUIMultiPage)) builder.buildAction.onBuild(new GUIBuildEvent((GUIHolder) inv.getHolder(), p, inv));
			return inv;
		}

		/**
		 * Returns this GUI represented as an inventory for the specified player
		 * @param p The player this inventory is for (Used in all method calls to GUIElements and similar)
		 * @return An inventory representation of this GUI
		 */
		public Inventory getForPlayer(Player p, Map<String, Object> properties) {
			Inventory inv = buildInternally(p, properties);
			if(builder.buildAction != null && !(this instanceof GUIMultiPage)) builder.buildAction.onBuild(new GUIBuildEvent((GUIHolder) inv.getHolder(), p, inv));
			return inv;
		}
		
		protected Inventory buildInternally(Player p, Map<String, Object> properties) {
			Inventory inv;
			GUIHolder holder = new GUIHolder(this, properties);
			if(builder.isCustomType) {
				inv = Bukkit.createInventory(holder, builder.invType, builder.title);
			} else {
				inv = Bukkit.createInventory(holder, builder.size, builder.title);
			}
			
			GUIBuildEvent event = new GUIBuildEvent(holder, p, inv);
			
			for(Map.Entry<Integer, GUIElement> el : builder.elements.entrySet()) {
				inv.setItem(el.getKey(), el.getValue().getItem(event));
			}
			return inv;
		}

		/**
		 * Returns the default GUIHolder properties for this GUI<br>
		 * Can be used to set GUI-specific properties
		 * @return The GUIHolder instance for this GUI (unique to this GUI)
		 */
		public Map<String, Object> getDefaultHolderProperties() {
			return builder.properties;
		}
		
		/**
		 * @param slot The slot the element is on
		 * @return The element on that slot, null if none
		 */
		public GUIElement getElementBySlot(int slot) {
			return builder.elements.get(slot);
		}
		
		/**
		 * @return All the elements in this GUI. This does not include page-specific items for {@link GUIMultiPage} GUIs
		 */
		public Map<Integer, GUIElement> getElements(){
			return builder.elements;
		}
		
		/**
		 * @return The GUIAction listener for this GUI, null if none
		 */
		public GUIAction getAction() {
			return builder.action;
		}
		
		/**
		 * @return GUIDragDropListener for this GUI, null if none
		 */
		public GUIDragDropListener getDragDropListener() {
			return builder.dragDrop;
		}
		
		/**
		 * The builder this GUI was built from<br>
		 * All changes made to the builder will be applied to the GUI as well
		 * @return The GUI builder for this GUI
		 */
		public GUIBuilder getBuilder() {
			return builder;
		}
		
		/**
		 * Refreshes all (opened) instances of this GUI<br>
		 * <br>
		 * This method iterates through every player on the server, sees if they have that specific GUI open<br>
		 * and, if they do, replaces it with a newly built instance of that GUI using {@link #refreshInstance(Player)}<br>
		 * <br>
		 * This call is equivalent to calling<br>
		 * <code>
		 * for(Player player : Bukkit.getOnlinePlayers()) {<br>
		 * &nbsp;&nbsp;&nbsp;&nbsp;gui.refreshInstance(player);<br>
		 * }
		 * </code>
		 */
		public void refreshAllInstances() {
			for(Player player : Bukkit.getOnlinePlayers()) refreshInstance(player);
		}
		
		/**
		 * Refreshes a specific player's instance of this GUI<br>
		 * If the player doesn't have an this GUI open, this method will do nothing
		 * @param player The player to refresh this GUI for
		 */
		public void refreshInstance(Player player) {
			if(player.getOpenInventory() != null && player.getOpenInventory().getTopInventory() != null) {
				Inventory oldInv = player.getOpenInventory().getTopInventory();
				GUIHolder holder = GUIUtils.getGUIHolder(oldInv);
				if(holder != null && holder.gui.equals(this)) {
					openNewInstance(player, oldInv, holder);
				}
			}
		}
		
		public void closeAllInstances() {
			for(Player player : Bukkit.getOnlinePlayers()) closeInstance(player);
		}
		
		public void closeInstance(Player player) {
			if(player.getOpenInventory() != null && player.getOpenInventory().getTopInventory() != null) {
				Inventory oldInv = player.getOpenInventory().getTopInventory();
				GUIHolder holder = GUIUtils.getGUIHolder(oldInv);
				if(holder != null && holder.gui.equals(this)) {
					player.closeInventory();
				}
			}
		}
		
		public List<Player> getAllOpenInstances() {
			return Bukkit.getOnlinePlayers().stream().filter(p -> hasOpenInstance(p)).collect(Collectors.toList());
		}
		
		public boolean hasOpenInstance(Player player) {
			if(player.getOpenInventory() != null && player.getOpenInventory().getTopInventory() != null) {
				Inventory oldInv = player.getOpenInventory().getTopInventory();
				GUIHolder holder = GUIUtils.getGUIHolder(oldInv);
				if(holder != null && holder.gui.equals(this)) {
					return true;
				}
			}
			return false;
		}
		
		protected void openNewInstance(Player player, Inventory oldInv, GUIHolder holder) {
			Inventory newInv = getForPlayer(player, holder.getProperties());
			GUIHolder newHolder = getGUIHolder(newInv);
			
			// This should preserve all custom properties while still remaining all the "refreshed" ones
			holder.properties.putAll(newHolder.properties);
			
			if(builder.buildAction != null) builder.buildAction.onBuild(new GUIBuildEvent(holder, player, newInv));
			changeInventory(oldInv, newInv);
		}
		
	}
	
	public static class GUIMultiPage<T> extends GUI {

		private GUIBuilderMultiPage<T> builder;
		
		protected GUIMultiPage(GUIBuilderMultiPage<T> builder) {
			super(builder);
			this.builder = builder;
		}
		
		/**
		 * This will return page 0 of the GUI
		 */
		@Override
		public Inventory getForPlayer(Player p) {
			return getForPlayer(p, 0);
		}
		
		/**
		 * Returns an inventory representation of the specified page of this GUI for a specified player
		 * @param p The player this inventory is for (Used in all method calls to GUIElements and similar)
		 * @param page The page to be used
		 * @return An inventory representation for this GUI, null if the page doesn't exist
		 */
		public Inventory getForPlayer(Player p, int page) {
			Inventory inv = buildInternally(p, page, new HashMap<>());
			if(builder.buildAction != null) builder.buildAction.onBuild(new GUIBuildEvent((GUIHolder) inv.getHolder(), p, inv));
			return inv;
		}
		
		/**
		 * This will return page 0 of the GUI
		 */
		@Override
		public Inventory getForPlayer(Player p, Map<String, Object> properties) {
			return getForPlayer(p, 0, properties);
		}
		
		/**
		 * Returns an inventory representation of the specified page of this GUI for a specified player
		 * @param p The player this inventory is for (Used in all method calls to GUIElements and similar)
		 * @param page The page to be used
		 * @return An inventory representation for this GUI, null if the page doesn't exist
		 */
		public Inventory getForPlayer(Player p, int page, Map<String, Object> properties) {
			Inventory inv = buildInternally(p, page, properties);
			if(builder.buildAction != null) builder.buildAction.onBuild(new GUIBuildEvent((GUIHolder) inv.getHolder(), p, inv));
			return inv;
		}
		
		protected Inventory buildInternally(Player p, int page, Map<String, Object> properties) {
			Inventory base = super.getForPlayer(p, properties);
			GUIHolder holder = (GUIHolder) base.getHolder();
			holder.getProperties().put("page", page);
			List<Integer> slots = builder.customItemSlots;
			int nSlots = slots.size();
			
			GUIBuildEvent event = new GUIBuildEvent(holder, p, base);
			
			ItemSupplier<T> supp = builder.supplier;
			List<T> items = supp.getItems(event);
			
			List<GUIElement> elements = items.stream().map(i -> supp.toGUIElement(event, i)).collect(Collectors.toList());
			Map<Integer, GUIElement> elSlots = new HashMap<>();
			int pages = items.size()/nSlots;
			
			if(page <= pages && page >= 0) {
				int start = page*nSlots;
				int end = (items.size()<=start+nSlots)?items.size():start+nSlots;
				for(int i = start; i < end; i++){
					GUIElement el = elements.get(i);
					int slot = slots.get(i-start);
					base.setItem(slot, el.getItem(event));
					elSlots.put(slot, el);
				}
				holder.getProperties().put("page-elements", elSlots);
				return base;
			}else {
				return null;
			}
		}
		
		/**
		 * Returns the page number for this inventory
		 * @param inv The inventory to be checked
		 * @return The page for this gui
		 * @throws IllegalArgumentException if the given Inventory is not a valid GUI
		 */
		public static int getPage(Inventory inv) {
			if(!(inv.getHolder() instanceof GUIHolder)) {
				throw new IllegalArgumentException("Provided inventory is not a GUIMultiPage");
			}
			GUIHolder holder = (GUIHolder) inv.getHolder();
			if(!(holder.gui instanceof GUIMultiPage<?>)) {
				throw new IllegalArgumentException("Provided inventory is not a GUIMultiPage");
			}
			return (int) holder.getProperties().get("page");
		}
		
		@Override
		protected void openNewInstance(Player player, Inventory oldInv, GUIHolder holder) {
			Inventory newInv = getForPlayer(player, (int) holder.getProperty("page"), holder.getProperties());
			GUIHolder newHolder = getGUIHolder(newInv);
			
			// This should preserve all custom properties while still remaining all the "refreshed" ones
			holder.properties.putAll(newHolder.properties);
			
			if(builder.buildAction != null) builder.buildAction.onBuild(new GUIBuildEvent(holder, player, newInv));
			changeInventory(oldInv, newInv);
		}
		
	}
	
	/**
	 * Do NOT register this listener yourself. It will be handled by MrCore and registering it will cause double calls to GUIs
	 * @author MrLetsplay2003
	 */
	public static class GUIListener implements Listener{
		
		@SuppressWarnings("unchecked")
		@EventHandler
		public void onInvClick(InventoryClickEvent e) {
			if(e.getClickedInventory() == null) return;
			Inventory inv = e.getInventory();
			Player player = (Player) e.getWhoClicked();
			if(inv.getHolder() instanceof GUIHolder) {
				e.setResult(Result.ALLOW);
				GUIHolder holder = (GUIHolder) inv.getHolder();
				GUI gui = holder.gui;
				ClickAction action = ClickAction.getFromEvent(e);
				if(action==null) {
					e.setCancelled(true);
					return;
				}
				if(e.getClickedInventory().equals(e.getInventory())) {
					//Gui clicked
					int slot = e.getSlot();
					GUIElement elClicked = gui.getElementBySlot(slot);
					e.setCancelled(true);
					if(elClicked != null && elClicked.action != null) {
						GUIElementActionEvent event = new GUIElementActionEvent(player, action, inv, gui, holder, e);
						elClicked.action.onAction(event);
						if(!event.isCancelled()) e.setCancelled(false);
					}
					if(gui.builder.action != null) {
						GUIActionEvent event = new GUIActionEvent(player, action, elClicked, e.getClickedInventory(), gui, holder, e);
						gui.builder.action.onAction(event);
						if(!event.isCancelled()) e.setCancelled(false);
					}
					if(gui instanceof GUIMultiPage<?>) {
						Map<Integer, GUIElement> pageElements = (Map<Integer, GUIElement>) holder.getProperties().get("page-elements");
						GUIElement pElClicked = pageElements.get(slot);
						if(pElClicked != null && pElClicked.action != null) {
							GUIElementActionEvent event = new GUIElementActionEvent(player, action, inv, gui, holder, e);
							pElClicked.action.onAction(event);
							if(!event.isCancelled()) e.setCancelled(false);
						}
					}
				} else {
					//Player inv clicked
					
					if(e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
						e.setCancelled(true);
						return;
					}
					
					if(gui.builder.dragDrop != null) {
						ItemStack pickedUp = e.getCurrentItem();
						if(pickedUp!=null) {
							GUIDragDropEvent event = new GUIDragDropEvent(player, pickedUp, e.getClickedInventory(), gui, holder, e);
							gui.builder.dragDrop.onDragDrop(event);
							if(event.isCancelled()) {
								e.setCancelled(true);
							}
						}
					}else {
						e.setCancelled(true);
					}
				}
			}
		}
		
	}
	
	public static class GUIHolderPropertyMap extends HashMap<String, Object> {

		private static final long serialVersionUID = 8608526183300903401L;
		
		public void put(Plugin pl, String key, Object value) {
			super.put(pl.getName()+"_"+key, value);
		}
		
		public Object get(Plugin pl, String key) {
			return super.get(pl.getName()+"_"+key);
		}
		
	}
	
	public static class GUIHolder implements InventoryHolder {

		private GUI gui;
		private Map<String, Object> properties;
		
		/**
		 * Creates a gui holder for the specified GUI<br>
		 * It will not automatically be registered to the GUI
		 * @param gui The GUI this holder is for
		 * @param properties The property hashmap to use
		 */
		public GUIHolder(GUI gui, Map<String, Object> properties) {
			this.gui = gui;
			this.properties = properties;
			this.properties.putAll(this.gui.builder.properties);
		}
		
		/**
		 * @return The GUI instance this holder belongs to
		 */
		public GUI getGui() {
			return gui;
		}
		
		/**
		 * Sets a property in the property HashMap to a specific value<br>
		 * If you're implementing this into a plugin and don't want to modify a "raw" property (like mrcore_page)<br>
		 * or one from another plugin, use {@link #setProperty(Plugin, String, Object)}
		 * @param key The key of the property
		 * @param value The value of the property
		 */
		public void setProperty(String key, Object value) {
			properties.put(key, value);
		}
		
		/**
		 * Gets a property from the property HashMap<br>
		 * If you're implementing this into a plugin and don't want to modify a "raw" property (like mrcore_page)<br>
		 * or one from another plugin, use {@link #getProperty(Plugin, String)}
		 * @param key The key of the property
		 * @return The value of the property or null if not set
		 */
		public Object getProperty(String key) {
			return properties.get(key);
		}
		
		/**
		 * Sets a property in the property HashMap<br>
		 * This is a convenience method to easily create cross-plugin compatible properties<br>
		 * with the same name
		 * @param key
		 * @param value
		 */
		public void setProperty(Plugin plugin, String key, Object value) {
			properties.put(plugin.getName()+"_"+key, value);
		}
		
		/**
		 * Gets a plugin-specific property from the property HashMap<br>
		 * This is a convenience method to easily create cross-plugin compatible properties<br>
		 * with the same name
		 * @param key The key of the property
		 * @return The value of the property or null if not set
		 */
		public Object getProperty(Plugin plugin, String key) {
			return properties.get(plugin.getName()+"_"+key);
		}
		
		/**
		 * @return The property HashMap for this holder
		 */
		public Map<String, Object> getProperties() {
			return properties;
		}
		
		/**
		 * Clones this GUIHolder. This will be called in order to supply individual holders to inventories
		 * @return A copy of this GUIHolder
		 */
		public GUIHolder clone() {
			GUIHolder newH = new GUIHolder(gui, properties);
			newH.properties = new HashMap<>(properties);
			return newH;
		}
		
		/**
		 * @deprecated It is not recommended for this method to be used as it could cause errors in some cases
		 */
		@Override
		@Deprecated
		public Inventory getInventory() {
			return gui.getForPlayer(null);
		}
		
	}
	
	/**
	 * Gets a GUI from an inventory
	 * @param inv The inventory to be converted
	 * @return The respective GUI, null if none (not a GUI)
	 */
	public static GUI getGUI(Inventory inv) {
		if(inv.getHolder() instanceof GUIHolder) {
			return ((GUIHolder) inv.getHolder()).gui;
		}else {
			return null;
		}
	}
	
	/**
	 * Gets a GUIHolder from an inventory
	 * @param inv The inventory to be converted
	 * @return The respective GUIHolder, null if none (not a GUI)
	 */
	public static GUIHolder getGUIHolder(Inventory inv) {
		if(inv.getHolder() instanceof GUIHolder) {
			return (GUIHolder) inv.getHolder();
		}else {
			return null;
		}
	}
	
	public static void changeInventory(Inventory oldInv, Inventory newInv) {
		oldInv.setContents(newInv.getContents());
	}

}
