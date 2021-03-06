package me.mrletsplay.mrcore.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

import me.mrletsplay.mrcore.config.mapper.ObjectMapper;
import me.mrletsplay.mrcore.io.IOUtils;
import me.mrletsplay.mrcore.misc.Complex;

public interface CustomConfig extends ConfigSection {
	
	// Default methods
	
	@Override
	public default void set(String key, Object value) {
		getMainSection().set(key, value);
	}
	
	@Override
	public default void unset(String key) {
		getMainSection().unset(key);
	}
	
	@Override
	public default void clear() {
		getMainSection().clear();
	}
	
	@Override
	public default ConfigProperty getProperty(String key) {
		return getMainSection().getProperty(key);
	}
	
	@Override
	public default <T> T getComplex(String key, Complex<T> complex) {
		return getMainSection().getComplex(key, complex);
	}
	
	public default void load(File file) {
		try {
			if(!file.exists()) {
				if(!getFlags().contains(ConfigFlag.CREATE_EMPTY_FILE)) return;
				IOUtils.createFile(file);
				return;
			}
			FileInputStream in = new FileInputStream(file);
			try {
				load(in);
				in.close();
			}catch(ConfigException e) {
				in.close();
				throw e;
			}
		}catch(IOException e) {
			throw new ConfigException(e);
		}
	}
	
	public default void save(File file) {
		try {
			if(isEmpty()) {
				if(file.exists() && getFlags().contains(ConfigFlag.DELETE_EMPTY_FILE)) {
					file.delete();
					return;
				}
				
				if(!file.exists() && !getFlags().contains(ConfigFlag.CREATE_EMPTY_FILE)) return;
			}
			
			IOUtils.createFile(file);
			FileOutputStream out = new FileOutputStream(file);
			try {
				save(out);
				out.close();
			}catch(ConfigException e) {
				out.close();
				throw e;
			}
		}catch(IOException e) {
			throw new ConfigException(e);
		}
	}
	
	@Override
	public default CustomConfig getConfig() {
		return this;
	}
	
	@Override
	public default Map<String, ConfigProperty> getAllProperties() {
		return getMainSection().getAllProperties();
	}
	
	@Override
	public default Map<String, String> getComments() {
		return getMainSection().getComments();
	}
	
	@Override
	public default ConfigSection getSubsection(String name) {
		return getMainSection().getSubsection(name);
	}
	
	@Override
	public default ConfigSection getOrCreateSubsection(String name) {
		return getMainSection().getOrCreateSubsection(name);
	}
	
	@Override
	public default void setComment(String key, String value) {
		getMainSection().setComment(key, value);
	}
	
	@Override
	public default String getComment(String key) throws ConfigException {
		return getMainSection().getComment(key);
	}
	
	public default void setHeader(String value) {
		getMainSection().setComment(null, value);
	}
	
	public default String getHeader() {
		return getMainSection().getComment(null);
	}
	
	public default void applyDefaults() {
		applyDefaults(false);
	}
	
	// Must be implemented
	
	public ConfigSection getMainSection();
	
	public ConfigSection createEmptySection();
	
	public void load(InputStream in) throws ConfigException;
	
	public void save(OutputStream out) throws ConfigException;
	
	public void addDefaults(CustomConfig defaultConfig);
	
	public void addDefault(String key, Object value);
	
	public void applyDefaults(boolean override);
	
	public default void registerMapper(ObjectMapper<?, ?> mapper) {
		registerMapper(0, mapper);
	}
	
	public void registerMapper(int priority, ObjectMapper<?, ?> mapper);
	
	public Map<ObjectMapper<?, ?>, Integer> getMappers();
	
	public default void registerLowLevelMapper(ObjectMapper<?, ?> lowLevelMapper) {
		registerLowLevelMapper(0, lowLevelMapper);
	}
	
	public void registerLowLevelMapper(int priority, ObjectMapper<?, ?> lowLevelMapper);
	
	public Map<ObjectMapper<?, ?>, Integer> getLowLevelMappers();
	
	public void addFlags(ConfigFlag... flags);
	
	public void removeFlags(ConfigFlag... flags);
	
	public Set<ConfigFlag> getFlags();
	
}
