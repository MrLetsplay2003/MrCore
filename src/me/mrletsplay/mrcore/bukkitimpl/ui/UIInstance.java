package me.mrletsplay.mrcore.bukkitimpl.ui;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * This class represents one instance (one chat message) of an UI for a specific player<br>
 * It also contains an instance-specific property HashMap containing the UI elements, which can also be used to store extra data
 * @author MrLetsplay2003
 */
public class UIInstance {
	
	private String id;
	private Player player;
	private Map<String, Object> properties;
	private UI ui;
	
	public UIInstance(String id, UI ui, Map<String, Object> properties, Player player) {
		this.id = id;
		this.ui = ui;
		this.properties = properties;
		this.player = player;
	}
	
	/**
	 * @return This instance's (unique) instance id
	 */
	public String getID() {
		return id;
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
	 * @return This instance's property HashMap
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	/**
	 * @return The UI this is an instance of
	 */
	public UI getUI() {
		return ui;
	}
	
	/**
	 * @return The player this instance was created for/who was passed in the {@link UI#getForPlayer(Player)} function
	 */
	public Player getPlayer() {
		return player;
	}
	
}
