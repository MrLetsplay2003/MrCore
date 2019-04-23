package me.mrletsplay.mrcore.config.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.mrletsplay.mrcore.config.ConfigException;
import me.mrletsplay.mrcore.config.ConfigSection;
import me.mrletsplay.mrcore.config.CustomConfig;
import me.mrletsplay.mrcore.config.DefaultConfigMappers;
import me.mrletsplay.mrcore.config.FileCustomConfig;
import me.mrletsplay.mrcore.config.IncompatibleConfigVersionException;
import me.mrletsplay.mrcore.config.impl.DefaultConfigParser.ConfigSectionDescriptor;
import me.mrletsplay.mrcore.config.impl.DefaultConfigParser.Marker;
import me.mrletsplay.mrcore.config.mapper.ObjectMapper;

public class DefaultFileCustomConfig implements FileCustomConfig {

	private static final String VERSION = "2.0";
	
	private File configFile;
	private ConfigSection mainSection;
	private Map<ObjectMapper<?, ?>, Integer> lowLevelMappers;
	private Map<ObjectMapper<?, ?>, Integer> mappers;
	
	private Map<String, Object> defaults;
	
	public DefaultFileCustomConfig(File configFile) {
		this.configFile = configFile;
		this.mainSection = new DefaultConfigSectionImpl(this);
		this.mappers = new LinkedHashMap<>();
		this.lowLevelMappers = new LinkedHashMap<>();
		this.defaults = new LinkedHashMap<>();
		registerLowLevelMapper(0, DefaultConfigMappers.JSON_OBJECT_MAPPER);
		registerLowLevelMapper(0, DefaultConfigMappers.JSON_ARRAY_MAPPER);
		registerLowLevelMapper(0, DefaultConfigMappers.MAP_MAPPER);
	}
	
	@Override
	public ConfigSection getMainSection() {
		return mainSection;
	}

	@Override
	public ConfigSection createEmptySection() {
		return new DefaultConfigSectionImpl(this);
	}
	
	@Override
	public void load(InputStream in) throws ConfigException {
		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
			List<String> lines = new ArrayList<>();
			String s;
			while((s = r.readLine()) != null) {
				lines.add(s + "\n");
			}
			DefaultConfigParser p = new DefaultConfigParser(lines.toArray(new String[lines.size()]));
			if(!p.hasMore()) {
				return;
			}
			String version = p.readVersionDescriptor();
			if(!version.equals(VERSION)) throw new IncompatibleConfigVersionException(version, VERSION);
			if(!p.hasMore()) return;
			String header = p.readHeader();
			if(header != null && !header.isEmpty()) mainSection.setComment(null, header);
			if(!p.hasMore()) return;
			ConfigSectionDescriptor d = p.readSubsection(new Marker(0, 0), 0);
			mainSection.loadFromMap(d.getProperties());
			d.getComments().forEach(mainSection::setComment);
		}catch(IOException e) {
			throw new ConfigException("Unexpected IO exception", e);
		}
	}

	@Override
	public void save(OutputStream out) {
		try {
			BufferedWriter o = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
			DefaultConfigFormatter f = new DefaultConfigFormatter(o);
			f.writeConfigVersionDescriptor(VERSION);
			if(getHeader() != null) f.writeHeader(getHeader());
			f.writeSubsection(0, getMainSection().toMap(), getMainSection().commentsToMap());
			o.flush();
		}catch(IOException e) {
			throw new ConfigException("Unexpected IO exception", e);
		}
	}

	@Override
	public void addDefaults(CustomConfig defaultConfig) {
		defaultConfig.toMap().forEach(this::addDefault);
	}

	@Override
	public void addDefault(String key, Object value) {
		defaults.put(key, value);
	}

	@Override
	public void applyDefaults(boolean override) {
		defaults.forEach((k, v) -> {
			if(override || !isSet(k)) set(k, v);
		});
		defaults.clear();
	}

	@Override
	public File getConfigFile() {
		return configFile;
	}

	@Override
	public void registerMapper(int priority, ObjectMapper<?, ?> mapper) {
		mappers.put(mapper, priority);
	}

	@Override
	public Map<ObjectMapper<?, ?>, Integer> getMappers() {
		return mappers;
	}

	@Override
	public void registerLowLevelMapper(int priority, ObjectMapper<?, ?> lowLevelMapper) {
		lowLevelMappers.put(lowLevelMapper, priority);
	}
	
	@Override
	public Map<ObjectMapper<?, ?>, Integer> getLowLevelMappers() {
		return lowLevelMappers;
	}
	
}
