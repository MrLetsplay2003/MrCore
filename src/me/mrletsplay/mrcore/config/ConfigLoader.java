package me.mrletsplay.mrcore.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import me.mrletsplay.mrcore.config.impl.DefaultFileCustomConfig;
import me.mrletsplay.mrcore.io.IOUtils;

public class ConfigLoader {

	public static FileCustomConfig loadFileConfig(File configFile, boolean saveConverted) throws ConfigException {
		return loadConfigFromFile(new DefaultFileCustomConfig(configFile), configFile, saveConverted);
	}

	public static CustomConfig loadStreamConfig(InputStream stream, boolean closeAfterLoad) throws ConfigException {
		return loadConfigFromStream(new DefaultFileCustomConfig(null), stream, closeAfterLoad);
	}
	
	public static <T extends CustomConfig> T loadConfigFromStream(T config, InputStream stream, boolean closeAfterLoad) throws ConfigException {
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
	
	public static <T extends CustomConfig> T loadConfigFromFile(T config, File configFile, boolean saveConverted) throws ConfigException {
		try {
			config.load(configFile);
		}catch(IncompatibleConfigVersionException e) {
			try {
				T c =	 ConfigConverter.convertConfig(IOUtils.readAllBytes(new FileInputStream(configFile)), config, e.getGivenDefaultVersion(), e.getRequiredDefaultVersion());
				if(saveConverted) c.save(configFile);
				return c;
			} catch (IOException e1) {
				throw new ConfigException(e1);
			}
		}
		return config;
	}
	
}
