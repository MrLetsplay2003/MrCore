package me.mrletsplay.mrcore.mysql.protocol.type;

import java.io.IOException;
import java.util.function.Function;

import me.mrletsplay.mrcore.mysql.protocol.io.MySQLReader;

public class MySQLDataType<T> {

	private byte identifier;
	private Class<T> javaType;
	private Function<MySQLString, T> parsingFunction;
	private Function<T, String> formattingFunction;
	private UnsafeFunction<MySQLReader, T> readingFunction;
	
//	public MySQLDataType(int identifier, Class<T> javaType, Function<MySQLString, T> parsingFunction, Function<T, MySQLString> formattingFunction) {
//		this.identifier = (byte) identifier;
//		this.javaType = javaType;
//		this.parsingFunction = parsingFunction;
//		this.formattingFunction = formattingFunction;
//		MySQLDataTypes.DEFAULT_TYPES.add(this);
//	}
	
	public MySQLDataType(int identifier, Class<T> javaType, Function<MySQLString, T> parsingFunction, Function<T, String> formattingFunction, UnsafeFunction<MySQLReader, T> readingFunction) {
		this.identifier = (byte) identifier;
		this.javaType = javaType;
		this.parsingFunction = parsingFunction;
		this.formattingFunction = formattingFunction;
		this.readingFunction = readingFunction;
		MySQLDataTypes.DEFAULT_TYPES.add(this);
	}
	
	public MySQLDataType(int identifier, Class<T> javaType, Function<MySQLString, T> parsingFunction, Function<T, String> formattingFunction) {
		this(identifier, javaType, parsingFunction, formattingFunction, null);
	}
	
	public byte getSQLIdentifier() {
		return identifier;
	}
	
	public Class<T> getJavaType() {
		return javaType;
	}
	
	public T parse(MySQLString string) {
		if(string == null) return null;
		return parsingFunction.apply(string);
	}
	
	public MySQLString format(Object value) {
		if(value != null && !javaType.isInstance(value)) throw new RuntimeException("Invalid type: " + value.getClass().getName());
		if(value == null) return null;
		return new MySQLString(formattingFunction.apply(javaType.cast(value)));
	}
	
	public T read(MySQLReader reader) throws IOException {
		if(readingFunction == null) {
			return parse(reader.readLengthEncodedString());
		}
		try {
			return readingFunction.apply(reader);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@FunctionalInterface
	public static interface UnsafeFunction<I, O> {
		
		public O apply(I in) throws Exception;
		
	}
	
}
