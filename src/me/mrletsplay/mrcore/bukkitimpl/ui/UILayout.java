package me.mrletsplay.mrcore.bukkitimpl.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used by UIs to define a basic, flexible layout which will then be parsed into the final chat message<br>
 * This class is only available to the UIBuilder/UI (single page)
 * @author MrLetsplay2003
 */
public class UILayout {
	
	private List<UILayoutElement> elements;
	
	public UILayout() {
		this.elements = new ArrayList<>();
	}
	
	/**
	 * Adds plain text to the layout<br>
	 * This will not be altered in the final message
	 * @param text The text to add
	 * @return This UILayout instance
	 */
	public UILayout addText(String text) {
		elements.add(new UILayoutElement(UILayoutElementType.TEXT).setProperty("text", text));
		return this;
	}
	
	/**
	 * Adds an element to the layout<br>
	 * This will be replaced with the layout of the element defined by the key<br>
	 * If no element with that key is defined, it will not be replaced
	 * @param element The element's key
	 * @return This UILayout instance
	 */
	public UILayout addElement(String element) {
		elements.add(new UILayoutElement(UILayoutElementType.ELEMENT).setProperty("element", element));
		return this;
	}
	
	public List<UILayoutElement> getElements() {
		return elements;
	}
	
	/**
	 * Adds a new line to the message
	 * @return This UILayout instance
	 */
	public UILayout newLine() {
		elements.add(new UILayoutElement(UILayoutElementType.NEWLINE));
		return this;
	}
	
}
