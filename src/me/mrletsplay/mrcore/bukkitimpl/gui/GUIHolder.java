package me.mrletsplay.mrcore.bukkitimpl.gui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

import me.mrletsplay.mrcore.bukkitimpl.MrCorePlugin;

public class GUIHolder implements InventoryHolder {

	private GUI gui;
	private Map<String, Object> properties;
	private int cps = 0;
	private long lastC = 0;
	
	/**
	 * Creates a gui holder for the specified GUI<br>
	 * It will not automatically be registered to the GUI
	 * @param gui The GUI this holder is for
	 * @param properties The property hashmap to use
	 */
	public GUIHolder(GUI gui, Map<String, Object> properties) {
		this.gui = gui;
		this.properties = properties;
		this.properties.putAll(this.gui.getBuilder().getDefaultProperties());
	}
	
	/**
	 * Creates a gui holder for the specified GUI<br>
	 * It will not automatically be registered to the GUI
	 * @param gui The GUI this holder is for
	 */
	public GUIHolder(GUI gui) {
		this.gui = gui;
		this.properties = new HashMap<>();
		this.properties.putAll(this.gui.getBuilder().getDefaultProperties());
	}
	
	/**
	 * @return The GUI instance this holder belongs to
	 */
	public GUI getGUI() {
		return gui;
	}
	
	/**
	 * Sets a property in the property HashMap to a specific value<br>
	 * If you're implementing this into a plugin and don't want to modify a "raw" property (like mrcore_page)<br>
	 * or one from another plugin, use {@link #setProperty(Plugin, String, Object)}
	 * @param key The key of the property
	 * @param value The value of the property
	 */
	public void setProperty(String key, Object value) {
		properties.put(key, value);
	}
	
	/**
	 * Gets a property from the property HashMap<br>
	 * If you're implementing this into a plugin and don't want to modify a "raw" property (like mrcore_page)<br>
	 * or one from another plugin, use {@link #getProperty(Plugin, String)}
	 * @param key The key of the property
	 * @return The value of the property or null if not set
	 */
	public Object getProperty(String key) {
		return properties.get(key);
	}
	
	/**
	 * Sets a property in the property HashMap<br>
	 * This is a convenience method to easily create cross-plugin compatible properties<br>
	 * with the same name
	 * @param key
	 * @param value
	 */
	public void setProperty(Plugin plugin, String key, Object value) {
		properties.put(plugin.getName()+"_"+key, value);
	}
	
	/**
	 * Gets a plugin-specific property from the property HashMap<br>
	 * This is a convenience method to easily create cross-plugin compatible properties<br>
	 * with the same name
	 * @param key The key of the property
	 * @return The value of the property or null if not set
	 */
	public Object getProperty(Plugin plugin, String key) {
		return properties.get(plugin.getName()+"_"+key);
	}
	
	/**
	 * @return The property HashMap for this holder
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	public void cps() {
		cps++;
		if(System.currentTimeMillis() / 1000 == lastC / 1000) {
			if(cps > 10) MrCorePlugin.getInstance().getLogger().warning("Spam of Inventory#close on GUI (#close/sec = " + cps + ", Title = " + gui.getBuilder().getTitle() + ")");
		}else {
			cps = 0;
		}
		lastC = System.currentTimeMillis();
	}
	
	/**
	 * Clones this GUIHolder. This will be called in order to supply individual holders to inventories
	 * @return A copy of this GUIHolder
	 */
	public GUIHolder clone() {
		GUIHolder newH = new GUIHolder(gui, properties);
		newH.properties = new HashMap<>(properties);
		return newH;
	}
	
	/**
	 * @deprecated It is not recommended for this method to be used as it could cause errors in some cases
	 */
	@Override
	@Deprecated
	public Inventory getInventory() {
		return gui.getForPlayer(null);
	}
	
}
