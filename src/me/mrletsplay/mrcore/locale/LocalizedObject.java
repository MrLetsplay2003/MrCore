package me.mrletsplay.mrcore.locale;

public interface LocalizedObject<T> {

	public T cast(Object obj);

	public String getPath();
	
	public T getDefault();
	
}
