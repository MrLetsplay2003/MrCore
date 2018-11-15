package me.mrletsplay.mrcore.config.v2.builder;

import me.mrletsplay.mrcore.config.v2.ConfigSection;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.QuadPredicate;

public abstract class BasicMapper<Self extends BasicMapper<Self, P, T>, P extends SubMappable<P, T>, T> implements MapperElement<Self, P, T> {

	private P parent;
	private QuadPredicate<T, ConfigSection, JSONObject, String> mappingCondition, constructingCondition;
	
	public BasicMapper(P parent) {
		this.parent = parent;
	}
	
	@Override
	public P then() {
		return parent;
	}
	
	@Override
	public Self onlyMapIf(QuadPredicate<T, ConfigSection, JSONObject, String> condition) {
		if(this.mappingCondition == null) {
			this.mappingCondition = condition;
		}else {
			this.mappingCondition = this.mappingCondition.and(condition);
		}
		return getSelf();
	}
	
	@Override
	public Self onlyConstructIf(QuadPredicate<T, ConfigSection, JSONObject, String> condition) {
		if(this.constructingCondition == null) {
			this.constructingCondition = condition;
		}else {
			this.constructingCondition = this.constructingCondition.and(condition);
		}
		return getSelf();
	}
	
	@Override
	public QuadPredicate<T, ConfigSection, JSONObject, String> getMappingCondition() {
		return mappingCondition;
	}
	
	@Override
	public QuadPredicate<T, ConfigSection, JSONObject, String> getConstructingCondition() {
		return constructingCondition;
	}
	
}
