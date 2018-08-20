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
import me.mrletsplay.mrcore.bukkitimpl.versioned.NMSVersion;
import me.mrletsplay.mrcore.config.ConfigLoader;
import me.mrletsplay.mrcore.config.CustomConfig;
import me.mrletsplay.mrcore.http.server.HttpConnection;
import me.mrletsplay.mrcore.http.webinterface.ConsoleInterceptor;
import me.mrletsplay.mrcore.http.webinterface.Webinterface;
import me.mrletsplay.mrcore.http.webinterface.WebinterfaceDataManager;
import me.mrletsplay.mrcore.main.MrCore;

public class MrCorePlugin extends JavaPlugin{
	
	private static CustomConfig config;
	public static JavaPlugin pl;
	
	@Override
	public void onEnable() {
		pl = this;
//		MaterialLookup.loadAll();
		NMSVersion nmsv = NMSVersion.getCurrentServerVersion();
		getLogger().info("Applying compat for " + nmsv.friendlyName + " / " + nmsv.name());
		Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
		getCommand("mrcoreui").setExecutor(new UIListener());
		config = ConfigLoader.loadConfig(new File(getDataFolder(), "config.yml"));
		WebinterfaceDataManager.init();
		if(WebinterfaceDataManager.isWebinterfaceEnabled()) Webinterface.start();
		if(config.getBoolean("versioning.check-update", true, true)) {
			String version = config.getString("versioning.version-to-use", "latest", true);
			MrCoreUpdateChecker.checkForUpdate(version);
		}
		config.saveConfigSafely();
		getLogger().info("And MrCore is on board as well! :wave:");
		ConsoleInterceptor.a();
	}
	
	public static JavaPlugin getInstance() {
		return pl;
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
				if(GUIUtils.getGUI(inv) != null) p.closeInventory();
			}
		}
		
		Webinterface.stop();
		
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
			}else if(args[0].equalsIgnoreCase("web")) {
				if(!(sender instanceof Player)) {
					sender.sendMessage("§cThe console can't do that");
					return true;
				}
				if(args.length == 1) return sendCommandHelp(sender, null);
				if(args[1].equalsIgnoreCase("register-confirm")) {
					if(args.length != 3) return sendCommandHelp(sender, null);
					HttpConnection con = Webinterface.getServer().getConnection(args[2]);
					if(con == null) return sendCommandHelp(sender, null);
					Webinterface.confirmRegistration((Player) sender, con);
				}else return sendCommandHelp(sender, null);
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
