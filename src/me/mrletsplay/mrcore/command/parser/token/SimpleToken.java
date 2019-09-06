package me.mrletsplay.mrcore.command.parser.token;

import java.util.List;
import java.util.function.Function;

public class SimpleToken<T> implements ParserToken<T> {
	
	private boolean isComplete;
	private T value;
	private List<String> completions;
	
	public SimpleToken(T value) {
		this.isComplete = true;
		this.value = value;
	}
	
	public SimpleToken(List<String> completions) {
		this.isComplete = false;
		this.completions = completions;
	}
	
	@Override
	public boolean isComplete() {
		return isComplete;
	}
	
	@Override
	public T getValue() throws IllegalStateException {
		if(!isComplete) throw new IllegalStateException("Token is incomplete");
		return value;
	}
	
	@Override
	public List<String> getCompletions() throws IllegalStateException {
		if(isComplete) throw new IllegalStateException("Token is complete");
		return completions;
	}
	
	@Override
	public <O> ParserToken<O> map(Function<T, O> mapper) {
		if(!isComplete) return new SimpleToken<>(completions);
		return new SimpleToken<>(mapper.apply(value));
	}

}
