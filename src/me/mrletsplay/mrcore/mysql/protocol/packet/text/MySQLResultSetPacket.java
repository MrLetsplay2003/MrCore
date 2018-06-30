package me.mrletsplay.mrcore.mysql.protocol.packet.text;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.mysql.protocol.MySQLServerConnection;
import me.mrletsplay.mrcore.mysql.protocol.io.MySQLReader;
import me.mrletsplay.mrcore.mysql.protocol.io.RawPacket;
import me.mrletsplay.mrcore.mysql.protocol.packet.server.MySQLServerPacketType;

public class MySQLResultSetPacket implements MySQLTextPacket {

	private byte[] payload;
	private int columnCount;
	private List<MySQLColumnDefinition41Packet> columnDefinitions;
	private List<MySQLResultSetRowPacket> resultSetRows;
	
	public MySQLResultSetPacket(MySQLServerConnection con, byte[] payload, int command) throws IOException {
		this.payload = payload;
		MySQLReader r = new MySQLReader(new ByteArrayInputStream(payload));
		columnCount = (int) r.readLengthEncodedInteger();
		columnDefinitions = new ArrayList<>();
		for(int i = 0; i < columnCount; i++) {
			columnDefinitions.add(con.readPacket().parseTextPacket(con, MySQLColumnDefinition41Packet.class, command));
		}
		resultSetRows = new ArrayList<>();
		while(con.hasData()) {
			RawPacket raw = con.readPacket();
			if(raw.getServerPacketType().equals(MySQLServerPacketType.OK)) break; // No more rows
			resultSetRows.add(raw.parseTextPacket(con, MySQLResultSetRowPacket.class, command));
		}
	}
	
	@Override
	public byte[] getPayload() {
		return payload;
	}
	
	public int getColumnCount() {
		return columnCount;
	}
	
	public List<MySQLColumnDefinition41Packet> getColumnDefinitions() {
		return columnDefinitions;
	}
	
	public List<MySQLResultSetRowPacket> getResultSetRows() {
		return resultSetRows;
	}
	
}
