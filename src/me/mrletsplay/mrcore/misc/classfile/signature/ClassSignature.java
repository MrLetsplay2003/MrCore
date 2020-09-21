package me.mrletsplay.mrcore.misc.classfile.signature;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.misc.CharReader;

public class ClassSignature implements Signature {
	
	private List<TypeParameter> typeParameters;
	private ClassTypeSignature superclassSignature;
	private List<ClassTypeSignature> superinterfaceSignatures;
	
	public ClassSignature(List<TypeParameter> typeParameters, ClassTypeSignature superclassSignature, List<ClassTypeSignature> superinterfaceSignatures) {
		this.typeParameters = typeParameters;
		this.superclassSignature = superclassSignature;
		this.superinterfaceSignatures = superinterfaceSignatures;
	}
	
	public List<TypeParameter> getTypeParameters() {
		return typeParameters;
	}
	
	public ClassTypeSignature getSuperclassSignature() {
		return superclassSignature;
	}
	
	public List<ClassTypeSignature> getSuperinterfaceSignatures() {
		return superinterfaceSignatures;
	}
	
	public static ClassSignature read(CharReader reader) {
		List<TypeParameter> typeParameters = new ArrayList<>();
		if(reader.next() == '<') {
			while(reader.next() != '>') {
				reader.revert();
				typeParameters.add(TypeParameter.read(reader));
			}
		}else reader.revert();
		
		ClassTypeSignature superclassSig = ClassTypeSignature.read(reader);
		
		List<ClassTypeSignature> superinterfaceSigs = new ArrayList<>();
		if(reader.hasNext()) {
			while(reader.next() == 'L') {
				reader.revert();
				superinterfaceSigs.add(ClassTypeSignature.read(reader));
			}
			
			reader.revert(); // Revert the extra character
		}
		
		return new ClassSignature(typeParameters, superclassSig, superinterfaceSigs);
	}
	
	public static ClassSignature parse(String signature) {
		return read(new CharReader(signature));
	}
	
}
