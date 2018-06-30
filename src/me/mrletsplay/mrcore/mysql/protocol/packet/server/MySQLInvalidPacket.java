package me.mrletsplay.mrcore.mysql.protocol.packet.server;

import me.mrletsplay.mrcore.mysql.protocol.MySQLServerConnection;

public class MySQLInvalidPacket implements MySQLServerPacket {

	private byte[] payload;
	
	public MySQLInvalidPacket(MySQLServerConnection con, byte[] payload) {
		this.payload = payload;
	}
	
	@Override
	public byte[] getPayload() {
		return payload;
	}

	@Override
	public MySQLServerPacketType getType() {
		return MySQLServerPacketType.INVALID;
	}

}
