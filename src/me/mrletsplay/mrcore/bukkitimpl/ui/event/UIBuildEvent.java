package me.mrletsplay.mrcore.bukkitimpl.ui.event;

import org.bukkit.entity.Player;

import me.mrletsplay.mrcore.bukkitimpl.ui.UIInstance;

public class UIBuildEvent {
	
	private UIInstance ui;
	private Player p;
	
	public UIBuildEvent(UIInstance ui, Player p) {
		this.ui = ui;
		this.p = p;
	}
	
	public UIInstance getUIInstance() {
		return ui;
	}
	
	public Player getPlayer() {
		return p;
	}
	
}
