package me.mrletsplay.mrcore.misc.classfile.attribute.stackmap;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StackMapChopFrame extends AbstractStackMapFrame {

	private int offsetDelta;
	private int numAbsentLocals;
	
	public StackMapChopFrame(int tag, DataInputStream dIn) throws IOException {
		super(tag);
		this.offsetDelta = dIn.readUnsignedShort();
		this.numAbsentLocals = 251 - tag;
	}
	
	public StackMapChopFrame(int offsetDelta, int numAbsentLocals) {
		super(251 - numAbsentLocals);
		this.offsetDelta = offsetDelta;
		this.numAbsentLocals = numAbsentLocals;
	}

	@Override
	public StackMapFrameType getType() {
		return StackMapFrameType.CHOP_FRAME;
	}

	@Override
	public int getOffsetDelta() {
		return offsetDelta;
	}
	
	@Override
	public int getTag() {
		return 251 - numAbsentLocals;
	}
	
	public void setNumAbsentLocals(int numAbsentLocals) {
		this.numAbsentLocals = numAbsentLocals;
	}
	
	public int getNumAbsentLocals() {
		return numAbsentLocals;
	}
	
	@Override
	public void write(DataOutputStream dOut) throws IOException {
		dOut.write(getTag());
		dOut.writeShort(offsetDelta);
	}
	
}
