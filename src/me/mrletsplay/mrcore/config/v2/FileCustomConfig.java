package me.mrletsplay.mrcore.config.v2;

import java.io.File;

public interface FileCustomConfig extends CustomConfig {
	
	public default void set(String key, Object value, boolean save) {
		set(key, value);
		save(getConfigFile());
	}
	
	public File getConfigFile();
	
}
