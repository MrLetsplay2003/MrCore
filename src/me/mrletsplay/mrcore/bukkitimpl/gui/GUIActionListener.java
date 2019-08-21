package me.mrletsplay.mrcore.bukkitimpl.gui;

import me.mrletsplay.mrcore.bukkitimpl.gui.event.GUIActionEvent;

@FunctionalInterface
public interface GUIActionListener {

	public void onAction(GUIActionEvent event);
	
}
