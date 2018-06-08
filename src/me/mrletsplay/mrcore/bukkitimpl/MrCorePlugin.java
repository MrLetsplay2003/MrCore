package me.mrletsplay.mrcore.bukkitimpl;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import me.mrletsplay.mrcore.bukkitimpl.ChatUI.UIListener;
import me.mrletsplay.mrcore.bukkitimpl.GUIUtils.GUIListener;
import me.mrletsplay.mrcore.config.CustomConfig;
import me.mrletsplay.mrcore.extensions.ExtensionManager;
import me.mrletsplay.mrcore.extensions.JARLoader;
import me.mrletsplay.mrcore.extensions.MrCoreExtension;
import me.mrletsplay.mrcore.extensions.MrCorePluginLoader;
import me.mrletsplay.mrcore.main.MrCore;

public class MrCorePlugin extends JavaPlugin{
	
	private static CustomConfig config;
	public static JavaPlugin pl;
	
	@Override
	public void onEnable() {
		pl = this;
		getLogger().info("And MrCore is on board as well! :wave:");
		MaterialLookup.loadAll();
		Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
		Bukkit.getPluginManager().registerEvents(MrCorePluginLoader.getInstance(), this);
		getCommand("mrcoreui").setExecutor(new UIListener());
		config = new CustomConfig(new File(getDataFolder(), "config.yml")).loadConfigSafely();
		if(config.getBoolean("versioning.check-update", true, true)) {
			String version = config.getString("versioning.version-to-use", "latest", true);
			MrCoreUpdateChecker.checkForUpdate(version);
		}
		config.saveConfigSafely();
		
		JARLoader.registerFakeClass("org.bukkit.plugin.java.JavaPlugin", new File(getBaseFolder(), "JavaPlugin.class"));
		JARLoader.registerFakeClass("org.bukkit.command.PluginCommand", new File(getBaseFolder(), "PluginCommand.class"));
		
		ExtensionManager.loadExtensions(MrCorePluginLoader.getInstance().getPluginFolder());
		
		getLogger().info("Loaded a total of "+(JARLoader.totalBytesLoaded/1024)+" kb of classes");
	}
	
	public static JavaPlugin getInstance() {
		return pl;
	}
	
	public static File getBaseFolder() {
		return getInstance().getDataFolder();
	}
	
	@Override
	public void onDisable() {
		//Close all GUIs because them staying open would cause bugs
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.getOpenInventory() != null && p.getOpenInventory().getTopInventory() != null) {
				Inventory inv = p.getOpenInventory().getTopInventory();
				if(GUIUtils.getGUI(inv) != null) p.closeInventory();
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
			}else if(args[0].equalsIgnoreCase("plugins")) {
				if(args.length == 1) return sendCommandHelp(sender, "plugins");
				if(args[1].equalsIgnoreCase("enable")) {
					if(args.length == 2) return sendCommandHelp(sender, "plugins");
					MrCoreExtension ex = MrCorePluginLoader.getInstance().getPlugin(args[2]);
					if(ex == null) {
						sender.sendMessage("§cPlugin not found");
						return true;
					}
					if(!ex.isEnabled()) {
						ExtensionManager.enableExtension(ex);
						sender.sendMessage("§aPlugin enabled successfully");
					}else {
						sender.sendMessage("§cPlugin already enabled");
					}
				}else if(args[1].equalsIgnoreCase("disable")) {
					if(args.length == 2) return sendCommandHelp(sender, "plugins");
					MrCoreExtension ex = MrCorePluginLoader.getInstance().getPlugin(args[2]);
					if(ex == null) {
						sender.sendMessage("§cPlugin not found");
						return true;
					}
					if(ex.isEnabled()) {
						ExtensionManager.disableExtension(ex);
						sender.sendMessage("§aPlugin disabled successfully");
					}else {
						sender.sendMessage("§cPlugin not enabled");
					}
				}else if(args[1].equalsIgnoreCase("reload")) {
					if(args.length == 2) return sendCommandHelp(sender, "plugins");
					MrCoreExtension ex = MrCorePluginLoader.getInstance().getPlugin(args[2]);
					if(ex == null) {
						sender.sendMessage("§cPlugin not found");
						return true;
					}
					if(ex.isEnabled()) ExtensionManager.disableExtension(ex);
					ExtensionManager.enableExtension(ex);
					sender.sendMessage("§aPlugin reloaded successfully");
				}else if(args[1].equalsIgnoreCase("load")) {
					if(args.length == 2) return sendCommandHelp(sender, "plugins");
					sender.sendMessage("§cNot implemented yet");
				}else if(args[1].equalsIgnoreCase("unload")) {
					if(args.length == 2) return sendCommandHelp(sender, "plugins");
					MrCoreExtension ex = MrCorePluginLoader.getInstance().getPlugin(args[2]);
					if(ex == null) {
						sender.sendMessage("§cPlugin not found");
						return true;
					}
					ExtensionManager.unloadExtension(ex);
					sender.sendMessage("§aPlugin unloaded successfully");
				}else if(args[1].equalsIgnoreCase("info")) {
					if(args.length == 2) return sendCommandHelp(sender, "plugins");
					MrCoreExtension ex = MrCorePluginLoader.getInstance().getPlugin(args[2]);
					if(ex == null) {
						sender.sendMessage("§cPlugin not found");
						return true;
					}
					sender.sendMessage("§cNot implemented yet");
				}else if(args[1].equalsIgnoreCase("list")) {
					MrCorePluginLoader.getInstance().getAllPlugins().forEach(pl -> {
						sender.sendMessage("§7"+pl.getName()+" §8| "+(pl.isEnabled() ? "§aEnabled" : "§cDisabled"));
					});
				}else {
					sendCommandHelp(sender, "plugins");
				}
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
			sender.sendMessage("");
			sender.sendMessage("§6/mrcore help plugins §b- Shows help about the plugin/extension manager");
		}else if(topic.equalsIgnoreCase("plugins")) {
			sender.sendMessage("§6/mrcore plugins enable <Name> §b- Enables a plugin");
			sender.sendMessage("§6/mrcore plugins disable <Name>§b- Disables a plugin");
			sender.sendMessage("§6/mrcore plugins reload <Name> §b- Reloads a plugin"); 
			sender.sendMessage("§6/mrcore plugins load <File name> §b- Loads the plugin from the given file");
			sender.sendMessage("§6/mrcore plugins unload <Name> §b- Unloads a plugin to allow changing it");
			sender.sendMessage("§6/mrcore plugins info <Name> §b- Shows info about a plugin");
		}else {
			sender.sendMessage("§cUnknown topic");
		}
		return true;
	}
	
}
