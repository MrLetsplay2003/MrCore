package me.mrletsplay.mrcore.bukkitimpl.gui;

import me.mrletsplay.mrcore.bukkitimpl.gui.event.GUIClosedEvent;

@FunctionalInterface
public interface GUIClosedListener {
	
	public void onClosed(GUIClosedEvent event);
	
}
