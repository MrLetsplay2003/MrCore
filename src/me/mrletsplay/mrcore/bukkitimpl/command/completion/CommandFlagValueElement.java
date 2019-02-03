package me.mrletsplay.mrcore.bukkitimpl.command.completion;

import me.mrletsplay.mrcore.bukkitimpl.command.CommandFlag;

public class CommandFlagValueElement extends CommandElement {

	private CommandFlag<?> flag;
	private String operator;
	private boolean isQuoted, needsClosingQuote;
	
	public CommandFlagValueElement(String value, CommandFlag<?> flag, String operator, boolean isQuoted, boolean needsClosingQuote) {
		super(value);
		this.flag = flag;
		this.operator = operator;
		this.isQuoted = isQuoted;
		this.needsClosingQuote = needsClosingQuote;
	}
	
	public CommandFlag<?> getFlag() {
		return flag;
	}
	
	public String getOperator() {
		return operator;
	}
	
	public boolean isQuoted() {
		return isQuoted;
	}
	
	public boolean needsClosingQuote() {
		return needsClosingQuote;
	}

	@Override
	public CommandElementType getType() {
		return CommandElementType.FLAG_VALUE;
	}
	
	@Override
	public String toString() {
		return "[CFlg for \"" + flag.getName() + "\" w/ op \"" + operator + "\": \"" + getValue() + "\"]";
	}
	
}
