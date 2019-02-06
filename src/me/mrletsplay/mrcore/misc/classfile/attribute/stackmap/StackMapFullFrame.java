package me.mrletsplay.mrcore.misc.classfile.attribute.stackmap;

import java.io.DataInputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.misc.classfile.attribute.stackmap.verification.VerificationTypeInfo;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;

public class StackMapFullFrame extends AbstractStackMapFrame {

	private int offsetDelta;
	private VerificationTypeInfo[] locals, stack;
	
	public StackMapFullFrame(ConstantPool pool, int tag, DataInputStream dIn) throws IOException {
		super(tag);
		this.offsetDelta = dIn.readUnsignedShort();
		this.locals = new VerificationTypeInfo[dIn.readUnsignedShort()];
		for(int i = 0; i < locals.length; i++) {
			locals[i] = VerificationTypeInfo.read(pool, dIn);
		}
		this.stack = new VerificationTypeInfo[dIn.readUnsignedShort()];
		for(int i = 0; i < stack.length; i++) {
			stack[i] = VerificationTypeInfo.read(pool, dIn);
		}
	}
	
	public VerificationTypeInfo[] getLocals() {
		return locals;
	}
	
	public VerificationTypeInfo[] getStack() {
		return stack;
	}
	
	@Override
	public StackMapFrameType getType() {
		return StackMapFrameType.APPEND_FRAME;
	}

	@Override
	public int getOffsetDelta() {
		return offsetDelta;
	}
	
}
