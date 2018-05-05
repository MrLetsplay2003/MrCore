package me.mrletsplay.mrcore.bukkitimpl;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import me.mrletsplay.mrcore.bukkitimpl.ChatUI.UIListener;
import me.mrletsplay.mrcore.bukkitimpl.GUIUtils.GUIListener;
import me.mrletsplay.mrcore.config.CustomConfig;
import me.mrletsplay.mrcore.main.MrCore;

public class MrCorePlugin extends JavaPlugin{
	
	private static CustomConfig config;
	public static JavaPlugin pl;
	
	@Override
	public void onEnable() {
		pl = this;
		getLogger().info("And MrCore is on board as well! :wave:");
		Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
		getCommand("mrcoreui").setExecutor(new UIListener());
		config = new CustomConfig(new File(getDataFolder(), "config.yml"), false).loadConfigSafely();
		if(config.getBoolean("versioning.auto-update", true, true)) {
			String version = config.getString("versioning.version-to-use", "latest", true);
			MrCoreUpdater.update(version);
		}
		config.saveConfigSafely();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equals("mrcore")) {
			sender.sendMessage("MrCore version "+MrCore.getVersion());
			return true;
		}
		return true;
	}
	
}
