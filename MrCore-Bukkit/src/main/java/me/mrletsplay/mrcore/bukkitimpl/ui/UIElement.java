package me.mrletsplay.mrcore.bukkitimpl.ui;

import me.mrletsplay.mrcore.bukkitimpl.ui.event.UIBuildEvent;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * This class represents a flexible, interactive UI element whose layout is dependent on the player (or any other own factors)
 * @author MrLetsplay2003
 */
public abstract class UIElement {
	
	private UIElementAction action;
	private String hoverText;
	
	public abstract BaseComponent[] getLayout(UIBuildEvent event);
	
	/**
	 * Sets the action for this UIElement<br>
	 * The action will be called when this element is clicked in chat
	 * @param action The action to use
	 * @return This UIElement instance
	 */
	public UIElement setAction(UIElementAction action) {
		this.action = action;
		return this;
	}
	
	public UIElementAction getAction() {
		return action;
	}
	
	/**
	 * Sets the hover text to be displayed when the player hovers over it in chat
	 * @param hoverText The hover text to use
	 * @return This UIElement instance
	 */
	public UIElement setHoverText(String hoverText) {
		this.hoverText = hoverText;
		return this;
	}
	
	public String getHoverText() {
		return hoverText;
	}
	
}
