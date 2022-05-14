package me.mrletsplay.mrcore.command.parser;

import java.util.function.Predicate;

import me.mrletsplay.mrcore.command.event.filter.ArgumentFilterEvent;
import me.mrletsplay.mrcore.command.event.filter.CommandFilterEvent;
import me.mrletsplay.mrcore.command.event.filter.OptionArgumentFilterEvent;
import me.mrletsplay.mrcore.command.event.filter.OptionFilterEvent;

public class CommandParsingProperties {
	
	private boolean caseSensitive;
	
	private Predicate<CommandFilterEvent> tabCompleteCommandFilter;
	private Predicate<OptionFilterEvent> tabCompleteOptionFilter;
	private Predicate<ArgumentFilterEvent> tabCompleteArgumentFilter;
	private Predicate<OptionArgumentFilterEvent> tabCompleteOptionArgumentFilter;
	
	public CommandParsingProperties() {
		this.caseSensitive = false;
		this.tabCompleteCommandFilter = c -> true;
		this.tabCompleteOptionFilter = o -> true;
		this.tabCompleteArgumentFilter = a -> true;
		this.tabCompleteOptionArgumentFilter = a -> true;
	}
	
	/**
	 * Sets whether this command parser is case sensitive or not (default <code>false</code>)<br>
	 * If this is <code>true</code>, then command names will only be interpreted as valid commands if they match the command name exactly (including case)
	 * @param caseSensitive Whether this command parser is case sensitive or not
	 */
	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}
	
	/**
	 * Returns whether this command parser is case sensitive or not (default <code>false</code>)<br>
	 * @return Whether this command parser is case sensitive or not
	 * @see #setCaseSensitive(boolean)
	 */
	public boolean isCaseSensitive() {
		return caseSensitive;
	}
	
	/**
	 * Sets the filter for tab completions of (sub-)commands<br>
	 * This will be called for each completion after all of the possible completions have been determined.
	 * @param tabCompleteCommandFilter The filter
	 */
	public void setTabCompleteCommandFilter(Predicate<CommandFilterEvent> tabCompleteCommandFilter) {
		this.tabCompleteCommandFilter = tabCompleteCommandFilter;
	}
	
	/**
	 * Returns the filter for tab completions of (sub-)commands
	 * @return The filter
	 * @see #setTabCompleteCommandFilter(Predicate)
	 */
	public Predicate<CommandFilterEvent> getTabCompleteCommandFilter() {
		return tabCompleteCommandFilter;
	}

	/**
	 * Sets the filter for tab completions of command option names<br>
	 * This will be called for each completion after all of the possible completions have been determined.
	 * @param tabCompleteOptionFilter The filter
	 */
	public void setTabCompleteOptionFilter(Predicate<OptionFilterEvent> tabCompleteOptionFilter) {
		this.tabCompleteOptionFilter = tabCompleteOptionFilter;
	}
	
	/**
	 * Returns the filter for tab completions of command option names
	 * @return The filter
	 * @see #setTabCompleteOptionFilter(Predicate)
	 */
	public Predicate<OptionFilterEvent> getTabCompleteOptionFilter() {
		return tabCompleteOptionFilter;
	}
	
	/**
	 * Sets the filter for tab completions of command arguments<br>
	 * This will be called for each completion after all of the possible completions have been determined.
	 * @param tabCompleteArgumentFilter The filter
	 */
	public void setTabCompleteArgumentFilter(Predicate<ArgumentFilterEvent> tabCompleteArgumentFilter) {
		this.tabCompleteArgumentFilter = tabCompleteArgumentFilter;
	}

	/**
	 * Returns the filter for tab completions of command arguments
	 * @return The filter
	 * @see #setTabCompleteArgumentFilter(Predicate)
	 */
	public Predicate<ArgumentFilterEvent> getTabCompleteArgumentFilter() {
		return tabCompleteArgumentFilter;
	}

	/**
	 * Sets the filter for tab completions of command option arguments<br>
	 * This will be called for each completion after all of the possible completions have been determined.
	 * @param tabCompleteOptionArgumentFilter The filter
	 */
	public void setTabCompleteOptionArgumentFilter(Predicate<OptionArgumentFilterEvent> tabCompleteOptionArgumentFilter) {
		this.tabCompleteOptionArgumentFilter = tabCompleteOptionArgumentFilter;
	}
	
	/**
	 * Returns the filter for tab completions of command option arguments
	 * @return The filter
	 * @see #setTabCompleteOptionArgumentFilter(Predicate)
	 */
	public Predicate<OptionArgumentFilterEvent> getTabCompleteOptionArgumentFilter() {
		return tabCompleteOptionArgumentFilter;
	}

}
