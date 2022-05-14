package me.mrletsplay.mrcore.bukkitimpl.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.mrletsplay.mrcore.bukkitimpl.gui.event.GUIElementActionEvent;

/**
 * @see <a href="https://github.com/MrLetsplay2003/MrCore/wiki/GUIUtils">GUIUtils wiki</a>
 * @author MrLetsplay2003
 */
public class GUIBuilderMultiPage<T> extends GUIBuilder {

	private List<Integer> pageSlots;
	private GUIItemSupplier<T> supplier;
	
	/**
	 * Creates a GUI builder for a multi page GUI
	 * @see <a href="https://github.com/MrLetsplay2003/MrCore/wiki/GUIUtils">GUIUtils wiki</a>
	 * @param title The title for this inventory
	 * @param rows The amount of rows (Should not be more than 6)
	 */
	public GUIBuilderMultiPage(String title, int rows) {
		super(title, rows);
		this.pageSlots = new ArrayList<>();
	}
	
	/**
	 * Adds the specified page slots to the GUI builder
	 * @param slots The specific slots to add
	 * @return This GUIBuilderMultiPage instance
	 */
	public GUIBuilderMultiPage<T> addPageSlots(int... slots) {
		Arrays.stream(slots).forEach(pageSlots::add);
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
		slots.forEach(pageSlots::add);
		return this;
	}
	
	public List<Integer> getPageSlots() {
		return pageSlots;
	}
	
	/**
	 * Sets the {@link GUIItemSupplier} for this multi page GUI
	 * @param supplier The supplier to use
	 * @return This GUIBuilderMultiPage instance
	 */
	public GUIBuilderMultiPage<T> setSupplier(GUIItemSupplier<T> supplier){
		this.supplier = supplier;
		return this;
	}
	
	public GUIItemSupplier<T> getSupplier() {
		return supplier;
	}
	
	@Override
	public GUIBuilderMultiPage<T> addElement(int slot, GUIElement e) {
		super.addElement(slot, e);
		return this;
	}
	
	@Override
	public GUIBuilderMultiPage<T> setActionListener(GUIActionListener a) {
		super.setActionListener(a);
		return this;
	}
	
	@Override
	@Deprecated
	public GUIBuilderMultiPage<T> setDragDropListener(GUIDragDropListener dragDrop) {
		super.setDragDropListener(dragDrop);
		return this;
	}
	
	@Override
	public GUIBuilderMultiPage<T> setPutItemListener(GUIPutItemListener putItemListener) {
		super.setPutItemListener(putItemListener);
		return this;
	}
	
	@Override
	public GUIBuilderMultiPage<T> setTakeItemListener(GUITakeItemListener takeItemListener) {
		super.setTakeItemListener(takeItemListener);
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
					Inventory nPage = guiMP.getForPlayer(event.getPlayer(), pg + diff, event.getGUIHolder().getProperties());
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
	public GUIMultiPage<T> create() {
		return new GUIMultiPage<T>(this);
	}
	
}
