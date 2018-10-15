package me.mrletsplay.mrcore.config.v2;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import me.mrletsplay.mrcore.config.ConfigExpansions.ExpandableCustomConfig;

public abstract class ObjectMapper<T> {

	public Class<T> mappingClass;
	public ExpandableCustomConfig config;
	public int priority;
	
	public ObjectMapper(Class<T> clazz) {
		this.mappingClass = clazz;
	}
	
	public ObjectMapper(int priority, Class<T> clazz) {
		this.mappingClass = clazz;
	}
	
	protected void init(ExpandableCustomConfig config) {
		this.config = config;
	}
	
	public Class<T> getMappingClass() {
		return mappingClass;
	}
	
	public int getPriority() {
		return priority;
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
	
	public <G> G castGeneric(Object o, Class<G> clazz) {
		return getConfig().castGeneric(o, clazz);
	}
	
	public <G> List<G> castGenericList(Object o, Class<G> clazz) {
		return getConfig().castGenericList(castGeneric(o, List.class), clazz);
	}
	
	@SuppressWarnings("unchecked")
	public <G> Map<String, G> castGenericMap(Object o, Class<G> clazz) {
		return getConfig().castGenericMap(castGeneric(o, Map.class), clazz);
	}
	
	public abstract Map<String, Object> mapObject(T object);
	
	public abstract T constructObject(Map<String, Object> map);
	
}
