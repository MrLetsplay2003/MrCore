package me.mrletsplay.mrcore.http.server.html;

import me.mrletsplay.mrcore.http.server.js.JSFunction;

public class CustomPollHandlerImpl implements CustomPollHandler {

	private PollType type;
	private JSFunction handlingFunction;
	
	protected CustomPollHandlerImpl(PollType type, JSFunction handlingFunction) {
		this.type = type;
		this.handlingFunction = handlingFunction;
	}
	
	@Override
	public JSFunction getHandlingFunction() {
		return handlingFunction;
	}

	@Override
	public PollType getHandlingType() {
		return type;
	}

}
