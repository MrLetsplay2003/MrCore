package me.mrletsplay.mrcore.misc;

public class ByteUtils {

	public static String bytesToHex(byte[] bytes) {
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if(hex.length() == 1) str.append('0');
			str.append(hex);
		}
		return str.toString();
	}

	public static byte[] hexToBytes(String hex) {
		if(hex.length() % 2 != 0) throw new IllegalArgumentException("Invalid hex String");
		byte[] bytes = new byte[hex.length() / 2];
		for(int i = 0; i < hex.length(); i += 2) {
			int hi = Character.digit(hex.charAt(i), 16);
			int lo = Character.digit(hex.charAt(i + 1), 16);
			if(hi == -1 || lo == -1) throw new IllegalArgumentException("Invalid hex String");
			bytes[i / 2] = (byte) ((hi << 4) | lo);
		}
		return bytes;
	}

}
