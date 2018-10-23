package me.mrletsplay.mrcore.config.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.config.v2.ConfigException;
import me.mrletsplay.mrcore.config.v2.FileCustomConfig;
import me.mrletsplay.mrcore.config.v2.IncompatibleConfigVersionException;
import me.mrletsplay.mrcore.io.IOUtils;

public class ConfigLoader {

	public static FileCustomConfig loadFileConfig(File configFile) {
		FileCustomConfigImpl cc = new FileCustomConfigImpl(configFile);
		try {
			cc.loadFromFile();
		}catch(IncompatibleConfigVersionException e) {
			try {
				return ConfigConverter.convertConfig(IOUtils.readAllBytes(new FileInputStream(configFile)), cc, e.getGivenDefaultVersion(), e.getRequiredDefaultVersion());
			} catch (IOException e1) {
				throw new ConfigException(e1);
			}
		}
		return cc;
	}
	
}
