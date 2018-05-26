package me.mrletsplay.mrcore.main;

import org.bukkit.plugin.java.JavaPlugin;

public class MrCoreBukkitExtension {

	private JavaPlugin plugin;
	
	public MrCoreBukkitExtension(JavaPlugin pl) {
		this.plugin = pl;
	}
	
	public JavaPlugin getPlugin() {
		return plugin;
	}
	
}
