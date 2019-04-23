package me.mrletsplay.mrcore.mysql.protocol.io;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import me.mrletsplay.mrcore.mysql.protocol.MySQLServerConnection;
import me.mrletsplay.mrcore.mysql.protocol.misc.MySQLException;
import me.mrletsplay.mrcore.mysql.protocol.packet.binary.MySQLBinaryPacket;
import me.mrletsplay.mrcore.mysql.protocol.packet.server.MySQLServerPacket;
import me.mrletsplay.mrcore.mysql.protocol.packet.server.MySQLServerPacketType;
import me.mrletsplay.mrcore.mysql.protocol.packet.text.MySQLTextPacket;

public class RawPacket {
	
	public static final int MAX_SIZE = 0xffffff;
	
	private int length;
	private byte sequenceID;
	private byte[] payload;
	private byte packetID;
	
	public RawPacket(int length, byte sequenceID, byte[] payload) {
		this.length = length;
		this.sequenceID = sequenceID;
		this.payload = payload;
		this.packetID = payload[0]; // Packet header
	}
	
	public RawPacket(int length, byte[] payload) {
		this.length = length;
		this.sequenceID = -1;
		this.payload = payload;
		this.packetID = payload[0]; // Packet header
	}
	
	public byte getPacketID() {
		return packetID;
	}
	
	public int getLength() {
		return length;
	}
	
	public byte getSequenceID() {
		return sequenceID;
	}
	
	public byte[] getPayload() {
		return payload;
	}
	
	public void append(RawPacket other) {
		byte[] newPayload = new byte[payload.length + other.payload.length];
		System.arraycopy(payload, 0, newPayload, 0, payload.length);
		System.arraycopy(other.payload, 0, newPayload, payload.length, other.payload.length);
		this.payload = newPayload;
	}
	
	public RawPacket[] chop() {
		if(payload.length == MAX_SIZE) { // Special case - A packet with size 0xffffff counts as two packets (the packet + an empty one)
			return new RawPacket[] {this, RawPacket.empty()};
		}
		int numPackets = (int) Math.ceil(payload.length / (double) MAX_SIZE);
		RawPacket[] tR = new RawPacket[numPackets];
		for(int i = 0; i < numPackets; i++) {
			byte[] data = new byte[Math.min(payload.length - (i * MAX_SIZE), MAX_SIZE)];
			System.arraycopy(payload, i * MAX_SIZE, data, 0, data.length);
			tR[i] = RawPacket.of(data);
		}
		return tR;
	}
	
	@Override
	public String toString() {
		return "#" + sequenceID + " -> " + packetID+ " | " + new String(payload) + " ->" + Arrays.toString(payload);
	}
	
	public MySQLServerPacket parseServerPacket(MySQLServerConnection con) {
		try {
			return MySQLServerPacketType.getByID(packetID)
					.getPacketClass()
					.getConstructor(MySQLServerConnection.class, byte[].class)
					.newInstance(con, payload);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new MySQLException(e);
		}
	}
	
	public MySQLServerPacketType getServerPacketType() {
		return MySQLServerPacketType.getByID(packetID);
	}
	
	public <T extends MySQLTextPacket> T parseTextPacket(MySQLServerConnection con, Class<T> packetClass, int command) {
		try {
			return packetClass
					.getConstructor(MySQLServerConnection.class, byte[].class, int.class)
					.newInstance(con, payload, command);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new MySQLException(e);
		}
	}
	
	public <T extends MySQLBinaryPacket> T parseBinaryPacket(MySQLServerConnection con, Class<T> packetClass, int command) {
		try {
			return packetClass
					.getConstructor(MySQLServerConnection.class, byte[].class, int.class)
					.newInstance(con, payload, command);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new MySQLException(e);
		}
	}
	
	public static RawPacket of(byte... payload) {
		return new RawPacket(payload.length, payload);
	}
	
	public static RawPacket empty() {
		return new RawPacket(0, new byte[0]);
	}
	
}
