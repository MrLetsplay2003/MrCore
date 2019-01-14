package me.mrletsplay.mrcore.json.converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface JSONParameter {

	public String value();
	
	public boolean mustBePresent() default true;
	
}
