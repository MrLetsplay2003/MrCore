package me.mrletsplay.mrcore.bukkitimpl.gui.event;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.mrletsplay.mrcore.bukkitimpl.gui.GUI;
import me.mrletsplay.mrcore.bukkitimpl.gui.GUIHolder;

public class GUIDragDropEvent {
	
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
