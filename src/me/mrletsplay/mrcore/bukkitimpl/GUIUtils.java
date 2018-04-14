package me.mrletsplay.mrcore.bukkitimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class GUIUtils {
	
	public static class GUIBuilder {
		
		private String title;
		private int size;
		private List<GUIElement> elements;
		private GUIDragDropListener dragDrop;
		private GUIAction action;
		
		public GUIBuilder(String title, int rows) {
			this.title = title;
			this.size = rows * 9;
			this.elements = new ArrayList<>();
		}
		
		public GUIBuilder addElement(GUIElement e) {
			elements.add(e);
			return this;
		}
		
		public GUIBuilder setActionListener(GUIAction a) {
			this.action = a;
			return this;
		}
		
		public GUIBuilder setDragDropListener(GUIDragDropListener dragDrop) {
			this.dragDrop = dragDrop;
			return this;
		}
		
		public GUI build() {
			return new GUI(this);
		}
		
	}
	
	public static class GUIBuilderMultiPage<T> extends GUIBuilder {

		private List<Integer> customItemSlots;
		private ItemSupplier<T> supplier;
		
		public GUIBuilderMultiPage(String title, int rows) {
			super(title, rows);
			this.customItemSlots = new ArrayList<>();
		}
		
		public GUIBuilderMultiPage<T> addCustomItemSlots(int... slots) {
			Arrays.stream(slots).forEach(customItemSlots::add);
			return this;
		}
		
		public GUIBuilderMultiPage<T> setSupplier(ItemSupplier<T> supplier){
			this.supplier = supplier;
			return this;
		}
		
		@Override
		public GUIBuilderMultiPage<T> addElement(GUIElement e) {
			super.addElement(e);
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
		
		public GUIBuilderMultiPage<T> addNextPageItem(int slot, ItemStack it){
			return addElement(changePageItem(slot, it, 1));
		}
		
		public GUIBuilderMultiPage<T> addPreviousPageItem(int slot, ItemStack it){
			return addElement(changePageItem(slot, it, -1));
		}
		
		private static GUIElement changePageItem(int slot, ItemStack it, int diff) {
			return new StaticGUIElement(slot, it)
			.setAction(new GUIElementAction() {
				
				@Override
				public boolean action(Player p, ClickAction button, ItemStack clickedWith, Inventory inv, GUI gui) {
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
		
		public abstract ItemStack toItemStack(T item);
		
	}
	
	public static abstract class GUIElement {

		private int slot;
		private GUIElementAction action;
		
		public GUIElement(int slot) {
			this.slot = slot;
		}
		
		public abstract ItemStack getItem(Player p);
		
		public GUIElement setAction(GUIElementAction a) {
			this.action = a;
			return this;
		}
		
	}
	
	public static class StaticGUIElement extends GUIElement{
		
		private ItemStack it;
		
		public StaticGUIElement(int slot, ItemStack it) {
			super(slot);
			this.it = it;
		}

		@Override
		public ItemStack getItem(Player p) {
			return it;
		}
		
	}
	
	public static abstract class GUIElementAction {
		
		public abstract boolean action(Player p, ClickAction button, ItemStack clickedWith, Inventory inv, GUI gui);
		
	}
	
	public static abstract class GUIDragDropListener {
		
		public abstract boolean allowDragDrop(Player p, ItemStack item, Inventory inv, GUI gui);
		
	}
	
	public static abstract class GUIAction {
		
		public abstract boolean action(Player p, ClickAction action, ItemStack clickedWith, GUIElement clickedElement, Inventory inv, GUI gui);
		
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
		
		public GUI(GUIBuilder builder) {
			this.builder = builder;
			this.holder = new GUIHolder(this);
		}
		
		public Inventory getForPlayer(Player p) {
			Inventory inv = Bukkit.createInventory(holder.clone(), builder.size, builder.title);
			for(GUIElement el : builder.elements) {
				inv.setItem(el.slot, el.getItem(p));
			}
			return inv;
		}
		
		public GUIHolder getHolder() {
			return holder;
		}
		
		public GUIElement getElementBySlot(int slot) {
			return builder.elements.stream().filter(e -> e.slot == slot).findFirst().orElse(null);
		}
		
		public List<GUIElement> getElements(){
			return builder.elements;
		}
		
		public GUIAction getAction() {
			return builder.action;
		}
		
		public GUIDragDropListener getDragDropListener() {
			return builder.dragDrop;
		}
		
		public GUIBuilder getBuilder() {
			return builder;
		}
		
	}
	
	public static class GUIMultiPage<T> extends GUI {

		private GUIBuilderMultiPage<T> builder;
		
		public GUIMultiPage(GUIBuilderMultiPage<T> builder) {
			super(builder);
			this.builder = builder;
		}
		
		@Override
		public Inventory getForPlayer(Player p) {
			return getForPlayer(p, 0);
		}
		
		public Inventory getForPlayer(Player p, int page) {
			Inventory base = super.getForPlayer(p);
			GUIHolder holder = (GUIHolder)base.getHolder();
			holder.properties.put("page", page);
			List<Integer> slots = builder.customItemSlots;
			int nSlots = slots.size();
			ItemSupplier<T> supp = builder.supplier;
			List<T> items = supp.getItems();
			int pages = items.size()/nSlots;
			if(page <= pages && page >= 0) {
				int start = page*nSlots;
				int end = (items.size()<=start+nSlots)?items.size():start+nSlots;
				for(int i = start; i < end; i++){
					T item = items.get(i);
					int slot = slots.get(i-start);
					base.setItem(slot, supp.toItemStack(item));
				}
				return base;
			}else {
				return null;
			}
		}
		
		public static int getPage(Inventory inv) {
			if(!(inv.getHolder() instanceof GUIHolder)) {
				throw new IllegalArgumentException("Provided inventory is not a GUIMultiPage");
			}
			GUIHolder holder = (GUIHolder) inv.getHolder();
			if(!(holder.gui instanceof GUIMultiPage<?>)) {
				throw new IllegalArgumentException("Provided inventory is not a GUIMultiPage");
			}
			return (int) holder.properties.get("page");
		}
		
	}
	
	public static class GUIListener implements Listener{
		
		@EventHandler
		public void onInvClick(InventoryClickEvent e) {
			if(e.getClickedInventory() == null) return;
			e.setResult(Result.ALLOW);
			Inventory inv = e.getInventory();
			Player player = (Player) e.getWhoClicked();
			if(inv.getHolder() instanceof GUIHolder) {
				GUI gui = ((GUIHolder)inv.getHolder()).gui;
				ClickAction action = ClickAction.getFromEvent(e);
				if(action==null) {
					e.setCancelled(true);
					return;
				}
				if(e.getClickedInventory().equals(e.getInventory())) {
					//Gui clicked
					int slot = e.getSlot();
					GUIElement elClicked = gui.getElementBySlot(slot);
					if(gui.builder.action != null) {
						boolean cancel = gui.builder.action.action(player, action, e.getCursor(), elClicked, e.getClickedInventory(), gui);
						if(cancel) e.setCancelled(true);
					}
					if(elClicked != null && elClicked.action != null) {
						boolean cancel = elClicked.action.action(player, action, e.getCursor(), e.getClickedInventory(), gui);
						if(cancel) e.setCancelled(true);
					}
				}else {
					//Player inv clicked
					if(gui.builder.dragDrop != null) {
						ItemStack pickedUp = e.getCurrentItem();
						if(pickedUp!=null) {
							if(!gui.builder.dragDrop.allowDragDrop(player, pickedUp, e.getClickedInventory(), gui)) {
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
		private HashMap<String, Object> properties;
		
		public GUIHolder(GUI gui) {
			this(gui, new HashMap<>());
		}
		
		public GUIHolder(GUI gui, HashMap<String, Object> props) {
			this.gui = gui;
			this.properties = props;
		}
		
		public GUI getGui() {
			return gui;
		}
		
		public HashMap<String, Object> getProperties() {
			return properties;
		}
		
		public GUIHolder clone() {
			return new GUIHolder(gui, new HashMap<>(properties));
		}
		
		@Override
		public Inventory getInventory() {
			return gui.getForPlayer(null);
		}
		
	}
	
	

}
