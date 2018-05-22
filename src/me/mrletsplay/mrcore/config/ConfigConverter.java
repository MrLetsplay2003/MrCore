package me.mrletsplay.mrcore.config;

public class ConfigConverter {

	/**
	 * This method will convert a given CustomConfig instance to the specified version<br>
	 * by making a POST request to <a href="https://graphite-official.com/api/mrcore/convert_config.php">The CustomConfig conversion API</a><br>
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
	public static CustomConfig convertVersion(CustomConfig config, ConfigVersion version) {
		return null;
	}
	
	/**
	 * This class is provided as a convenience method when converting between CustomConfig versions
	 * @author MrLetsplay2003
	 */
	public static enum ConfigVersion {
		
		v1_0("1.0");
		
		public final String name;
		
		private ConfigVersion(String name) {
			this.name = name;
		}
		
	}
	
	public static class ConfigConversionException extends RuntimeException {
		
		private static final long serialVersionUID = -5048538918090166448L;

		public ConfigConversionException(String message) {
			super(message);
		}
		
	}
	
}
