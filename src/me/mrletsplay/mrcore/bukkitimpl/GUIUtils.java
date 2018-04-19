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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class GUIUtils {
	
	public static class GUIBuilder {
		
		private String title;
		private int size;
		private HashMap<Integer, GUIElement> elements;
		private GUIDragDropListener dragDrop;
		private GUIAction action;
		private HashMap<String, Object> properties;
		
		/**
		 * Creates a GUI builder for a single page GUI
		 * @see <a href="https://github.com/MrLetsplay2003/MrCore/wiki/GUIUtils">GUIUtils wiki</a>
		 * @param title The title for the GUI (inventory)
		 * @param rows The amount of rows of the GUI (inventory). Should not be more than 6
		 */
		public GUIBuilder(String title, int rows) {
			this.title = title;
			this.size = rows * 9;
			this.elements = new HashMap<>();
		}
		
		/**
		 * Adds an element to the GUI<br>
		 * If there's already an element in this slot, it will be overridden
		 * @param slot The slot to add the element to
		 * @param e The element to add
		 * @return This GUIBuilder instance
		 */
		public GUIBuilder addElement(int slot, GUIElement e) {
			elements.put(slot, e);
			return this;
		}
		
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
		public void setProperties(HashMap<String, Object> properties) {
			this.properties = properties;
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
		 * @param title
		 * @param rows
		 */
		public GUIBuilderMultiPage(String title, int rows) {
			super(title, rows);
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
					public boolean action(Player p, ClickAction button, ItemStack clickedWith, Inventory inv, GUI gui, InventoryClickEvent event) {
						int pg = GUIMultiPage.getPage(inv);
						GUIMultiPage<?> guiMP = (GUIMultiPage<?>)gui;
						Inventory nPage = guiMP.getForPlayer(p, pg+diff);
						if(nPage!=null) p.openInventory(nPage);
						return true;
					}
				});
		}
		
		@Override
		public GUIMultiPage<T> build() {
			return new GUIMultiPage<T>(this);
		}
		
	}
	
	public static abstract class ItemSupplier<T> {
		
		public abstract List<T> getItems();
		
		public abstract GUIElement toGUIElement(Player p, T item);
		
	}
	
	public static abstract class GUIElement {

		private GUIElementAction action;
		
		public abstract ItemStack getItem(Player p);
		
		/**
		 * Sets the {@link GUIElementAction} for this element
		 * @param a The action to be called
		 * @return This GUIElement instance
		 */
		public GUIElement setAction(GUIElementAction a) {
			this.action = a;
			return this;
		}
		
	}
	
	public static class StaticGUIElement extends GUIElement{
		
		private ItemStack it;
		
		public StaticGUIElement(ItemStack it) {
			this.it = it;
		}

		@Override
		public ItemStack getItem(Player p) {
			return it;
		}
		
		@Override
		public StaticGUIElement setAction(GUIElementAction a) {
			super.setAction(a);
			return this;
		}
		
	}

	public static abstract class GUIElementAction {
		
		public boolean action(Player p, ClickAction button, ItemStack clickedWith, Inventory inv, GUI gui, InventoryClickEvent event) { return true; }
		
	}
	
	public static abstract class GUIElementActionListener {
		
		public abstract boolean action(GUIElementActionEvent event);
		
	}
	
	public static abstract class GUIElementActionHandler {
		
		public abstract boolean action(GUIElementAction action);
		
	}
	
	public static class GUIElementActionEvent {
		
		private Player p;
		private ClickAction button;
		private Inventory inv;
		private GUI gui;
		private InventoryClickEvent event;
		
		public GUIElementActionEvent(Player p, ClickAction button, Inventory inv, GUI gui, InventoryClickEvent event) {
			this.p = p;
			this.button = button;
			this.inv = inv;
			this.gui = gui;
			this.event = event;
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
		
		public GUI getGui() {
			return gui;
		}
		
		public InventoryClickEvent getEvent() {
			return event;
		}
		
	}
	
	public static abstract class GUIDragDropListener {
		
		public abstract boolean allowDragDrop(Player p, ItemStack item, Inventory inv, GUI gui, InventoryClickEvent event);
		
	}
	
	public static abstract class GUIAction {

		public abstract boolean action(Player p, ClickAction action, ItemStack clickedWith, GUIElement clickedElement, Inventory inv, GUI gui, InventoryClickEvent event);
		
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
		
		private GUIHolder holder;
		private GUIBuilder builder;
		
		/**
		 * Creates a GUI<br>
		 * It is not recommended to use this method. Use {@link GUIBuilder#build()} instead
		 * @param builder The builder this GUI was built from
		 */
		public GUI(GUIBuilder builder) {
			this.builder = builder;
			this.holder = new GUIHolder(this);
		}
		
		/**
		 * Returns this GUI represented as an inventory for the specified player
		 * @param p The player this inventory is for (Used in all method calls to GUIElements and similar)
		 * @return An inventory representation of this GUI
		 */
		public Inventory getForPlayer(Player p) {
			Inventory inv = Bukkit.createInventory(holder.clone(), builder.size, builder.title);
			for(Map.Entry<Integer, GUIElement> el : builder.elements.entrySet()) {
				inv.setItem(el.getKey(), el.getValue().getItem(p));
			}
			return inv;
		}
		
		/**
		 * Returns the default GUIHolder for this GUI<br>
		 * Can be used to set GUI-specific properties (See {@link GUIHolder#setProperty(String, Object)})
		 * @return The GUIHolder instance for this GUI (unique to this GUI)
		 */
		public GUIHolder getHolder() {
			return holder;
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
		public HashMap<Integer, GUIElement> getElements(){
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
		
	}
	
	public static class GUIMultiPage<T> extends GUI {

		private GUIBuilderMultiPage<T> builder;
		

		/**
		 * Creates a GUI<br>
		 * It is not recommended to use this method. Use {@link GUIBuilderMultiPage#build()} instead
		 * @param builder The builder this GUI was built from
		 */
		public GUIMultiPage(GUIBuilderMultiPage<T> builder) {
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
			Inventory base = super.getForPlayer(p);
			GUIHolder holder = (GUIHolder)base.getHolder();
			holder.getProperties().put("page", page);
			List<Integer> slots = builder.customItemSlots;
			int nSlots = slots.size();
			ItemSupplier<T> supp = builder.supplier;
			List<T> items = supp.getItems();
			List<GUIElement> elements = items.stream().map(i -> supp.toGUIElement(p, i)).collect(Collectors.toList());
			HashMap<Integer, GUIElement> elSlots = new HashMap<>();
			int pages = items.size()/nSlots;
			if(page <= pages && page >= 0) {
				int start = page*nSlots;
				int end = (items.size()<=start+nSlots)?items.size():start+nSlots;
				for(int i = start; i < end; i++){
					GUIElement el = elements.get(i);
					int slot = slots.get(i-start);
					base.setItem(slot, el.getItem(p));
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
			e.setResult(Result.ALLOW);
			Inventory inv = e.getInventory();
			Player player = (Player) e.getWhoClicked();
			if(inv.getHolder() instanceof GUIHolder) {
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
						boolean cancel = elClicked.action.action(player, action, e.getCursor(), e.getClickedInventory(), gui, e);
						if(!cancel) e.setCancelled(false);
					}
					if(gui.builder.action != null) {
						boolean cancel = gui.builder.action.action(player, action, e.getCursor(), elClicked, e.getClickedInventory(), gui, e);
						if(!cancel) e.setCancelled(false);
					}
					if(gui instanceof GUIMultiPage<?>) {
						HashMap<Integer, GUIElement> pageElements = (HashMap<Integer, GUIElement>) holder.getProperties().get("page-elements");
						GUIElement pElClicked = pageElements.get(slot);
						if(pElClicked != null && pElClicked.action != null) {
							boolean cancel = pElClicked.action.action(player, action, e.getCursor(), e.getClickedInventory(), gui, e);
							if(!cancel) e.setCancelled(false);
						}
					}
				}else {
					//Player inv clicked
					if(gui.builder.dragDrop != null) {
						ItemStack pickedUp = e.getCurrentItem();
						if(pickedUp!=null) {
							if(!gui.builder.dragDrop.allowDragDrop(player, pickedUp, e.getClickedInventory(), gui, e)) {
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
	
	public static class GUIHolder implements InventoryHolder {

		private GUI gui;
		
		/**
		 * Creates a gui holder for the specified GUI<br>
		 * It will not automatically be registered to the GUI
		 * @param gui The GUI this holder is for
		 */
		public GUIHolder(GUI gui) {
			this(gui, new HashMap<>());
		}

		/**
		 * Creates a gui holder for the specified GUI<br>
		 * It will not automatically be registered to the GUI
		 * @param gui The GUI this holder is for
		 * @param props The property HashMap to be used
		 */
		public GUIHolder(GUI gui, HashMap<String, Object> props) {
			this.gui = gui;
			this.gui.builder.properties = props;
		}
		
		/**
		 * @return The GUI instance this holder belongs to
		 */
		public GUI getGui() {
			return gui;
		}
		
		/**
		 * Sets a property in the property HashMap to a specific value<br>
		 * If this is the default holder specified by {@link GUI#getHolder()}, the properties set by this method will be passed down to all children GUIs (inventories)
		 * @param key The key of the property
		 * @param value The value of the property
		 */
		public void setProperty(String key, Object value) {
			this.gui.builder.properties.put(key, value);
		}
		
		/**
		 * @return The property HashMap for this holder
		 */
		public HashMap<String, Object> getProperties() {
			return this.gui.builder.properties;
		}
		
		/**
		 * Clones this GUIHolder. This will be called in order to supply independent holders to inventories
		 * @return A copy of this GUIHolder
		 */
		public GUIHolder clone() {
			return new GUIHolder(gui, new HashMap<>(this.gui.builder.properties));
		}
		
		/**
		 * It is not recommended for this method to be used as it could cause errors in some cases
		 */
		@Override
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

}
