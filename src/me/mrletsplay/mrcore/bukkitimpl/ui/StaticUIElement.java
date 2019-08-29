package me.mrletsplay.mrcore.bukkitimpl.ui;

import me.mrletsplay.mrcore.bukkitimpl.ui.event.UIBuildEvent;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * This class represents an interactive UI element whose layout is fixed and will not change
 * @author MrLetsplay2003
 */
public class StaticUIElement extends UIElement{
	
	private BaseComponent[] layout;
	
	/**
	 * Constructs a UI element
	 * @param text The static text layout to use
	 * @see StaticUIElement
	 */
	public StaticUIElement(String text) {
		this.layout = new BaseComponent[] {new TextComponent(text)};
	}
	
	/**
	 * Constructs a UI element
	 * @param layout The static layout to use
	 * @see StaticUIElement
	 */
	public StaticUIElement(BaseComponent... layout) {
		this.layout = layout;
	}
	
	@Override
	public BaseComponent[] getLayout(UIBuildEvent event) {
		return layout;
	}
	
	@Override
	public StaticUIElement setAction(UIElementAction action) {
		super.setAction(action);
		return this;
	}
	
	@Override
	public StaticUIElement setHoverText(String hoverText) {
		super.setHoverText(hoverText);
		return this;
	}
	
}
