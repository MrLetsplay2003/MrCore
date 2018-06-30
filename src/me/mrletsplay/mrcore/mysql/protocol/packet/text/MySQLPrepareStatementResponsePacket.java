package me.mrletsplay.mrcore.mysql.protocol.packet.text;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.mysql.protocol.MySQLServerConnection;
import me.mrletsplay.mrcore.mysql.protocol.io.MySQLReader;

public class MySQLPrepareStatementResponsePacket implements MySQLTextPacket {

	private byte[] payload;
	private MySQLServerConnection connection;
	private int statementID;
	private short columnCount, paramCount, warningCount;
	private List<MySQLColumnDefinition41Packet> paramDefinitions, columnDefinitions;
	
	public MySQLPrepareStatementResponsePacket(MySQLServerConnection con, byte[] payload, int command) throws IOException {
		this.payload = payload;
		this.connection = con;
		MySQLReader r = new MySQLReader(new ByteArrayInputStream(payload));
		r.read(); // Status: OK [00]
		statementID = (int) r.readFixedLengthInteger(4);
		columnCount = (short) r.readFixedLengthInteger(2);
		paramCount = (short) r.readFixedLengthInteger(2);
		r.read(); // Filler
		warningCount = (short) r.readFixedLengthInteger(2);
		paramDefinitions = new ArrayList<>();
		columnDefinitions = new ArrayList<>();
		for(int i = 0; i < paramCount; i++) {
			paramDefinitions.add(con.readPacket().parseTextPacket(con, MySQLColumnDefinition41Packet.class, command));
		}
		System.out.println(paramDefinitions);
		for(int i = 0; i < columnCount; i++) {
			columnDefinitions.add(con.readPacket().parseTextPacket(con, MySQLColumnDefinition41Packet.class, command));
		}
		System.out.println(columnDefinitions.get(0).getName());
	}
	
	@Override
	public byte[] getPayload() {
		return payload;
	}
	
	public MySQLServerConnection getConnection() {
		return connection;
	}
	
	public int getStatementID() {
		return statementID;
	}
	
	public short getColumnCount() {
		return columnCount;
	}
	
	public List<MySQLColumnDefinition41Packet> getColumnDefinitions() {
		return columnDefinitions;
	}
	
	public short getParameterCount() {
		return paramCount;
	}
	
	public List<MySQLColumnDefinition41Packet> getParameterDefinitions() {
		return paramDefinitions;
	}
	
	public short getWarningCount() {
		return warningCount;
	}

}
