package me.mrletsplay.mrcore.extensions;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import me.mrletsplay.mrcore.bukkitimpl.MrCorePlugin;

public class MrCorePluginClassLoader {

	public static URLClassLoader newInstance(JARLoader loader) {
		try {
			Class<? extends URLClassLoader> plClassLoader = Bukkit.class.getClassLoader().loadClass("org.bukkit.plugin.java.PluginClassLoader").asSubclass(URLClassLoader.class);
			URLClassLoader l = plClassLoader.getConstructor(JavaPluginLoader.class, ClassLoader.class, PluginDescriptionFile.class, File.class, File.class)
						.newInstance(MrCorePlugin.getInstance().getPluginLoader(), loader, null, null, null);
			System.out.println(l);
			return l;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
//		Proxy.newProxyInstance(loader, interfaces, h)
	}
	
}
