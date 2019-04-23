package me.mrletsplay.mrcore.misc.classfile.attribute.stackmap;

public abstract class AbstractStackMapFrame implements StackMapFrame {
	
	private int tag;
	
	public AbstractStackMapFrame(int tag) {
		this.tag = tag;
	}
	
	@Override
	public int getTag() {
		return tag;
	}
	
	@Override
	public abstract StackMapFrameType getType();
	
	@Override
	public abstract int getOffsetDelta();
	
}
