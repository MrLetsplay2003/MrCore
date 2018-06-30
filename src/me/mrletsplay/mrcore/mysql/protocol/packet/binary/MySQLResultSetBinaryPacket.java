package me.mrletsplay.mrcore.mysql.protocol.packet.binary;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.mysql.impl.table.TableColumn;
import me.mrletsplay.mrcore.mysql.protocol.MySQLServerConnection;
import me.mrletsplay.mrcore.mysql.protocol.io.MySQLReader;
import me.mrletsplay.mrcore.mysql.protocol.io.RawPacket;
import me.mrletsplay.mrcore.mysql.protocol.packet.server.MySQLServerPacketType;
import me.mrletsplay.mrcore.mysql.protocol.packet.text.MySQLColumnDefinition41Packet;

public class MySQLResultSetBinaryPacket implements MySQLBinaryPacket {

	private byte[] payload;
	private int columnCount;
	private List<MySQLColumnDefinition41Packet> columnDefinitions;
	private List<MySQLResultSetRowBinaryPacket> resultSetRows;

	public MySQLResultSetBinaryPacket(MySQLServerConnection con, byte[] payload, int command) throws IOException, InterruptedException {
		this.payload = payload;
		MySQLReader r = new MySQLReader(new ByteArrayInputStream(payload));
		columnCount = (int) r.readLengthEncodedInteger();
		columnDefinitions = new ArrayList<>();
		for(int i = 0; i < columnCount; i++) {
			columnDefinitions.add(con.readPacket().parseTextPacket(con, MySQLColumnDefinition41Packet.class, command));
		}
		TableColumn[] columns = columnDefinitions.stream().map(c -> new TableColumn(c)).toArray(TableColumn[]::new);
		resultSetRows = new ArrayList<>();
		while(con.hasData()) {
			RawPacket raw = con.readPacket();
			Thread.sleep(200);
			if(raw.getPacketID() != 0x00 && raw.getServerPacketType().equals(MySQLServerPacketType.OK)) break; // No more rows
			resultSetRows.add(new MySQLResultSetRowBinaryPacket(con, raw.getPayload(), command, columns));
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
	
	public List<MySQLResultSetRowBinaryPacket> getResultSetRows() {
		return resultSetRows;
	}
	
}
