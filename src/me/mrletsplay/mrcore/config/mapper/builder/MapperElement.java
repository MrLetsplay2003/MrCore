package me.mrletsplay.mrcore.config.mapper.builder;

import com.google.common.base.Predicate;

import me.mrletsplay.mrcore.config.ConfigSection;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.JSONType;
import me.mrletsplay.mrcore.misc.QuadPredicate;

public interface MapperElement<S extends MapperElement<S, P, T>, P extends SubMappable<P, T>, T> {

	public void applyMap(T instance, ConfigSection section, JSONObject json, String key);
	
	public void applyConstruct(T instance, ConfigSection section, JSONObject json, String key);

	public S onlyMapIf(QuadPredicate<T, ConfigSection, JSONObject, String> condition);

	public default S onlyMapIf(Predicate<T> condition) {
		return onlyMapIf((t, s, j, k) -> condition.apply(t));
	}

	public S onlyConstructIf(QuadPredicate<T, ConfigSection, JSONObject, String> condition);

	public default S onlyConstructIfNotNull() {
		return onlyConstructIf((t, s, j, k) -> j.has(k) && !j.isOfType(k, JSONType.NULL));
	}
	
	public default S onlyConstructIfExists() {
		return onlyConstructIf((t, s, j, k) -> j.has(k));
	}
	
	public QuadPredicate<T, ConfigSection, JSONObject, String> getMappingCondition();
	
	public QuadPredicate<T, ConfigSection, JSONObject, String> getConstructingCondition();
	
	@SuppressWarnings("unchecked")
	public default S getSelf() {
		return (S) this;
	}
	
	public P then();
	
}
