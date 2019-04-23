package me.mrletsplay.mrcore.mysql.impl.statement.simple;

public interface MySQLStatement {

	public String asString();
	
	public static StatementCreateTable createTable(String name) {
		return new StatementCreateTable(name);
	}
	
}
