package me.mrletsplay.mrcore.misc.classfile.attribute.stackmap;

import java.io.DataInputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.misc.classfile.attribute.stackmap.verification.VerificationTypeInfo;
import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;

public class StackMapAppendFrame extends AbstractStackMapFrame {

	private int offsetDelta;
	private VerificationTypeInfo[] additionalTypeInfo;
	
	public StackMapAppendFrame(ConstantPool pool, int tag, DataInputStream dIn) throws IOException {
		super(tag);
		this.offsetDelta = dIn.readUnsignedShort();
		this.additionalTypeInfo = new VerificationTypeInfo[tag - 251];
		for(int i = 0; i < additionalTypeInfo.length; i++) {
			additionalTypeInfo[i] = VerificationTypeInfo.read(pool, dIn);
		}
	}
	
	public VerificationTypeInfo[] getAdditionalTypeInfo() {
		return additionalTypeInfo;
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
