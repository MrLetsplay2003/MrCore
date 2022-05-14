package me.mrletsplay.mrcore.bukkitimpl.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.mrletsplay.mrcore.command.AbstractCommand;
import me.mrletsplay.mrcore.command.event.CommandInvokedEvent;
import me.mrletsplay.mrcore.command.parser.CommandParser;
import me.mrletsplay.mrcore.command.parser.CommandParsingException;
import me.mrletsplay.mrcore.command.parser.ParsedCommand;
import me.mrletsplay.mrcore.command.provider.CommandProvider;

public abstract class BukkitCommand extends AbstractCommand<BukkitCommandProperties> implements CommandExecutor, TabCompleter, CommandProvider {
	
	private CommandParser parser;
	
	public BukkitCommand(String name, BukkitCommandProperties initialProperties) {
		super(name, initialProperties);
		this.parser = new CommandParser(this);
		
		parser.getParsingProperties().setTabCompleteCommandFilter(event -> {
			return ((BukkitCommand) event.getObject()).checkPermission(((BukkitCommandSender) event.getSender()).getBukkitSender());
		});
		
		parser.getParsingProperties().setTabCompleteOptionFilter(event -> {
			return ((BukkitCommand) event.getCommand()).checkPermission(((BukkitCommandSender) event.getSender()).getBukkitSender());
		});
		
		parser.getParsingProperties().setTabCompleteArgumentFilter(event -> {
			return ((BukkitCommand) event.getCommand()).checkPermission(((BukkitCommandSender) event.getSender()).getBukkitSender());
		});
		
		parser.getParsingProperties().setTabCompleteOptionArgumentFilter(event -> {
			return ((BukkitCommand) event.getCommand()).checkPermission(((BukkitCommandSender) event.getSender()).getBukkitSender());
		});
	}

	public BukkitCommand(String name) {
		this(name, new BukkitCommandProperties());
	}
	
	public BukkitCommand(PluginCommand command, BukkitCommandProperties initialProperties) {
		this(command.getName(), initialProperties);
		addAlias(command.getPlugin().getName() + ":" + command.getName());
		command.getAliases().forEach(a -> {
			addAlias(a);
			addAlias(command.getPlugin().getName() + ":" + a);
		});
		getProperties().setPermission(command.getPermission());
		setUsage(command.getUsage());
		setDescription(command.getDescription());
	}
	
	public BukkitCommand(PluginCommand command) {
		this(command, new BukkitCommandProperties());
	}
	
	@Override
	public List<BukkitCommand> getCommands() {
		return Collections.singletonList(this);
	}
	
	@Override
	public CommandParser getCommandParser() {
		return parser;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> s = tabComplete(new BukkitCommandSender(sender), alias + " " + Arrays.stream(args).collect(Collectors.joining(" ")));
		return s.stream()
				.map(c -> args[args.length - 1] + c)
				.collect(Collectors.toList());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			invoke(new BukkitCommandSender(sender), label + " " + Arrays.stream(args).collect(Collectors.joining(" ")));
		}catch(CommandParsingException e) {
			sender.sendMessage("§cError: §7" + e.getMessage());
		}
		return true;
	}
	
	public void setPermission(String permission) {
		getProperties().setPermission(permission);
	}
	
	public String getPermission() {
		return getProperties().getPermission();
	}
	
	public String getEffectivePermission() {
		String perm = getProperties().getPermission();
		
		if(BukkitCommandProperties.PERMISSION_DEFAULT.equals(perm)) {
			if(getParent() == null) return null; // We have reached a top level command with no explicit permission
			return ((BukkitCommand) getParent()).getEffectivePermission();
		}
		
		return perm;
	}
	
	private boolean checkPermission(CommandSender sender) {
		String perm = getEffectivePermission();
		if(perm == null) return true; // Explicitly no permission
		return sender.hasPermission(perm);
	}
	
	@Override
	public void invoke(me.mrletsplay.mrcore.command.CommandSender sender, String commandLine)throws CommandParsingException {
		ParsedCommand cmd = parseCommand(sender, commandLine);
		
		BukkitCommand b = (BukkitCommand) cmd.getCommand();
		if(!b.checkPermission(((BukkitCommandSender) sender).getBukkitSender())) {
			sender.sendMessage("§cYou don't have the required permission §7" + getEffectivePermission() + " §cto use this command");
			return;
		}
		
		cmd.getCommand().action(new CommandInvokedEvent(sender, cmd));
	}
	
	@Override
	public void sendCommandInfo(me.mrletsplay.mrcore.command.CommandSender sender) {
		sender.sendMessage("§6Command: §f" + getFullName());
		if(getDescription() != null) sender.sendMessage("§6Description: §f" + getDescription());
		if(!getOptions().isEmpty()) sender.sendMessage("§6Available options: §f" + getOptions().stream().map(o -> "--" + o.getLongName()).collect(Collectors.joining(", ")));
		if(getUsage() != null) sender.sendMessage("§6Usage: §f" + getUsage());
		if(!getSubCommands().isEmpty()) {
			sender.sendMessage("");
			sender.sendMessage("§6Sub commands:");
			for(me.mrletsplay.mrcore.command.Command sub : getSubCommands()) {
				sender.sendMessage((sub.getUsage() == null ? "§7/" + sub.getFullName() : "§7" + sub.getUsage()) + (sub.getDescription() == null ? "" : " §8- §f" + sub.getDescription()));
			}
		}
	}
	
	protected boolean isSenderPlayer(CommandInvokedEvent event) {
		return event.getSender() instanceof BukkitCommandSender
				&& ((BukkitCommandSender) event.getSender()).getBukkitSender() instanceof Player;
	}
	
	protected Player getSenderPlayer(CommandInvokedEvent event) {
		return ((BukkitCommandSender) event.getSender()).asPlayer();
	}
	
	protected boolean isSenderConsole(CommandInvokedEvent event) {
		return event.getSender() instanceof BukkitCommandSender
				&& ((BukkitCommandSender) event.getSender()).getBukkitSender() instanceof ConsoleCommandSender;
	}
	
}
