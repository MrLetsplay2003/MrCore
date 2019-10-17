package me.mrletsplay.mrcore.bukkitimpl.gui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import me.mrletsplay.mrcore.misc.Builder;

public class GUIBuilder implements Builder<GUI, GUIBuilder>{
	
	private String title;
	private int inventorySize;
	private Map<Integer, GUIElement> elements;
	private GUIDragDropListener dragDropListener;
	private GUIClosedListener closedListener;
	private GUIActionListener actionListener;
	private Map<String, Object> defaultProperties = new HashMap<>();
	private GUIBuildAction buildAction;
	
	/**
	 * Creates a GUI builder for a single page GUI
	 * @see <a href="https://github.com/MrLetsplay2003/MrCore/wiki/GUIUtils">GUIUtils wiki</a>
	 * @param title The title for the GUI (inventory)
	 * @param rows The amount of rows of the GUI (inventory). Should not be more than 6
	 */
	public GUIBuilder(String title, int rows) {
		this.title = title;
		this.inventorySize = rows * 9;
		this.elements = new HashMap<>();
		this.defaultProperties = new HashMap<>();
	}

	public String getTitle() {
		return title;
	}
	
	public int getInventorySize() {
		return inventorySize;
	}
	
	/**
	 * Adds an element to the GUI<br>
	 * If there's already an element in this slot, it will be overridden
	 * @param slot The slot to add the element to
	 * @param e The element to add
	 * @return This GUIBuilder instance
	 */
	public GUIBuilder addElement(int slot, GUIElement e) {
		if(e != null) {
			elements.put(slot, e);
		}else {
			elements.remove(slot);
		}
		return this;
	}
	
	public Map<Integer, GUIElement> getElements() {
		return elements;
	}
	
	/**
	 * Sets the {@link GUIActionListener} listener for this GUI<br>
	 * If there's already a listener registered, it will be overridden
	 * @param actionListener The action listener to use
	 * @return This GUIBuilder instance
	 */
	public GUIBuilder setActionListener(GUIActionListener actionListener) {
		this.actionListener = actionListener;
		return this;
	}
	
	public GUIActionListener getActionListener() {
		return actionListener;
	}
	
	/**
	 * Sets the {@link GUIDragDropListener} for this GUI<br>
	 * If there's already a listener registered, it will be overridden
	 * @param dragDropListener The listener to use
	 * @return This GUIBuilder instance
	 */
	public GUIBuilder setDragDropListener(GUIDragDropListener dragDropListener) {
		this.dragDropListener = dragDropListener;
		return this;
	}
	
	public GUIDragDropListener getDragDropListener() {
		return dragDropListener;
	}
	
	/**
	 * Sets the {@link GUIClosedListener} for this GUI<br>
	 * If there's already a listener registered, it will be overridden
	 * @param closedListener The listener to use
	 * @return This GUIBuilder instance
	 */
	public GUIBuilder setClosedListener(GUIClosedListener closedListener) {
		this.closedListener = closedListener;
		return this;
	}
	
	public GUIClosedListener getClosedListener() {
		return closedListener;
	}
	
	/**
	 * Sets the default properties for this GUIBuilder
	 * @param defaultProperties The properties to use
	 */
	public void setDefaultProperties(Map<String, Object> defaultProperties) {
		this.defaultProperties = defaultProperties;
	}
	
	/**
	 * Adds a default property for this GUIBuilder
	 * @param key The key of the property
	 * @param value the value of the property
	 */
	public void addDefaultProperty(String key, Object value) {
		defaultProperties.put(key, value);
	}
	
	public Map<String, Object> getDefaultProperties() {
		return defaultProperties;
	}
	
	/**
	 * Sets the build action/build listener for this instance<br>
	 * This can be used to add instance-specific items to the GUI when the GUI is built or refreshed<br>
	 * Note: This is not called when the {@link #create()} method is used but rather when the {@link GUI#getForPlayer(Player)} method is called
	 * @param buildAction The buid action listener to use
	 */
	public GUIBuilder setBuildAction(GUIBuildAction buildAction) {
		this.buildAction = buildAction;
		return this;
	}
	
	public GUIBuildAction getBuildAction() {
		return buildAction;
	}
	
	
	/**
	 * Builds this GUIBuilder into a functioning GUI
	 * @return The GUI that was built
	 */
	@Override
	public GUI create() throws IllegalStateException {
		return new GUI(this);
	}
	
}
