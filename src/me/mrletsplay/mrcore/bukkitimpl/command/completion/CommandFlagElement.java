package me.mrletsplay.mrcore.bukkitimpl.command.completion;

public class CommandFlagElement extends CommandElement {

	public CommandFlagElement(String value) {
		super(value);
	}

	@Override
	public CommandElementType getType() {
		return CommandElementType.FLAG_NAME;
	}
	
	@Override
	public String toString() {
		return "[CFlg: \"" + getValue() + "\"]";
	}

}
