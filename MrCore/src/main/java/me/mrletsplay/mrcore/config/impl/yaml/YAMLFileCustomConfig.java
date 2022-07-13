package me.mrletsplay.mrcore.config.impl.yaml;

import java.io.File;

import me.mrletsplay.mrcore.config.FileCustomConfig;

public class YAMLFileCustomConfig extends YAMLCustomConfig implements FileCustomConfig {

	private File configFile;

	public YAMLFileCustomConfig(File configFile) {
		this.configFile = configFile;
	}

	@Override
	public File getConfigFile() {
		return configFile;
	}

}
