package me.mrletsplay.mrcore.mysql.protocol.packet.server;

import me.mrletsplay.mrcore.mysql.protocol.packet.MySQLBasePacket;

public interface MySQLServerPacket extends MySQLBasePacket {
	
	public MySQLServerPacketType getType();
	
}
