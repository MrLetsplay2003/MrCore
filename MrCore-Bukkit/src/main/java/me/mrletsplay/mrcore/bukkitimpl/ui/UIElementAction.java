package me.mrletsplay.mrcore.bukkitimpl.ui;

import me.mrletsplay.mrcore.bukkitimpl.ui.event.UIElementActionEvent;

/**
 * This class is used to add actions to ui elements when you click on them
 * @author MrLetsplay2003
 */
@FunctionalInterface
public interface UIElementAction {
	
	public abstract void action(UIElementActionEvent event);
	
}
