package me.mrletsplay.mrcore.mysql.impl.statement;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MySQLType {

	private String name;
	private Object[] params;
	
	private MySQLType(String name, Object... params) {
		this.name = name;
		this.params = params;
	}
	
	public String getName() {
		return name;
	}
	
	public Object[] getParams() {
		return params;
	}
	
	public String asString() {
		return name + (params.length > 0 ? "("+Arrays.stream(params).map(o -> o.toString()).collect(Collectors.joining(","))+")" : "");
	}
	
	public static <T> MySQLType getType(String name, Object... params) {
		return new MySQLType(name, params);
	}
	
	public static MySQLType bit() {
		return getType("bit");
	}
	
	public static MySQLType bit(int numBits) {
		return getType("bit", numBits);
	}
	
	public static MySQLType tinyInt() {
		return getType("tinyint");
	}
	
	public static MySQLType bool() {
		return getType("bool");
	}
	
	public static MySQLType booleanT() {
		return getType("boolean");
	}
	
	public static MySQLType smallInt() {
		return getType("smallint");
	}
	
	public static MySQLType mediumInt() {
		return getType("mediumint");
	}
	
	public static MySQLType intT() {
		return getType("int");
	}
	
	public static MySQLType integer() {
		return getType("integer");
	}
	
	public static MySQLType bigInt() {
		return getType("bigint");
	}
	
	public static MySQLType decimal() {
		return getType("decimal");
	}
	
	public static MySQLType floatT() {
		return getType("float");
	}
	
	public static MySQLType doubleT() {
		return getType("double");
	}
	
	public static MySQLType floatT(int precisionBits) {
		return getType("float", precisionBits);
	}
	
	public static MySQLType date() {
		return getType("date");
	}
	
	public static MySQLType dateTime() {
		return getType("datetime");
	}
	
	public static MySQLType dateTime(int precisionBits) {
		return getType("datetime", precisionBits);
	}
	
	public static MySQLType timestamp() {
		return getType("timestamp");
	}
	
	public static MySQLType timestamp(int precisionBits) {
		return getType("timestamp", precisionBits);
	}
	
	public static MySQLType time() {
		return getType("time");
	}
	
	public static MySQLType time(int precisionBits) {
		return getType("time", precisionBits);
	}
	
	public static MySQLType year() {
		return getType("year");
	}
	
	public static MySQLType varChar(int length) {
		return getType("varchar", length);
	}
	
	public static MySQLType charT(int length) {
		return getType("char", length);
	}
	
}
