package me.mrletsplay.mrcore.config;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ConfigExpansions {
	
	public static class ExpandableCustomConfig extends CustomConfig {
		
		private List<ObjectMapper<?>> mappers = new ArrayList<>();

		public ExpandableCustomConfig(File configFile) {
			this(configFile, new ConfigSaveProperty[0]);
		}
		
		public ExpandableCustomConfig(File configFile, ConfigSaveProperty... defaultSaveProperties) {
			super(configFile, defaultSaveProperties);
		}

		public ExpandableCustomConfig(URL configURL) {
			this(configURL, new ConfigSaveProperty[0]);
		}

		public ExpandableCustomConfig(URL configURL, ConfigSaveProperty... defaultSaveProperties) {
			super(configURL, defaultSaveProperties);
		}
		
		public void registerMapper(ObjectMapper<?> mapper) {
			mappers.add(mapper);
		}
		
		@SuppressWarnings("unchecked")
		public <T> T getMappable(String key, Class<T> mappingClass) {
			ObjectMapper<?> mapper = mappers.stream().filter(m -> m.mappingClass.equals(mappingClass)).findFirst().orElse(null);
			return ((ObjectMapper<T>)mapper).constructObject(getMap(key));
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
	
	public static class BukkitCustomConfig extends ExpandableCustomConfig {

		public BukkitCustomConfig(File configFile) {
			this(configFile, new ConfigSaveProperty[0]);
		}
		
		public BukkitCustomConfig(File configFile, ConfigSaveProperty... defaultSaveProperties) {
			super(configFile, defaultSaveProperties);
			registerMappers();
		}

		public BukkitCustomConfig(URL configURL) {
			this(configURL, new ConfigSaveProperty[0]);
		}

		public BukkitCustomConfig(URL configURL, ConfigSaveProperty... defaultSaveProperties) {
			super(configURL, defaultSaveProperties);
			registerMappers();
		}
		
		private void registerMappers() {
			registerMapper(new ObjectMapper<Location>(Location.class) {

				@Override
				public Map<String, Object> mapObject(Location loc) {
					HashMap<String, Object> map = new HashMap<>();
					map.put("world", loc.getWorld().getName());
					map.put("x", loc.getX());
					map.put("y", loc.getY());
					map.put("z", loc.getZ());
					map.put("pitch", loc.getPitch());
					map.put("yaw", loc.getYaw());
					return map;
				}

				@Override
				public Location constructObject(Map<String, Object> map) {
					return new Location(
								Bukkit.getWorld((String) map.get("world")),
								(double) map.get("x"),
								(double) map.get("y"),
								(double) map.get("z"),
								(float) map.get("yaw"),
								(float) map.get("pitch")
							);
				}
				
			});
		}
		
		public Location getLocation(String key) {
			return getMappable(key, Location.class);
		}
		
		public Location getLocation(String key, Location defaultVal, boolean applyDefault) {
			Location o = getMappable(key, Location.class);
			if(o != null) return o;
			if(applyDefault) set(key, defaultVal);
			return defaultVal;
		}
		
	}
	
}
