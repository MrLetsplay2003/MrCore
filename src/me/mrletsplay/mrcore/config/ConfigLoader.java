package me.mrletsplay.mrcore.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.bukkitimpl.BukkitCustomConfig;
import me.mrletsplay.mrcore.config.ConfigConverter.ConfigVersion;
import me.mrletsplay.mrcore.config.CustomConfig.ConfigSaveProperty;
import me.mrletsplay.mrcore.config.CustomConfig.InvalidConfigException;
import me.mrletsplay.mrcore.config.CustomConfig.InvalidConfigVersionException;

public class ConfigLoader {

	/**
	 * Loads the given file as a CustomConfig<br>
	 * This method will apply conversion using the {@link ConfigConverter#convertToLatestVersion(String, CustomConfig, ConfigVersion)} method if needed
	 * @param configFile The file to load
	 * @param defaultSaveProperties The default save properties (See {@link CustomConfig#CustomConfig(File, ConfigSaveProperty...)})
	 * @throws InvalidConfigException If an IO error occurs while loading/converting the config
	 * @return A loaded CustomConfig instance
	 */
	public static CustomConfig loadConfig(File configFile, ConfigSaveProperty... defaultSaveProperties) {
		CustomConfig cc = new CustomConfig(configFile, defaultSaveProperties);
		try {
			cc.loadConfigSafely();
			return cc;
		}catch(InvalidConfigVersionException e) {
			try {
				ConfigVersion v = ConfigVersion.getByName(e.getVersion());
				if(v == null) throw new InvalidConfigVersionException("Invalid version: "+e.getVersion(), null);
				CustomConfig cfg = ConfigConverter.convertToLatestVersion(
						Files.readAllLines(configFile.toPath()).stream().collect(Collectors.joining(System.getProperty("line.separator"))),
						cc,
						v);
				cfg.saveConfigSafely();
				return cfg;
			} catch (IOException e1) {
				throw new InvalidConfigException("Failed to load config from file", -1);
			}
		}
	}

	/**
	 * Loads the given InputStream as a CustomConfig<br>
	 * The returned CustomConfig instance will be as loaded from a file with a file parameter of null<br>
	 * This method will apply conversion using the {@link ConfigConverter#convertToLatestVersion(String, CustomConfig, ConfigVersion)} method if needed
	 * @param in The InputStream to load
	 * @param defaultSaveProperties The default save properties (See {@link CustomConfig#CustomConfig(File, ConfigSaveProperty...)})
	 * @throws InvalidConfigException If an IO error occurs while loading/converting the config
	 * @return A loaded CustomConfig instance
	 */
	public static CustomConfig loadConfig(InputStream in, ConfigSaveProperty... defaultSaveProperties) {
		CustomConfig cc = new CustomConfig((File) null, defaultSaveProperties);
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] b = new byte[4096];
			int len;
			while((len = in.read(b)) > 0) {
				out.write(b, 0, len);
			}
			try {
				cc.loadConfig(new ByteArrayInputStream(out.toByteArray()));
				return cc;
			}catch(InvalidConfigVersionException e) {
				ConfigVersion v = ConfigVersion.getByName(e.getVersion());
				if(v == null) throw new InvalidConfigVersionException("Invalid version: "+e.getVersion(), null);
				CustomConfig cfg = ConfigConverter.convertToLatestVersion(
						new String(out.toByteArray(), StandardCharsets.UTF_8),
						cc,
						v);
				return cfg;
			}
		}catch(IOException e2) {
			throw new InvalidConfigException("Failed to load config from InputStream", -1);
		}
	}

	/**
	 * See {@link #loadConfig(File, ConfigSaveProperty...)}
	 */
	public static BukkitCustomConfig loadBukkitConfig(File configFile, ConfigSaveProperty... defaultSaveProperties) {
		BukkitCustomConfig cc = new BukkitCustomConfig(configFile, defaultSaveProperties);
		try {
			return cc.loadConfigSafely();
		}catch(InvalidConfigVersionException e) {
			try {
				ConfigVersion v = ConfigVersion.getByName(e.getVersion());
				if(v == null) throw new InvalidConfigVersionException("Invalid version: "+e.getVersion(), null);
				BukkitCustomConfig cfg = (BukkitCustomConfig) new BukkitCustomConfig(configFile).loadDefault(ConfigConverter.convertToLatestVersion(
						Files.readAllLines(configFile.toPath()).stream().collect(Collectors.joining(System.getProperty("line.separator"))),
						cc,
						v) , true);
				cfg.saveConfigSafely();
				return cfg;
			} catch (IOException e1) {
				throw new InvalidConfigException("Failed to load config from file", -1);
			}
		}
	}

	/**
	 * Loads the given file as a CustomConfig<br>
	 * This method will apply conversion using the {@link ConfigConverter#convertToLatestVersion(String, CustomConfig, ConfigVersion)} method if needed
	 * @param configFile The file to load
	 * @param defaultSaveProperties The default save properties (See {@link CustomConfig#CustomConfig(File, ConfigSaveProperty...)})
	 * @throws InvalidConfigException If an IO error occurs while loading/converting the config
	 * @return A loaded CustomConfig instance
	 */
	public static CompactCustomConfig loadCompactConfig(File configFile, ConfigSaveProperty... defaultSaveProperties) {
		CompactCustomConfig cc = new CompactCustomConfig(configFile, defaultSaveProperties);
		try {
			cc.loadConfigSafely();
			return cc;
		}catch(InvalidConfigVersionException e) {
			try {
				ConfigVersion v = ConfigVersion.getByName(e.getVersion());
				if(v == null) throw new InvalidConfigVersionException("Invalid version: "+e.getVersion(), null);
				return ConfigConverter.convertToLatestCompactVersion(
						Files.readAllLines(configFile.toPath()).stream().collect(Collectors.joining(System.getProperty("line.separator"))),
						cc,
						v);
			} catch (IOException e1) {
				throw new InvalidConfigException("Failed to load config from file", -1);
			}
		}
	}
	
}
