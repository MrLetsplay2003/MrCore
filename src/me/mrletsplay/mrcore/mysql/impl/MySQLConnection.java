package me.mrletsplay.mrcore.mysql.impl;

import me.mrletsplay.mrcore.mysql.impl.statement.PreparedStatement;
import me.mrletsplay.mrcore.mysql.impl.statement.simple.MySQLStatement;
import me.mrletsplay.mrcore.mysql.protocol.MySQLServerConnection;

public class MySQLConnection {

	private MySQLServerConnection serverConnection;
	
	public MySQLConnection(MySQLServerConnection serverConnection) {
		this.serverConnection = serverConnection;
	}
	
	public void selectSchema(String schemaName) {
		serverConnection.selectSchema(schemaName);
	}
	
	public ResultSet query(String query) {
		return serverConnection.query(query);
	}
	
	public ResultSet query(MySQLStatement stmt) {
		return serverConnection.query(stmt.asString());
	}
	
	public PreparedStatement prepareStatement(String query) {
		return serverConnection.prepareStatement(query);
	}
	
	public void disconnect() {
		serverConnection.disconnect();
	}
	
	public MySQLServerConnection getServerConnection() {
		return serverConnection;
	}
	
}
