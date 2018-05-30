package me.mrletsplay.mrcore.config;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigExpansions {
	
	public static class ExpandableCustomConfig extends CustomConfig {
		
		private List<ObjectMapper<?>> mappers = new ArrayList<>();
		
		public ExpandableCustomConfig(File configFile, ConfigSaveProperty... defaultSaveProperties) {
			super(configFile, defaultSaveProperties);
			setFormatter(new ExpandableConfigFormatter(this));
		}

		public ExpandableCustomConfig(URL configURL, ConfigSaveProperty... defaultSaveProperties) {
			super(configURL, defaultSaveProperties);
			setFormatter(new ExpandableConfigFormatter(this));
		}
		
		public void registerMapper(ObjectMapper<?> mapper) {
			mappers.add(mapper);
			mapper.init(this);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public <T> T castGeneric(Object obj, Class<T> clazz) {
			try {
				return super.castGeneric(obj, clazz);
			}catch(InvalidTypeException e) {
				ObjectMapper<T> mapper = getMapper(clazz);
				if(!(obj instanceof Map) || mapper == null) throw new InvalidTypeException("Unsupported type: "+clazz.getName());
				return mapper.constructObject((Map<String, Object>) obj);
			}
		}
		
		@SuppressWarnings("unchecked")
		public <T> ObjectMapper<T> getMapper(Class<T> mappingClass) {
			return (ObjectMapper<T>) mappers.stream().filter(m -> m.mappingClass.equals(mappingClass)).findFirst().orElse(null);
		}
		
		public <T> T getMappable(String key, Class<T> mappingClass) {
			try {
				return getMapper(mappingClass).constructObject(getMap(key));
			} catch(Exception e) {
				throw new InvalidTypeException(key, "Failed to parse into "+mappingClass.getName(), e);
			}
		}
		
		public <T> List<T> getMappableList(String key, Class<T> mappingClass) {
			List<Map<String, Object>> list = getMapList(key);
			try {
				ObjectMapper<T> mapper = getMapper(mappingClass);
				return list.stream().map(e -> mapper.constructObject(e)).collect(Collectors.toList());
			} catch(Exception e) {
				throw new InvalidTypeException(key, "Failed to parse into "+mappingClass.getName(), e);
			}
		}
		
		public List<ObjectMapper<?>> getMappers() {
			return mappers;
		}
		
		public static abstract class ObjectMapper<T> {
			
			public Class<T> mappingClass;
			public ExpandableCustomConfig config;
			
			public ObjectMapper(Class<T> clazz) {
				this.mappingClass = clazz;
			}
			
			protected void init(ExpandableCustomConfig config) {
				this.config = config;
			}
			
			public ExpandableCustomConfig getConfig() {
				return config;
			}
			
			public boolean canMap(Object o) {
				if(o == null) return false;
				return mappingClass.isAssignableFrom(o.getClass());
			}
			
			public boolean requireKeys(Map<String, Object> map, String... keys) {
				return Arrays.stream(keys).allMatch(map::containsKey);
			}
			
			public Map<String, Object> map(Object o) {
				return mapObject(mappingClass.cast(o));
			}
			
			public abstract Map<String, Object> mapObject(T object);
			
			public abstract T constructObject(Map<String, Object> map);
			
		}
		
		public static class ExpandableConfigFormatter extends DefaultConfigFormatter {

			private ExpandableCustomConfig config;
			
			public ExpandableConfigFormatter(ExpandableCustomConfig config) {
				super(config);
				this.config = config;
			}
			
			@Override
			public FormattedProperty formatObject(Object o) {
				FormattedProperty fp = super.formatObject(o);
				if(fp.isSpecific()) return fp;
				
				ObjectMapper<?> mapper = config.mappers.stream().filter(m -> m.canMap(o)).findFirst().orElse(null);
				if(mapper != null) {
					return FormattedProperty.map(this, mapper.map(o));
				}
				
				return fp;
			}
			
		}
		
	}
	
}
