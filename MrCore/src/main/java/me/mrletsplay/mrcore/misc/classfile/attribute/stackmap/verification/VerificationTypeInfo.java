package me.mrletsplay.mrcore.misc.classfile.attribute.stackmap.verification;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;

public interface VerificationTypeInfo {

	public VerificationType getType();
	
	public void write(DataOutputStream dOut) throws IOException;
	
	public default <T extends VerificationTypeInfo> T as(Class<T> clazz) {
		if(!clazz.isInstance(this)) throw new RuntimeException("Illegal verification type info subtype");
		return clazz.cast(this);
	}
	
	public static VerificationTypeInfo read(ConstantPool pool, DataInputStream dIn) throws IOException {
		VerificationType t = VerificationType.getByValue(dIn.readUnsignedByte());
		switch(t) {
			case DOUBLE:
			case FLOAT:
			case INTEGER:
			case LONG:
			case NULL:
			case TOP:
			case UNINITIALIZED_THIS:
				return new VariableInfoGeneric(t);
			case OBJECT:
				return new VariableInfoObject(pool, dIn);
			case UNINITIALIZED_VARIABLE:
				return new VariableInfoUninitialized(dIn);
		}
		throw new RuntimeException();
	}
	
}
