package me.mrletsplay.mrcore.extensions;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;

import me.mrletsplay.mrcore.misc.OtherTools.FriendlyException;

public class MrCoreBukkitExtension extends MrCoreExtension {

	private Object extension;
	private Class<?> extensionClass;

	public MrCoreBukkitExtension(Class<?> extensionClass, Object bukkitExtension) {
		this.extensionClass = extensionClass;
		this.extension = bukkitExtension;
	}
	
	@Override
	public void init(JARLoader loader) {
		invokeMethod("init", Arrays.asList(JARLoader.class), loader);
	}
	
	@Override
	public JARLoader getLoader() {
		return (JARLoader) invokeMethod("getLoader", Collections.emptyList());
	}
	
	@Override
	public ClassLoader getClassLoader() {
		return (ClassLoader) invokeMethod("getClassLoader", Collections.emptyList());
	}
	
	private Object invokeMethod(String name, List<Class<?>> classes, Object... args) {
		try {
//			System.out.println(">> invoke "+name);
//			System.out.println(Arrays.stream(extensionClass.getMethods()).map(m -> m.getName() + " | "+Arrays.toString(m.getParameterTypes())).collect(Collectors.joining("\n")));
			return extensionClass.getMethod(name, classes.stream().toArray(Class[]::new))
				.invoke(extension, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new FriendlyException("Failed to invoke method "+name, e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return (List<String>) invokeMethod("onTabComplete",
				Arrays.asList(CommandSender.class, Command.class, String.class, String[].class),
				sender, command, alias, args);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return (boolean) invokeMethod("onCommand",
				Arrays.asList(CommandSender.class, Command.class, String.class, String[].class),
				sender, command, label, args);
	}

	@Override
	public File getDataFolder() {
		return (File) invokeMethod("getDataFolder", Collections.emptyList());
	}

	@Override
	public PluginDescriptionFile getDescription() {
		return (PluginDescriptionFile) invokeMethod("getDescription", Collections.emptyList());
	}

	@Override
	public FileConfiguration getConfig() {
		return (FileConfiguration) invokeMethod("getConfig", Collections.emptyList());
	}

	@Override
	public InputStream getResource(String filename) {
		return (InputStream) invokeMethod("getResource", Arrays.asList(String.class), filename);
	}

	@Override
	public void saveConfig() {
		invokeMethod("saveConfig", Collections.emptyList());
	}

	@Override
	public void saveDefaultConfig() {
		invokeMethod("saveDefaultConfig", Collections.emptyList());
	}

	@Override
	public void saveResource(String resourcePath, boolean replace) {
		invokeMethod("saveResource", Arrays.asList(String.class, boolean.class), resourcePath, replace);
	}

	@Override
	public void reloadConfig() {
		invokeMethod("reloadConfig", Collections.emptyList());
	}

	@Override
	public PluginLoader getPluginLoader() {
		return (PluginLoader) invokeMethod("getPluginLoader", Collections.emptyList());
	}

	@Override
	public Server getServer() {
		return (Server) invokeMethod("getServer", Collections.emptyList());
	}

	@Override
	public boolean isEnabled() {
		return (boolean) invokeMethod("isEnabled", Collections.emptyList());
	}

	@Override
	public void onDisable() {
		invokeMethod("onDisable", Collections.emptyList());
	}

	@Override
	public void onLoad() {
		invokeMethod("onLoad", Collections.emptyList());
	}

	@Override
	public void onEnable() {
		invokeMethod("onEnable", Collections.emptyList());
	}

	@Override
	public boolean isNaggable() {
		return (boolean) invokeMethod("isNaggable", Collections.emptyList());
	}

	@Override
	public void setNaggable(boolean canNag) {
		invokeMethod("setNaggable", Arrays.asList(boolean.class), canNag);
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return (ChunkGenerator) invokeMethod("getDefaultWorldGenerator", Arrays.asList(String.class, String.class), worldName, id);
	}

	@Override
	public Logger getLogger() {
		return (Logger) invokeMethod("getLogger", Collections.emptyList());
	}
	
	@Override
	public String getName() {
		return (String) invokeMethod("getName", Collections.emptyList());
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		invokeMethod("setEnabled", Arrays.asList(boolean.class), enabled);
	}
	
}
