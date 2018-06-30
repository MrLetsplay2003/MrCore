package me.mrletsplay.mrcore.mysql.impl.table;

import me.mrletsplay.mrcore.mysql.protocol.type.MySQLDataType;
import me.mrletsplay.mrcore.mysql.protocol.type.MySQLString;

public class TableEntry {

	private MySQLDataType<?> type;
	private MySQLString raw;
	private Object parsed;
	
	public TableEntry(MySQLDataType<?> type, MySQLString raw) {
		this.type = type;
		this.raw = raw;
		this.parsed = type.parse(raw);
	}
	
	public MySQLDataType<?> getType() {
		return type;
	}
	
	public MySQLString getRaw() {
		return raw;
	}
	
	public Object getParsed() {
		return parsed;
	}
	
}
