package me.mrletsplay.mrcore.mysql.protocol.packet.text;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import me.mrletsplay.mrcore.mysql.protocol.MySQLServerConnection;
import me.mrletsplay.mrcore.mysql.protocol.command.MySQLCommand;
import me.mrletsplay.mrcore.mysql.protocol.io.MySQLReader;
import me.mrletsplay.mrcore.mysql.protocol.type.MySQLString;

public class MySQLColumnDefinition41Packet implements MySQLTextPacket {
	
	private byte[] payload;
	
	private MySQLString
		catalog,
		schema,
		table,
		orgTable,
		name,
		orgName;
	
	private short
		charSet,
		flags;
	
	private int
		columnLength;
	
	private byte
		columnType,
		decimals;
	
	private MySQLString defaultValues;
	
	public MySQLColumnDefinition41Packet(MySQLServerConnection con, byte[] payload, int command) throws IOException {
		this.payload = payload;
		MySQLReader r = new MySQLReader(new ByteArrayInputStream(payload));
		catalog = r.readLengthEncodedString();
		schema = r.readLengthEncodedString();
		table = r.readLengthEncodedString();
		orgTable = r.readLengthEncodedString();
		name = r.readLengthEncodedString();
		orgName = r.readLengthEncodedString();
		r.readLengthEncodedInteger(); // length of the following fields (always 0x0c)
		charSet = (short) r.readFixedLengthInteger(2);
		columnLength = (int) r.readFixedLengthInteger(4);
		columnType = (byte) r.read();
		flags = (short) r.readFixedLengthInteger(2);
		decimals = (byte) r.read();
		r.read(2); // Filler
		if(command == MySQLCommand.COM_FIELD_LIST) {
			defaultValues = r.readString((int) r.readLengthEncodedInteger());
		}
	}
	
	@Override
	public byte[] getPayload() {
		return payload;
	}
	
	public MySQLString getCatalog() {
		return catalog;
	}
	
	public MySQLString getSchema() {
		return schema;
	}
	
	public MySQLString getTable() {
		return table;
	}
	
	public MySQLString getOrgTable() {
		return orgTable;
	}
	
	public MySQLString getName() {
		return name;
	}
	
	public MySQLString getOrgName() {
		return orgName;
	}
	
	public short getCharSet() {
		return charSet;
	}
	
	public int getColumnLength() {
		return columnLength;
	}
	
	public byte getColumnType() {
		return columnType;
	}
	
	public short getFlags() {
		return flags;
	}
	
	public byte getDecimals() {
		return decimals;
	}
	
	public MySQLString getDefaultValues() {
		return defaultValues;
	}
	
}
