package me.mrletsplay.mrcore.mysql.protocol.auth;

import java.io.IOException;

import me.mrletsplay.mrcore.mysql.protocol.MySQLServerConnection;

public interface MySQLAuthPluginBase {
	
	public byte[] getInitialAuthResponse(String password, byte[] authData);
	
	public void handleFurtherProcessing(MySQLServerConnection con) throws IOException;

	public MySQLAuthPlugin getType();
	
	public String getName();
	
}
