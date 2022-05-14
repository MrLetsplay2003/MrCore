package me.mrletsplay.mrcore.bukkitimpl.gui;

import me.mrletsplay.mrcore.bukkitimpl.gui.event.GUIDragDropEvent;

/**
 * @deprecated Use {@link GUIPutItemListener} instead
 */
@Deprecated
@FunctionalInterface
public interface GUIDragDropListener {
	
	public void onDragDrop(GUIDragDropEvent event);
	
}
