package me.mrletsplay.mrcore.bukkitimpl.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.mrletsplay.mrcore.command.AbstractCommand;
import me.mrletsplay.mrcore.command.properties.CommandProperties;
import me.mrletsplay.mrcore.command.provider.CommandProvider;

public abstract class BukkitCommand extends AbstractCommand<CommandProperties> implements CommandExecutor, TabCompleter, CommandProvider {

	public BukkitCommand(String name) {
		super(name);
	}
	
	public BukkitCommand(String name, CommandProperties initialProperties) {
		super(name, initialProperties);
	}
	
	@Override
	public List<BukkitCommand> getCommands() {
		return Collections.singletonList(this);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> s = tabComplete(alias + " " + Arrays.stream(args).collect(Collectors.joining(" ")));
		return s.stream()
				.map(c -> args[args.length - 1] + c)
				.collect(Collectors.toList());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		invoke(new BukkitCommandSender(sender), label + " " + Arrays.stream(args).collect(Collectors.joining(" ")));
		return true;
	}
	
	@Override
	public void sendCommandInfo(me.mrletsplay.mrcore.command.CommandSender sender) {
		sender.sendMessage("§6Command: §f" + getName());
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
	
}