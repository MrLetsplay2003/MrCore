package me.mrletsplay.mrcore.main;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.UnknownDependencyException;

public class MrCorePluginLoader implements PluginLoader {

	@Override
	public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener arg0, Plugin arg1) {
		return null;
	}

	@Override
	public void disablePlugin(Plugin p) {
		
	}

	@Override
	public void enablePlugin(Plugin p) {
		
	}

	@Override
	public PluginDescriptionFile getPluginDescription(File f) throws InvalidDescriptionException {
		return null;
	}

	@Override
	public Pattern[] getPluginFileFilters() {
		return null;
	}

	@Override
	public Plugin loadPlugin(File f) throws InvalidPluginException, UnknownDependencyException {
		return null;
	}

}
