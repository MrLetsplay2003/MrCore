package me.mrletsplay.mrcore.mysql.impl.statement;

import me.mrletsplay.mrcore.mysql.protocol.packet.text.MySQLColumnDefinition41Packet;
import me.mrletsplay.mrcore.mysql.protocol.type.MySQLDataType;
import me.mrletsplay.mrcore.mysql.protocol.type.MySQLDataTypes;
import me.mrletsplay.mrcore.mysql.protocol.type.MySQLString;

public class StatementParameter {

	private MySQLColumnDefinition41Packet definingPacket;
	private MySQLDataType<?> dataType;
	private Object value;
	
	public StatementParameter(MySQLColumnDefinition41Packet fromPacket) {
		this.definingPacket = fromPacket;
		this.dataType = MySQLDataTypes.getTypeById(fromPacket.getColumnType());
		this.value = null;
	}
	
	public MySQLColumnDefinition41Packet getDefiningPacket() {
		return definingPacket;
	}
	
	public MySQLDataType<?> getDataType() {
		return dataType;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}
	
	public MySQLString getFormattedValue() {
		return dataType.format(value);
	}
	
}
