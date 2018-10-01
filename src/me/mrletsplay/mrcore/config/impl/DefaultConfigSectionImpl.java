package me.mrletsplay.mrcore.config.impl;

import java.util.Map;

import me.mrletsplay.mrcore.config.v2.ConfigProperty;
import me.mrletsplay.mrcore.config.v2.ConfigSection;
import me.mrletsplay.mrcore.config.v2.CustomConfig;

public class DefaultConfigSectionImpl implements ConfigSection {

	private CustomConfig config;
	private ConfigSection parent;
	private String name;
	private Map<String, ConfigSection> subSections;
	private Map<String, ConfigProperty> properties;
	private Map<String, String> comments;
	
	public DefaultConfigSectionImpl(CustomConfig config, ConfigSection parent, String name) {
		
	}
	
	@Override
	public CustomConfig getConfig() {
		return config;
	}

	@Override
	public ConfigSection getParent() {
		return parent;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Map<String, ConfigProperty> getProperties() {
		return properties;
	}

	@Override
	public Map<String, String> getComments() {
		return comments;
	}

	@Override
	public Map<String, ConfigSection> getSubsections() {
		return subSections;
	}

	@Override
	public void set(String key, Object value) {
		
	}

	@Override
	public ConfigProperty getProperty(String key) {
		return null;
	}

	@Override
	public String saveToString() {
		return null;
	}
	
}
