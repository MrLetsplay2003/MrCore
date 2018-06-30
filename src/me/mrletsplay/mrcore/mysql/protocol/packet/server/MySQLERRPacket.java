package me.mrletsplay.mrcore.mysql.protocol.packet.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.mysql.protocol.MySQLServerConnection;
import me.mrletsplay.mrcore.mysql.protocol.flag.MySQLCapabilityFlag;
import me.mrletsplay.mrcore.mysql.protocol.io.MySQLReader;
import me.mrletsplay.mrcore.mysql.protocol.type.MySQLString;

public class MySQLERRPacket implements MySQLServerPacket {
	
	private short errorCode;
	private MySQLString sqlStateMarker, sqlState, errorMessage;
	private byte[] payload;
	
	public MySQLERRPacket(MySQLServerConnection con, byte[] data) throws IOException {
		this.payload = data;
		MySQLReader reader = new MySQLReader(new ByteArrayInputStream(data));
		reader.read(); // ERR header
		errorCode = (short) reader.readFixedLengthInteger(2);
		
		if(con.hasCapability(MySQLCapabilityFlag.CLIENT_PROTOCOL_41)) {
			sqlStateMarker = reader.readString(1);
			sqlState = reader.readString(5);
		}
		
		errorMessage = reader.readEOFString();
	}
	
	public short getErrorCode() {
		return errorCode;
	}
	
	public MySQLString getSQLState() {
		return sqlState;
	}
	
	public MySQLString getSQLStateMarker() {
		return sqlStateMarker;
	}
	
	public MySQLString getErrorMessage() {
		return errorMessage;
	}
	
	@Override
	public MySQLServerPacketType getType() {
		return MySQLServerPacketType.ERR;
	}

	@Override
	public byte[] getPayload() {
		return payload;
	}
	
}
