package me.mrletsplay.mrcore.misc.classfile.attribute.stackmap;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StackMapSameFrameExtended extends AbstractStackMapFrame {

	private int offsetDelta;
	
	public StackMapSameFrameExtended(int tag, DataInputStream dIn) throws IOException {
		super(tag);
		this.offsetDelta = dIn.readUnsignedShort();
	}
	
	public StackMapSameFrameExtended(int offsetDelta) {
		super(251);
		this.offsetDelta = offsetDelta;
	}

	@Override
	public StackMapFrameType getType() {
		return StackMapFrameType.SAME_FRAME_EXTENDED;
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
		dOut.writeShort(offsetDelta);
	}
	
}
