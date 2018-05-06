package me.mrletsplay.mrcore.bukkitimpl;

import java.io.File;
import java.io.IOException;

import me.mrletsplay.mrcore.config.CustomConfig;

public class LocaleProvider {
	
	private String defaultLocale;
	private String paramPrefix, paramSuffix;
	private File localeFolder;
	private LocaleNullMode nullMode;
	private CustomLocaleProvider customProvider;
	
	public LocaleProvider(File localeFolder) {
		this.localeFolder = localeFolder;
		this.nullMode = LocaleNullMode.USE_NULL;
		this.paramPrefix = "{";
		this.paramSuffix = "}";
	}
	
	public LocaleProvider withParameterFormat(String prefix, String suffix) {
		this.paramPrefix = prefix;
		this.paramSuffix = suffix;
		return this;
	}
	
	public LocaleProvider registerLocale(String locale, CustomConfig config) {
		try {
			CustomConfig tmpCfg = new CustomConfig(getLocaleFile(locale), false).loadDefault(config, true);
			tmpCfg.saveConfigSafely();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public LocaleProvider useNullMode(LocaleNullMode mode) {
		this.nullMode = mode;
		return this;
	}
	
	public LocaleProvider setCustomLocaleProvider(CustomLocaleProvider customProvider) {
		this.customProvider = customProvider;
		customProvider.provider = this;
		return this;
	}
	
	public LocaleProvider setDefaultLocale(String defaultLocale) {
		this.defaultLocale = defaultLocale;
		return this;
	}
	
	public Locale getLocale(String locale) {
		CustomConfig lConfig = getLocaleConfig(locale);
		if(lConfig == null) {
			if(customProvider != null) {
				return customProvider.getCustomLocale(locale);
			}
			return null;
		}
		return new Locale(this, lConfig);
	}
	
	public Locale getDefaultLocale() {
		if(defaultLocale == null) return null;
		return getLocale(defaultLocale);
	}
	
	public File getLocaleFile(String locale) {
		return new File(localeFolder, locale+".yml");
	}
	
	public CustomConfig getLocaleConfig(String locale) {
		File localeFile = getLocaleFile(locale);
		if(!localeFile.exists()) {
			return null;
		}
		return new CustomConfig(localeFile, false).loadConfigSafely();
	}
	
	
	public static class CustomLocaleProvider {
		
		private LocaleProvider provider;
		private File customLocaleFolder;
		
		public CustomLocaleProvider(File customLocaleFolder) {
			this.customLocaleFolder = customLocaleFolder;
		}
		
		public File getCustomLocaleFolder() {
			return customLocaleFolder;
		}
		
		public LocaleProvider getProvider() {
			return provider;
		}
		
		public Locale getCustomLocale(String locale) {
			Locale l = provider.getLocale(locale);
			if(l == null) {
				l = new Locale(provider, getCustomLocaleConfig(locale));
			}
			return l;
		}
		
		public CustomConfig getCustomLocaleConfig(String locale) {
			CustomConfig cfg = new CustomConfig(getCustomLocaleFile(locale), false).loadConfigSafely();
			Locale defaults = provider.getDefaultLocale();
			if(defaults != null) {
				try {
					return cfg.loadDefault(defaults.cfg, false);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return cfg;
		}
		
		public File getCustomLocaleFile(String locale) {
			return new File(customLocaleFolder, locale+".yml");
		}
		
	}
	
	public static class Locale {
		
		private LocaleProvider provider;
		private CustomConfig cfg;
		
		private Locale(LocaleProvider provider, CustomConfig config) {
			this.provider = provider;
			this.cfg = config;
		}		
		
		public String getMessage(String path) {
			return getMessage(path, new String[] {});
		}
		
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
		
		public CustomConfig getConfig() {
			return cfg;
		}
		
	}
	
	public static enum LocaleNullMode {
		
		USE_PATH,
		USE_DEFAULT,
		USE_NULL;
		
	}

}
