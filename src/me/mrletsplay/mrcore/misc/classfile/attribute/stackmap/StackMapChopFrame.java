package me.mrletsplay.mrcore.misc.classfile.attribute.stackmap;

import java.io.DataInputStream;
import java.io.IOException;

public class StackMapChopFrame extends AbstractStackMapFrame {

	private int offsetDelta;
	
	public StackMapChopFrame(int tag, DataInputStream dIn) throws IOException {
		super(tag);
		this.offsetDelta = dIn.readUnsignedShort();
	}
	
	@Override
	public StackMapFrameType getType() {
		return StackMapFrameType.CHOP_FRAME;
	}

	@Override
	public int getOffsetDelta() {
		return offsetDelta;
	}
	
	public int getNumAbsentLocals() {
		return 251 - getTag();
	}
	
}
