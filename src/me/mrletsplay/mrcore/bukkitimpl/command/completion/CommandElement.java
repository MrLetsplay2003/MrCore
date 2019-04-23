package me.mrletsplay.mrcore.bukkitimpl.command.completion;

public abstract class CommandElement {

	private String value;
	
	public CommandElement(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public abstract CommandElementType getType();
	
	public boolean isOfType(CommandElementType type) {
		return getType().equals(type);
	}
	
}
