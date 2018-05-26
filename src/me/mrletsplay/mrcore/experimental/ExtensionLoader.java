package me.mrletsplay.mrcore.main;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ExtensionLoader {

	public static MrCoreExtension loadExtension(File file) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		JARLoader loader = new JARLoader(file);
		
		Class<?> mainClazz = loader.getBukkitMainClass();
		if(!MrCoreExtension.class.isAssignableFrom(mainClazz)) {
			loader.close();
			throw new RuntimeException("swe");
		}
		MrCoreExtension ex = (MrCoreExtension) mainClazz.newInstance();
		ex.onEnable();
		loader.close();
		return ex;
	}
	
}
