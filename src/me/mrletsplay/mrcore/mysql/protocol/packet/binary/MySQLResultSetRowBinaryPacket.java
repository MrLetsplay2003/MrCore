package me.mrletsplay.mrcore.mysql.protocol.packet.binary;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.mrletsplay.mrcore.mysql.impl.table.TableColumn;
import me.mrletsplay.mrcore.mysql.protocol.MySQLServerConnection;
import me.mrletsplay.mrcore.mysql.protocol.io.MySQLReader;
import me.mrletsplay.mrcore.mysql.protocol.misc.NullBitmap;
import me.mrletsplay.mrcore.mysql.protocol.type.MySQLString;

public class MySQLResultSetRowBinaryPacket implements MySQLBinaryPacket {

	private byte[] payload;
	private List<Object> encodedData;
	
	public MySQLResultSetRowBinaryPacket(MySQLServerConnection con, byte[] payload, int command, TableColumn[] columns) throws IOException {
		this.payload = payload;
		MySQLReader r = new MySQLReader(new ByteArrayInputStream(payload));
		r.read(); // Packet header
		System.out.println("BYTES: "+Arrays.toString(payload));
		encodedData = new ArrayList<>();
		System.out.println(NullBitmap.getRequiredBytes(columns.length, 0));
		NullBitmap bm = new NullBitmap(r.read(NullBitmap.getRequiredBytes(columns.length, 0)), 0);
		System.out.println("BM: "+Arrays.toString(bm.getBytes()));
		for(int i = 0; i < columns.length; i++) {
			if(bm.hasNullBit(i)) {
				System.out.println("NULL!");
				encodedData.add(null);
				continue;
			}
			encodedData.add(columns[i].getColumnType().read(r));
//			encodedData.add(r.readLengthEncodedString());
			System.out.println("ENC: "+encodedData);
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
