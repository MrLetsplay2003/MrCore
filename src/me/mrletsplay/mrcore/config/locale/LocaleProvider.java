package me.mrletsplay.mrcore.config.locale;

import java.io.File;

import me.mrletsplay.mrcore.config.ConfigLoader;
import me.mrletsplay.mrcore.config.CustomConfig;
import me.mrletsplay.mrcore.config.FileCustomConfig;

/**
 * Provides language files for your application
 * @author MrLetsplay2003
 * @see <a href="https://github.com/MrLetsplay2003/MrCore/wiki/LocaleProvider">LocaleProvider wiki</a>
 */
public class LocaleProvider {
	
	private String defaultLocale;
	private String paramPrefix, paramSuffix;
	private File localeFolder;
	private LocaleNullMode nullMode;
	private CustomLocaleProvider customProvider;
	
	/**
	 * Constructs a locale provider with the given locale folder<br>
	 * This will not allow for custom locales by default<br>
	 * The default parameter format is prefix = "{", suffix = "}"<br>
	 * The default null mode is {@link LocaleNullMode#USE_NULL}
	 * @param localeFolder The folder to save the locales in
	 * @see <a href="https://github.com/MrLetsplay2003/MrCore/wiki/LocaleProvider">LocaleProvider wiki</a>
	 */
	public LocaleProvider(File localeFolder) {
		this.localeFolder = localeFolder;
		this.nullMode = LocaleNullMode.USE_NULL;
		this.paramPrefix = "{";
		this.paramSuffix = "}";
	}
	
	/**
	 * Sets the parameter format<br>
	 * The default format is prefix = "{", suffix = "}"<br>
	 * When getting messages using {@link Locale#getMessage(String, String...)}, parameters of the format<br>
	 * <i>prefix</i> + <i>parameterName</i> + <i>suffix</i> will be replaced
	 * @param prefix The parameter prefix
	 * @param suffix The parameter suffix
	 * @return This LocaleProvider instance
	 */
	public LocaleProvider setParameterFormat(String prefix, String suffix) {
		this.paramPrefix = prefix;
		this.paramSuffix = suffix;
		return this;
	}
	
	/**
	 * Registers a "default" locale<br>
	 * This will automatically create a file in the <i>localeFolder</i>
	 * @param locale The locale key to register this as
	 * @param config A CustomConfig with all the default messages
	 * @return This LocaleProvider instance
	 */
	public LocaleProvider registerLocale(String locale, CustomConfig config) {
		FileCustomConfig tmpCfg = ConfigLoader.loadFileConfig(getLocaleFile(locale));
		tmpCfg.addDefaults(config);
		tmpCfg.applyDefaults();
		tmpCfg.reload(true);
		return this;
	}
	
	/**
	 * Sets the null mode to use for this provider<br>
	 * The null mode decides what to do when a message is not set in the config<br>
	 * Note: This will not happen if your defaults contain every message
	 * @param mode The null mode to use
	 * @return This LocaleProvider instance
	 * @see LocaleNullMode
	 * @see Locale#getMessage(String)
	 */
	public LocaleProvider useNullMode(LocaleNullMode mode) {
		this.nullMode = mode;
		return this;
	}
	
	/**
	 * Sets the custom locale provider<br>
	 * This will allow users of your application to create own locale files<br>
	 * The locale key is equal to the file name of the custom locale
	 * @param customProvider The custom provider to use
	 * @return This LocaleProvider instance
	 */
	public LocaleProvider setCustomLocaleProvider(CustomLocaleProvider customProvider) {
		this.customProvider = customProvider;
		customProvider.provider = this;
		return this;
	}
	
	/**
	 * This will define the default locale to use for {@link LocaleNullMode#USE_DEFAULT} and for creating/completing custom locales
	 * @param defaultLocale The locale key of the locale to use
	 * @return This LocaleProvider instance
	 */
	public LocaleProvider setDefaultLocale(String defaultLocale) {
		this.defaultLocale = defaultLocale;
		return this;
	}
	
	/**
	 * This will return the locale with the given key<br>
	 * If custom locales are enabled, default ones are prioritized<br>
	 * Also a new custom locale will be created (with the default locale, if defined) if it doesn't exist
	 * @param locale The key of the locale
	 * @return The locale or null if it doesn't exist and custom locales are disabled
	 */
	public Locale getLocale(String locale) {
		FileCustomConfig lConfig = getLocaleConfig(locale);
		if(lConfig == null) {
			if(customProvider != null) {
				return customProvider.getCustomLocale(locale);
			}
			return null;
		}
		return new Locale(this, lConfig);
	}
	
	/**
	 * @return The default locale specified by {@link #setDefaultLocale(String)} or null if none is set
	 */
	public Locale getDefaultLocale() {
		if(defaultLocale == null) return null;
		return getLocale(defaultLocale);
	}
	
	/**
	 * Returns the config file for a locale<br>
	 * This does not include custom locales (if they are not in the same folder)
	 * @param locale The key of the locale
	 * @return The file for a locale
	 */
	public File getLocaleFile(String locale) {
		return new File(localeFolder, locale+".yml");
	}
	
	/**
	 * Returns whether a locale exists<br>
	 * This ignores custom locales (if they are not in the same folder)
	 * @param locale The key of the locale
	 * @return Whether the specified locale exists
	 */
	public boolean existsDefaultLocale(String locale) {
		return getLocaleConfig(locale) != null;
	}
	
	/**
	 * Returns a CustomConfig instance representing a locale<br>
	 * This does not include custom locales (if they are not in the same folder)
	 * @param locale The key of the locale
	 * @return The CustomConfig for that locale or null if it doesn't exist
	 */
	public FileCustomConfig getLocaleConfig(String locale) {
		File localeFile = getLocaleFile(locale);
		if(!localeFile.exists()) {
			return null;
		}
		return ConfigLoader.loadFileConfig(localeFile);
	}
	
	
	/**
	 * This class is used to allow for custom locales in a {@link LocaleProvider}
	 * @author MrLetsplay2003
	 */
	public static class CustomLocaleProvider {
		
		private LocaleProvider provider;
		private File customLocaleFolder;
		
		/**
		 * Creates a CustomLocaleProvider with the given custom locale folder<br>
		 * Custom locales need to be placed in the custom locale folder to be recognized
		 * @param customLocaleFolder The custom locale folder to use
		 */
		public CustomLocaleProvider(File customLocaleFolder) {
			this.customLocaleFolder = customLocaleFolder;
		}
		
		/**
		 * @return The custom locale folder specified in {@link CustomLocaleProvider#CustomLocaleProvider(File) CustomLocaleProvider(File)}
		 */
		public File getCustomLocaleFolder() {
			return customLocaleFolder;
		}
		
		/**
		 * @return The {@link LocaleProvider} this belongs to
		 */
		public LocaleProvider getProvider() {
			return provider;
		}
		
		/**
		 * Returns a custom locale<br>
		 * This will not include default locales (if they are not in the same folder)
		 * @param locale The key of the locale
		 * @return The custom locale
		 */
		public Locale getCustomLocale(String locale) {
			return new Locale(provider, getCustomLocaleConfig(locale));
		}
		
		/**
		 * Returns a CustomConfig representing a custom locale<br>
		 * This will not include default locales (if they are not in the same folder)
		 * @param locale The key of the locale
		 * @return The config for this locale
		 */
		public FileCustomConfig getCustomLocaleConfig(String locale) {
			FileCustomConfig cfg = ConfigLoader.loadFileConfig(getCustomLocaleFile(locale));
			Locale defaults = provider.getDefaultLocale();
			if(defaults != null) {
				cfg.addDefaults(defaults.cfg);
				cfg.saveToFile();
			}
			return cfg;
		}

		/**
		 * Returns the config file for a custom locale<br>
		 * This does not include default locales (if they are not in the same folder)
		 * @param locale The key of the locale
		 * @return The file for a custom locale
		 */
		public File getCustomLocaleFile(String locale) {
			return new File(customLocaleFolder, locale+".yml");
		}
		
	}
	
	/**
	 * A locale used for retrieving messages<br>
	 * Provided by a {@link LocaleProvider}
	 * @author MrLetsplay2003
	 */
	public static class Locale {
		
		private LocaleProvider provider;
		private FileCustomConfig cfg;
		
		private Locale(LocaleProvider provider, FileCustomConfig config) {
			this.provider = provider;
			this.cfg = config;
		}
		
		/**
		 * Returns a message by the given path<br>
		 * If no message at that path is defined, the {@link LocaleProvider}'s {@link LocaleNullMode} will decide what should be returned<br>
		 * This method is equal to<br>
		 * {@code <code>locale.getMessage(path, new String[0]);</code>}
		 * @param path The path of the message
		 * @return The message from the config
		 */
		public String getMessage(String path) {
			return getMessage(path, new String[0]);
		}
		
		/**
		 * Gets a message and replaces all the parameters in the format defined by {@link LocaleProvider#setParameterFormat(String, String)}<br>
		 * or the default format (prefix = "{", suffix = "}"<br>
		 * <i>params</i> needs to be in the form:<br>
		 * <i>key</i>, <i>value</i>, <i>key</i>, <i>value</i>, ...<br>
		 * Non-existant parameters will be ignored
		 * @param path The path of the message
		 * @param params The parameters to replace
		 * @return The message from the config with all parameters replaced
		 */
		public String getMessage(String path, String... params) {
			if((params.length % 2) != 0) {
				throw new IllegalArgumentException("Invalid args");
			}
			String msg = cfg.getString(path);
			
			if(msg == null) {
				switch(provider.nullMode) {
					case USE_PATH:
						msg = path;
						break;
					case USE_DEFAULT:
						if(provider.getDefaultLocale() == null) throw new UnsupportedOperationException("NullMode.USE_DEFAULT requires a default locale to be specified");
						return provider.getDefaultLocale().getMessage(path, params);
					default:
						break;
				}
			}
			
			if(msg != null) {
				for(int i = 0; i < params.length; i+=2) {
					msg = msg.replace(provider.paramPrefix + params[i] + provider.paramSuffix, params[i+1]);
				}
			}
			return msg;
		}
		
		/**
		 * @return The CustomConfig this locale uses to get its messages
		 */
		public FileCustomConfig getConfig() {
			return cfg;
		}
		
	}
	
	public static enum LocaleNullMode {
		
		USE_PATH,
		USE_DEFAULT,
		USE_NULL;
		
	}

}
