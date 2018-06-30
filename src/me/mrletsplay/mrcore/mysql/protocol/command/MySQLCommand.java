package me.mrletsplay.mrcore.mysql.protocol.command;

public class MySQLCommand {

	public static final byte
			COM_SLEEP = 0x00,
			COM_QUIT = 0x01,
			COM_INIT_DB = 0x02,
			COM_QUERY = 0x03,
			COM_FIELD_LIST = 0x04,
			COM_CREATE_DB = 0x05,
			COM_DROP_DP = 0x06,
			COM_REFRESH = 0x07,
			COM_SHUTDOWN = 0x08,
			COM_STATISTICS = 0x09,
			COM_PROCESS_INFO = 0x0a,
			COM_CONNECT = 0x0b,
			COM_PROCESS_KILL = 0x0c,
			COM_DEBUG = 0x0d,
			COM_PING = 0x0e,
			COM_TIME = 0x0f,
			COM_DELAYED_INSERT = 0x10,
			COM_CHANGE_USER = 0x11,
			COM_RESET_CONNECTION = 0x1f,
			COM_DAEMON = 0x1d,
			
			COM_STMT_PREPARE = 0x16,
			COM_STMT_EXECUTE = 0x17,
			COM_STMT_CLOSE = 0x19;
	
}
