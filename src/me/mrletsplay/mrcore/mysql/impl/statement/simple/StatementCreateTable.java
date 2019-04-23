package me.mrletsplay.mrcore.mysql.impl.statement.simple;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.mysql.impl.statement.MySQLType;

public class StatementCreateTable implements MySQLStatement {

	private List<ColumnDefinition> columns = new ArrayList<>();
	private String name;
	
	public StatementCreateTable(String name) {
		this.name = name;
	}
	
	public StatementCreateTable withColumn(String name, MySQLType type) {
		columns.add(new ColumnDefinition(name, type));
		return this;
	}

	@Override
	public String asString() {
		return "CREATE TABLE " + name + "(" + columns.stream().map(c -> c.name + " " + c.type.asString()).collect(Collectors.joining(",")) + ");";
	}
	
	private static class ColumnDefinition {
		
		private String name;
		private MySQLType type;
		
		public ColumnDefinition(String name, MySQLType type) {
			this.name = name;
			this.type = type;
		}
		
	}
	
}
