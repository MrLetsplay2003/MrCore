package me.mrletsplay.mrcore.mysql.impl.table;

import me.mrletsplay.mrcore.mysql.impl.ResultSet;
import me.mrletsplay.mrcore.mysql.protocol.misc.MySQLException;
import me.mrletsplay.mrcore.mysql.protocol.type.MySQLDataType;

public class TableColumn {

	private ResultSet resultSet;
	
	private ColumnDefinition definition;
	private int columnIndex;
	
	public TableColumn(ResultSet resultSet, ColumnDefinition definition, int columnIndex) {
		this.resultSet = resultSet;
		this.definition = definition;
		this.columnIndex = columnIndex;
	}
	
	public ResultSet getResultSet() {
		return resultSet;
	}
	
	public ColumnDefinition getDefinition() {
		return definition;
	}
	
	public TableEntry[] getEntries() {
		TableEntry[] entries = new TableEntry[resultSet.getRows().length];
		for(int i = 0; i < resultSet.getRows().length; i++) entries[i] = resultSet.getRows()[i].getEntry(columnIndex);
		return entries;
	}
	
	public TableEntry getEntry(int index) {
		return resultSet.getRows()[index].getEntry(columnIndex);
	}
	
	public <T> T getEntry(int index, MySQLDataType<T> type) {
		if(!definition.getColumnType().equals(type)) throw new MySQLException("Invalid type");
		return type.getJavaType().cast(getEntry(index));
	}
	
}
