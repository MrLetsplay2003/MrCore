package me.mrletsplay.mrcore.mysql.protocol.packet.server;

import java.util.Arrays;

public enum MySQLServerPacketType {

	OK(MySQLOKPacket.class, 0x00, 0xfe),
	ERR(MySQLERRPacket.class, 0xff),
	INVALID(MySQLInvalidPacket.class);
	
	private Byte[] packetIDs;
	private Class<? extends MySQLServerPacket> packetClass; 

	private MySQLServerPacketType(Class<? extends MySQLServerPacket> packetClass, int... packetIDs) {
		this.packetIDs = Arrays.stream(packetIDs).mapToObj(i -> Byte.valueOf((byte) i)).toArray(Byte[]::new);
		this.packetClass = packetClass;
	}
	
	public Byte[] getPacketIDs() {
		return packetIDs;
	}
	
	public Class<? extends MySQLServerPacket> getPacketClass() {
		return packetClass;
	}
	
	public static MySQLServerPacketType getByID(byte id) {
		return Arrays.stream(values())
				.filter(p -> Arrays.stream(p.packetIDs)
						.anyMatch(i -> i == id))
				.findFirst().orElse(MySQLServerPacketType.INVALID);
	}
	
}
