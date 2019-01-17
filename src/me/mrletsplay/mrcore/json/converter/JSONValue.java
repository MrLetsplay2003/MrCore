package me.mrletsplay.mrcore.json.converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JSONValue {

	public String value() default "";
	
	public boolean decode() default true;
	
	public boolean encode() default true;
	
}
