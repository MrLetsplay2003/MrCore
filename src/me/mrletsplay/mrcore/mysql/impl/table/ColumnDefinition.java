package me.mrletsplay.mrcore.mysql.impl.table;

import me.mrletsplay.mrcore.mysql.protocol.packet.text.MySQLColumnDefinition41Packet;
import me.mrletsplay.mrcore.mysql.protocol.type.MySQLDataType;
import me.mrletsplay.mrcore.mysql.protocol.type.MySQLDataTypes;
import me.mrletsplay.mrcore.mysql.protocol.type.MySQLString;

public class ColumnDefinition {

	private MySQLString
		schema,
		table,
		orgTable,
		name,
		orgName;
	
	private short
		charSet;
	
	private MySQLDataType<?>
		columnType;
	
	private MySQLColumnDefinition41Packet definingPacket;
	
	public ColumnDefinition(MySQLColumnDefinition41Packet fromPacket) {
		this.definingPacket = fromPacket;
		this.schema = fromPacket.getSchema();
		this.table = fromPacket.getTable();
		this.orgTable = fromPacket.getOrgTable();
		this.name = fromPacket.getName();
		this.orgName = fromPacket.getOrgName();
		this.charSet = fromPacket.getCharSet();
		this.columnType = MySQLDataTypes.getTypeById(fromPacket.getColumnType());
	}
	
	public MySQLString getSchema() {
		return schema;
	}
	
	public MySQLString getVirtualTableName() {
		return table;
	}
	
	public MySQLString getPhysicalTableName() {
		return orgTable;
	}
	
	public MySQLString getVirtualName() {
		return name;
	}
	
	public MySQLString getPhysicalName() {
		return orgName;
	}
	
	public short getCharSet() {
		return charSet;
	}
	
	public MySQLDataType<?> getColumnType() {
		return columnType;
	}
	
	public MySQLColumnDefinition41Packet getDefiningPacket() {
		return definingPacket;
	}
	
}
