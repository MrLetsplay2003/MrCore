package me.mrletsplay.mrcore.config.mapper.builder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

import me.mrletsplay.mrcore.config.ConfigSection;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.TriConsumer;

public class SubMapper<S extends SubMapper<S, P, T, M>, P extends SubMappable<P, T>, T, M> extends BasicMapper<S, P, T> implements SubMappable<S, M> {

	private Map<String, MapperElement<?, S, M>> elements;
	private BiFunction<ConfigSection, JSONObject, M> constructor;
	private BiFunction<ConfigSection, T, M> getter;
	private TriConsumer<ConfigSection, T, M> setter;
	
	public SubMapper(P parent, BiFunction<ConfigSection, JSONObject, M> constructor, BiFunction<ConfigSection, T, M> getter, TriConsumer<ConfigSection, T, M> setter) {
		super(parent);
		this.elements = new LinkedHashMap<>();
		this.constructor = constructor;
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public void applyMap(T instance, ConfigSection section, JSONObject json, String key) {
		JSONObject obj = new JSONObject();
		M inst = getter.apply(section, instance);
		for(Map.Entry<String, MapperElement<?, S, M>> el : elements.entrySet()) {
			if(el.getValue().getMappingCondition() != null && el.getValue().getMappingCondition().test(inst, section, obj, el.getKey())) continue;
			el.getValue().applyMap(inst, section, obj, el.getKey());
		}
		json.put(key, obj);
	}

	@Override
	public void applyConstruct(T instance, ConfigSection section, JSONObject json, String key) {
		if(setter == null) return;
		JSONObject obj = json.getJSONObject(key);
		M inst = constructor.apply(section, obj);
		for(Map.Entry<String, MapperElement<?, S, M>> el : elements.entrySet()) {
			if(el.getValue().getConstructingCondition() != null && !el.getValue().getConstructingCondition().test(inst, section, obj, el.getKey())) continue;
			el.getValue().applyConstruct(inst, section, obj, el.getKey());
		}
		setter.accept(section, instance, inst);
	}
	
	@Override
	public <Q extends MapperElement<Q, S, M>> void addMapperElement(String key, MapperElement<Q, S, M> element) {
		elements.put(key, element);
	}

	@Override
	public Map<String, MapperElement<?, S, M>> getElements() {
		return elements;
	}

	@Override
	public S getSelf() {
		return super.getSelf();
	}

}
