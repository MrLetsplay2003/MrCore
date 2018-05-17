package me.mrletsplay.mrcore.bukkitimpl;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import me.mrletsplay.mrcore.config.ConfigExpansions.ExpandableCustomConfig;

public class BukkitCustomConfig extends ExpandableCustomConfig {

	public BukkitCustomConfig(File configFile) {
		this(configFile, new ConfigSaveProperty[0]);
	}
	
	public BukkitCustomConfig(File configFile, ConfigSaveProperty... defaultSaveProperties) {
		super(configFile, defaultSaveProperties);
		registerMappers();
	}

	public BukkitCustomConfig(URL configURL) {
		this(configURL, new ConfigSaveProperty[0]);
	}

	public BukkitCustomConfig(URL configURL, ConfigSaveProperty... defaultSaveProperties) {
		super(configURL, defaultSaveProperties);
		registerMappers();
	}
	
	private void registerMappers() {
		registerMapper(new ObjectMapper<Location>(Location.class) {

			@Override
			public Map<String, Object> mapObject(Location loc) {
				HashMap<String, Object> map = new HashMap<>();
				map.put("world", loc.getWorld().getName());
				map.put("x", loc.getX());
				map.put("y", loc.getY());
				map.put("z", loc.getZ());
				map.put("pitch", loc.getPitch());
				map.put("yaw", loc.getYaw());
				return map;
			}

			@Override
			public Location constructObject(Map<String, Object> map) {
				return new Location(
							Bukkit.getWorld((String) map.get("world")),
							(double) map.get("x"),
							(double) map.get("y"),
							(double) map.get("z"),
							(float) map.get("yaw"),
							(float) map.get("pitch")
						);
			}
			
		});
	}
	
	public Location getLocation(String key) {
		return getMappable(key, Location.class);
	}
	
	public Location getLocation(String key, Location defaultVal, boolean applyDefault) {
		Location o = getMappable(key, Location.class);
		if(o != null) return o;
		if(applyDefault) set(key, defaultVal);
		return defaultVal;
	}
	
}
