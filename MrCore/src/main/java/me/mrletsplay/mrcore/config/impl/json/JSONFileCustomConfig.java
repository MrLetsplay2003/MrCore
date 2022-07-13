package me.mrletsplay.mrcore.config.impl.json;

import java.io.File;

import me.mrletsplay.mrcore.config.FileCustomConfig;

public class JSONFileCustomConfig extends JSONCustomConfig implements FileCustomConfig {

	private File configFile;

	public JSONFileCustomConfig(File configFile) {
		this.configFile = configFile;
	}

	@Override
	public File getConfigFile() {
		return configFile;
	}

}
