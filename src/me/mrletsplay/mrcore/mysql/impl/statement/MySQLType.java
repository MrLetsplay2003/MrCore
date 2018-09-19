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
	
	public static <T> MySQLType ofType(String name, Object... params) {
		return new MySQLType(name, params);
	}
	
	public static MySQLType bit() {
		return ofType("bit");
	}
	
	public static MySQLType bit(int numBits) {
		return ofType("bit", numBits);
	}
	
	public static MySQLType tinyInt() {
		return ofType("tinyint");
	}
	
	public static MySQLType bool() {
		return ofType("bool");
	}
	
	public static MySQLType booleanT() {
		return ofType("boolean");
	}
	
	public static MySQLType smallInt() {
		return ofType("smallint");
	}
	
	public static MySQLType mediumInt() {
		return ofType("mediumint");
	}
	
	public static MySQLType intT() {
		return ofType("int");
	}
	
	public static MySQLType integer() {
		return ofType("integer");
	}
	
	public static MySQLType bigInt() {
		return ofType("bigint");
	}
	
	public static MySQLType decimal() {
		return ofType("decimal");
	}
	
	public static MySQLType floatT() {
		return ofType("float");
	}
	
	public static MySQLType doubleT() {
		return ofType("double");
	}
	
	public static MySQLType floatT(int precisionBits) {
		return ofType("float", precisionBits);
	}
	
	public static MySQLType date() {
		return ofType("date");
	}
	
	public static MySQLType dateTime() {
		return ofType("datetime");
	}
	
	public static MySQLType dateTime(int precisionBits) {
		return ofType("datetime", precisionBits);
	}
	
	public static MySQLType timestamp() {
		return ofType("timestamp");
	}
	
	public static MySQLType timestamp(int precisionBits) {
		return ofType("timestamp", precisionBits);
	}
	
	public static MySQLType time() {
		return ofType("time");
	}
	
	public static MySQLType time(int precisionBits) {
		return ofType("time", precisionBits);
	}
	
	public static MySQLType year() {
		return ofType("year");
	}
	
	public static MySQLType varChar(int length) {
		return ofType("varchar", length);
	}
	
	public static MySQLType charT(int length) {
		return ofType("char", length);
	}
	
}
