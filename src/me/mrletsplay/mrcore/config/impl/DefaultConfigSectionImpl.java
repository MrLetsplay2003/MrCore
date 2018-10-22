package me.mrletsplay.mrcore.config.impl;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import me.mrletsplay.mrcore.config.v2.ConfigException;
import me.mrletsplay.mrcore.config.v2.ConfigPath;
import me.mrletsplay.mrcore.config.v2.ConfigProperty;
import me.mrletsplay.mrcore.config.v2.ConfigSection;
import me.mrletsplay.mrcore.config.v2.CustomConfig;
import me.mrletsplay.mrcore.config.v2.StringifiableConfigSection;

public class DefaultConfigSectionImpl implements StringifiableConfigSection {

	private CustomConfig config;
	private Map<String, ConfigProperty> rawProperties;
	private Map<String, String> comments;
	
	public DefaultConfigSectionImpl(CustomConfig config) {
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
			s = new DefaultConfigSectionImpl(config);
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
	public ConfigProperty getProperty(String key) throws ConfigException{
		ConfigPath path = ConfigPath.of(key);
		if(path.hasSubpaths()) {
			ConfigSection section = getOrCreateSubsection(path.getName());
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
	public String saveToString() {
		return saveToString(0);
	}

	public String saveToString(int indents) {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		BufferedWriter w = new BufferedWriter(new OutputStreamWriter(bOut));
		DefaultConfigFormatter f = new DefaultConfigFormatter(w);
		try {
			f.writeSubsection(indents, toMap(), commentsToMap());
			w.close();
		} catch (IOException e) {}
		return new String(bOut.toByteArray(), StandardCharsets.UTF_8);
	}
	
}
