package me.mrletsplay.mrcore.mysql;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import me.mrletsplay.mrcore.mysql.impl.MySQLConnection;
import me.mrletsplay.mrcore.mysql.protocol.MySQLServerConnection;

/**
 * @deprecated Not actively updated, to be removed
 * @author MrLetsplay2003
 *
 */
@Deprecated
public class MySQL {

	public static MySQLConnection connect(String host, int port, String userName, String password, String database) throws UnknownHostException, IOException {
		Socket s = new Socket(host, port);
		InputStream in = s.getInputStream();
		OutputStream out = s.getOutputStream();
		return new MySQLConnection(new MySQLServerConnection(s, in, out, userName, password, database));
	}

	public static MySQLConnection connect(String host, int port, String userName, String password) throws UnknownHostException, IOException {
		Socket s = new Socket(host, port);
		InputStream in = s.getInputStream();
		OutputStream out = s.getOutputStream();
		return new MySQLConnection(new MySQLServerConnection(s, in, out, userName, password, null));
	}
	
}
