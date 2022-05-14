package me.mrletsplay.mrcore.locale;

public interface LocalizedString extends LocalizedObject<String> {
	
	@Override
	default String cast(Object obj) {
		return (String) obj;
	}
	
}
