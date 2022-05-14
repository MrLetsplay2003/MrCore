package me.mrletsplay.mrcore.bukkitimpl.ui;

import java.util.HashMap;

public class UIBuilder {
	
	private HashMap<String, UIElement> elements;
	private UILayout layout;
	private HashMap<String, Object> properties;
	
	/**
	 * Constructs a UIBuilder
	 * @see <a href="https://github.com/MrLetsplay2003/MrCore/wiki/ChatUI">ChatUI wiki</a>
	 */
	public UIBuilder() {
		elements = new HashMap<>();
		properties = new HashMap<>();
		layout = new UILayout();
	}

	/**
	 * Adds an element to the UI
	 * @param key The key of the element. This should be a unique value
	 * @param element The {@link UIElement} to add under that key
	 * @return This UIBuilder instance
	 */
	public UIBuilder addElement(String key, UIElement element) {
		elements.put(key, element);
		return this;
	}
	
	public HashMap<String, UIElement> getElements() {
		return elements;
	}
	
	/**
	 * Sets the UILayout for the UI
	 * @param layout The {@link UILayout} to use
	 * @return This UIBuilder instance
	 */
	public UIBuilder setLayout(UILayout layout) {
		this.layout = layout;
		return this;
	}
	
	public UILayout getLayout() {
		return layout;
	}
	
	/**
	 * Sets the default properties for this UI<br>
	 * For more info, please see the <a href="https://github.com/MrLetsplay2003/MrCore/wiki/ChatUI">ChatUI wiki</a>
	 * @param properties The property map to use
	 * @return This UIBuilder instance
	 */
	public UIBuilder setProperties(HashMap<String, Object> properties) {
		this.properties = properties;
		return this;
	}
	
	public HashMap<String, Object> getProperties() {
		return properties;
	}
	
	/**
	 * Builds this UIBuilder into a {@link UI} instance
	 * @return The built UI
	 */
	public UI build() {
		return new UI(this);
	}
	
}
