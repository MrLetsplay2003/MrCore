package me.mrletsplay.mrcore.misc.classfile.attribute.stackmap;

import java.io.DataInputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.misc.classfile.pool.ConstantPool;

public interface StackMapFrame {

	public int getTag();
	
	public StackMapFrameType getType();
	
	public int getOffsetDelta();
	
	public static StackMapFrame readEntry(ConstantPool pool, DataInputStream dIn) throws IOException {
		int tag = dIn.readUnsignedByte();
		StackMapFrameType t = StackMapFrameType.getByValue(tag);
		switch(t) {
			case APPEND_FRAME:
				return new StackMapAppendFrame(pool, tag, dIn);
			case CHOP_FRAME:
				return new StackMapChopFrame(tag, dIn);
			case FULL_FRAME:
				return new StackMapFullFrame(pool, tag, dIn);
			case SAME_FRAME:
				return new StackMapSameFrame(tag);
			case SAME_FRAME_EXTENDED:
				return new StackMapSameFrameExtended(tag, dIn);
			case SAME_LOCALS_1_STACK_ITEM_FRAME:
				return new StackMapSameLocals1StackItemFrame(pool, tag, dIn);
			case SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED:
				return new StackMapSameLocals1StackItemFrameExtended(pool, tag, dIn);
		}
		throw new RuntimeException();
	}
	
}
