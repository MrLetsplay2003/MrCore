package me.mrletsplay.mrcore.mysql.protocol.type;

import java.nio.charset.StandardCharsets;

public class MySQLString {

	private byte[] bytes;
	
	public MySQLString(byte[] bytes) {
		this.bytes = bytes;
	}
	
	public MySQLString(String string) {
		this.bytes = string.getBytes(StandardCharsets.UTF_8);
	}
	
	public byte[] getBytes() {
		return bytes;
	}
	
	public MySQLString concat(MySQLString other) {
		byte[] newBytes = new byte[bytes.length + other.bytes.length];
		System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
		System.arraycopy(other.bytes, 0, newBytes, bytes.length, other.bytes.length);
		return new MySQLString(newBytes);
	}
	
	@Override
	public String toString() {
		return new String(bytes, StandardCharsets.UTF_8);
	}

	public char charAt(int index) {
		return (char) bytes[index];
	}
	
	public int length() {
		return bytes.length;
	}
	
}
