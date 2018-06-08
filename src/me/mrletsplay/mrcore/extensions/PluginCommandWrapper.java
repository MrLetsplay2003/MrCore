package me.mrletsplay.mrcore.extensions;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

// TODO: Add methods from org.bukkit.command.Command
public class PluginCommandWrapper extends Command implements PluginIdentifiableCommand {

	private PluginCommand base;

	public PluginCommandWrapper(String name, Plugin owner) {
		super(name);
		try {
			base = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class).newInstance(name, owner);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	public PluginCommandWrapper(PluginCommand base) {
		super(base.getName());
		this.base = base;
	}

	public PluginCommandWrapper(PluginCommandWrapper base) {
		super(base.getName());
		this.base = base.base;
	}

	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		return base.execute(sender, commandLabel, args);
	}

	public void setExecutor(CommandExecutor executor) {
		base.setExecutor(executor);
	}

	public CommandExecutor getExecutor() {
		return base.getExecutor();
	}

	public void setTabCompleter(TabCompleter completer) {
		base.setTabCompleter(completer);
	}

	public TabCompleter getTabCompleter() {
		return base.getTabCompleter();
	}

	public Plugin getPlugin() {
		return base.getPlugin();
	}

	public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
		return base.tabComplete(sender, alias, args);
	}

	public PluginCommand getBase() {
		return base;
	}

	public String toString() {
		return base.toString();
	}

	public List<String> getAliases() {
		return base.getAliases();
	}

	public String getDescription() {
		return base.getDescription();
	}

	public String getLabel() {
		return base.getLabel();
	}

	public String getName() {
		return base.getName();
	}

	public String getPermission() {
		return base.getPermission();
	}

	public String getPermissionMessage() {
		return base.getPermissionMessage();
	}

	public String getUsage() {
		return base.getUsage();
	}

	public Command setAliases(List<String> aliases) {
		return base.setAliases(aliases);
	}

	public Command setDescription(String description) {
		return base.setDescription(description);
	}

	public boolean setLabel(String name) {
		return base.setLabel(name);
	}

	public boolean setName(String name) {
		return base.setName(name);
	}

	public void setPermission(String permission) {
		base.setPermission(permission);
	}

	public Command setPermissionMessage(String permissionMessage) {
		return base.setPermissionMessage(permissionMessage);
	}

	public Command setUsage(String usage) {
		return base.setUsage(usage);
	}

	public boolean isRegistered() {
		return base.isRegistered();
	}

	public boolean register(CommandMap commandMap) {
		return base.register(commandMap);
	}

	public boolean unregister(CommandMap commandMap) {
		return base.unregister(commandMap);
	}

	public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location)
			throws IllegalArgumentException {
		return base.tabComplete(sender, alias, args, location);
	}

	public boolean testPermission(CommandSender target) {
		return base.testPermission(target);
	}

	public boolean testPermissionSilent(CommandSender target) {
		return base.testPermissionSilent(target);
	}

}
