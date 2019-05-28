package me.mrletsplay.mrcore.misc;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractAutoUpdateable implements AutoUpdateable {

	private boolean changed;
	private Map<Field, Object> values;
	
	public AbstractAutoUpdateable() {
		values = getCurrentValues();
	}
	
	@Override
	public void update() {
		changed = true;
	}
	
	@Override
	public boolean hasChanged() {
		Map<Field, Object> vs = getCurrentValues();
		return changed || !vs.keySet().stream().allMatch(k -> Objects.deepEquals(vs.get(k), values.get(k))); // TODO: array changes
	}
	
	@Override
	public void resetChanged() {
		values = getCurrentValues();
		changed = false;
	}
	
	private Map<Field, Object> getCurrentValues() {
		Map<Field, Object> fs = new HashMap<>();
		ClassUtils.getFields(getClass()).forEach(f -> {
			if(!f.isAnnotationPresent(UpdateableField.class)) return;
			f.setAccessible(true);
			try {
				fs.put(f, f.get(this));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new FriendlyException("Failed to get value for field " + f, e);
			}
		});
		return fs;
	}
	
}
