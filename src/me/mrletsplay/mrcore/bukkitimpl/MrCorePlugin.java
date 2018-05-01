package me.mrletsplay.mrcore.bukkitimpl;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import me.mrletsplay.mrcore.bukkitimpl.ChatUI.UIListener;
import me.mrletsplay.mrcore.bukkitimpl.GUIUtils.GUIListener;

public class MrCorePlugin extends JavaPlugin{
	
	@Override
	public void onEnable() {
		getLogger().info("And MrCore is on board as well! :wave:");
		Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
		getCommand("mrcoreui").setExecutor(new UIListener());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return true;
	}
	
}
