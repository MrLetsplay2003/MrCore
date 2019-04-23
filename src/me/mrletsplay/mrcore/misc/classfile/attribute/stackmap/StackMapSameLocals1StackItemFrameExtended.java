package me.mrletsplay.mrcore.misc.classfile.attribute.stackmap;

import java.io.DataInputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.misc.classfile.attribute.stackmap.verification.VerificationTypeInfo;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;

public class StackMapSameLocals1StackItemFrameExtended extends AbstractStackMapFrame {

	private int offsetDelta;
	private VerificationTypeInfo typeInfo;
	
	public StackMapSameLocals1StackItemFrameExtended(ConstantPool pool, int tag, DataInputStream dIn) throws IOException {
		super(tag);
		this.offsetDelta = dIn.readUnsignedShort();
		this.typeInfo = VerificationTypeInfo.read(pool, dIn);
	}
	
	public VerificationTypeInfo getTypeInfo() {
		return typeInfo;
	}
	
	@Override
	public StackMapFrameType getType() {
		return StackMapFrameType.SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED;
	}

	@Override
	public int getOffsetDelta() {
		return offsetDelta;
	}
	
}
