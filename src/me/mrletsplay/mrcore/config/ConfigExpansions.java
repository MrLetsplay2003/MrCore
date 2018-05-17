package me.mrletsplay.mrcore.config;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigExpansions {
	
	public static class ExpandableCustomConfig extends CustomConfig {
		
		private List<ObjectMapper<?>> mappers = new ArrayList<>();
		
		public ExpandableCustomConfig(File configFile, ConfigSaveProperty... defaultSaveProperties) {
			super(configFile, defaultSaveProperties);
		}

		public ExpandableCustomConfig(URL configURL, ConfigSaveProperty... defaultSaveProperties) {
			super(configURL, defaultSaveProperties);
		}
		
		public void registerMapper(ObjectMapper<?> mapper) {
			mappers.add(mapper);
		}
		
		@SuppressWarnings("unchecked")
		public <T> T getMappable(String key, Class<T> mappingClass) {
			ObjectMapper<T> mapper = (ObjectMapper<T>) mappers.stream().filter(m -> m.mappingClass.equals(mappingClass)).findFirst().orElse(null);
			return mapper.constructObject(getMap(key));
		}
		
		@SuppressWarnings("unchecked")
		public <T> List<T> getMappableList(String key, Class<T> mappingClass) {
			ObjectMapper<T> mapper = (ObjectMapper<T>) mappers.stream().filter(m -> m.mappingClass.equals(mappingClass)).findFirst().orElse(null);
			List<Map<String, Object>> list = getMapList(key);
			return list.stream().map(e -> mapper.constructObject(e)).collect(Collectors.toList());
		}
		
		public List<ObjectMapper<?>> getMappers() {
			return mappers;
		}
		
		@Override
		public void set(String key, Object val, boolean save, List<ConfigSaveProperty> props) {
			final Object o = val;
			ObjectMapper<?> mapper = mappers.stream().filter(m -> m.canMap(o)).findFirst().orElse(null);
			if(mapper != null) {
				val = mapper.map(val);
			}
			super.set(key, val, save, props);
		}
		
		public static abstract class ObjectMapper<T> {
			
			public Class<T> mappingClass;
			
			public ObjectMapper(Class<T> clazz) {
				this.mappingClass = clazz;
			}
			
			public boolean canMap(Object o) {
				if(o == null) return false;
				return mappingClass.isAssignableFrom(o.getClass());
			}
			
			public Map<String, Object> map(Object o) {
				return mapObject(mappingClass.cast(o));
			}
			
			public abstract Map<String, Object> mapObject(T object);
			
			public abstract T constructObject(Map<String, Object> map);
			
		}
		
	}
	
}
