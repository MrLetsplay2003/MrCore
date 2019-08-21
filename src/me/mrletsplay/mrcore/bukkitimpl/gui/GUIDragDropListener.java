package me.mrletsplay.mrcore.bukkitimpl.gui;

import me.mrletsplay.mrcore.bukkitimpl.gui.event.GUIDragDropEvent;

@FunctionalInterface
public interface GUIDragDropListener {
	
	public void onDragDrop(GUIDragDropEvent event);
	
}
