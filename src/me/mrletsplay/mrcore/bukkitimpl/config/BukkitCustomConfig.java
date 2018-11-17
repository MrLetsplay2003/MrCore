package me.mrletsplay.mrcore.bukkitimpl.config;

import java.io.File;

import org.bukkit.Location;

import me.mrletsplay.mrcore.config.impl.FileCustomConfigImpl;
import me.mrletsplay.mrcore.misc.Complex;

public class BukkitCustomConfig extends FileCustomConfigImpl {

	public BukkitCustomConfig(File configFile) {
		super(configFile);
		registerMapper(BukkitConfigMappers.LOCATION_MAPPER);
		registerMapper(BukkitConfigMappers.ITEM_MAPPER);
	}
	
	public Location getLocation(String key) {
		return getComplex(key, Complex.value(Location.class));
	}
	
	public Location getLocation(String key, Location defaultValue, boolean applyDefault) {
		return getComplex(key, Complex.value(Location.class), defaultValue, applyDefault);
	}

}
