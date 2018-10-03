package me.mrletsplay.mrcore.config.impl;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import me.mrletsplay.mrcore.config.v2.ConfigException;
import me.mrletsplay.mrcore.config.v2.ConfigSection;
import me.mrletsplay.mrcore.config.v2.FileCustomConfig;

public class FileCustomConfigImpl implements FileCustomConfig {

	private File configFile;
	private ConfigSection mainSection;
	
	public FileCustomConfigImpl(File configFile) {
		this.configFile = configFile;
		this.mainSection = new DefaultConfigSectionImpl(this, null, null);
	}
	
	@Override
	public ConfigSection getMainSection() {
		return mainSection;
	}

	@Override
	public void load(InputStream in) throws ConfigException {
		
	}

	@Override
	public void save(OutputStream out) {
		
	}

	@Override
	public File getConfigFile() {
		return configFile;
	}
	
	
	
}
