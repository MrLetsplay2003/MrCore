package me.mrletsplay.mrcore.command.properties.auto;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyAnnotation {

	public Class<? extends Annotation> type();
	
	public String name();
	
}
