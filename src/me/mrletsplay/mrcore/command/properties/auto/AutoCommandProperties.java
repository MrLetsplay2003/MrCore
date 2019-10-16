package me.mrletsplay.mrcore.command.properties.auto;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import me.mrletsplay.mrcore.command.Command;
import me.mrletsplay.mrcore.command.properties.CommandProperties;
import me.mrletsplay.mrcore.misc.FriendlyException;
import me.mrletsplay.mrcore.misc.NullableOptional;

public interface AutoCommandProperties extends CommandProperties {
	
	public Command getCommand();
	
	public default Object getPropertyRaw(String key) throws IllegalArgumentException {
		return getProperty(key).orElseThrow(() -> new IllegalArgumentException("Property is not set"));
	}
	
	public default NullableOptional<Object> getProperty(String key) throws IllegalArgumentException {
		try {
			Field vField = getClass().getDeclaredField(key);
			if(vField == null) throw new IllegalArgumentException("Property does not exist");
			vField.setAccessible(true);
			Object value = vField.get(this);
			if(value == null) {
				PropertyAnnotation pA = vField.getAnnotation(PropertyAnnotation.class);
				if(pA != null) {
					Annotation a = getCommand().getAnnotation(pA.type());
					Method m = pA.type().getMethod(pA.name());
					if(a != null) {
						return NullableOptional.of(m.invoke(a));
					}else if(m.getDefaultValue() != null){
						return NullableOptional.of(m.getDefaultValue());
					}
				}
				return getDefaultValue(vField);
			}
			return NullableOptional.of(value);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new FriendlyException(e);
		}
	}
	
	public default NullableOptional<Object> getDefaultValue(Field valueField) {
		BooleanDefault bD = valueField.getAnnotation(BooleanDefault.class);
		if(bD != null) return NullableOptional.of(bD.value());
		StringDefault sD = valueField.getAnnotation(StringDefault.class);
		if(sD != null) return NullableOptional.of(sD.value());
		IntDefault iD = valueField.getAnnotation(IntDefault.class);
		if(iD != null) return NullableOptional.of(iD.value());
		return NullableOptional.empty();
	}

}
