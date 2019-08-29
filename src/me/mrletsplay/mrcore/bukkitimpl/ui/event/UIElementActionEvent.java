package me.mrletsplay.mrcore.bukkitimpl.ui.event;

import org.bukkit.entity.Player;

import me.mrletsplay.mrcore.bukkitimpl.ui.UIElementAction;
import me.mrletsplay.mrcore.bukkitimpl.ui.UIInstance;

/**
 * This is the event passed to {@link UIElementAction#action(UIElementActionEvent)} when the corresponding element is clicked
 * @author MrLetsplay2003
 */
public class UIElementActionEvent {
	
	private UIInstance ui;
	private Player p;
	
	public UIElementActionEvent(UIInstance ui, Player p) {
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
