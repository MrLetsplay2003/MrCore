package me.mrletsplay.mrcore.config.impl.json;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import me.mrletsplay.mrcore.config.ConfigException;
import me.mrletsplay.mrcore.config.ConfigFlag;
import me.mrletsplay.mrcore.config.ConfigSection;
import me.mrletsplay.mrcore.config.CustomConfig;
import me.mrletsplay.mrcore.config.DefaultConfigMappers;
import me.mrletsplay.mrcore.config.impl.yaml.YAMLConfigSection;
import me.mrletsplay.mrcore.config.impl.yaml.YAMLConfigFormatter;
import me.mrletsplay.mrcore.config.mapper.ObjectMapper;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.JSONParseException;

public class JSONCustomConfig implements CustomConfig {

	private ConfigSection mainSection;
	private Map<ObjectMapper<?, ?>, Integer> lowLevelMappers;
	private Map<ObjectMapper<?, ?>, Integer> mappers;
	private EnumSet<ConfigFlag> flags;

	private Map<String, Object> defaults;

	public JSONCustomConfig() {
		this.mainSection = new YAMLConfigSection(this);
		this.mappers = new LinkedHashMap<>();
		this.lowLevelMappers = new LinkedHashMap<>();
		this.defaults = new LinkedHashMap<>();
		this.flags = EnumSet.noneOf(ConfigFlag.class);
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
		return new YAMLConfigSection(this);
	}

	@Override
	public void load(InputStream in) throws ConfigException {
		try {
			String cfgString = new String(in.readAllBytes(), StandardCharsets.UTF_8);
			JSONObject cfgObject;
			try {
				cfgObject = new JSONObject(cfgString);
			}catch(JSONParseException | ClassCastException e) {
				throw new ConfigException("Invalid JSON config");
			}

			mainSection = new JSONConfigSection(this);
			mainSection.loadFromJSON(cfgObject);
//			mainSection = JSONConfigParser.parseSection(this, cfgObject);
		}catch(IOException e) {
			throw new ConfigException("Unexpected IO exception", e);
		}
	}

	@Override
	public void save(OutputStream out) {
		try {
			BufferedWriter o = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
			YAMLConfigFormatter f = new YAMLConfigFormatter(o);
			if(getHeader() != null) f.writeHeader(getHeader());
			f.writeSubsection(0, getMainSection().toMap(), getMainSection().commentsToMap());
			o.flush();
		}catch(IOException e) {
			throw new ConfigException("Unexpected IO exception", e);
		}
	}

	@Override
	public void addDefaults(CustomConfig defaultConfig) {
		defaultConfig.getKeys(true, true).forEach(k -> addDefault(k, defaultConfig.getProperty(k).getValue()));
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

	@Override
	public void addFlags(ConfigFlag... flags) {
		this.flags.addAll(Arrays.asList(flags));
	}

	@Override
	public void removeFlags(ConfigFlag... flags) {
		this.flags.removeAll(Arrays.asList(flags));
	}

	@Override
	public Set<ConfigFlag> getFlags() {
		return flags;
	}

}
