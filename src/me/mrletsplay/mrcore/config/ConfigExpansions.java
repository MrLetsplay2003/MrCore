package me.mrletsplay.mrcore.config;

import java.io.File;
import java.net.URL;

public class ConfigExpansions {
	
	/**
	 * TODO: Bukkit implementation
	 * @author MrLetsplay
	 *
	 */
	public static class BukkitCustomConfig extends CustomConfig {

		public BukkitCustomConfig(File configFile) {
			this(configFile, new ConfigSaveProperty[0]);
		}
		
		public BukkitCustomConfig(File configFile, ConfigSaveProperty[] defaultSaveProperties) {
			super(configFile, defaultSaveProperties);
		}

		public BukkitCustomConfig(URL configURL) {
			this(configURL, new ConfigSaveProperty[0]);
		}

		public BukkitCustomConfig(URL configURL, ConfigSaveProperty[] defaultSaveProperties) {
			super(configURL, defaultSaveProperties);
		}
		
		

	}
	
}
