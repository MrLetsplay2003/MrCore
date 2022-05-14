package me.mrletsplay.mrcore.misc.classfile.signature;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.misc.CharReader;

public class TypeParameter {
	
	private String identifier;
	private ReferenceTypeSignature classBound;
	private List<ReferenceTypeSignature> interfaceBound;
	
	public TypeParameter(String identifier, ReferenceTypeSignature classBound,
			List<ReferenceTypeSignature> interfaceBound) {
		this.identifier = identifier;
		this.classBound = classBound;
		this.interfaceBound = interfaceBound;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public ReferenceTypeSignature getClassBound() {
		return classBound;
	}
	
	public List<ReferenceTypeSignature> getInterfaceBound() {
		return interfaceBound;
	}
	
	public static TypeParameter read(CharReader reader) {
		String identifier = reader.nextUntil(':');
		reader.next(); // Skip :
		ReferenceTypeSignature classBound = ReferenceTypeSignature.read(reader);
		List<ReferenceTypeSignature> interfaceBound = new ArrayList<>();
		while(reader.next() == ':') {
			interfaceBound.add(ReferenceTypeSignature.read(reader));
		}
		reader.revert(); // Go back the extra character we advanced
		return new TypeParameter(identifier, classBound, interfaceBound);
	}
	
}
