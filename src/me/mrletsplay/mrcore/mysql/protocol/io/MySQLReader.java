package me.mrletsplay.mrcore.mysql.protocol.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import me.mrletsplay.mrcore.mysql.protocol.type.MySQLString;

public class MySQLReader {

	private InputStream in;
	
	public MySQLReader(InputStream in) {
		this.in = in;
	}
	
	public boolean hasMore() throws IOException {
		return in.available() > 0;
	}
	
	public int read() throws IOException {
		return in.read();
	}
	
	public byte[] read(int numBytes) throws IOException {
		byte[] buf = new byte[numBytes];
		in.read(buf);
		return buf;
	}
	
	public byte[] readReversed(int numBytes) throws IOException {
		return reverseBytes(read(numBytes));
	}
	
	private byte[] reverseBytes(byte[] arr) {
		byte[] newArray = new byte[arr.length];
		for(int i = 0; i < arr.length; i++) {
			newArray[arr.length - i - 1] = arr[i];
		}
		return newArray;
	}
	
	public RawPacket readPacket() throws IOException {
		RawPacket raw = readSingularPacket();
		if(raw == null) return null;
		int len = raw.getLength();
		while(len == RawPacket.MAX_SIZE) {
			RawPacket r = readSingularPacket();
			raw.append(r);
			len = r.getLength();
		}
		return raw;
	}
	
	private RawPacket readSingularPacket() throws IOException {
		int length = (int) readFixedLengthInteger(3);
		if(length < 0) throw new IOException("Invalid packet length");
		if(length == 0) return null;
		byte sequenceID = (byte) readFixedLengthInteger(1);
		byte[] payload = read(length);
		return new RawPacket(length, sequenceID, payload);
	}
	
	public long readFixedLengthInteger(int length) throws IOException {
		return fromByteArray(read(length));
	}
	
	private long fromByteArray(byte[] bytes) {
		long total = 0;
		for(int i = 0; i < bytes.length; i++) {
			total += ((int) bytes[i] & 0xFF) << (8 * i);
		}
		return total;
	}
	
	public long readLengthEncodedInteger() throws IOException {
		int len = read();
		if(len <= 0xfb) {
			return len;
		}else if(len == 0xfc) {
			return fromByteArray(read(2));
		}else if(len == 0xfd) {
			return fromByteArray(read(3));
		}else if(len == 0xfe) {
			return fromByteArray(read(4));
		}else {
			return -1L;
		}
	}
	
	public MySQLString readEOFString() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[4096];
		int len;
		while((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		return new MySQLString(out.toByteArray());
	}
	
	public MySQLString readString(int length) throws IOException {
		return new MySQLString(read(length));
	}
	
	public MySQLString readNullTerminatedString() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int lByte;
		while((lByte = in.read()) != 0) {
			out.write(lByte);
		}
		return new MySQLString(out.toByteArray());
	}
	
	public MySQLString readLengthEncodedString() throws IOException {
		int len = (int) readLengthEncodedInteger();
		return readString(len);
	}
	
}
