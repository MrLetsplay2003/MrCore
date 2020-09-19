package me.mrletsplay.mrcore.misc;

public class CharReader {
	
	private int currentIndex;
	private String string;
	
	public CharReader(String string) {
		this.string = string;
		this.currentIndex = 0;
	}
	
	public char next() {
		if(currentIndex == string.length()) throw new FriendlyException("EOF reached");
		return string.charAt(currentIndex++);
	}
	
	public String next(int amount) {
		char[] chs = new char[amount];
		for(int i = 0; i < amount; i++) chs[i] = next();
		return new String(chs);
	}
	
	public char nextIgnoreWhitespaces() {
		char c = next();
		while(Character.isWhitespace(c)) c = next();
		return c;
	}
	
	public CharReader revert(int num) {
		currentIndex -= num;
		if(currentIndex < 0) {
			currentIndex += num;
			throw new FriendlyException("Illegal reader move");
		}
		return this;
	}
	
	public String nextUntil(char terminator) {
		StringBuilder b = new StringBuilder();
		char c;
		while((c = next()) != terminator) {
			if(!hasNext()) throw new FriendlyException("Reached end of string");
			b.append(c);
		}
		revert();
		return b.toString();
	}
	
	public String nextUntilAny(char... terminator) {
		String str = new String(terminator);
		StringBuilder b = new StringBuilder();
		char c;
		while(str.indexOf(c = next()) == -1) {
			if(!hasNext()) throw new FriendlyException("Reached end of string");
			b.append(c);
		}
		revert();
		return b.toString();
	}
	
	public CharReader revert() {
		return revert(1);
	}
	
	public boolean contains(String s) {
		return string.substring(currentIndex, string.length()).contains(s);
	}
	
	public boolean hasNext() {
		return currentIndex < string.length();
	}

}
