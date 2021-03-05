package me.mrletsplay.mrcore.bukkitimpl;

import java.io.File;

import me.mrletsplay.mrcore.config.ConfigLoader;
import me.mrletsplay.mrcore.config.FileCustomConfig;

public class MrCoreConfig {

	private FileCustomConfig config;
	
	public MrCoreConfig() {
		config = ConfigLoader.loadFileConfig(new File(MrCorePlugin.getInstance().getDataFolder(), "config.yml"));
		config.addDefault("versioning.check-update", true);
		config.addDefault("versioning.version-to-use", "latest");
		config.addDefault("nms-version", "auto");
		
		config.applyDefaults();
		config.saveToFile();
	}
	
	public boolean isUpdateCheckEnabled() {
		return config.getBoolean("versioning.check-update");
	}
	
	public String getVersionToUse() {
		return config.getString("versioning.version-to-use", "latest", true);
	}
	
	public String getNMSVersion() {
		return config.getString("nms-version", "auto", true);
	}
	
}
