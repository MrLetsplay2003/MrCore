package me.mrletsplay.mrcore.misc.classfile.attribute.stackmap;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.misc.classfile.attribute.stackmap.verification.VerificationTypeInfo;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;

public class StackMapSameLocals1StackItemFrame extends AbstractStackMapFrame {

	private int offsetDelta;
	private VerificationTypeInfo typeInfo;
	
	public StackMapSameLocals1StackItemFrame(ConstantPool pool, int tag, DataInputStream dIn) throws IOException {
		super(tag);
		this.offsetDelta = tag - 64;
		this.typeInfo = VerificationTypeInfo.read(pool, dIn);
	}
	
	public StackMapSameLocals1StackItemFrame(int offsetDelta, VerificationTypeInfo typeInfo) {
		super(offsetDelta + 64);
		this.offsetDelta = offsetDelta;
		this.typeInfo = typeInfo;
	}

	public VerificationTypeInfo getTypeInfo() {
		return typeInfo;
	}
	
	@Override
	public StackMapFrameType getType() {
		return StackMapFrameType.SAME_LOCALS_1_STACK_ITEM_FRAME;
	}
	
	public void setOffsetDelta(int offsetDelta) {
		this.offsetDelta = offsetDelta;
	}

	@Override
	public int getOffsetDelta() {
		return offsetDelta;
	}
	
	@Override
	public void write(DataOutputStream dOut) throws IOException {
		dOut.write(getTag());
		typeInfo.write(dOut);
	}
	
}
