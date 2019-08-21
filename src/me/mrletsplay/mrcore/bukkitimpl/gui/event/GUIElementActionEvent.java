package me.mrletsplay.mrcore.bukkitimpl.gui.event;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.mrletsplay.mrcore.bukkitimpl.gui.GUI;
import me.mrletsplay.mrcore.bukkitimpl.gui.GUIHolder;

public class GUIElementActionEvent {
	
	private Player p;
	private Inventory inv;
	private GUI gui;
	private GUIHolder guiHolder;
	private InventoryClickEvent event;
	private boolean cancelled;
	
	public GUIElementActionEvent(Player p, Inventory inv, GUI gui, GUIHolder holder, InventoryClickEvent event) {
		this.p = p;
		this.inv = inv;
		this.gui = gui;
		this.guiHolder = holder;
		this.event = event;
		this.cancelled = true;
	}
	
	public Player getPlayer() {
		return p;
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
	
	public void refreshInstance() {
		gui.refreshInstance(p);
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
