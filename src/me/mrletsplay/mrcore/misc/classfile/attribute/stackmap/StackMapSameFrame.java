package me.mrletsplay.mrcore.misc.classfile.attribute.stackmap;

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
	
}
