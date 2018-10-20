package me.mrletsplay.mrcore.config.v2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import me.mrletsplay.mrcore.misc.Complex;

public interface CustomConfig extends ConfigSection {
	
	// Default methods
	
	@Override
	public default void set(String key, Object value) {
		getMainSection().set(key, value);
	}
	
	@Override
	public default ConfigProperty getProperty(String key) {
		return getMainSection().getProperty(key);
	}
	
	@Override
	public default <T> T getGeneric(Class<T> clazz, String key) {
		return getMainSection().getGeneric(clazz, key);
	}
	
	@Override
	public default <T> T getComplex(Complex<T> complex, String key) {
		return getMainSection().getComplex(complex, key);
	}
	
	@Override
	public default String getString(String key) {
		return getMainSection().getString(key);
	}
	
	@Override
	public default boolean getBoolean(String key) {
		return getMainSection().getBoolean(key);
	}
	
	@Override
	public default byte getByte(String key) {
		return getMainSection().getByte(key);
	}
	
	@Override
	public default short getShort(String key) {
		return getMainSection().getShort(key);
	}
	
	@Override
	public default int getInt(String key) {
		return getMainSection().getInt(key);
	}
	
	@Override
	public default long getLong(String key) {
		return getMainSection().getLong(key);
	}
	
	@Override
	public default float getFloat(String key) {
		return getMainSection().getFloat(key);
	}
	
	@Override
	public default double getDouble(String key) {
		return getMainSection().getDouble(key);
	}
	
	public default void load(File file) {
		try {
			FileInputStream in = new FileInputStream(file);
			load(in);
			in.close();
		}catch(IOException e) {
			throw new ConfigException(e);
		}
	}
	
	public default void save(File file) {
		try {
			FileOutputStream out = new FileOutputStream(file);
			save(out);
			out.close();
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
	
	// Must be implemented
	
	public ConfigSection getMainSection();
	
	public void load(InputStream in) throws ConfigException;
	
	public void save(OutputStream out) throws ConfigException;
	
	public default void registerMapper(ObjectMapper<?, ?> mapper) {
		registerMapper(0, mapper);
	}
	
	public void registerMapper(int priority, ObjectMapper<?, ?> mapper);
	
	public Map<ObjectMapper<?, ?>, Integer> getMappers();
	
}
