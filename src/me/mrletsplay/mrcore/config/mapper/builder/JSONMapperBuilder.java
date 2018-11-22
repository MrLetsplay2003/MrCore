package me.mrletsplay.mrcore.config.mapper.builder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

import me.mrletsplay.mrcore.config.ConfigSection;
import me.mrletsplay.mrcore.config.mapper.JSONObjectMapper;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.Complex;

public class JSONMapperBuilder<Self extends JSONMapperBuilder<Self, T>, T> implements SubMappable<Self, T> {

	private Complex<T> mappingType;
	private BiFunction<ConfigSection, JSONObject, T> constructor;
	private Map<String, MapperElement<?, Self, T>> elements;
	
	public JSONMapperBuilder(Class<T> mappingType, BiFunction<ConfigSection, JSONObject, T> constructor) {
		this(Complex.value(mappingType), constructor);
	}
	
	public JSONMapperBuilder(Complex<T> mappingType, BiFunction<ConfigSection, JSONObject, T> constructor) {
		this.mappingType = mappingType;
		this.constructor = constructor;
		this.elements = new LinkedHashMap<>();
	}
	
	public JSONObjectMapper<T> create() {
		return JSONObjectMapper.create(mappingType,
				(section, t) -> {
					JSONObject json = new JSONObject();
					for(Map.Entry<String, MapperElement<?, Self, T>> el : elements.entrySet()) {
						if(el.getValue().getMappingCondition() != null && !el.getValue().getMappingCondition().test(t, section, json, el.getKey())) continue;
						el.getValue().applyMap(t, section, json, el.getKey());
					}
					return json;
				},
				(section, json) -> {
					T t = constructor.apply(section, json);
					for(Map.Entry<String, MapperElement<?, Self, T>> el : elements.entrySet()) {
						if(el.getValue().getConstructingCondition() != null && !el.getValue().getConstructingCondition().test(t, section, json, el.getKey())) continue;
						el.getValue().applyConstruct(t, section, json, el.getKey());
					}
					return t;
				});
	}

	@Override
	public <Q extends MapperElement<Q, Self, T>> void addMapperElement(String key, MapperElement<Q, Self, T> element) {
		elements.put(key, element);
	}

	@Override
	public Map<String, MapperElement<?, Self, T>> getElements() {
		return elements;
	}
	
}
