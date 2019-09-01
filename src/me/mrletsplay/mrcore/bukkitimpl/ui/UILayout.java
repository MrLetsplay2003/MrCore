package me.mrletsplay.mrcore.bukkitimpl.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	private static final Pattern TOKEN_PATTERN = Pattern.compile("\\{(?<token>.+?)\\}");
	
	public static UILayout of(String formatString) {
		UILayout layout = new UILayout();
		String rest = formatString;
		Matcher m;
		while((m = TOKEN_PATTERN.matcher(rest)).find()) {
			String pre = rest.substring(0, m.start());
			appendText(layout, pre);
			layout.addElement(m.group("token"));
			rest = rest.substring(m.end());
		}
		appendText(layout, rest);
		return layout;
	}
	
	private static void appendText(UILayout layout, String text) {
		String[] lines = text.split("\n", -1);
		layout.addText(lines[0]);
		for(int i = 1; i < lines.length; i++) {
			layout.newLine();
			layout.addText(lines[i]);
		}
	}
	
}
