package me.mrletsplay.mrcore.mysql.impl.table;

import me.mrletsplay.mrcore.mysql.protocol.type.MySQLDataType;
import me.mrletsplay.mrcore.mysql.protocol.type.MySQLString;

public class TableEntry {

	private MySQLDataType<?> type;
	private Object parsed;
	
	public TableEntry(MySQLDataType<?> type, MySQLString raw) {
		this.type = type;
		this.parsed = type.parse(raw);
	}
	
	public TableEntry(MySQLDataType<?> type, Object parsed) {
		this.type = type;
		this.parsed = parsed;
	}
	
	public MySQLDataType<?> getType() {
		return type;
	}
	
	public Object getValue() {
		return parsed;
	}
	
	@Override
	public String toString() {
		return parsed == null ? "null" : parsed.toString();
	}
	
}
