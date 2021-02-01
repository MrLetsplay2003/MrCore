package me.mrletsplay.mrcore.command.parser;

import java.util.function.Predicate;

import me.mrletsplay.mrcore.command.event.filter.ArgumentFilterEvent;
import me.mrletsplay.mrcore.command.event.filter.CommandFilterEvent;
import me.mrletsplay.mrcore.command.event.filter.OptionArgumentFilterEvent;
import me.mrletsplay.mrcore.command.event.filter.OptionFilterEvent;

public class CommandParsingProperties {
	
	private Predicate<CommandFilterEvent> tabCompleteCommandFilter;
	private Predicate<OptionFilterEvent> tabCompleteOptionFilter;
	private Predicate<ArgumentFilterEvent> tabCompleteArgumentFilter;
	private Predicate<OptionArgumentFilterEvent> tabCompleteOptionArgumentFilter;
	
	public CommandParsingProperties() {
		this.tabCompleteCommandFilter = c -> true;
		this.tabCompleteOptionFilter = o -> true;
		this.tabCompleteArgumentFilter = a -> true;
		this.tabCompleteOptionArgumentFilter = a -> true;
	}
	
	public void setTabCompleteCommandFilter(Predicate<CommandFilterEvent> tabCompleteCommandFilter) {
		this.tabCompleteCommandFilter = tabCompleteCommandFilter;
	}
	
	public Predicate<CommandFilterEvent> getTabCompleteCommandFilter() {
		return tabCompleteCommandFilter;
	}
	
	public void setTabCompleteOptionFilter(Predicate<OptionFilterEvent> tabCompleteOptionFilter) {
		this.tabCompleteOptionFilter = tabCompleteOptionFilter;
	}
	
	public Predicate<OptionFilterEvent> getTabCompleteOptionFilter() {
		return tabCompleteOptionFilter;
	}
	
	public void setTabCompleteArgumentFilter(Predicate<ArgumentFilterEvent> tabCompleteArgumentFilter) {
		this.tabCompleteArgumentFilter = tabCompleteArgumentFilter;
	}
	
	public Predicate<ArgumentFilterEvent> getTabCompleteArgumentFilter() {
		return tabCompleteArgumentFilter;
	}
	
	public void setTabCompleteOptionArgumentFilter(Predicate<OptionArgumentFilterEvent> tabCompleteOptionArgumentFilter) {
		this.tabCompleteOptionArgumentFilter = tabCompleteOptionArgumentFilter;
	}
	
	public Predicate<OptionArgumentFilterEvent> getTabCompleteOptionArgumentFilter() {
		return tabCompleteOptionArgumentFilter;
	}

}
