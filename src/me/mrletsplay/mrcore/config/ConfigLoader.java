package me.mrletsplay.mrcore.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import me.mrletsplay.mrcore.config.impl.DefaultFileCustomConfig;
import me.mrletsplay.mrcore.io.IOUtils;

public class ConfigLoader {

	public static FileCustomConfig loadFileConfig(File configFile) throws ConfigException {
		DefaultFileCustomConfig cc = new DefaultFileCustomConfig(configFile);
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
		DefaultFileCustomConfig cc = new DefaultFileCustomConfig(null);
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
