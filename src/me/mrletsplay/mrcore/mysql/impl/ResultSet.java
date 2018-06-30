package me.mrletsplay.mrcore.mysql.impl;

import java.util.Arrays;

import me.mrletsplay.mrcore.mysql.impl.table.TableColumn;
import me.mrletsplay.mrcore.mysql.impl.table.TableRow;
import me.mrletsplay.mrcore.mysql.protocol.packet.text.MySQLResultSetPacket;

public class ResultSet {

	private TableColumn[] columns;
	private TableRow[] rows;
	
	public ResultSet(MySQLResultSetPacket fromPacket) {
		this.columns = fromPacket.getColumnDefinitions().stream()
				.map(def -> new TableColumn(def))
				.toArray(TableColumn[]::new);
		this.rows = fromPacket.getResultSetRows().stream()
				.map(def -> new TableRow(columns, def))
				.toArray(TableRow[]::new);
	}
	
	public TableColumn[] getColumns() {
		return columns;
	}
	
	public TableColumn getColumn(int index) {
		return columns[index];
	}
	
	public TableColumn getColumnByName(String name) {
		return Arrays.stream(columns)
				.filter(c -> c.getPhysicalName().toString().equals(name))
				.findFirst().orElse(null);
	}
	
	public TableRow[] getRows() {
		return rows;
	}
	
}
