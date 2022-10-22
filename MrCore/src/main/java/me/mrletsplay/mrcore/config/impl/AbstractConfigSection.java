package me.mrletsplay.mrcore.config.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import me.mrletsplay.mrcore.config.ConfigException;
import me.mrletsplay.mrcore.config.ConfigPath;
import me.mrletsplay.mrcore.config.ConfigProperty;
import me.mrletsplay.mrcore.config.ConfigSection;
import me.mrletsplay.mrcore.config.CustomConfig;
import me.mrletsplay.mrcore.config.impl.yaml.YAMLConfigSection;

public abstract class AbstractConfigSection implements ConfigSection {

	protected CustomConfig config;
	protected Map<String, ConfigProperty> rawProperties;
	protected Map<String, String> comments;

	public AbstractConfigSection(CustomConfig config) {
		this.config = config;
		this.rawProperties = new LinkedHashMap<>();
		this.comments = new LinkedHashMap<>();
	}

	@Override
	public CustomConfig getConfig() {
		return config;
	}

	@Override
	public Map<String, ConfigProperty> getAllProperties() {
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
			s = new YAMLConfigSection(config);
			rawProperties.put(name, DefaultConfigPropertyImpl.create(this, name, s));
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
	public void unset(String key) {
		ConfigPath path = ConfigPath.of(key);
		if(path.hasSubpaths()) {
			ConfigSection section = getSubsection(path.getName());
			if(section == null) return;
			section.unset(path.traverseDown().toRawPath());
		}else {
			rawProperties.remove(path.getName());
		}
	}

	@Override
	public void clear() {
		rawProperties.clear();
	}

	@Override
	public ConfigProperty getProperty(String key) throws ConfigException{
		ConfigPath path = ConfigPath.of(key);
		if(path.hasSubpaths()) {
			ConfigSection section = getSubsection(path.getName());
			if(section == null) return null;
			return section.getProperty(path.traverseDown().toRawPath());
		}else {
			ConfigProperty o = rawProperties.get(path.getName());
			if(o == null) return null;
			return o;
		}
	}

	@Override
	public void setComment(String key, String value) {
		ConfigPath path = ConfigPath.of(key);
		if(path.hasSubpaths()) {
			ConfigSection section = getOrCreateSubsection(path.getName());
			section.setComment(path.traverseDown().toRawPath(), value);
		}else {
			comments.put(path.getName(), value);
		}
	}

	@Override
	public String getComment(String key) {
		ConfigPath path = ConfigPath.of(key);
		if(path.hasSubpaths()) {
			ConfigSection section = getOrCreateSubsection(path.getName());
			return section.getComment(path.traverseDown().toRawPath());
		}else {
			return comments.get(path.getName());
		}
	}

	@Override
	public String toString() {
		return "[S: " + rawProperties + "]";
	}

}
