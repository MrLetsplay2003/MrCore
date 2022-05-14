package me.mrletsplay.mrcore.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import me.mrletsplay.mrcore.config.impl.DefaultFileCustomConfig;

public class ConfigLoader {

	/**
	 * Loads a config from a file according to the implementation of {@link DefaultFileCustomConfig}
	 * @param configFile The file to load the config from
	 * @param flags The flags to be set for the config
	 * @return The config after it has been loaded
	 * @throws ConfigException If an error occurs while loading the config
	 */
	public static FileCustomConfig loadFileConfig(File configFile, ConfigFlag... flags) throws ConfigException {
		DefaultFileCustomConfig cfg = new DefaultFileCustomConfig(configFile);
		cfg.addFlags(flags);
		cfg.load(configFile);
		return cfg;
	}
	
	/**
	 * Loads a config from a file according to the implementation of {@link DefaultFileCustomConfig}
	 * @param configFile The file to load the config from
	 * @return The config after it has been loaded
	 * @throws ConfigException If an error occurs while loading the config
	 */
	public static FileCustomConfig loadFileConfig(File configFile) throws ConfigException {
		return loadFileConfig(configFile, new ConfigFlag[0]);
	}

	/**
	 * Loads a config from a stream according to the implementation of {@link DefaultFileCustomConfig}.<br>
	 * While the returned type will technically be a {@link FileCustomConfig}, it should not be treated as one, as it has a <code>null</code> file. This behaviour might also be changed in a future version
	 * @param stream The stream to load the config from
	 * @param flags The flags to be set for the config
	 * @return The config after it has been loaded
	 * @throws ConfigException If an error occurs while loading the config
	 */
	public static CustomConfig loadStreamConfig(InputStream stream, ConfigFlag... flags) throws ConfigException {
		return loadStreamConfig(stream, false, flags);
	}

	/**
	 * Loads a config from a stream according to the implementation of {@link DefaultFileCustomConfig}.<br>
	 * While the returned type will technically be a {@link FileCustomConfig}, it should not be treated as one, as it has a <code>null</code> file. This behaviour might also be changed in a future version
	 * @param stream The stream to load the config from
	 * @return The config after it has been loaded
	 * @throws ConfigException If an error occurs while loading the config
	 */
	public static CustomConfig loadStreamConfig(InputStream stream) throws ConfigException {
		return loadStreamConfig(stream, false);
	}

	/**
	 * Loads a config from a stream according to the implementation of {@link DefaultFileCustomConfig} and optionally closes the stream afterwards.<br>
	 * While the returned type will technically be a {@link FileCustomConfig}, it should not be treated as one, as it has a <code>null</code> file. This behaviour might also be changed in a future version
	 * @param stream The stream to load the config from
	 * @param closeAfterLoad Whether to close the stream after loading the config from it
	 * @param flags The flags to be set for the config
	 * @return The config after it has been loaded
	 * @throws ConfigException If an error occurs while loading the config
	 */
	public static CustomConfig loadStreamConfig(InputStream stream, boolean closeAfterLoad, ConfigFlag... flags) throws ConfigException {
		DefaultFileCustomConfig cfg = new DefaultFileCustomConfig(null);
		cfg.addFlags(flags);
		return loadConfigFromStream(cfg, stream, closeAfterLoad);
	}

	/**
	 * Loads a config from a stream according to the implementation of {@link DefaultFileCustomConfig} and optionally closes the stream afterwards.<br>
	 * While the returned type will technically be a {@link FileCustomConfig}, it should not be treated as one, as it has a <code>null</code> file. This behaviour might also be changed in a future version
	 * @param stream The stream to load the config from
	 * @param closeAfterLoad Whether to close the stream after loading the config from it
	 * @return The config after it has been loaded
	 * @throws ConfigException If an error occurs while loading the config
	 */
	public static CustomConfig loadStreamConfig(InputStream stream, boolean closeAfterLoad) throws ConfigException {
		return loadStreamConfig(stream, closeAfterLoad, new ConfigFlag[0]);
	}
	
	/**
	 * Loads the config from the provided stream using {@link CustomConfig#load(InputStream)} and optionally closes the stream afterwards
	 * @param <T> The type of the config
	 * @param config The config to load from the provided stream
	 * @param stream The stream to load the config from
	 * @param closeAfterLoad Whether to close the stream after the config has been loaded from it
	 * @return The same config after calling {@link CustomConfig#load(InputStream)} on it
	 * @throws ConfigException If an error occurs while loading the config or closing the stream
	 */
	public static <T extends CustomConfig> T loadConfigFromStream(T config, InputStream stream, boolean closeAfterLoad) throws ConfigException {
		if(config == null) throw new NullPointerException("The provided config may not be null");
		config.load(stream);
		if(closeAfterLoad) {
			try {
				stream.close();
			} catch (IOException e) {
				throw new ConfigException(e);
			}
		}
		return config;
	}
	
	/**
	 * Just a convenience method which invokes {@link CustomConfig#load(File)} and returns the config.<br>
	 * Unlike {@link #loadFileConfig(File)}, this will take any {@link CustomConfig} type and load it from the provided file
	 * @param <T> The type of the config
	 * @param config The config to load from the provided file
	 * @param configFile The file to load the config from
	 * @return The same config after calling {@link CustomConfig#load(File)} with the provided file on it
	 * @throws NullPointerException If the provided config is null
	 */
	public static <T extends CustomConfig> T loadConfigFromFile(T config, File configFile) throws NullPointerException {
		if(config == null) throw new NullPointerException("The provided config may not be null");
		config.load(configFile);
		return config;
	}
	
	/**
	 * Just a convenience method which invokes {@link FileCustomConfig#loadFromFile()} and returns the config
	 * @param <T> The type of the config
	 * @param config The config to load from its corresponding file, usually set via the constructor
	 * @return The same config after calling {@link FileCustomConfig#loadFromFile()} on it
	 * @throws NullPointerException If the provided config is null
	 */
	public static <T extends FileCustomConfig> T loadFileConfig(T config) throws NullPointerException {
		if(config == null) throw new NullPointerException("The provided config may not be null");
		config.loadFromFile();
		return config;
	}
	
}
