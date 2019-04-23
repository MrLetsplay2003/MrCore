package me.mrletsplay.mrcore.config;

import java.io.File;

public interface FileCustomConfig extends CustomConfig {
	
	public default void set(String key, Object value, boolean save) {
		set(key, value);
		save(getConfigFile());
	}
	
	public default void loadFromFile() {
		load(getConfigFile());
	}
	
	public default void saveToFile() {
		save(getConfigFile());
	}
	
	public default void reload(boolean save) {
		if(save) saveToFile();
		loadFromFile();
	}
	
	public File getConfigFile();
	
}
