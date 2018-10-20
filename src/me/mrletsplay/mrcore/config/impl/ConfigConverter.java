package me.mrletsplay.mrcore.config.impl;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.bukkit.configuration.file.YamlConfiguration;

import me.mrletsplay.mrcore.bukkitimpl.config.BukkitCustomConfig;
import me.mrletsplay.mrcore.config.v2.CustomConfig;
import me.mrletsplay.mrcore.http.HttpRequest;
import me.mrletsplay.mrcore.json.JSONObject;

public class ConfigConverter {

	/**
	 * This method will convert a given CustomConfig instance to the specified version<br>
	 * by making a POST request to <a href="http://graphite-official.com/api/mrcore/convert_config.php">The CustomConfig conversion API</a><br>
	 * This can be used to provide compatability inbetween version changes and thus provides a better experience to the user<br>
	 * The API will be kept up to date with the latest config versions<br>
	 * Note: The config version will only be changed when larger changes to the algorithm are made<br>
	 * Classes extending the CustomConfig with custom saving algorithms will not work with this API<br>
	 * Customized CustomConfig instances will also not work<br>
	 * The value returned by this method will always be a non-modified CustomConfig/CompactCustomConfig instance (default comment prefix, spl string, etc.)
	 * @param config The config to convert
	 * @param version The {@link ConfigVersion} to convert to
	 * @throws ConfigConversionException If the config provided is not supported by the API or a conversion error occurs
	 * @return
	 */
	public static CustomConfig convertConfig(String config, CustomConfig toConfig, ConfigVersion fromVersion, ConfigVersion toVersion) {
		toConfig.load(new ByteArrayInputStream(convertVersion(config, fromVersion, toVersion).getBytes(StandardCharsets.UTF_8)));
		return toConfig;
	}
	
	public static String convertVersion(String configString, ConfigVersion fromVersion, ConfigVersion toVersion) {
		return convertVersion(configString, fromVersion.name, toVersion.name);
	}
	
	public static String convertVersion(String configString, String fromVersion, String toVersion) {
		String getURL = "https://graphite-official.com/api/mrcore/convert_config.php?from_version="+fromVersion+"&to_version="+toVersion;
		JSONObject result = HttpRequest.createPost(getURL)
								.setPostParameter("data", configString)
								.execute()
								.asJSONObject();
		if(result.getBoolean("success")) {
			return result.getString("data");
		}else {
			throw new ConfigConversionException("Failed to convert config: " + result.getString("error"));
		}
	}
	
	/**
	 * Converts a bukkit YamlConfiguration to a BukkitCustomConfig<br>
	 * by making a POST request to <a href="http://graphite-official.com/api/mrcore/convert_yaml.php">The YAML conversion API</a>
	 * @param bukkitConfig The bukkit YamlConfiguration instance to convert
	 * @param saveTo The BukkitCustomConfig to save the properties to
	 * @return The <code>saveTo</code> parameter with the properties set to the ones of the bukkit config
	 */
	public static BukkitCustomConfig convertYaml(YamlConfiguration bukkitConfig, BukkitCustomConfig saveTo) {
		String getURL = "https://graphite-official.com/api/mrcore/convert_yaml.php";
		String cfgString = bukkitConfig.saveToString();
		JSONObject result = HttpRequest.createPost(getURL)
				.setPostParameter("data", cfgString)
				.execute()
				.asJSONObject();
		if(result.getBoolean("success")) {
			saveTo.load(new ByteArrayInputStream(result.getString("data").getBytes(StandardCharsets.UTF_8)));
			return saveTo;
		}else {
			throw new ConfigConversionException("Failed to convert config: " + result.getString("error"));
		}
	}
	
	public static class ConfigConversionException extends RuntimeException {
		
		private static final long serialVersionUID = -5048538918090166448L;

		public ConfigConversionException(String message) {
			super(message);
		}

		public ConfigConversionException(String message, Exception cause) {
			super(message, cause);
		}
		
	}
	
}
