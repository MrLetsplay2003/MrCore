package me.mrletsplay.mrcore.bukkitimpl.gui;

import me.mrletsplay.mrcore.bukkitimpl.gui.event.GUIElementActionEvent;

@FunctionalInterface
public interface GUIElementAction {
	
	public void onAction(GUIElementActionEvent event);
	
}
