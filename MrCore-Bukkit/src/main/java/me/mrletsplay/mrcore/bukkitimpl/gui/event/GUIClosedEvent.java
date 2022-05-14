package me.mrletsplay.mrcore.bukkitimpl.gui.event;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.mrletsplay.mrcore.bukkitimpl.gui.GUIHolder;

public class GUIClosedEvent {
	
	private GUIHolder holder;
	private Player player;
	private Inventory inv;
	private boolean cancelled;
	
	public GUIClosedEvent(GUIHolder holder, Player player, Inventory inv) {
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
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
}
