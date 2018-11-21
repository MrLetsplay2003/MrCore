package me.mrletsplay.mrcore.config;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.bukkit.configuration.file.YamlConfiguration;

import me.mrletsplay.mrcore.bukkitimpl.config.BukkitCustomConfig;
import me.mrletsplay.mrcore.http.HttpRequest;
import me.mrletsplay.mrcore.json.JSONObject;

public class ConfigConverter {
	
	public static <T extends CustomConfig> T convertConfig(byte[] config, T toConfig, ConfigVersion fromVersion, ConfigVersion toVersion) {
		toConfig.load(new ByteArrayInputStream(convertVersion(config, fromVersion, toVersion)));
		return toConfig;
	}
	
	public static byte[] convertVersion(byte[] config, ConfigVersion fromVersion, ConfigVersion toVersion) {
		return convertVersion(config, fromVersion.name, toVersion.name);
	}
	
	public static byte[] convertVersion(byte[] config, String fromVersion, String toVersion) {
		String getURL = "http://localhost:8745/api/mrcore/convert_config_v2.php?from_version="+fromVersion+"&to_version="+toVersion;
		JSONObject result = HttpRequest.createPost(getURL)
								.setPostParameter("data", Base64.getEncoder().encodeToString(config))
								.execute()
								.asJSONObject();
		if(result.getBoolean("success")) {
			System.out.println(new String(Base64.getDecoder().decode(result.getString("data")), StandardCharsets.UTF_8));
			return Base64.getDecoder().decode(result.getString("data"));
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
