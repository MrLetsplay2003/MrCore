package me.mrletsplay.mrcore.bukkitimpl.ui;

import java.util.Map;

import org.bukkit.entity.Player;

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
