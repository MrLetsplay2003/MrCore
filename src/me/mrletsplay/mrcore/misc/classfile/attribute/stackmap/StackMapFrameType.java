package me.mrletsplay.mrcore.misc.classfile.attribute.stackmap;

import java.util.Arrays;

public enum StackMapFrameType {

	SAME_FRAME(0, 63),
	SAME_LOCALS_1_STACK_ITEM_FRAME(64, 127),
	SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED(247),
	CHOP_FRAME(248, 250),
	SAME_FRAME_EXTENDED(251),
	APPEND_FRAME(252, 254),
	FULL_FRAME(255),
	;
	
	private final int rangeStart, rangeEnd;
	
	private StackMapFrameType(int rangeStart, int rangeEnd) {
		this.rangeStart = rangeStart;
		this.rangeEnd = rangeEnd;
	}
	
	private StackMapFrameType(int value) {
		this(value, value);
	}
	
	public int getRangeStart() {
		return rangeStart;
	}
	
	public int getRangeEnd() {
		return rangeEnd;
	}
	
	public static StackMapFrameType getByValue(int value) {
		return Arrays.stream(values()).filter(t -> t.rangeStart >= value && t.rangeEnd <= value).findFirst().orElse(null);
	}
	
}
