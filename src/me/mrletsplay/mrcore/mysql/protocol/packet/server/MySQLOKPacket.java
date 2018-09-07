package me.mrletsplay.mrcore.mysql.protocol.packet.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.misc.FlagCompound;
import me.mrletsplay.mrcore.mysql.protocol.MySQLServerConnection;
import me.mrletsplay.mrcore.mysql.protocol.flag.MySQLCapabilityFlag;
import me.mrletsplay.mrcore.mysql.protocol.flag.MySQLStatusFlag;
import me.mrletsplay.mrcore.mysql.protocol.io.MySQLReader;
import me.mrletsplay.mrcore.mysql.protocol.type.MySQLString;

public class MySQLOKPacket implements MySQLServerPacket {
	
	private long affectedRows, lastInsertedID;
	private short warnings;
	private FlagCompound statusFlags;
	private MySQLString info;
	private byte[] sessionStateChanges, payload;
	
	@SuppressWarnings("unused")
	public MySQLOKPacket(MySQLServerConnection con, byte[] data) throws IOException {
		this.payload = data;
		MySQLReader reader = new MySQLReader(new ByteArrayInputStream(data));
		reader.read(); // OK header (0x00 or 0xfe)
		this.affectedRows = reader.readLengthEncodedInteger();
		this.lastInsertedID = reader.readLengthEncodedInteger();
		
		if(con.hasCapability(MySQLCapabilityFlag.CLIENT_PROTOCOL_41)) {
			this.statusFlags = new FlagCompound((short) reader.readFixedLengthInteger(2));
			this.warnings = (short) reader.readFixedLengthInteger(2);
		}else if(con.hasCapability(MySQLCapabilityFlag.CLIENT_TRANSACTIONS)) {
			this.statusFlags = new FlagCompound((short) reader.readFixedLengthInteger(2));
		}
		
		if(con.hasCapability(MySQLCapabilityFlag.CLIENT_SESSION_TRACK)) {
			this.info = reader.readLengthEncodedString();
			if(statusFlags.hasFlag(MySQLStatusFlag.SERVER_SESSION_STATE_CHANGED)) {
				this.sessionStateChanges = reader.readLengthEncodedString().getBytes();
			}
		}else {
			this.info = reader.readEOFString();
		}
		
		if(this.sessionStateChanges != null) {
			MySQLReader iReader = new MySQLReader(new ByteArrayInputStream(sessionStateChanges));
			byte iType = (byte) iReader.read();
			byte[] iData = iReader.readLengthEncodedString().getBytes();
			// TODO
			if(iType == 0) { // SESSION_TRACK_SYSTEM_VARIABLES
				MySQLString vName = iReader.readLengthEncodedString();
				MySQLString vValue = iReader.readLengthEncodedString();
				
			}else if(iType == 1) { // SESSION_TRACK_SCHEMA
				MySQLString sName = iReader.readLengthEncodedString();
				
			}else if(iType == 2) { // SESSION_TRACK_STATE_CHANGE
				MySQLString isTracked = iReader.readLengthEncodedString();
				
			}else if(iType == 3) { // SESSION_TRACK_GTIDS
				
			}
		}
	}
	
	public long getAffectedRows() {
		return affectedRows;
	}
	
	public long getLastInsertedID() {
		return lastInsertedID;
	}
	
	public FlagCompound getStatusFlags() {
		return statusFlags;
	}
	
	public byte[] getSessionStateChanges() {
		return sessionStateChanges;
	}
	
	public short getWarnings() {
		return warnings;
	}
	
	public MySQLString getInfo() {
		return info;
	}
	
	@Override
	public MySQLServerPacketType getType() {
		return MySQLServerPacketType.OK;
	}
	
	@Override
	public byte[] getPayload() {
		return payload;
	}
	
}
