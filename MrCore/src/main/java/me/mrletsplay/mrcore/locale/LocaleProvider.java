package me.mrletsplay.mrcore.locale;

public interface LocaleProvider {
	
	public void registerLocale(String localeShort, Locale locale);

	public Locale getLocale(String localeShort);
	
	public Locale generateDefaultLocale(LocalizedObject<?>... objects);
	
}
