package me.mrletsplay.mrcore.bukkitimpl.ui;

import java.util.List;

import me.mrletsplay.mrcore.bukkitimpl.ui.event.UIBuildPageItemEvent;

public interface UIItemSupplier<T> {
	
	public List<T> getItems();
	
	public UIElement toUIElement(UIBuildPageItemEvent<T> event, T item);
	
}
