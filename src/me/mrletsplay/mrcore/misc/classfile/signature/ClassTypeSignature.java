package me.mrletsplay.mrcore.misc.classfile.signature;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.misc.CharReader;

public class ClassTypeSignature extends ReferenceTypeSignature {
	
	private String className;
	private List<TypeArgumentSignature> typeArguments;
	private String suffixes;
	private List<List<TypeArgumentSignature>> suffixTypeArguments;

	public ClassTypeSignature(String className, List<TypeArgumentSignature> typeArguments) {
		this.className = className;
		this.typeArguments = typeArguments;
	}
	
	public String getClassName() {
		return className;
	}
	
	public List<TypeArgumentSignature> getTypeArguments() {
		return typeArguments;
	}
	
	public String getSuffixes() {
		return suffixes;
	}
	
	public List<List<TypeArgumentSignature>> getSuffixTypeArguments() {
		return suffixTypeArguments;
	}
	
	public static ClassTypeSignature read(CharReader reader) {
		reader.next(); // Skip the L
		
		String className = reader.nextUntilAny(';', '<');
		List<TypeArgumentSignature> typeArguments = new ArrayList<>();
		if(reader.next() == '<') { // Type parameters
			reader.revert();
			typeArguments = readTypeArguments(reader);
		}
		
		List<String> suffixes = new ArrayList<>();
		List<List<TypeArgumentSignature>> suffixTypeArguments = new ArrayList<>();
		
		if(reader.hasNext()) {
			while(reader.next() == '.') {
				String identifier = reader.nextUntilAny('<', ';', '.');
				suffixes.add(identifier);
				char next = reader.next();
				switch(next) {
					case '<':
						reader.revert();
						suffixTypeArguments.add(readTypeArguments(reader));
						continue;
					default:
						suffixTypeArguments.add(new ArrayList<>());
						reader.revert();
						continue;
				}
			}
			
			reader.revert(); // Revert the extra character
		}
		
		reader.next(); // Skip the ;
		return new ClassTypeSignature(className, typeArguments);
	}
	
	private static List<TypeArgumentSignature> readTypeArguments(CharReader reader) {
		reader.next(); // Skip the <
		List<TypeArgumentSignature> typeArguments = new ArrayList<>();
		while(reader.next() != '>') {
			reader.revert();
			typeArguments.add(TypeArgumentSignature.read(reader));
		}
		return typeArguments;
	}
	
}