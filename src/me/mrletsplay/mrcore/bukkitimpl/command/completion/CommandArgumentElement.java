package me.mrletsplay.mrcore.bukkitimpl.command.completion;

public class CommandArgumentElement extends CommandElement {

	private int argumentIndex;
	private boolean isQuoted, needsClosingQuote;
	
	public CommandArgumentElement(String value, int argumentIndex, boolean isQuoted, boolean needsClosingQuote) {
		super(value);
		this.argumentIndex = argumentIndex;
		this.isQuoted = isQuoted;
		this.needsClosingQuote = needsClosingQuote;
	}
	
	public int getArgumentIndex() {
		return argumentIndex;
	}
	
	public boolean isQuoted() {
		return isQuoted;
	}
	
	public boolean needsClosingQuote() {
		return needsClosingQuote;
	}

	@Override
	public CommandElementType getType() {
		return CommandElementType.ARGUMENT;
	}
	
	@Override
	public String toString() {
		return "[CArg @ " + argumentIndex + ": \"" + getValue() + "\"]";
	}

}
