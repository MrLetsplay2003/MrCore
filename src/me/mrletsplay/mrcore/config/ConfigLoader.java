package me.mrletsplay.mrcore.config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import me.mrletsplay.mrcore.config.impl.DefaultFileCustomConfig;
import me.mrletsplay.mrcore.io.IOUtils;

public class ConfigLoader {

	public static FileCustomConfig loadFileConfig(File configFile, ConfigFlag... flags) throws ConfigException {
		return loadFileConfig(configFile, false, flags);
	}

	public static CustomConfig loadStreamConfig(InputStream stream, ConfigFlag... flags) throws ConfigException {
		return loadStreamConfig(stream, false, flags);
	}

	public static FileCustomConfig loadFileConfig(File configFile, boolean saveConverted, ConfigFlag... flags) throws ConfigException {
		DefaultFileCustomConfig cfg = new DefaultFileCustomConfig(configFile);
		cfg.addFlags(flags);
		return loadConfigFromFile(cfg, configFile, saveConverted);
	}

	public static CustomConfig loadStreamConfig(InputStream stream, boolean closeAfterLoad, ConfigFlag... flags) throws ConfigException {
		DefaultFileCustomConfig cfg = new DefaultFileCustomConfig(null);
		cfg.addFlags(flags);
		return loadConfigFromStream(cfg, stream, closeAfterLoad);
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
				T c = ConfigConverter.convertConfig(IOUtils.readAllBytes(new FileInputStream(configFile)), config, e.getGivenDefaultVersion(), e.getRequiredDefaultVersion());
				if(saveConverted) c.save(configFile);
				return c;
			} catch (IOException e1) {
				throw new ConfigException(e1);
			}
		}
		return config;
	}
	
}
