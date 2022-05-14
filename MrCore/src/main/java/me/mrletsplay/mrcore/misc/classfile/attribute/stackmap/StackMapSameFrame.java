package me.mrletsplay.mrcore.misc.classfile.attribute.stackmap;

import java.io.DataOutputStream;
import java.io.IOException;

public class StackMapSameFrame extends AbstractStackMapFrame {

	public StackMapSameFrame(int tag) {
		super(tag);
	}
	
	@Override
	public StackMapFrameType getType() {
		return StackMapFrameType.SAME_FRAME;
	}

	@Override
	public int getOffsetDelta() {
		return getTag();
	}
	
	@Override
	public void write(DataOutputStream dOut) throws IOException {
		dOut.write(getTag());
	}
	
}
