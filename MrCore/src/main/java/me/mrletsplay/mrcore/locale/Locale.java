package me.mrletsplay.mrcore.locale;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import me.mrletsplay.mrcore.io.IOUtils;
import me.mrletsplay.mrcore.misc.FriendlyException;

public interface Locale {
	
	public void applyDefaults(LocalizedObject<?>... objects);

	public <T> T get(LocalizedObject<T> object);
	
	public void save(OutputStream out);
	
	public default void save(File file) throws FriendlyException {
		IOUtils.createFile(file);
		try (FileOutputStream out = new FileOutputStream(file)){
			save(out);
		}catch(IOException e) {
			throw new FriendlyException(e);
		}
	}
	
}
