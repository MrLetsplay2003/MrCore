package me.mrletsplay.mrcore.bukkitimpl.command.completion;

import java.util.List;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandTabCompletions {

	private boolean overrideDefaultCompletions;
	private List<String> completions;
	
	public CommandTabCompletions() {
		this.overrideDefaultCompletions = false;
		this.completions = new ArrayList<>();
	}
	
	public void setOverrideDefaultCompletions(boolean override) {
		this.overrideDefaultCompletions = override;
	}
	
	public boolean shouldOverrideDefaultCompletions() {
		return overrideDefaultCompletions;
	}
	
	public void addCompletion(String completion) {
		completions.add(completion);
	}
	
	public void addCompletions(List<String> completions) {
		this.completions.addAll(completions);
	}
	
	public void addCompletions(String... completions) {
		this.completions.addAll(Arrays.asList(completions));
	}
	
	public void removeCompletion(String completion) {
		completions.remove(completion);
	}
	
	public void removeCompletions(List<String> completions) {
		this.completions.removeAll(completions);
	}
	
	public void removeCompletions(String... completions) {
		this.completions.removeAll(Arrays.asList(completions));
	}
	
	public List<String> getCompletions() {
		return completions;
	}
	
	public static CommandTabCompletions of(List<String> completions) {
		CommandTabCompletions c = new CommandTabCompletions();
		c.addCompletions(completions);
		return c;
	}
	
}
