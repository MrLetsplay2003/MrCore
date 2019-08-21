package me.mrletsplay.mrcore.bukkitimpl.gui;

import org.bukkit.inventory.ItemStack;

import me.mrletsplay.mrcore.bukkitimpl.gui.event.GUIBuildEvent;

public class StaticGUIElement extends GUIElement {
	
	private ItemStack it;
	
	public StaticGUIElement(ItemStack it) {
		this.it = it;
	}

	@Override
	public ItemStack getItem(GUIBuildEvent event) {
		return it;
	}
	
	@Override
	public StaticGUIElement setAction(GUIElementAction a) {
		super.setAction(a);
		return this;
	}
	
}
