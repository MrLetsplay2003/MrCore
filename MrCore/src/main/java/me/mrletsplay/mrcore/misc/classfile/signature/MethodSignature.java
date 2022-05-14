package me.mrletsplay.mrcore.misc.classfile.signature;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.misc.CharReader;
import me.mrletsplay.mrcore.misc.classfile.signature.Result.JavaTypeResult;
import me.mrletsplay.mrcore.misc.classfile.signature.Result.VoidResult;
import me.mrletsplay.mrcore.misc.classfile.signature.ThrowsSignature.ClassTypeThrowsSignature;
import me.mrletsplay.mrcore.misc.classfile.signature.ThrowsSignature.TypeVariableThrowsSignature;

public class MethodSignature implements Signature {
	
	private List<TypeParameter> typeParameters;
	private List<JavaTypeSignature> parameters;
	private Result result;
	private List<ThrowsSignature> thrownTypes;
	
	public MethodSignature(List<TypeParameter> typeParameters, List<JavaTypeSignature> parameters, Result result, List<ThrowsSignature> thrownTypes) {
		this.typeParameters = typeParameters;
		this.parameters = parameters;
		this.result = result;
		this.thrownTypes = thrownTypes;
	}
	
	public List<TypeParameter> getTypeParameters() {
		return typeParameters;
	}
	
	public List<JavaTypeSignature> getParameters() {
		return parameters;
	}
	
	public Result getResult() {
		return result;
	}
	
	public List<ThrowsSignature> getThrownTypes() {
		return thrownTypes;
	}
	
	public static MethodSignature read(CharReader reader) {
		List<TypeParameter> typeParameters = new ArrayList<>();
		if(reader.next() == '<') {
			while(reader.next() != '>') {
				reader.revert();
				typeParameters.add(TypeParameter.read(reader));
			}
		}else reader.revert();
		
		reader.next(); // Skip the (
		
		List<JavaTypeSignature> params = new ArrayList<>();
		if(reader.next() != ')') {
			reader.revert();
			while(reader.next() != ')') {
				reader.revert();
				params.add(JavaTypeSignature.read(reader));
			}
		}
		
		Result res;
		if(reader.next() == 'V') {
			res = new VoidResult();
		}else {
			reader.revert();
			res = new JavaTypeResult(JavaTypeSignature.read(reader));
		}
		
		List<ThrowsSignature> thrownTypes = new ArrayList<>();
		while(reader.hasNext() && reader.next() == '^') {
			if(reader.next() == 'L') {
				reader.revert();
				thrownTypes.add(new ClassTypeThrowsSignature(ClassTypeSignature.read(reader)));
			}else {
				thrownTypes.add(new TypeVariableThrowsSignature(TypeVariableSignature.read(reader)));
			}
		}
		
		return new MethodSignature(typeParameters, params, res, thrownTypes);
	}
	
	public static MethodSignature parse(String signature) {
		return read(new CharReader(signature));
	}

}
