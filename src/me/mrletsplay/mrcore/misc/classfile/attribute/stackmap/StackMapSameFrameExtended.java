package me.mrletsplay.mrcore.misc.classfile.attribute.stackmap;

import java.io.DataInputStream;
import java.io.IOException;

public class StackMapSameFrameExtended extends AbstractStackMapFrame {

	private int offsetDelta;
	
	public StackMapSameFrameExtended(int tag, DataInputStream dIn) throws IOException {
		super(tag);
		this.offsetDelta = dIn.readUnsignedShort();
	}
	
	@Override
	public StackMapFrameType getType() {
		return StackMapFrameType.SAME_FRAME_EXTENDED;
	}

	@Override
	public int getOffsetDelta() {
		return offsetDelta;
	}
	
}
