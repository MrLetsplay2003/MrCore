package me.mrletsplay.mrcore.misc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
		return changed || !vs.keySet().stream().allMatch(k -> {
			Object
				vN = vs.get(k),
				vO = values.get(k);
			if(vN instanceof Updateable && ((Updateable) vN).hasChanged()) return false;
			return Objects.deepEquals(vN, vO);
		});
	}
	
	@Override
	public void resetChanged() {
		values = getCurrentValues();
		for(Object e : values.entrySet())
			if(e instanceof Updateable)
				((Updateable) e).resetChanged();
		changed = false;
	}
	
	private Map<Field, Object> getCurrentValues() {
		Map<Field, Object> fs = new HashMap<>();
		ClassUtils.getFields(getClass()).forEach(f -> {
			if(!f.isAnnotationPresent(UpdateableField.class)) return;
			f.setAccessible(true);
			try {
				Object v = f.get(this);
				if(v instanceof Cloneable) {
					try {
						Method m = ClassUtils.getDeclaredMethodRecursively(v.getClass(), "clone");
						m.setAccessible(true);
						v = m.invoke(v);
					}catch(Exception ignored) {
						ignored.printStackTrace();
					}
				}
				fs.put(f, v);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new FriendlyException("Failed to get value for field " + f, e);
			}
		});
		return fs;
	}
	
}
