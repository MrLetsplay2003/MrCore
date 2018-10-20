package me.mrletsplay.mrcore.config.impl;

import java.io.File;

import me.mrletsplay.mrcore.config.v2.FileCustomConfig;
import me.mrletsplay.mrcore.config.v2.IncompatibleConfigVersionException;

public class ConfigLoader {

	public static FileCustomConfig loadFileConfig(File configFile) {
		FileCustomConfigImpl cc = new FileCustomConfigImpl(configFile);
		try {
			cc.loadFromFile();
		}catch(IncompatibleConfigVersionException e) {
			e.printStackTrace();
		}
		return cc;
	}
	
}
