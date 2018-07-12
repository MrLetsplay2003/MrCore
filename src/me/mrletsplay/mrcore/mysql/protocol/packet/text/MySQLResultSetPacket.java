package me.mrletsplay.mrcore.mysql.protocol.packet.text;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.mysql.impl.table.ColumnDefinition;
import me.mrletsplay.mrcore.mysql.protocol.MySQLServerConnection;
import me.mrletsplay.mrcore.mysql.protocol.io.MySQLReader;
import me.mrletsplay.mrcore.mysql.protocol.io.RawPacket;
import me.mrletsplay.mrcore.mysql.protocol.packet.server.MySQLServerPacketType;

public class MySQLResultSetPacket implements MySQLTextPacket {

	private byte[] payload;
	private int columnCount;
	private ColumnDefinition[] columnDefinitions;
	private List<MySQLColumnDefinition41Packet> columnDefinitionPackets;
	private List<MySQLResultSetRowPacket> resultSetRowPackets;
	
	public MySQLResultSetPacket(MySQLServerConnection con, byte[] payload, int command) throws IOException {
		this.payload = payload;
		MySQLReader r = new MySQLReader(new ByteArrayInputStream(payload));
		columnCount = (int) r.readLengthEncodedInteger();
		columnDefinitionPackets = new ArrayList<>();
		for(int i = 0; i < columnCount; i++) {
			columnDefinitionPackets.add(con.readPacket().parseTextPacket(con, MySQLColumnDefinition41Packet.class, command));
		}
		columnDefinitions = columnDefinitionPackets.stream().map(col -> new ColumnDefinition(col)).toArray(ColumnDefinition[]::new);
		resultSetRowPackets = new ArrayList<>();
		while(con.hasData()) {
			RawPacket raw = con.readPacket();
			if(raw.getServerPacketType().equals(MySQLServerPacketType.OK)) continue;
			resultSetRowPackets.add(raw.parseTextPacket(con, MySQLResultSetRowPacket.class, command));
		}
	}
	
	@Override
	public byte[] getPayload() {
		return payload;
	}
	
	public int getColumnCount() {
		return columnCount;
	}
	
	public ColumnDefinition[] getColumnDefinitions() {
		return columnDefinitions;
	}
	
	public List<MySQLColumnDefinition41Packet> getColumnDefinitionPackets() {
		return columnDefinitionPackets;
	}
	
	public List<MySQLResultSetRowPacket> getResultSetRowPackets() {
		return resultSetRowPackets;
	}
	
}
