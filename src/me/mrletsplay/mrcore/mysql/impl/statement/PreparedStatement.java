package me.mrletsplay.mrcore.mysql.impl.statement;

import me.mrletsplay.mrcore.mysql.impl.ResultSet;
import me.mrletsplay.mrcore.mysql.protocol.packet.text.MySQLPrepareStatementResponsePacket;

public class PreparedStatement {

	private MySQLPrepareStatementResponsePacket definingPacket;
	private StatementParameter[] parameters;
	private int id;
	
	public PreparedStatement(MySQLPrepareStatementResponsePacket fromPacket) {
		this.definingPacket = fromPacket;
		this.parameters = fromPacket.getParameterDefinitions().stream()
							.map(p -> new StatementParameter(p))
							.toArray(StatementParameter[]::new);
		this.id = fromPacket.getStatementID();
	}
	
	public MySQLPrepareStatementResponsePacket getDefiningPacket() {
		return definingPacket;
	}
	
	public StatementParameter[] getParameters() {
		return parameters;
	}
	
	public StatementParameter getParameter(int index) {
		return parameters[index];
	}
	
	public int getID() {
		return id;
	}
	
	public ResultSet execute() {
		return definingPacket.getConnection().executeStatement(this);
	}
	
}
