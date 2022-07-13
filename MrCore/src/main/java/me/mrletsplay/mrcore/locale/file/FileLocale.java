package me.mrletsplay.mrcore.locale.file;

import java.io.File;
import java.io.OutputStream;

import me.mrletsplay.mrcore.config.ConfigLoader;
import me.mrletsplay.mrcore.config.CustomConfig;
import me.mrletsplay.mrcore.config.impl.yaml.YAMLCustomConfig;
import me.mrletsplay.mrcore.locale.Locale;
import me.mrletsplay.mrcore.locale.LocalizedObject;

public class FileLocale implements Locale {

	private CustomConfig config;

	public FileLocale(File file) {
		if(file != null) {
			this.config = ConfigLoader.loadFileConfig(file);
		}else {
			this.config = new YAMLCustomConfig();
		}
	}

	@Override
	public void applyDefaults(LocalizedObject<?>... objects) {
		for(LocalizedObject<?> obj : objects) {
			config.set(obj.getPath(), obj.getDefault());
		}
	}

	@Override
	public <T> T get(LocalizedObject<T> object) {
		Object o = config.getProperty(object.getPath()).getValue();
		return o == null ? object.getDefault() : object.cast(o);
	}

	@Override
	public void save(OutputStream out) {
		config.save(out);
	}

}
