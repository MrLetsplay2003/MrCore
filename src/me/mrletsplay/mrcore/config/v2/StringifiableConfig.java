package me.mrletsplay.mrcore.config.v2;

public interface StringifiableConfig extends CustomConfig {
	
	@Override
	public StringifiableConfigSection getMainSection();
	
	public default String saveToString() {
		return getMainSection().saveToString();
	}
	
}
