package me.mrletsplay.mrcore.mysql.protocol.misc;

public class NullBitmap {

	private byte[] bytes;
	private int offset;
	
	public NullBitmap(int numFields, int offset) {
		bytes = new byte[getRequiredBytes(numFields, offset)];
		this.offset = offset;
	}
	
	public NullBitmap(byte[] bytes, int offset) {
		this.bytes = bytes;
		this.offset = offset;
	}
	
	public static int getRequiredBytes(int numFields, int offset) {
		return (int) Math.ceil((numFields + offset) / 8D);
	}
	
	public void setNullBit(int fieldPos) {
		int ind = Math.floorDiv(fieldPos + offset, 8);
		int bitInd = (fieldPos + offset) % 8;
		bytes[ind] |= (1 << bitInd);
	}
	
	public void unsetNullBit(int fieldPos) {
		int ind = (fieldPos + offset) / 8;
		int bitInd = (fieldPos + offset) % 8;
		bytes[ind] ^= (1 << bitInd);
	}
	
	public boolean hasNullBit(int fieldPos) {
		int ind = (fieldPos + offset) / 8;
		int bitInd = (fieldPos + offset) % 8;
		return (bytes[ind] & (1 << bitInd)) == (1 << bitInd);
	}
	
	public byte[] getBytes() {
		return bytes;
	}
	
}
