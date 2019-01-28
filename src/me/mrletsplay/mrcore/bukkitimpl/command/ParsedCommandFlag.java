package me.mrletsplay.mrcore.bukkitimpl.command;

public class ParsedCommandFlag<T> {

	private CommandFlag<T> flag;
	private T value;
	
	public ParsedCommandFlag(CommandFlag<T> flag, T value) {
		this.flag = flag;
		this.value = value;
	}
	
	public CommandFlag<T> getFlag() {
		return flag;
	}
	
	public T getValue() {
		return value;
	}
	
}
