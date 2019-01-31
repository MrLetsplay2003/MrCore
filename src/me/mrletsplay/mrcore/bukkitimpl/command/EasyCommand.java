package me.mrletsplay.mrcore.bukkitimpl.command;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.mrletsplay.mrcore.misc.ErroringNullableOptional;
import me.mrletsplay.mrcore.misc.FriendlyException;
import me.mrletsplay.mrcore.misc.MiscUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public abstract class EasyCommand implements CommandExecutor, TabCompleter {

	private EasyCommand parent;
	private String name;
	private List<String> aliases;
	private String permission, description, usage;
	private List<EasyCommand> subCommands;
	private List<CommandFlag<?>> registeredFlags;
	private CommandExecutionProperties executionProperties;
	
	public EasyCommand(EasyCommand parent, String name) {
		this.parent = parent;
		this.name = name;
		this.executionProperties = new CommandExecutionProperties();
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
	
	public EasyCommand getSubCommand(String name) {
		return subCommands.stream()
				.filter(s -> s.getName().equalsIgnoreCase(name) || s.getAliases().stream().anyMatch(a -> a.equalsIgnoreCase(name)))
				.findFirst().orElse(null);
	}
	
	public EasyCommand registerFor(JavaPlugin plugin) {
		if(parent != null) throw new IllegalStateException("Command is not parent command");
		plugin.getCommand(name).setExecutor(this);
		return this;
	}
	
	public String getFullName() {
		return parent != null ? parent.getFullName() + " " + name : name;
	}
	
	public CommandExecutionProperties getExecutionProperties() {
		return executionProperties;
	}
	
	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		try {
			return getClass().getMethod("action", CommandInvokedEvent.class).getAnnotation(annotationClass);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new FriendlyException(e);
		}
	}
	
	public CommandExecutionProperties getEffectiveExecutionProperties() {
		CommandExecution ex = getAnnotation(CommandExecution.class);
		return new CommandExecutionProperties(executionProperties.getAllowSubCommands()
				.orElse((boolean) MiscUtils.getAnnotationValueOrDefault(CommandExecution.class, ex, "onlyAllowSubCommands")));
	}
	
	public abstract void action(CommandInvokedEvent event);
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length > 0) {
			EasyCommand sC = getSubCommand(args[0]);
			if(sC != null) return sC.onCommand(sender, command, args[0], Arrays.stream(args).skip(1).toArray(String[]::new));
		}
		String argsString = Arrays.stream(args).collect(Collectors.joining(" "));
		ErroringNullableOptional<ParsedCommand, CommandParsingException> c = CommandParser.parse(this, label, argsString);
		if(!c.isPresent()) {
			sendErrorMessage(sender, c.getException(), argsString);
			return true;
		}
		CommandInvokedEvent event = new CommandInvokedEvent(sender, c.get());
		try {
			action(event);
		} catch(CommandExecutionException e) {
			sendErrorMessage(sender, e, argsString);
		}
		return true;
	}
	
	private void sendErrorMessage(CommandSender sender, CommandException e, String argsString) {
		if(sender instanceof Player) {
			sender.sendMessage("§cAn error occured while executing the command: §7" + e.getMessage());
			sender.sendMessage("");
			String prev = argsString.substring(0, e.getIndex());
			String f = argsString.substring(e.getIndex(), e.getIndex() + e.getLength());
			String after = argsString.substring(e.getIndex() + e.getLength());
			
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
			tryAgain.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + getFullName() + " " + argsString));
			tryAgain.setColor(ChatColor.YELLOW);
			
			((Player) sender).spigot().sendMessage(new ComponentBuilder("/" + getFullName()).color(ChatColor.GRAY)
					.append(new TextComponent(" "))
					.append(prevC)
					.append(fC)
					.append(afterC)
					.append(new TextComponent(" "))
					.append(tryAgain)
					.create());
		}else {
			sender.sendMessage("§cAn error occured while parsing the command: §7" + e.getMessage());
			sender.sendMessage("");
			String prev = argsString.substring(0, e.getIndex());
			String f = argsString.substring(e.getIndex(), e.getIndex() + e.getLength());
			String after = argsString.substring(e.getIndex() + e.getLength());
			sender.sendMessage("§7" + getFullName() + " " + prev + "§c" + f + "§r" + after);
			sender.sendMessage(StringUtils.repeat("-", getFullName().length() + e.getIndex() + 1) + StringUtils.repeat("^", e.getLength()) + " There is an error here");
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(args.length > 0) {
			EasyCommand sC = getSubCommand(args[0]);
			if(sC != null) return sC.onTabComplete(sender, command, args[0], Arrays.stream(args).skip(1).toArray(String[]::new));
		}
		return subCommands.stream().map(EasyCommand::getName).filter(s -> args.length == 0 ? true : s.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList()); // TODO: Dynamic completions
	}
	
}
