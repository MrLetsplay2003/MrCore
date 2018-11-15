package me.mrletsplay.mrcore.config.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import me.mrletsplay.mrcore.config.v2.ConfigException;
import me.mrletsplay.mrcore.config.v2.CustomConfig;
import me.mrletsplay.mrcore.config.v2.FileCustomConfig;
import me.mrletsplay.mrcore.config.v2.IncompatibleConfigVersionException;
import me.mrletsplay.mrcore.io.IOUtils;

public class ConfigLoader {

	public static FileCustomConfig loadFileConfig(File configFile) throws ConfigException {
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

	public static CustomConfig loadStreamConfig(InputStream stream, boolean closeAfterLoad) throws ConfigException {
		FileCustomConfigImpl cc = new FileCustomConfigImpl(null);
		try {
			cc.load(stream);
			if(closeAfterLoad) stream.close();
		}catch(IncompatibleConfigVersionException e) {
			try {
				return ConfigConverter.convertConfig(IOUtils.readAllBytes(stream), cc, e.getGivenDefaultVersion(), e.getRequiredDefaultVersion());
			} catch (IOException e1) {
				throw new ConfigException(e1);
			}
		}catch(IOException e) {
			throw new ConfigException(e);
		}
		return cc;
	}
	
	public static <T extends CustomConfig> T loadConfigFromStream(T config, InputStream stream, boolean closeAfterLoad) {
		try {
			config.load(stream);
			if(closeAfterLoad) stream.close();
		}catch(IncompatibleConfigVersionException e) {
			try {
				config.clear();
				return ConfigConverter.convertConfig(IOUtils.readAllBytes(stream), config, e.getGivenDefaultVersion(), e.getRequiredDefaultVersion());
			} catch (IOException e1) {
				throw new ConfigException(e1);
			}
		}catch(IOException e) {
			throw new ConfigException(e);
		}
		return config;
	}
	
	public static <T extends CustomConfig> T loadConfigFromFile(T config, File configFile, boolean closeAfterLoad) {
		try {
			config.load(configFile);
		}catch(IncompatibleConfigVersionException e) {
			try {
				return ConfigConverter.convertConfig(IOUtils.readAllBytes(new FileInputStream(configFile)), config, e.getGivenDefaultVersion(), e.getRequiredDefaultVersion());
			} catch (IOException e1) {
				throw new ConfigException(e1);
			}
		}
		return config;
	}
	
}
