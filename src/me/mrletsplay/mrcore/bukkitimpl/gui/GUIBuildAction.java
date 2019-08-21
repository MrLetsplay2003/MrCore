package me.mrletsplay.mrcore.bukkitimpl.gui;

import me.mrletsplay.mrcore.bukkitimpl.gui.event.GUIBuildEvent;

@FunctionalInterface
public interface GUIBuildAction {
	
	public void onBuild(GUIBuildEvent event);
	
}
