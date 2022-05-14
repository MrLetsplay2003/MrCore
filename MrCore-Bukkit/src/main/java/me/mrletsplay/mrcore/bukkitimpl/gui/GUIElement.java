package me.mrletsplay.mrcore.bukkitimpl.gui;

import org.bukkit.inventory.ItemStack;

import me.mrletsplay.mrcore.bukkitimpl.gui.event.GUIBuildEvent;

public abstract class GUIElement {

	private GUIElementAction action;
	
	public abstract ItemStack getItem(GUIBuildEvent event);
	
	/**
	 * Sets the {@link GUIElementAction} for this element
	 * @param a The action to be called
	 * @return This GUIElement instance
	 */
	public GUIElement setAction(GUIElementAction a) {
		this.action = a;
		return this;
	}
	
	public GUIElementAction getAction() {
		return action;
	}
	
}
