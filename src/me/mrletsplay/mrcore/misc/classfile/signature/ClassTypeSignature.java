package me.mrletsplay.mrcore.misc.classfile.signature;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.misc.CharReader;

public class ClassTypeSignature extends ReferenceTypeSignature {
	
	private String className;
	private List<TypeArgument> typeArguments;
	private String suffixes;
	private List<List<TypeArgument>> suffixTypeArguments;

	public ClassTypeSignature(String className, List<TypeArgument> typeArguments) {
		this.className = className;
		this.typeArguments = typeArguments;
	}
	
	public String getClassName() {
		return className;
	}
	
	public List<TypeArgument> getTypeArguments() {
		return typeArguments;
	}
	
	public String getSuffixes() {
		return suffixes;
	}
	
	public List<List<TypeArgument>> getSuffixTypeArguments() {
		return suffixTypeArguments;
	}
	
	public static ClassTypeSignature read(CharReader reader) {
		reader.next(); // Skip the L
		
		String className = reader.nextUntilAny(';', '<');
		List<TypeArgument> typeArguments = new ArrayList<>();
		if(reader.next() == '<') { // Type parameters
			reader.revert();
			typeArguments = readTypeArguments(reader);
		}
		
		List<String> suffixes = new ArrayList<>();
		List<List<TypeArgument>> suffixTypeArguments = new ArrayList<>();
		
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
	
	private static List<TypeArgument> readTypeArguments(CharReader reader) {
		reader.next(); // Skip the <
		List<TypeArgument> typeArguments = new ArrayList<>();
		while(reader.next() != '>') {
			reader.revert();
			typeArguments.add(TypeArgument.read(reader));
		}
		return typeArguments;
	}
	
}