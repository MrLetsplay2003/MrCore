package me.mrletsplay.mrcore.mysql.protocol.packet.text;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.mysql.protocol.MySQLServerConnection;
import me.mrletsplay.mrcore.mysql.protocol.io.MySQLReader;
import me.mrletsplay.mrcore.mysql.protocol.type.MySQLString;

public class MySQLResultSetRowPacket implements MySQLTextPacket {

	private byte[] payload;
	private List<MySQLString> encodedData;
	
	public MySQLResultSetRowPacket(MySQLServerConnection con, byte[] payload, int command) throws IOException {
		this.payload = payload;
		PushbackInputStream pb = new PushbackInputStream(new ByteArrayInputStream(payload));
		MySQLReader r = new MySQLReader(pb);
		encodedData = new ArrayList<>();
		while(r.hasMore()) {
			int b = pb.read();
			if(b == 0xfb) {
				this.encodedData.add(null);
				continue;
			}
			pb.unread(b);
			this.encodedData.add(r.readLengthEncodedString());
		}
	}
	
	@Override
	public byte[] getPayload() {
		return payload;
	}
	
	public List<MySQLString> getEncodedData() {
		return encodedData;
	}

}
