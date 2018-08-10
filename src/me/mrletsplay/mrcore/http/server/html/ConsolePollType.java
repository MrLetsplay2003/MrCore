package me.mrletsplay.mrcore.http.server.html;

public enum ConsolePollType implements PollType {
	
	CONSOLE_LINE("console_line");

	private String identifier;
	
	private ConsolePollType(String identifier) {
		this.identifier = identifier;
	}
	
	@Override
	public String getIdentifier() {
		return identifier;
	}

}
