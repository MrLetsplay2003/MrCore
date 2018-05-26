package me.mrletsplay.mrcore.main;

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

public class MrCoreExtension implements Plugin {

	private JARLoader loader;
	
	public MrCoreExtension() {
		Bukkit.getPluginManager().enablePlugin(this);
		if(!(getClassLoader() instanceof JARLoader)) {
			throw new RuntimeException("yay: "+getClassLoader().getClass().getName());
		}
	}
	
	public void init(JARLoader loader) {
		this.loader = loader;
	}
	
	public ClassLoader getClassLoader() {
		return getClass().getClassLoader();
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
		return null;
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String arg0, String arg1) {
		return null;
	}

	@Override
	public PluginDescriptionFile getDescription() {
		try {
			System.out.println(loader.getResource("plugin.yml"));
			return new PluginDescriptionFile(new InputStreamReader(loader.getResource("plugin.yml").openStream()));
		} catch (InvalidDescriptionException | IOException e) {
			e.printStackTrace();
			return null;
		}
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
		return null;
	}

	@Override
	public InputStream getResource(String arg0) {
		return null;
	}

	@Override
	public Server getServer() {
		return Bukkit.getServer();
	}

	@Override
	public boolean isEnabled() {
		return false;
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
