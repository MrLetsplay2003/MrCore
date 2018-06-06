package me.mrletsplay.mrcore.extensions;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.UnknownDependencyException;

import me.mrletsplay.mrcore.bukkitimpl.MrCorePlugin;
import me.mrletsplay.mrcore.misc.OtherTools.FriendlyException;

public class MrCorePluginLoader implements PluginLoader, Listener {

	public static MrCorePluginLoader instance = new MrCorePluginLoader();
	
	public static MrCorePluginLoader getInstance() {
		return instance;
	}
	
	private List<MrCoreExtension> plugins;
	
	public MrCorePluginLoader() {
		plugins = new ArrayList<>();
	}
	
	public List<MrCoreExtension> getAllPlugins() {
		return plugins;
	}
	
	public List<MrCoreExtension> getEnabledPlugins() {
		return plugins.stream().filter(e -> e.isEnabled()).collect(Collectors.toList());
	}
	
	public MrCoreExtension getPlugin(String name) {
		return plugins.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
	}
	
	@Override
	public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener l, Plugin p) {
		Map<Class<? extends Event>, Set<RegisteredListener>> listeners = new HashMap<>();
		for(Method m : l.getClass().getMethods()) {
			m.setAccessible(true);
			if(m.isAnnotationPresent(EventHandler.class)) {
				Class<?> clazz = m.getParameterTypes()[0];
				if(!Event.class.isAssignableFrom(clazz)) throw new FriendlyException("Not an event: "+clazz.getName());
				Class<? extends Event> eventClazz = clazz.asSubclass(Event.class);
				EventHandler e = m.getAnnotation(EventHandler.class);
				Set<RegisteredListener> ls = listeners.getOrDefault(eventClazz, new HashSet<>());
				RegisteredListener rl = new RegisteredListener(l, new EventExecutor() {
					
					@Override
					public void execute(Listener l, Event e) throws EventException {
						try {
							if(!eventClazz.isAssignableFrom(e.getClass())) return;
							m.invoke(l, e);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
							e1.printStackTrace();
						}
					}
				}, e.priority(), p, e.ignoreCancelled());
				ls.add(rl);
				listeners.put(eventClazz, ls);
			}
		}
		MrCorePlugin.pl.getLogger().info("Registered "+listeners.size()+" listener(s) for "+p.getName());
		return listeners;
	}

	/**
	 * @see ExtensionManager#enableExtension(MrCoreExtension)
	 */
	@Override
	@Deprecated
	public void enablePlugin(Plugin p) {
		MrCoreExtension ex = (MrCoreExtension) p;
		ex.onLoad();
		ex.setEnabled(true);
		plugins.add((MrCoreExtension) p);
	}

	@Override
	public void disablePlugin(Plugin p) {
		MrCoreExtension ex = (MrCoreExtension) p;
		ex.setEnabled(false);
		plugins.remove(p);
	}
	
	public void disablePlugins() {
		plugins.forEach(MrCorePluginLoader.getInstance()::disablePlugin);
	}

	@Override
	public PluginDescriptionFile getPluginDescription(File f) throws InvalidDescriptionException {
		return ExtensionManager.preLoad(f).getDescription();
	}

	@Override
	public Pattern[] getPluginFileFilters() {
		return new Pattern[0];
	}

	@Override
	public MrCoreExtension loadPlugin(File f) throws InvalidPluginException, UnknownDependencyException {
		return ExtensionManager.loadExtension(f);
	}
	
	public File getPluginDataFolder() {
		return new File(MrCorePlugin.pl.getDataFolder(), "extensions");
	}
	
	public File getPluginFolder() {
		return new File(MrCorePlugin.pl.getDataFolder(), "extensions");
	}
	
	@EventHandler
	public void onTab(TabCompleteEvent e) {
		e.getCompletions().clear();
		if(e.getBuffer().charAt(0) == '/') {
			for(MrCoreExtension ex : getEnabledPlugins()) {
				e.getCompletions().add(ex.getName());
			}
		}
	}

}
