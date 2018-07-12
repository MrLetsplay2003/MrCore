package me.mrletsplay.mrcore.mysql.impl.table;

import java.util.Arrays;

import me.mrletsplay.mrcore.mysql.impl.ResultSet;
import me.mrletsplay.mrcore.mysql.protocol.packet.MySQLBasePacket;
import me.mrletsplay.mrcore.mysql.protocol.packet.binary.MySQLResultSetRowBinaryPacket;
import me.mrletsplay.mrcore.mysql.protocol.packet.text.MySQLResultSetRowPacket;
import me.mrletsplay.mrcore.mysql.protocol.type.MySQLDataType;

public class TableRow {

	private ResultSet resultSet;
	private TableEntry[] entries;
	
	private MySQLBasePacket definingPacket;
	
	public TableRow(ResultSet resultSet, MySQLResultSetRowPacket fromPacket) {
		this.resultSet = resultSet;
		this.definingPacket = fromPacket;
		entries = new TableEntry[resultSet.getColumns().length];
		for(int i = 0; i < resultSet.getColumns().length; i++) {
			entries[i] = new TableEntry(resultSet.getColumn(i).getDefinition().getColumnType(), fromPacket.getEncodedData().get(i));
		}
	}
	
	public TableRow(ResultSet resultSet, MySQLResultSetRowBinaryPacket fromPacket) {
		this.resultSet = resultSet;
		this.definingPacket = fromPacket;
		entries = new TableEntry[resultSet.getColumns().length];
		for(int i = 0; i < resultSet.getColumns().length; i++) {
			entries[i] = new TableEntry(resultSet.getColumn(i).getDefinition().getColumnType(), fromPacket.getEncodedData().get(i));
		}
	}
	
	public TableEntry[] getEntries() {
		return entries;
	}
	
	public TableEntry getEntry(int index) {
		return entries[index];
	}
	
	public TableEntry getEntry(String columnName) {
		for(int i = 0; i < resultSet.getColumns().length; i++) {
			if(resultSet.getColumn(i).getDefinition().getPhysicalName().toString().equals(columnName)) {
				return entries[i];
			}
		}
		throw new RuntimeException("Invalid column \"" + columnName + "\" specified");
	}
	
	public <T> T getEntry(int index, MySQLDataType<T> type) {
		if(!resultSet.getColumn(index).getDefinition().getColumnType().equals(type)) throw new RuntimeException("Invalid type");
		return type.getJavaType().cast(entries[index]);
	}
	
	public <T> T getEntry(String columnName, MySQLDataType<T> type) {
		for(int i = 0; i < resultSet.getColumns().length; i++) {
			if(resultSet.getColumn(i).getDefinition().getPhysicalName().toString().equals(columnName)) {
				if(!resultSet.getColumn(i).getDefinition().getColumnType().equals(type)) throw new RuntimeException("Invalid type");
				return type.getJavaType().cast(entries[i]);
			}
		}
		throw new RuntimeException("Invalid column \"" + columnName + "\" specified");
	}
	
	public MySQLBasePacket getDefiningPacket() {
		return definingPacket;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(entries);
	}
	
}
