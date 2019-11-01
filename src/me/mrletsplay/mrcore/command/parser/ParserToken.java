package me.mrletsplay.mrcore.command.parser;

import java.util.List;
import java.util.function.Function;

public class ParserToken<T> {
	
	private boolean isComplete;
	private T value;
	private List<String> completions;
	
	public ParserToken(T value) {
		this.isComplete = true;
		this.value = value;
	}
	
	public ParserToken(List<String> completions) {
		this.isComplete = false;
		this.completions = completions;
	}
	
	public boolean isComplete() {
		return isComplete;
	}
	
	public T getValue() throws IllegalStateException {
		if(!isComplete) throw new IllegalStateException("Token is incomplete");
		return value;
	}
	
	public List<String> getCompletions() throws IllegalStateException {
		if(isComplete) throw new IllegalStateException("Token is complete");
		return completions;
	}
	
	public <O> ParserToken<O> map(Function<T, O> mapper) {
		if(!isComplete) return new ParserToken<>(completions);
		return new ParserToken<>(mapper.apply(value));
	}

}
