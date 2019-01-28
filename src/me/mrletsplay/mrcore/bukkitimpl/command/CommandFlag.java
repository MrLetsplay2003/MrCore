package me.mrletsplay.mrcore.bukkitimpl.command;

public class CommandFlag<T> {

	private String name;
	private boolean needsValue;
	private CommandValueType<T> valueType;
	
	public CommandFlag(String name, boolean needsValue, CommandValueType<T> valueType) {
		this.name = name;
		this.needsValue = needsValue;
		this.valueType = valueType;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean needsValue() {
		return needsValue;
	}
	
	public CommandValueType<T> getValueType() {
		return valueType;
	}
	
}
