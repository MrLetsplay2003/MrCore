package me.mrletsplay.mrcore.config.mapper.builder;

import com.google.common.base.Predicate;

import me.mrletsplay.mrcore.config.ConfigSection;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.JSONType;
import me.mrletsplay.mrcore.misc.QuadPredicate;

public interface MapperElement<Self extends MapperElement<Self, P, T>, P extends SubMappable<P, T>, T> {

	public void applyMap(T instance, ConfigSection section, JSONObject json, String key);
	
	public void applyConstruct(T instance, ConfigSection section, JSONObject json, String key);

	public Self onlyMapIf(QuadPredicate<T, ConfigSection, JSONObject, String> condition);

	public default Self onlyMapIf(Predicate<T> condition) {
		return onlyMapIf((t, s, j, k) -> condition.apply(t));
	}

	public Self onlyConstructIf(QuadPredicate<T, ConfigSection, JSONObject, String> condition);
	
	public default Self onlyConstructIfNotNull() {
		return onlyConstructIf((t, s, j, k) -> j.has(k) && !j.isOfType(k, JSONType.NULL));
	}
	
	public default Self onlyConstructIfExists() {
		return onlyConstructIf((t, s, j, k) -> j.has(k));
	}
	
	public QuadPredicate<T, ConfigSection, JSONObject, String> getMappingCondition();
	
	public QuadPredicate<T, ConfigSection, JSONObject, String> getConstructingCondition();
	
	@SuppressWarnings("unchecked")
	public default Self getSelf() {
		return (Self) this;
	}
	
	public P then();
	
}
