package me.mrletsplay.mrcore.mysql.protocol.packet.binary;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.mysql.impl.table.TableColumn;
import me.mrletsplay.mrcore.mysql.protocol.MySQLServerConnection;
import me.mrletsplay.mrcore.mysql.protocol.io.MySQLReader;
import me.mrletsplay.mrcore.mysql.protocol.misc.NullBitmap;

public class MySQLResultSetRowBinaryPacket implements MySQLBinaryPacket {

	private byte[] payload;
	private List<Object> encodedData;
	
	public MySQLResultSetRowBinaryPacket(MySQLServerConnection con, byte[] payload, int command, TableColumn[] columns) throws IOException {
		this.payload = payload;
		MySQLReader r = new MySQLReader(new ByteArrayInputStream(payload));
		r.read(); // Packet header
		encodedData = new ArrayList<>();
		NullBitmap bm = new NullBitmap(r.read(NullBitmap.getRequiredBytes(columns.length, 2)), 2);
		for(int i = 0; i < columns.length; i++) {
			if(bm.hasNullBit(i)) {
				encodedData.add(null);
				continue;
			}
			encodedData.add(columns[i].getColumnType().read(r));
		}
	}
	
	@Override
	public byte[] getPayload() {
		return payload;
	}
	
	public List<Object> getEncodedData() {
		return encodedData;
	}
	
}
