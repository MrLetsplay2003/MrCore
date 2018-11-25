package me.mrletsplay.mrcore.config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import me.mrletsplay.mrcore.config.impl.DefaultFileCustomConfig;
import me.mrletsplay.mrcore.io.IOUtils;

public class ConfigLoader {

	public static FileCustomConfig loadFileConfig(File configFile) throws ConfigException {
		return loadFileConfig(configFile, false);
	}

	public static CustomConfig loadStreamConfig(InputStream stream) throws ConfigException {
		return loadStreamConfig(stream, false);
	}

	public static FileCustomConfig loadFileConfig(File configFile, boolean saveConverted) throws ConfigException {
		return loadConfigFromFile(new DefaultFileCustomConfig(configFile), configFile, saveConverted);
	}

	public static CustomConfig loadStreamConfig(InputStream stream, boolean closeAfterLoad) throws ConfigException {
		return loadConfigFromStream(new DefaultFileCustomConfig(null), stream, closeAfterLoad);
	}
	
	public static <T extends CustomConfig> T loadConfigFromStream(T config, InputStream stream, boolean closeAfterLoad) throws ConfigException {
		byte[] b;
		try {
			b = IOUtils.readAllBytes(stream);
			if(closeAfterLoad) stream.close();
		} catch (IOException e) {
			throw new ConfigException(e);
		}
		try {
			config.load(new ByteArrayInputStream(b));
		}catch(IncompatibleConfigVersionException e) {
				config.clear();
				return ConfigConverter.convertConfig(b, config, e.getGivenDefaultVersion(), e.getRequiredDefaultVersion());
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
