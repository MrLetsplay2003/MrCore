package me.mrletsplay.mrcore.mysql.impl;

import java.util.Arrays;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.mysql.impl.table.ColumnDefinition;
import me.mrletsplay.mrcore.mysql.impl.table.TableColumn;
import me.mrletsplay.mrcore.mysql.impl.table.TableRow;
import me.mrletsplay.mrcore.mysql.protocol.packet.binary.MySQLResultSetBinaryPacket;
import me.mrletsplay.mrcore.mysql.protocol.packet.text.MySQLResultSetPacket;

public class ResultSet {

	private ColumnDefinition[] columnDefinitions;
	private TableColumn[] columns;
	private TableRow[] rows;
	
	public ResultSet(MySQLResultSetPacket fromPacket) {
		this.columnDefinitions = fromPacket.getColumnDefinitions();
		this.columns = new TableColumn[fromPacket.getColumnDefinitions().length];
		for(int i = 0; i < columns.length; i++) {
			columns[i] = new TableColumn(this, fromPacket.getColumnDefinitions()[i], i);
		}
		this.rows = fromPacket.getResultSetRowPackets().stream()
				.map(def -> new TableRow(this, def))
				.toArray(TableRow[]::new);
	}
	
	public ResultSet(MySQLResultSetBinaryPacket fromPacket) {
		this.columnDefinitions = fromPacket.getColumnDefinitions();
		this.columns = new TableColumn[fromPacket.getColumnDefinitions().length];
		for(int i = 0; i < columns.length; i++) {
			columns[i] = new TableColumn(this, fromPacket.getColumnDefinitions()[i], i);
		}
		this.rows = fromPacket.getResultSetRowPackets().stream()
				.map(def -> new TableRow(this, def))
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
				.filter(c -> c.getDefinition().getPhysicalName().toString().equals(name))
				.findFirst().orElse(null);
	}
	
	public TableRow getRow(int index) {
		return rows[index];
	}
	
	public TableRow[] getRows() {
		return rows;
	}
	
	public ColumnDefinition[] getColumnDefinitions() {
		return columnDefinitions;
	}
	
	public boolean isEmpty() {
		return rows.length == 0;
	}
	
	@Override
	public String toString() {
		return Arrays.stream(rows).map(r -> r.toString()).collect(Collectors.joining("\n"));
	}
	
}
