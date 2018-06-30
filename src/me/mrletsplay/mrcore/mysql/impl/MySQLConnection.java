package me.mrletsplay.mrcore.mysql.impl;

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
	
	public void disconnect() {
		serverConnection.disconnect();
	}
	
	public MySQLServerConnection getServerConnection() {
		return serverConnection;
	}
	
}
