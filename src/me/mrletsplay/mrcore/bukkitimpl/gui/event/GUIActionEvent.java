package me.mrletsplay.mrcore.bukkitimpl.gui.event;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.mrletsplay.mrcore.bukkitimpl.gui.GUI;
import me.mrletsplay.mrcore.bukkitimpl.gui.GUIElement;
import me.mrletsplay.mrcore.bukkitimpl.gui.GUIHolder;

public class GUIActionEvent {
	
	private Player p;
	private GUIElement elClicked;
	private Inventory inv;
	private GUI gui;
	private GUIHolder guiHolder;
	private InventoryClickEvent event;
	private boolean cancelled;
	
	public GUIActionEvent(Player p, GUIElement elClicked, Inventory inv, GUI gui, GUIHolder holder, InventoryClickEvent event) {
		this.p = p;
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
	
	public ClickType getClickType() {
		return event.getClick();
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
