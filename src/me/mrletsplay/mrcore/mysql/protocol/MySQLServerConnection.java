package me.mrletsplay.mrcore.mysql.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import me.mrletsplay.mrcore.misc.FlagCompound;
import me.mrletsplay.mrcore.misc.FlagCompound.CombinationMode;
import me.mrletsplay.mrcore.mysql.impl.ResultSet;
import me.mrletsplay.mrcore.mysql.impl.statement.PreparedStatement;
import me.mrletsplay.mrcore.mysql.impl.statement.StatementParameter;
import me.mrletsplay.mrcore.mysql.impl.table.TableColumn;
import me.mrletsplay.mrcore.mysql.protocol.auth.MySQLAuthPlugin;
import me.mrletsplay.mrcore.mysql.protocol.auth.MySQLAuthPluginBase;
import me.mrletsplay.mrcore.mysql.protocol.command.MySQLCommand;
import me.mrletsplay.mrcore.mysql.protocol.flag.MySQLCapabilityFlag;
import me.mrletsplay.mrcore.mysql.protocol.io.MySQLReader;
import me.mrletsplay.mrcore.mysql.protocol.io.MySQLWriter;
import me.mrletsplay.mrcore.mysql.protocol.io.RawPacket;
import me.mrletsplay.mrcore.mysql.protocol.misc.MySQLCharset;
import me.mrletsplay.mrcore.mysql.protocol.misc.NullBitmap;
import me.mrletsplay.mrcore.mysql.protocol.packet.binary.MySQLResultSetBinaryPacket;
import me.mrletsplay.mrcore.mysql.protocol.packet.server.MySQLERRPacket;
import me.mrletsplay.mrcore.mysql.protocol.packet.server.MySQLOKPacket;
import me.mrletsplay.mrcore.mysql.protocol.packet.server.MySQLServerPacket;
import me.mrletsplay.mrcore.mysql.protocol.packet.server.MySQLServerPacketType;
import me.mrletsplay.mrcore.mysql.protocol.packet.text.MySQLPrepareStatementResponsePacket;
import me.mrletsplay.mrcore.mysql.protocol.packet.text.MySQLResultSetPacket;
import me.mrletsplay.mrcore.mysql.protocol.type.MySQLString;

public class MySQLServerConnection {
	
	private static FlagCompound defaultCapabilityFlags = new FlagCompound(
			MySQLCapabilityFlag.CLIENT_PROTOCOL_41,
			MySQLCapabilityFlag.CLIENT_LONG_PASSWORD,
			MySQLCapabilityFlag.CLIENT_SECURE_CONNECTION,
			MySQLCapabilityFlag.CLIENT_PLUGIN_AUTH,
			MySQLCapabilityFlag.CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA,
			MySQLCapabilityFlag.CLIENT_DEPRECATE_EOF,
			MySQLCapabilityFlag.CLIENT_MULTI_STATEMENTS,
			MySQLCapabilityFlag.CLIENT_MULTI_RESULTS,
			MySQLCapabilityFlag.CLIENT_PS_MULTI_RESULTS
		);

	private Socket socket;
	private InputStream in;
	private OutputStream out;
	
	private MySQLWriter globalWriter;
	private MySQLReader globalReader;
	public byte lastSequenceID;
	
	private byte protocolVersion;
	private MySQLString serverVersion;
	private int connectionID;
	private byte[] authPluginData;
	private FlagCompound serverCapabilityFlags, clientCapabilityFlags, commonCapabilityFlags;
	private byte charSet;
	private MySQLString authPluginName;
	private String username, database;
	private MySQLAuthPluginBase authPlugin;
	
	public MySQLServerConnection(Socket socket, InputStream in, OutputStream out, String username, String password, String database) throws IOException {
		this.socket = socket;
		this.in = in;
		this.out = out;
		this.username = username;
		this.database = database;
		this.clientCapabilityFlags = new FlagCompound(defaultCapabilityFlags);
		if(database != null) this.clientCapabilityFlags.addFlag(MySQLCapabilityFlag.CLIENT_CONNECT_WITH_DB);
		init(password);
	}
	
	private void init(String password) throws IOException {
		globalReader = new MySQLReader(in);
		globalWriter = new MySQLWriter(out);
		RawPacket handshake = readPacket();
		MySQLReader hReader = new MySQLReader(new ByteArrayInputStream(handshake.getPayload()));
		protocolVersion = (byte) hReader.read();
		serverVersion = hReader.readNullTerminatedString();
		connectionID = (int) hReader.readFixedLengthInteger(4);
		MySQLString authPluginData = hReader.readString(8);
		hReader.read(); // Filler
		if(hReader.hasMore()) {
			serverCapabilityFlags = new FlagCompound((short) hReader.readFixedLengthInteger(2));
			if(hReader.hasMore()) {
				charSet = (byte) hReader.read();
				hReader.read(2); // Status flags
				serverCapabilityFlags.setCompound(serverCapabilityFlags.getCompound() | (hReader.readFixedLengthInteger(2) << 16));
				byte authLength = (byte) hReader.read(); // No check for capability flag
				hReader.read(10); // Reserved
				if(serverCapabilityFlags.hasFlag(MySQLCapabilityFlag.CLIENT_SECURE_CONNECTION)) {
					authPluginData = authPluginData.concat(hReader.readString(Math.max(13, authLength - 8)));
				}
				if(serverCapabilityFlags.hasFlag(MySQLCapabilityFlag.CLIENT_PLUGIN_AUTH)) {
					authPluginName = hReader.readNullTerminatedString();
					MySQLAuthPlugin plugin = MySQLAuthPlugin.getByName(authPluginName.toString());
					if(plugin == null) throw new UnsupportedOperationException("Invalid auth plugin \""+authPluginName+"\"");
					authPlugin = plugin.newInstance();
				}
			}
			commonCapabilityFlags = FlagCompound.combine(serverCapabilityFlags, clientCapabilityFlags, CombinationMode.AND);
		}
		this.authPluginData = authPluginData.getBytes();
		sendHandshakeResponse(password, authPluginData.getBytes());
	}
	
	private void sendHandshakeResponse(String password, byte[] authPluginData) throws IOException {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		MySQLWriter writer = new MySQLWriter(bOut);
		writer.writeFixedLengthInteger(4, (int) commonCapabilityFlags.getCompound());
		writer.writeFixedLengthInteger(4, 0x40000000);
		writer.write(MySQLCharset.LATIN1_GERMAN1_CI);
		writer.write(new byte[23]);
		writer.writeNullTerminatedString(new MySQLString(username));
		if(commonCapabilityFlags.hasFlag(MySQLCapabilityFlag.CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA)) {
			byte[] auth = authPlugin.getInitialAuthResponse(password, authPluginData);
			writer.writeLengthEncodedInteger(auth.length);
			writer.write(auth);
		}else if(commonCapabilityFlags.hasFlag(MySQLCapabilityFlag.CLIENT_SECURE_CONNECTION)) {
			byte[] auth = authPlugin.getInitialAuthResponse(password, authPluginData);
			writer.writeFixedLengthInteger(1, auth.length);
			writer.write(auth);
		}else {
			byte[] auth = authPlugin.getInitialAuthResponse(password, authPluginData);
			writer.writeNullTerminatedString(new MySQLString(auth));
		}
		
		if(commonCapabilityFlags.hasFlag(MySQLCapabilityFlag.CLIENT_CONNECT_WITH_DB)) {
			writer.writeNullTerminatedString(new MySQLString(database));
		}
		
		if(commonCapabilityFlags.hasFlag(MySQLCapabilityFlag.CLIENT_PLUGIN_AUTH)) {
			writer.writeNullTerminatedString(new MySQLString(authPlugin.getName()));
		}
		if(commonCapabilityFlags.hasFlag(MySQLCapabilityFlag.CLIENT_CONNECT_ATTRS)) {
			// Keys, values etc.
		}
		
		sendPacket(RawPacket.of(bOut.toByteArray()));
		authPlugin.handleFurtherProcessing(this);
		awaitOkay("Failed to connect");
		newLifecycle();
	}
	
	public void newLifecycle() {
		lastSequenceID = -1; // New connection lifecycle
	}
	
	public byte[] getAuthPluginData() {
		return authPluginData;
	}
	
	public MySQLString getAuthPluginName() {
		return authPluginName;
	}
	
	public FlagCompound getClientCapabilityFlags() {
		return clientCapabilityFlags;
	}
	
	public FlagCompound getCommonCapabilityFlags() {
		return commonCapabilityFlags;
	}
	
	public MySQLAuthPluginBase getAuthPlugin() {
		return authPlugin;
	}
	
	public String getDatabase() {
		return database;
	}
	
	public FlagCompound getServerCapabilityFlags() {
		return serverCapabilityFlags;
	}
	
	public byte getCharSet() {
		return charSet;
	}
	
	public int getConnectionID() {
		return connectionID;
	}
	
	public byte getProtocolVersion() {
		return protocolVersion;
	}
	
	public MySQLString getServerVersion() {
		return serverVersion;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public InputStream getInputStream() {
		return in;
	}
	
	public OutputStream getOutputStream() {
		return out;
	}
	
	public FlagCompound getCapabilities() {
		return commonCapabilityFlags;
	}
	
	public boolean hasCapability(int flag) {
		return getCapabilities().hasFlag(flag);
	}
	
	public void sendPacket(RawPacket packet) throws IOException {
		globalWriter.writePacket(++lastSequenceID, packet);
	}
	
	private MySQLOKPacket awaitOkay(String errorMessage) throws IOException {
		RawPacket raw = readPacket();
		if(raw == null) throw new IOException("Missing OK packet");
		MySQLServerPacket r = raw.parseServerPacket(this);
		switch(r.getType()) {
			case OK:
				return (MySQLOKPacket) r;
			case ERR:
				throw new RuntimeException(errorMessage + ": \"" + ((MySQLERRPacket) r).getErrorMessage() + "\" (from Server)");
			default:
				throw new RuntimeException(errorMessage);
		}
	}
	
	public boolean hasData() throws IOException {
		return globalReader.hasMore();
	}
	
	public RawPacket readPacket() throws IOException {
		RawPacket raw = globalReader.readPacket();
		if(raw == null) return null;
		this.lastSequenceID = raw.getSequenceID();
		return raw;
	}
	
	public ResultSet query(String query) {
		try {
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			MySQLWriter w = new MySQLWriter(bOut);
			w.write(MySQLCommand.COM_QUERY);
			w.writeString(new MySQLString(query));
			sendPacket(RawPacket.of(bOut.toByteArray()));
			RawPacket raw = readPacket();
			if(raw.getServerPacketType().equals(MySQLServerPacketType.OK)) {
				newLifecycle();
				return null; // No further data
			}
			if(raw.getServerPacketType().equals(MySQLServerPacketType.ERR)) throw new RuntimeException(((MySQLERRPacket) raw.parseServerPacket(this)).getErrorMessage().toString()); // No further data
			MySQLResultSetPacket packet = raw.parseTextPacket(this, MySQLResultSetPacket.class, MySQLCommand.COM_QUERY);
			newLifecycle();
			return new ResultSet(packet);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public PreparedStatement prepareStatement(String query) {
		try {
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			MySQLWriter w = new MySQLWriter(bOut);
			w.write(MySQLCommand.COM_STMT_PREPARE);
			w.writeString(new MySQLString(query));
			sendPacket(RawPacket.of(bOut.toByteArray()));
			RawPacket raw = readPacket();
			if(raw.getServerPacketType().equals(MySQLServerPacketType.ERR)) throw new RuntimeException(((MySQLERRPacket) raw.parseServerPacket(this)).getErrorMessage().toString()); // No further data
			MySQLPrepareStatementResponsePacket packet = raw.parseTextPacket(this, MySQLPrepareStatementResponsePacket.class, MySQLCommand.COM_QUERY);
			newLifecycle();
			return new PreparedStatement(packet);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public ResultSet executeStatement(PreparedStatement statement) {
		try {
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			MySQLWriter w = new MySQLWriter(bOut);
			w.write(MySQLCommand.COM_STMT_EXECUTE);
			w.writeFixedLengthInteger(4, statement.getID());
			w.write(0x00); // Flags
			w.writeFixedLengthInteger(4, 0x01); // Iteration count (always 1)
			if(statement.getParameters().length > 0) {
				NullBitmap bitmap = new NullBitmap(statement.getParameters().length, 0);
				for(int i = 0; i < statement.getParameters().length; i++) {
					if(statement.getParameter(i).getValue() == null) bitmap.setNullBit(i);
				}
				w.write(bitmap.getBytes());
				w.write(0x01); // new-params-bound-flag
				for(StatementParameter p : statement.getParameters()) {
					w.write(p.getDataType().getSQLIdentifier());
					w.write(0x80); // Flag byte
				}
				for(StatementParameter p : statement.getParameters()) {
					if(p.getValue() != null) w.writeLengthEncodedString(p.getFormattedValue());
				}
			}
			sendPacket(RawPacket.of(bOut.toByteArray()));
			MySQLResultSetBinaryPacket raw = readPacket().parseBinaryPacket(this, MySQLResultSetBinaryPacket.class, MySQLCommand.COM_STMT_EXECUTE);
			newLifecycle();
			return new ResultSet(raw);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void closeStatement(PreparedStatement statement) {
		try {
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			MySQLWriter w = new MySQLWriter(bOut);
			w.write(MySQLCommand.COM_STMT_CLOSE);
			w.writeFixedLengthInteger(4, statement.getID());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void createSimpleTable(String name, TableColumn... columns) {
		
	}
	
	public void selectSchema(String schemaName) {
		try {
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			MySQLWriter w = new MySQLWriter(bOut);
			w.write(MySQLCommand.COM_INIT_DB);
			w.writeString(new MySQLString(schemaName));
			sendPacket(RawPacket.of(bOut.toByteArray()));
			awaitOkay("Failed to select database");
			this.database = schemaName;
			newLifecycle();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void disconnect() {
		try {
			sendPacket(RawPacket.of(MySQLCommand.COM_QUIT));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try {
			awaitOkay("Improper disconnect");
		}catch(IOException e) {} // Connection already closed
		newLifecycle();
		closeConnection();
	}
	
	private void closeConnection() {
		try {
			if(!socket.isClosed()) socket.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
