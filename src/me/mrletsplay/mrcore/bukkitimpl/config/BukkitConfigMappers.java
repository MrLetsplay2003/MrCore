package me.mrletsplay.mrcore.bukkitimpl.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import me.mrletsplay.mrcore.config.v2.JSONObjectMapper;
import me.mrletsplay.mrcore.json.JSONObject;

public class BukkitConfigMappers {

	public static final JSONObjectMapper<Location> LOCATION_MAPPER = JSONObjectMapper.create(Location.class, (c, loc) -> {
		JSONObject o = new JSONObject();
		o.put("world", loc.getWorld().getName());
		o.put("x", loc.getX());
		o.put("y", loc.getY());
		o.put("z", loc.getZ());
		o.put("pitch", loc.getPitch());
		o.put("yaw", loc.getYaw());
		return o;
	}, (c, o) -> {
		return new Location(Bukkit.getWorld(o.getString("world")), o.getDouble("x"), o.getDouble("y"), o.getDouble("z"));
	});
	
}
