package me.mrletsplay.mrcore.extensions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;

import me.mrletsplay.mrcore.bukkitimpl.MrCorePlugin;

public class JavaPlugin implements Plugin {

	private JARLoader loader;
	private PluginDescriptionFile desc;
	private boolean enabled;
	
	public JavaPlugin() {
		/*if(!(getClassLoader() instanceof JARLoader)) {
			throw new RuntimeException("yay: "+getClassLoader().getClass().getName());
		}*/
		System.out.println("hi");
	}
	
	public void init(JARLoader loader) {
		this.loader = loader;
		try {
			desc = new PluginDescriptionFile(new InputStreamReader(loader.getResource("plugin.yml").openStream()));
		} catch (InvalidDescriptionException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public ClassLoader getClassLoader() {
		return getClass().getClassLoader();
	}
	
	public JARLoader getLoader() {
		return loader;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		return false;
	}

	@Override
	public FileConfiguration getConfig() {
		return null;
	}

	@Override
	public File getDataFolder() {
		return new File(MrCorePlugin.getInstance().getDataFolder(), getName()+"/config.yml");
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String arg0, String arg1) {
		return null;
	}

	@Override
	public PluginDescriptionFile getDescription() {
		return desc;
	}

	@Override
	public Logger getLogger() {
		return Logger.getLogger(getName());
	}

	@Override
	public String getName() {
		return "MrCore_extension";
	}

	@Override
	public PluginLoader getPluginLoader() {
		return MrCorePluginLoader.getInstance();
	}

	@Override
	public InputStream getResource(String name) {
		try {
			return loader.getResource(name).openStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Server getServer() {
		return Bukkit.getServer();
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if(enabled) {
			onEnable();
		}else {
			onDisable();
		}
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean isNaggable() {
		return false;
	}

	@Override
	public void onDisable() {
		
	}

	@Override
	public void onEnable() {
		
	}

	@Override
	public void onLoad() {
		
	}

	@Override
	public void reloadConfig() {
		
	}

	@Override
	public void saveConfig() {
		
	}

	@Override
	public void saveDefaultConfig() {
		
	}

	@Override
	public void saveResource(String arg0, boolean arg1) {
		
	}

	@Override
	public void setNaggable(boolean arg0) {
		
	}
	
}
