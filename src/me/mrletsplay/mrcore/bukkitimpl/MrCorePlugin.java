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
import me.mrletsplay.mrcore.bukkitimpl.command.CommandFlag;
import me.mrletsplay.mrcore.bukkitimpl.command.CommandInvokedEvent;
import me.mrletsplay.mrcore.bukkitimpl.command.DefaultCommandValueType;
import me.mrletsplay.mrcore.bukkitimpl.command.EasyCommand;
import me.mrletsplay.mrcore.bukkitimpl.command.completion.CommandTabCompletions;
import me.mrletsplay.mrcore.bukkitimpl.versioned.NMSVersion;
import me.mrletsplay.mrcore.config.ConfigLoader;
import me.mrletsplay.mrcore.config.FileCustomConfig;
import me.mrletsplay.mrcore.main.MrCore;

public class MrCorePlugin extends JavaPlugin{
	
	private static FileCustomConfig config;
	public static JavaPlugin pl;
	
	@Override
	public void onEnable() {
		pl = this;
		NMSVersion nmsv = NMSVersion.getCurrentServerVersion();
		getLogger().info("Applying compat for " + nmsv.getFriendlyName() + " / " + nmsv.name());
		Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
		getCommand("mrcoreui").setExecutor(new UIListener());
		config = ConfigLoader.loadFileConfig(new File(getDataFolder(), "config.yml"), true);
		if(config.getBoolean("versioning.check-update", true, true)) {
			String version = config.getString("versioning.version-to-use", "latest", true);
			MrCoreUpdateChecker.checkForUpdate(version);
		}
		
		EasyCommand c = new EasyCommand("mrcoredebug") {
			
			@Override
			public void action(CommandInvokedEvent event) {
				event.getSender().sendMessage("Hello there!");
				event.getSender().sendMessage(event.getParsedCommand().getRaw());
			}
		}
		.registerFlag(new CommandFlag<>("aflag", true, DefaultCommandValueType.STRING))
		.registerFlag(new CommandFlag<>("bflag", true, DefaultCommandValueType.INT))
		.registerFlag(new CommandFlag<>("cflag", false, null))
		.setTabCompleter(event -> {
			System.out.println(event.getElementToComplete());
			CommandTabCompletions t = new CommandTabCompletions();
			switch(event.getElementToComplete().getType()) {
				case ARGUMENT:
					t.addCompletion("arg1");
					t.addCompletion("arg2");
					break;
				case FLAG_NAME:
//					t.addCompletion("fname");
//					t.addCompletion("aflag");
					break;
				case FLAG_VALUE:
					t.addCompletion("fval");
					t.addCompletion("1");
					break;
				default:
					break;
			}
			return t;
		});
		

		c.addSubCommand("subcommand", event -> {
			event.getSender().sendMessage("Why tho?");
		});
		c.registerFor(this);
		
		config.saveToFile();
		getLogger().info("And MrCore is on board as well! :wave:");
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
