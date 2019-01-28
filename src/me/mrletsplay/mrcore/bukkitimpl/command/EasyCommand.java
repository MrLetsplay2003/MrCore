package me.mrletsplay.mrcore.bukkitimpl.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mrletsplay.mrcore.misc.ErroringNullableOptional;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public abstract class EasyCommand implements CommandExecutor {

	private EasyCommand parent;
	private String name;
	private List<String> aliases;
	private String permission, description, usage;
	private List<EasyCommand> subCommands;
	private List<CommandFlag<?>> registeredFlags;
	
	public EasyCommand(EasyCommand parent, String name) {
		this.parent = parent;
		this.name = name;
		this.aliases = new ArrayList<>();
		this.subCommands = new ArrayList<>();
		this.registeredFlags = new ArrayList<>();
	}
	
	public EasyCommand(String name) {
		this(null, name);
	}
	
	public EasyCommand getParent() {
		return parent;
	}
	
	public String getName() {
		return name;
	}
	
	public EasyCommand addAlias(String alias) {
		this.aliases.add(alias);
		return this;
	}
	
	public List<String> getAliases() {
		return aliases;
	}
	
	public EasyCommand setPermission(String permission) {
		this.permission = permission;
		return this;
	}
	
	public String getPermission() {
		return permission;
	}
	
	public EasyCommand setDescription(String description) {
		this.description = description;
		return this;
	}
	
	public String getDescription() {
		return description;
	}
	
	public EasyCommand setUsage(String usage) {
		this.usage = usage;
		return this;
	}
	
	public String getUsage() {
		return usage;
	}
	
	public EasyCommand addSubCommand(EasyCommand command) {
		this.subCommands.add(command);
		command.parent = this;
		return command;
	}
	
	public EasyCommand addSubCommand(String name, Consumer<CommandInvokedEvent> executor) {
		EasyCommand ec = new EasyCommand(this, name) {
			
			@Override
			public void action(CommandInvokedEvent event) {
				executor.accept(event);
			}
		};
		addSubCommand(ec);
		return ec;
	}
	
	public List<? extends EasyCommand> getSubCommands() {
		return subCommands;
	}

	public EasyCommand registerFlag(CommandFlag<?> flag) {
		if(registeredFlags.stream().anyMatch(f -> f.getName().equals(flag.getName()))) throw new IllegalArgumentException("A flag with that name is already registered");
		this.registeredFlags.add(flag);
		return this;
	}
	
	public CommandFlag<?> getRegisteredFlag(String name) {
		return registeredFlags.stream().filter(f -> f.getName().equals(name)).findFirst().orElse(null);
	}
	
	public List<CommandFlag<?>> getRegisteredFlags() {
		return registeredFlags;
	}
	
	public abstract void action(CommandInvokedEvent event);
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String argsString = Arrays.stream(args).collect(Collectors.joining(" "));
		ErroringNullableOptional<ParsedCommand, CommandParsingException> c = CommandParser.parse(this, label, argsString);
		if(!c.isPresent()) {
			if(sender instanceof Player) {
				sender.sendMessage("§cAn error occured while parsing the command: §7" + c.getException().getMessage());
				sender.sendMessage("");
				String prev = argsString.substring(0, c.getException().getIndex());
				String f = argsString.substring(c.getException().getIndex(), c.getException().getIndex() + c.getException().getLength());
				String after = argsString.substring(c.getException().getIndex() + c.getException().getLength());
				
				TextComponent prevC = new TextComponent(prev);
				prevC.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("This part is fine").color(ChatColor.GREEN).create()));
				prevC.setColor(ChatColor.GRAY);
				
				TextComponent fC = new TextComponent(f);
				fC.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("There is an error here").color(ChatColor.RED).create()));
				fC.setColor(ChatColor.RED);
				
				TextComponent afterC = new TextComponent(after);
				afterC.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("This part wasn't checked yet").color(ChatColor.YELLOW).create()));
				afterC.setColor(ChatColor.GRAY);
				
				TextComponent tryAgain = new TextComponent("[Try again]");
				tryAgain.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Reenteer your previous input").color(ChatColor.YELLOW).create()));
				tryAgain.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + label + " " + argsString));
				tryAgain.setColor(ChatColor.YELLOW);
				
				((Player) sender).spigot().sendMessage(new ComponentBuilder("/" + label).color(ChatColor.GRAY)
						.append(new TextComponent(" "))
						.append(prevC)
						.append(fC)
						.append(afterC)
						.append(new TextComponent(" "))
						.append(tryAgain)
						.create());
			}else {
				sender.sendMessage("§cAn error occured while parsing the command: §7" + c.getException().getMessage());
				sender.sendMessage("");
				String prev = argsString.substring(0, c.getException().getIndex());
				String f = argsString.substring(c.getException().getIndex(), c.getException().getIndex() + c.getException().getLength());
				String after = argsString.substring(c.getException().getIndex() + c.getException().getLength());
				sender.sendMessage("§7" + label + " " + prev + "§c" + f + "§r" + after);
				sender.sendMessage(StringUtils.repeat("-", label.length() + c.getException().getIndex() + 1) + StringUtils.repeat("^", c.getException().getLength()) + " There is an error here");
			}
			return true;
		}
		CommandInvokedEvent event = new CommandInvokedEvent(sender, c.get());
		action(event);
		return true;
	}
	
}
