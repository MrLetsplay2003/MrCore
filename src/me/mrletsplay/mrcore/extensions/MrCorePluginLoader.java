package me.mrletsplay.mrcore.extensions;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
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

public class MrCorePluginLoader implements PluginLoader, Listener, CommandMap {

	private static MrCorePluginLoader instance = new MrCorePluginLoader();
	private static File pluginFolder = new File(MrCorePlugin.getBaseFolder(), "extensions");
	private static File pluginDataFolder = new File(MrCorePlugin.getBaseFolder(), "extensions");
	private static HashMap<String, PluginCommandWrapper> commands = new HashMap<>();
	
	static {
		if(!pluginFolder.exists()) pluginFolder.mkdirs();
		if(!pluginDataFolder.exists()) pluginDataFolder.mkdirs();
	}
	
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
	
	public void addPlugin(MrCoreExtension e) {
		if(!e.isEnabled()) enablePlugin(e);
		plugins.add(e);
	}
	
	public void removePlugin(MrCoreExtension e) {
		if(e.isEnabled()) disablePlugin(e);
		plugins.remove(e);
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

	@Override
	public void enablePlugin(Plugin p) {
		MrCoreExtension ex = (MrCoreExtension) p;
		if(ex.isEnabled()) return;
		
		List<Command> cmds = PluginCommandYamlParser.parse(p);
		
		cmds.forEach(c -> register(p.getName(), c));
		
		ex.onLoad();
		ex.setEnabled(true);
	}

	@Override
	public void disablePlugin(Plugin p) {
		MrCoreExtension ex = (MrCoreExtension) p;
		if(!ex.isEnabled()) return;
		
		Iterator<Map.Entry<String, PluginCommandWrapper>> it = commands.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, PluginCommandWrapper> en = it.next();
			if(en.getValue().getPlugin().getName().equals(p.getName())) {
				en.getValue().unregister(this);
				it.remove();
			}
		}
		
		ex.setEnabled(false);
	}
	
	public void unloadPlugins() {
		plugins.forEach(ExtensionManager::unloadExtension);
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
		return pluginDataFolder;
	}
	
	public File getPluginFolder() {
		return pluginFolder;
	}

	@EventHandler
	public void onConsoleCommand(ServerCommandEvent e) {
		if(dispatch(e.getSender(), e.getCommand())) e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		if(dispatch(e.getPlayer(), e.getMessage())) e.setCancelled(true);
	}
	
	@EventHandler
	public void onTabComplete(TabCompleteEvent e) {
		List<String> c = new ArrayList<>(e.getCompletions());
		c.addAll(tabComplete(e.getSender(), e.getBuffer()));
		e.setCompletions(c);
	}

	@Override
	public void registerAll(String fallbackPrefix, List<Command> commands) {
		commands.forEach(c -> register(fallbackPrefix, c));
	}

	@Override
	public boolean register(String label, String fallbackPrefix, Command command) {
		PluginCommandWrapper pc = new PluginCommandWrapper((PluginCommand) command);
		label = label.toLowerCase().trim();
		String prefix = fallbackPrefix.toLowerCase().trim();
		
		commands.put(prefix + ":" + label, pc);
		
		if(commands.containsKey(label)) {
			PluginCommandWrapper conf = commands.get(label);
			MrCorePlugin.pl.getLogger().warning("Interfering command labels for /"+prefix+":"+label+" and /"+conf.getPlugin().getName()+":"+conf.getLabel());
			return false;
		}
		
		commands.put(label, pc);
		
		for(String alias : pc.getAliases()) {
			commands.put(prefix + ":" + alias, pc);
			if(commands.containsKey(alias)) continue;
			commands.put(alias, pc);
		}
		return true;
	}

	@Override
	public boolean register(String fallbackPrefix, Command command) {
		return register(command.getLabel(), fallbackPrefix, command);
	}

	@Override
	public boolean dispatch(CommandSender sender, String cmdLine) throws CommandException {
//		if(!cmdLine.startsWith("/")) return false;
		String[] spl = cmdLine.split(" ");
		String cmdName = spl[0].substring(1);
		PluginCommandWrapper wrapper = getCommand(cmdName);
		if(wrapper != null) {
			wrapper.execute(sender, cmdName, Arrays.stream(spl).skip(1).toArray(String[]::new));
			return true;
		}
		return false;
	}

	@Override
	public void clearCommands() {
		commands.values().forEach(c -> c.unregister(this));
		commands.clear();
	}

	@Override
	public PluginCommandWrapper getCommand(String name) {
		return commands.get(name);
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String cmdLine) throws IllegalArgumentException {
		if(!cmdLine.startsWith("/")) return new ArrayList<>();
		String[] spl = cmdLine.split(" ", -1);
		String cmdName = spl[0].substring(1);
		if(spl.length == 1 && !cmdLine.endsWith(" ")) {
			return commands.keySet().stream().filter(k -> k.startsWith(cmdName)).map(k -> "/"+k).collect(Collectors.toList());
		}else {
			PluginCommandWrapper wrapper = getCommand(cmdName);
			if(wrapper != null) {
				return wrapper.tabComplete(sender, cmdName, Arrays.stream(spl).skip(1).toArray(String[]::new));
			}
		}
		return new ArrayList<>();
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String cmdLine, Location location) throws IllegalArgumentException {
		return new ArrayList<>();
	}

}
