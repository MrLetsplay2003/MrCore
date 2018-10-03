package me.mrletsplay.mrcore.config.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import me.mrletsplay.mrcore.config.v2.ConfigPath;
import me.mrletsplay.mrcore.config.v2.ConfigProperty;
import me.mrletsplay.mrcore.config.v2.ConfigSection;
import me.mrletsplay.mrcore.config.v2.CustomConfig;

public class DefaultConfigSectionImpl implements ConfigSection {

	private CustomConfig config;
	private ConfigSection parent;
	private String name;
	private Map<String, Object> rawProperties;
	private Map<String, String> comments;
	
	public DefaultConfigSectionImpl(CustomConfig config, ConfigSection parent, String name) {
		this.config = config;
		this.parent = parent;
		this.name = name;
		this.rawProperties = new LinkedHashMap<>();
		this.comments = new LinkedHashMap<>();
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
	public Map<String, Object> getAllProperties() {
		return rawProperties;
	}

	@Override
	public Map<String, String> getComments() {
		return comments;
	}

	@Override
	public ConfigSection getOrCreateSubsection(String name) {
		ConfigSection s = getSubsection(name);
		if(s == null) {
			s = new DefaultConfigSectionImpl(config, this, name);
			rawProperties.put(name, s);
		}
		return s;
	}

	@Override
	public void set(String key, Object value) {
		ConfigPath path = ConfigPath.of(key);
		if(path.hasSubpaths()) {
			ConfigSection section = getOrCreateSubsection(path.getName());
			section.set(path.traverseDown().toRawPath(), value);
		}else {
			rawProperties.put(path.getName(), DefaultConfigPropertyImpl.create(this, path.getName(), value));
		}
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
