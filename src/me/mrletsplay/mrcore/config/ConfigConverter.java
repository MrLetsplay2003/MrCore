package me.mrletsplay.mrcore.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;

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
	public static CustomConfig convertToLatestVersion(String config, CustomConfig toConfig, ConfigVersion fromVersion) {
		try {
			return toConfig.loadConfig(new ByteArrayInputStream(convertVersion(config, fromVersion, ConfigVersion.getCurrentVersion()).getBytes()));
		} catch (IOException e) {
			throw new ConfigConversionException("Couldn't convert config", e);
		}
	}
	
	public static CompactCustomConfig convertToLatestCompactVersion(String config, CompactCustomConfig toConfig, ConfigVersion fromVersion) {
		try {
			return (CompactCustomConfig) toConfig.loadConfig(new ByteArrayInputStream(convertVersion(config, fromVersion, ConfigVersion.getCurrentCompactVersion()).getBytes()));
		} catch (IOException e) {
			throw new ConfigConversionException("Couldn't convert config", e);
		}
	}
	
	public static String convertVersion(String configString, ConfigVersion fromVersion, ConfigVersion toVersion) {
		String getURL = "https://graphite-official.com/api/mrcore/convert_config.php?from_version="+fromVersion.name+"&to_version="+toVersion.name;
		try {
			InputStream in = HttpUtils.httpPost(new URL(getURL), "data=" + configString);
			ByteArrayOutputStream nOut = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len;
			while((len = in.read(buf)) > 0) {
				nOut.write(buf, 0, len);
			}
			JSONObject obj = new JSONObject(new String(nOut.toByteArray()));
			if(obj.getBoolean("success")) {
				return obj.getString("data");
			}else {
				throw new ConfigConversionException("Failed to convert config: "+obj.getString("error"));
			}
		}catch(IOException e) {
			throw new ConfigConversionException("Failed to convert config", e);
		}
	}
	
	/**
	 * This class is provided as a convenience method when converting between CustomConfig versions
	 * @author MrLetsplay2003
	 */
	public static enum ConfigVersion {
		
		v1_0("1.0"),
		v1_0_compact("1.0_compact");
		
		public final String name;
		
		private ConfigVersion(String name) {
			this.name = name;
		}
		
		public static ConfigVersion getByName(String name) {
			return Arrays.stream(values()).filter(c -> c.name.equals(name)).findFirst().orElse(null);
		}
		
		public static ConfigVersion getCurrentVersion() {
			return getByName(CustomConfig.getVersion());
		}
		
		public static ConfigVersion getCurrentCompactVersion() {
			return getByName(CompactCustomConfig.getVersion());
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
