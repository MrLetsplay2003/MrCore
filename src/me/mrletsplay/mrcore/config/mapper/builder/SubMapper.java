package me.mrletsplay.mrcore.config.mapper.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import me.mrletsplay.mrcore.config.ConfigSection;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.TriConsumer;

public class SubMapper<Self extends SubMapper<Self, P, T, S>, P extends SubMappable<P, T>, T, S> extends BasicMapper<Self, P, T> implements SubMappable<Self, S> {

	private Map<String, MapperElement<?, Self, S>> elements;
	private BiFunction<ConfigSection, JSONObject, S> constructor;
	private BiFunction<ConfigSection, T, S> getter;
	private TriConsumer<ConfigSection, T, S> setter;
	
	public SubMapper(P parent, BiFunction<ConfigSection, JSONObject, S> constructor, BiFunction<ConfigSection, T, S> getter, TriConsumer<ConfigSection, T, S> setter) {
		super(parent);
		this.elements = new HashMap<>();
		this.constructor = constructor;
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public void applyMap(T instance, ConfigSection section, JSONObject json, String key) {
		JSONObject obj = new JSONObject();
		S inst = getter.apply(section, instance);
		for(Map.Entry<String, MapperElement<?, Self, S>> el : elements.entrySet()) {
			if(el.getValue().getMappingCondition() != null && el.getValue().getMappingCondition().test(inst, section, obj, el.getKey())) continue;
			el.getValue().applyMap(inst, section, obj, el.getKey());
		}
		json.put(key, obj);
	}

	@Override
	public void applyConstruct(T instance, ConfigSection section, JSONObject json, String key) {
		if(setter == null) return;
		JSONObject obj = json.getJSONObject(key);
		S inst = constructor.apply(section, obj);
		for(Map.Entry<String, MapperElement<?, Self, S>> el : elements.entrySet()) {
			if(el.getValue().getConstructingCondition() != null && !el.getValue().getConstructingCondition().test(inst, section, obj, el.getKey())) continue;
			el.getValue().applyConstruct(inst, section, obj, el.getKey());
		}
		setter.accept(section, instance, inst);
	}
	
	@Override
	public <Q extends MapperElement<Q, Self, S>> void addMapperElement(String key, MapperElement<Q, Self, S> element) {
		elements.put(key, element);
	}

	@Override
	public Map<String, MapperElement<?, Self, S>> getElements() {
		return elements;
	}

	@Override
	public Self getSelf() {
		return super.getSelf();
	}

}
