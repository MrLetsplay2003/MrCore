package me.mrletsplay.mrcore.locale.file;

import java.io.File;

import me.mrletsplay.mrcore.locale.AbstractLocaleProvider;
import me.mrletsplay.mrcore.locale.Locale;
import me.mrletsplay.mrcore.locale.LocalizedObject;

public class FileLocaleProvider extends AbstractLocaleProvider {

	private File localeFolder;

	public FileLocaleProvider(File localeFolder) {
		this.localeFolder = localeFolder;
		if(localeFolder != null) {
			localeFolder.mkdirs();
			for(File l : localeFolder.listFiles()) {
				if(!l.getName().matches("[a-zA-Z0-9\\-_]+\\.yml")) continue; // Treat as invalid locale
				registerLocale(l.getName().substring(0, l.getName().length() - ".yml".length()), new FileLocale(l));
			}
		}
	}

	public File getLocaleFolder() {
		return localeFolder;
	}

	@Override
	public Locale generateDefaultLocale(LocalizedObject<?>... objects) {
		FileLocale l = new FileLocale(null);
		l.applyDefaults(objects);
		return l;
	}

}
