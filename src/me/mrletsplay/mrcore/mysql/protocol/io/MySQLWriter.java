package me.mrletsplay.mrcore.mysql.protocol.io;

import java.io.IOException;
import java.io.OutputStream;

import me.mrletsplay.mrcore.mysql.protocol.type.MySQLString;

public class MySQLWriter {

	private OutputStream out;
	
	public MySQLWriter(OutputStream out) {
		this.out = out;
	}
	
	public void write(int b) throws IOException {
		out.write(b);
	}
	
	public void writeString(MySQLString varString) throws IOException {
		out.write(varString.getBytes());
	}
	
	public void write(byte[] bytes) throws IOException {
		out.write(bytes);
	}
	
	public int writePacket(byte startingSequenceID, RawPacket packet) throws IOException {
		RawPacket[] chopped = packet.chop();
		for(RawPacket r : chopped) {
			writeSingularPacket(startingSequenceID++, r);
		}
		return chopped.length;
	}
	
	private void writeSingularPacket(byte sequenceID, RawPacket packet) throws IOException {
		writeFixedLengthInteger(3, packet.getLength());
		writeFixedLengthInteger(1, sequenceID);
		write(packet.getPayload());
	}
	
	public void writeFixedLengthInteger(int length, int integer) throws IOException {
		write(integer & 0xff);
		if(length >= 2) write((integer >> 8) & 0xff);
		if(length >= 3) write((integer >> 16) & 0xff);
		if(length >= 4) write((integer >> 24) & 0xff);
	}
	
	public void writeLengthEncodedInteger(int integer) throws IOException {
		if(integer < 0xff) {
			writeFixedLengthInteger(1, integer);
		}else if(integer < 0xffff) {
			writeFixedLengthInteger(2, 0xfc + integer);
		}else if(integer < 0xffffff) {
			writeFixedLengthInteger(3, 0xfd + integer);
		}else {
			writeFixedLengthInteger(4, 0xfe + integer);
		}
	}
	
	public void writeLengthEncodedString(MySQLString varString) throws IOException {
		writeLengthEncodedInteger(varString.length());
		writeString(varString);
	}
	
	public void writeNullTerminatedString(MySQLString varString) throws IOException {
		writeString(varString);
		write(0x00);
	}
	
}
