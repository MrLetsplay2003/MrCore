package me.mrletsplay.mrcore.bukkitimpl.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import me.mrletsplay.mrcore.bukkitimpl.gui.event.GUIBuildEvent;
import me.mrletsplay.mrcore.bukkitimpl.gui.event.GUIBuildPageItemEvent;

public class GUIMultiPage<T> extends GUI {

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
		Inventory inv = buildInternally(p, page, null, new HashMap<>());
		if(builder.getBuildAction() != null) builder.getBuildAction().onBuild(new GUIBuildEvent((GUIHolder) inv.getHolder(), p, inv));
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
	 * This will return page 0 of the GUI
	 */
	@Override
	public Inventory getForPlayer(Player p, Plugin pl, Map<String, Object> properties) {
		Inventory inv = buildInternally(p, 0, pl, properties);
		if(builder.getBuildAction() != null) builder.getBuildAction().onBuild(new GUIBuildEvent((GUIHolder) inv.getHolder(), p, inv));
		return inv;
	}
	
	/**
	 * Returns an inventory representation of the specified page of this GUI for a specified player
	 * @param p The player this inventory is for (Used in all method calls to GUIElements and similar)
	 * @param page The page to be used
	 * @return An inventory representation for this GUI, null if the page doesn't exist
	 */
	public Inventory getForPlayer(Player p, int page, Map<String, Object> properties) {
		Inventory inv = buildInternally(p, page, null, properties);
		if(builder.getBuildAction() != null) builder.getBuildAction().onBuild(new GUIBuildEvent((GUIHolder) inv.getHolder(), p, inv));
		return inv;
	}
	
	/**
	 * Returns an inventory representation of the specified page of this GUI for a specified player
	 * @param p The player this inventory is for (Used in all method calls to GUIElements and similar)
	 * @param page The page to be used
	 * @param pl The plugin to set the properties for
	 * @return An inventory representation for this GUI, null if the page doesn't exist
	 */
	public Inventory getForPlayer(Player p, int page, Plugin pl, Map<String, Object> properties) {
		Inventory inv = buildInternally(p, page, pl, properties);
		if(builder.getBuildAction() != null) builder.getBuildAction().onBuild(new GUIBuildEvent((GUIHolder) inv.getHolder(), p, inv));
		return inv;
	}
	
	protected Inventory buildInternally(Player p, int page, Plugin pl, Map<String, Object> properties) {
		Inventory base = super.buildInternally(p, pl, properties);
		GUIHolder holder = (GUIHolder) base.getHolder();
		holder.getProperties().put("page", page);
		List<Integer> slots = builder.getPageSlots();
		int nSlots = slots.size();
		
		GUIBuildEvent event = new GUIBuildEvent(holder, p, base);
		
		GUIItemSupplier<T> supp = builder.getSupplier();
		List<T> items = supp.getItems(event);
		
		Map<Integer, GUIElement> elSlots = new HashMap<>();
		int pages = items.size()/nSlots;
		
		if(page <= pages && page >= 0) {
			int start = page*nSlots;
			int end = (items.size()<=start+nSlots)?items.size():start+nSlots;
			for(int i = start; i < end; i++){
				T rEl = items.get(i);
				GUIBuildPageItemEvent<T> ev = new GUIBuildPageItemEvent<>(holder, p, base, page, items, i, i - start);
				GUIElement el = supp.toGUIElement(ev, rEl);
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
		if(!(holder.getGUI() instanceof GUIMultiPage<?>)) {
			throw new IllegalArgumentException("Provided inventory is not a GUIMultiPage");
		}
		return (int) holder.getProperties().get("page");
	}
	
	@Override
	protected void openNewInstance(Player player, Inventory oldInv, GUIHolder holder) {
		Inventory newInv = getForPlayer(player, (int) holder.getProperty("page"), holder.getProperties());
		GUIHolder newHolder = getGUIHolder(newInv);
		
		// This should preserve all custom properties while still remaining all the "refreshed" ones
		holder.getProperties().putAll(newHolder.getProperties());
		
		if(builder.getBuildAction() != null) builder.getBuildAction().onBuild(new GUIBuildEvent(holder, player, newInv));
		changeInventory(oldInv, newInv);
	}
	
}
