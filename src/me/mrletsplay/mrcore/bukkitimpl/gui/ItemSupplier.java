package me.mrletsplay.mrcore.bukkitimpl.gui;

import java.util.List;

import me.mrletsplay.mrcore.bukkitimpl.gui.event.GUIBuildEvent;
import me.mrletsplay.mrcore.bukkitimpl.gui.event.GUIBuildPageItemEvent;

public interface ItemSupplier<T> {
	
	public GUIElement toGUIElement(GUIBuildPageItemEvent<T> event, T item);
	
	public List<T> getItems(GUIBuildEvent event);
	
}
