package me.mrletsplay.mrcore.misc.classfile.signature;

import me.mrletsplay.mrcore.misc.CharReader;

public class TypeArgumentSignature {
	
	private boolean any;
	private Character wildcardIndicator;
	private ReferenceTypeSignature typeSignature;
	
	public TypeArgumentSignature() {
		this.any = true;
	}
	
	public TypeArgumentSignature(Character wildcardIndicator, ReferenceTypeSignature typeSignature) {
		this.any = false;
		this.wildcardIndicator = wildcardIndicator;
		this.typeSignature = typeSignature;
	}

	public boolean isAny() {
		return any;
	}
	
	public Character getWildcardIndicator() {
		return wildcardIndicator;
	}
	
	public ReferenceTypeSignature getTypeSignature() {
		return typeSignature;
	}
	
	public boolean isExtends() {
		return wildcardIndicator != null && wildcardIndicator.equals('+');
	}
	
	public boolean isSuper() {
		return wildcardIndicator != null && wildcardIndicator.equals('-');
	}
	
	public boolean isExact() {
		return !any && wildcardIndicator == null;
	}
	
	public Wildcard getWildcard() {
		if(any) return Wildcard.ANY;
		if(wildcardIndicator == null) return Wildcard.NONE;
		return isExtends() ? Wildcard.EXTENDS : Wildcard.SUPER;
	}
	
	public static TypeArgumentSignature read(CharReader reader) {
		char c = reader.next();
		if(c == '*') return new TypeArgumentSignature();
		Character wc = c == '+' || c == '-' ? c : null;
		if(wc == null) reader.revert();
		return new TypeArgumentSignature(wc, ReferenceTypeSignature.read(reader));
	}
	
	public static enum Wildcard {
		
		ANY,
		EXTENDS,
		SUPER,
		NONE;
		
	}

}
