package me.mrletsplay.mrcore.bukkitimpl.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import me.mrletsplay.mrcore.config.v2.JSONObjectMapper;
import me.mrletsplay.mrcore.config.v2.builder.JSONMapperBuilder;
import me.mrletsplay.mrcore.misc.Complex;

public class BukkitConfigMappers {
	
	public static final JSONObjectMapper<Location> LOCATION_MAPPER = new JSONMapperBuilder<>(Complex.value(Location.class),
			(sec, json) -> new Location(
						Bukkit.getWorld(json.getString("world")),
						json.getDouble("x"),
						json.getDouble("y"),
						json.getDouble("z"),
						json.has("pitch") ? (float) json.getDouble("pitch") : 0f,
						json.has("yaw") ? (float) json.getDouble("yaw") : 0f
					)
			)
			.mapDouble("x", Location::getX, null).then()
			.mapDouble("y", Location::getY, null).then()
			.mapDouble("z", Location::getZ, null).then()
			.mapDouble("pitch", l -> (double) l.getPitch(), null).then()
			.mapDouble("yaw", l -> (double) l.getYaw(), null).then()
			.create();
	
}
