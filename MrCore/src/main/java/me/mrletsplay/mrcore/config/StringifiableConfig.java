package me.mrletsplay.mrcore.config;

public interface StringifiableConfig extends CustomConfig {
	
	@Override
	public StringifiableConfigSection getMainSection();
	
	public default String saveToString() {
		return getMainSection().saveToString();
	}
	
}
