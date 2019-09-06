package me.mrletsplay.mrcore.command.parser.token;

import java.util.List;
import java.util.function.Function;

public interface ParserToken<T> {
	
	public boolean isComplete();
	
	public T getValue() throws IllegalStateException;
	
	public List<String> getCompletions() throws IllegalStateException;
	
	public <O> ParserToken<O> map(Function<T, O> mapper);

}
