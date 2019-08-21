package me.mrletsplay.mrcore.bukkitimpl.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import me.mrletsplay.mrcore.bukkitimpl.MrCorePlugin;
import me.mrletsplay.mrcore.bukkitimpl.gui.event.GUIBuildEvent;

public class GUI {
	
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
		Inventory inv = buildInternally(p, null, new HashMap<>());
		if(builder.getBuildAction() != null && !(this instanceof GUIMultiPage)) builder.getBuildAction().onBuild(new GUIBuildEvent((GUIHolder) inv.getHolder(), p, inv));
		return inv;
	}

	/**
	 * @deprecated This will set global properties. Use {@link #getForPlayer(Player, Plugin, Map)} instead
	 * Returns this GUI represented as an inventory for the specified player
	 * @param p The player this inventory is for (Used in all method calls to GUIElements and similar)
	 * @return An inventory representation of this GUI
	 */
	@Deprecated
	public Inventory getForPlayer(Player p, Map<String, Object> properties) {
		Inventory inv = buildInternally(p, null, properties);
		if(builder.getBuildAction() != null && !(this instanceof GUIMultiPage)) builder.getBuildAction().onBuild(new GUIBuildEvent((GUIHolder) inv.getHolder(), p, inv));
		return inv;
	}

	/**
	 * Returns this GUI represented as an inventory for the specified player
	 * @param p The player this inventory is for (Used in all method calls to GUIElements and similar)
	 * @param pl The plugin to set the properties for
	 * @return An inventory representation of this GUI
	 */
	public Inventory getForPlayer(Player p, Plugin pl, Map<String, Object> properties) {
		Inventory inv = buildInternally(p, pl, properties);
		if(builder.getBuildAction() != null && !(this instanceof GUIMultiPage)) builder.getBuildAction().onBuild(new GUIBuildEvent((GUIHolder) inv.getHolder(), p, inv));
		return inv;
	}
	
	protected Inventory buildInternally(Player p, Plugin pl, Map<String, Object> properties) {
		Inventory inv;
		GUIHolder holder = new GUIHolder(this);
		if(pl != null) {
			properties.forEach((k, v) -> holder.setProperty(pl, k, v));
		}else {
			properties.forEach(holder::setProperty);
		}
		
		inv = Bukkit.createInventory(holder, builder.getInventorySize(), builder.getTitle());
		
		GUIBuildEvent event = new GUIBuildEvent(holder, p, inv);
		
		for(Map.Entry<Integer, GUIElement> el : builder.getElements().entrySet()) {
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
		return builder.getDefaultProperties();
	}
	
	/**
	 * @param slot The slot the element is on
	 * @return The element on that slot, null if none
	 */
	public GUIElement getElementBySlot(int slot) {
		return builder.getElements().get(slot);
	}
	
	/**
	 * @return All the elements in this GUI. This does not include page-specific items for {@link GUIMultiPage} GUIs
	 */
	public Map<Integer, GUIElement> getElements(){
		return builder.getElements();
	}
	
	/**
	 * @return The GUIAction listener for this GUI, null if none
	 */
	public GUIActionListener getActionListener() {
		return builder.getActionListener();
	}
	
	/**
	 * @return GUIDragDropListener for this GUI, null if none
	 */
	public GUIDragDropListener getDragDropListener() {
		return builder.getDragDropListener();
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
	 * Refreshes all (opened) instances of this GUI<br>
	 * <br>
	 * This method iterates through every player on the server, sees if they have that specific GUI open<br>
	 * and, if they do, replaces it with a newly built instance of that GUI using {@link #refreshInstance(Player)}<br>
	 * @param condition The condition for the GUI to be refreshed
	 */
	public void refreshAllInstances(Predicate<GUIHolder> condition) {
		for(Player player : Bukkit.getOnlinePlayers()) refreshInstance(player, condition);
	}
	
	/**
	 * Refreshes a specific player's instance of this GUI<br>
	 * If the player doesn't have an this GUI open, this method will do nothing
	 * @param player The player to refresh this GUI for
	 */
	public void refreshInstance(Player player) {
		refreshInstance(player, holder -> true);
	}
	
	/**
	 * Refreshes a specific player's instance of this GUI<br>
	 * If the player doesn't have an this GUI open, this method will do nothing
	 * @param player The player to refresh this GUI for
	 * @param condition The condition for the GUI to be refreshed
	 */
	public void refreshInstance(Player player, Predicate<GUIHolder> condition) {
		Bukkit.getScheduler().runTaskLater(MrCorePlugin.getInstance(), () -> {
			if(player.getOpenInventory() != null && player.getOpenInventory().getTopInventory() != null) {
				Inventory oldInv = player.getOpenInventory().getTopInventory();
				GUIHolder holder = getGUIHolder(oldInv);
				if(holder != null && holder.getGUI().equals(this)) {
					if(condition.test(holder)) openNewInstance(player, oldInv, holder);
				}
			}
		}, 1);
	}
	
	public void closeAllInstances() {
		for(Player player : Bukkit.getOnlinePlayers()) closeInstance(player);
	}
	
	public void closeInstance(Player player) {
		if(hasOpenInstance(player)) player.closeInventory();
	}
	
	// TODO: force synchronous access
	public List<Player> getAllOpenInstances() {
		return Bukkit.getOnlinePlayers().stream().filter(this::hasOpenInstance).collect(Collectors.toList());
	}
	
	public boolean hasOpenInstance(Player player) {
		if(player.getOpenInventory() != null && player.getOpenInventory().getTopInventory() != null) {
			Inventory oldInv = player.getOpenInventory().getTopInventory();
			GUIHolder holder = getGUIHolder(oldInv);
			if(holder != null && holder.getGUI().equals(this)) {
				return true;
			}
		}
		return false;
	}
	
	protected void openNewInstance(Player player, Inventory oldInv, GUIHolder holder) {
		Inventory newInv = getForPlayer(player, holder.getProperties());
		GUIHolder newHolder = getGUIHolder(newInv);
		
		// This should preserve all custom properties while still remaining all the "refreshed" ones
		holder.getProperties().putAll(newHolder.getProperties());
		
		if(builder.getBuildAction() != null) builder.getBuildAction().onBuild(new GUIBuildEvent(holder, player, newInv));
		changeInventory(oldInv, newInv);
	}
	
	/**
	 * Gets a GUI from an inventory
	 * @param inv The inventory to be converted
	 * @return The respective GUI, null if none (not a GUI)
	 */
	public static GUI getGUI(Inventory inv) {
		if(inv.getHolder() instanceof GUIHolder) {
			return ((GUIHolder) inv.getHolder()).getGUI();
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
