package me.mrletsplay.mrcore.bukkitimpl;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import me.mrletsplay.mrcore.bukkitimpl.gui.GUI;
import me.mrletsplay.mrcore.bukkitimpl.gui.GUIListener;
import me.mrletsplay.mrcore.bukkitimpl.multiblock.MultiBlockStructureListener;
import me.mrletsplay.mrcore.bukkitimpl.ui.UIListener;
import me.mrletsplay.mrcore.bukkitimpl.versioned.NMSVersion;
import me.mrletsplay.mrcore.main.MrCore;

public class MrCorePlugin extends JavaPlugin{
	
	private static MrCoreConfig config;
	public static JavaPlugin pl;
	
	@Override
	public void onEnable() {
		pl = this;
		config = new MrCoreConfig();
		NMSVersion nmsv = NMSVersion.getCurrentServerVersion();
		getLogger().info("Applying compat for " + nmsv.getFriendlyName() + " / " + nmsv.name());
		Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
		Bukkit.getPluginManager().registerEvents(new MultiBlockStructureListener(), this);
		getCommand("mrcoreui").setExecutor(new UIListener());
		if(config.isUpdateCheckEnabled()) {
			String version = config.getVersionToUse();
			MrCoreUpdateChecker.checkForUpdate(version);
		}
		getLogger().info("And MrCore is on board as well! :wave:");
	}
	
	public static JavaPlugin getInstance() {
		return pl;
	}
	
	public static MrCoreConfig getMrCoreConfig() {
		return config;
	}
	
	public static File getBaseFolder() {
		return getInstance().getDataFolder();
	}
	
	@Override
	public void onDisable() {
		// Close all GUIs because them staying open would cause bugs
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.getOpenInventory() != null && p.getOpenInventory().getTopInventory() != null) {
				Inventory inv = p.getOpenInventory().getTopInventory();
				if(GUI.getGUI(inv) != null) p.closeInventory();
			}
		}
		
		getLogger().info("Goodbye");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equals("mrcore")) {
			if(args.length == 0) return sendCommandHelp(sender, null);
			if(args[0].equalsIgnoreCase("help")) {
				if(args.length == 1) return sendCommandHelp(sender, null);
				sendCommandHelp(sender, args[1]);
			}else if(args[0].equalsIgnoreCase("version")) {
				sender.sendMessage("§6MrCore version: §r"+MrCore.getVersion());
			}else {
				sendCommandHelp(sender, null);
			}
			return true;
		}
		return true;
	}
	
	private boolean sendCommandHelp(CommandSender sender, String topic) {
		sender.sendMessage("§7§lMrCore §r§8| §cHelp");
		if(topic == null) {
			sender.sendMessage("§6/mrcore version §b- Shows the current version");
		}else {
			sender.sendMessage("§cUnknown topic");
		}
		return true;
	}
	
}
