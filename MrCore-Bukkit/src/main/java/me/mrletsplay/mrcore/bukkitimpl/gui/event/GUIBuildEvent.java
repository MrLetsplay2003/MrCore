package me.mrletsplay.mrcore.bukkitimpl.gui.event;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.mrletsplay.mrcore.bukkitimpl.gui.GUI;
import me.mrletsplay.mrcore.bukkitimpl.gui.GUIHolder;

public class GUIBuildEvent {
	
	private GUIHolder holder;
	private Player player;
	private Inventory inv;
	
	public GUIBuildEvent(GUIHolder holder, Player player, Inventory inv) {
		this.holder = holder;
		this.player = player;
		this.inv = inv;
	}
	
	public GUIHolder getGUIHolder() {
		return holder;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Inventory getInventory() {
		return inv;
	}
	
	public GUI getGUI() {
		return holder.getGUI();
	}
	
}
