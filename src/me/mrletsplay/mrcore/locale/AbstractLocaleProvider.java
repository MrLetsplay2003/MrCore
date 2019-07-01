package me.mrletsplay.mrcore.locale;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractLocaleProvider implements LocaleProvider {

	private Map<String, Locale> locales;
	
	public AbstractLocaleProvider() {
		this.locales = new HashMap<>();
	}
	
	@Override
	public void registerLocale(String localeShort, Locale locale) {
		locales.put(localeShort, locale);
	}

	@Override
	public Locale getLocale(String localeShort) {
		return locales.get(localeShort);
	}

}
