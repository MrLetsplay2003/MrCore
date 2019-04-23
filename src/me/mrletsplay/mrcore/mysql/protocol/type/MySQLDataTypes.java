package me.mrletsplay.mrcore.mysql.protocol.type;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.mysql.protocol.io.MySQLReader;
import me.mrletsplay.mrcore.mysql.protocol.misc.MySQLException;

public class MySQLDataTypes {
	
	protected static final List<MySQLDataType<?>> DEFAULT_TYPES = new ArrayList<>();

	public static final MySQLDataType<BigDecimal>
				DECIMAL = new MySQLDataType<>("decimal", 0x00, BigDecimal.class, s -> new BigDecimal(s.toString()), d -> d.toString()),
				NEWDECIMAL = new MySQLDataType<>("newdecimal", 0xf6, BigDecimal.class, s -> new BigDecimal(s.toString()), d -> d.toString());
	
	public static final MySQLDataType<Byte> TINY = new MySQLDataType<>("tiny", 0x01, Byte.class, s -> Byte.valueOf(s.toString()), b -> b.toString(), r -> new BigInteger(r.readReversed(1)).byteValue());
	
	public static final MySQLDataType<Short>
				SHORT = new MySQLDataType<>("short", 0x02, Short.class, s -> Short.valueOf(s.toString()), s -> s.toString(), r -> new BigInteger(r.readReversed(2)).shortValue()),
				YEAR = new MySQLDataType<>("year", 0x0d, Short.class, s -> Short.valueOf(s.toString()), s -> s.toString(), r -> new BigInteger(r.readReversed(2)).shortValue());
	
	public static final MySQLDataType<Integer>
				LONG = new MySQLDataType<>("long", 0x03, Integer.class, s -> Integer.valueOf(s.toString()), i -> i.toString(), r -> new BigInteger(r.readReversed(4)).intValue()),
				INT24 = new MySQLDataType<>("int24",0x09, Integer.class, s -> Integer.valueOf(s.toString()), i -> i.toString(), r -> new BigInteger(r.readReversed(4)).intValue());
	
	public static final MySQLDataType<Float> FLOAT = new MySQLDataType<>("float", 0x04, Float.class, s -> Float.valueOf(s.toString()), f -> f.toString(), r -> Float.intBitsToFloat((int) r.readFixedLengthInteger(4)));
	
	public static final MySQLDataType<Double> DOUBLE = new MySQLDataType<>("double", 0x05, Double.class, s -> Double.valueOf(s.toString()), f -> f.toString(), r -> Double.longBitsToDouble(r.readFixedLengthInteger(8)));
	
	public static final MySQLDataType<Void> NULL = new MySQLDataType<>("null", 0x06, Void.class, s -> null, v -> null);
	public static final MySQLDataType<Timestamp>
				TIMESTAMP = new MySQLDataType<>("timestamp", 0x07, Timestamp.class, s -> Timestamp.valueOf(s.toString()), t -> t.toString(), r -> new Timestamp(readTime(r))),
				DATETIME = new MySQLDataType<>("datetime", 0x0c, Timestamp.class, s -> Timestamp.valueOf(s.toString()), t -> t.toString(), r -> new Timestamp(readTime(r)));
	
	public static final MySQLDataType<Long> LONGLONG = new MySQLDataType<>("longlong", 0x08, Long.class, s -> Long.valueOf(s.toString()), l -> l.toString(), r -> new BigInteger(r.readReversed(8)).longValue());
	
	public static final MySQLDataType<Date> DATE = new MySQLDataType<>("date", 0x0a, Date.class, s -> Date.valueOf(s.toString()), d -> d.toString(), r -> new Date(readTime(r)));	
	public static final MySQLDataType<Time> TIME = new MySQLDataType<>("time", 0x0b, Time.class, s -> Time.valueOf(s.toString()), t -> t.toString(), r -> new Time(readTime(r)));
	
	public static final MySQLDataType<String>
				VARCHAR = new MySQLDataType<>("varchar", 0x0f, String.class, s -> s.toString(), s -> s),
				VAR_STRING = new MySQLDataType<>("varstring", 0xfd, String.class, s -> s.toString(), s -> s),
				STRING = new MySQLDataType<>("string", 0xfe, String.class, s -> s.toString(), s -> s);
	
	public static final MySQLDataType<MySQLString>
				TINY_BLOB = new MySQLDataType<>("tinyblob", 0xf9, MySQLString.class, s -> s, s -> s.toString()),
				MEDIUM_BLOB = new MySQLDataType<>("mediumblob", 0xfa, MySQLString.class, s -> s, s -> s.toString()),
				LONG_BLOB = new MySQLDataType<>("longblob", 0xfb, MySQLString.class, s -> s, s -> s.toString()),
				BLOB = new MySQLDataType<>("blob", 0xfc, MySQLString.class, s -> s, s -> s.toString());
	
	public static MySQLDataType<?> getTypeById(byte identifier) {
		return DEFAULT_TYPES.stream()
				.filter(t -> t.getSQLIdentifier() == identifier)
				.findFirst().orElseThrow(() -> new MySQLException("Invalid/unsupported data type identifier: "+Integer.toHexString(identifier)));
	}
	
	private static long readTime(MySQLReader r) throws IOException {
		int len = r.read();
		int year = 0, month = 0, day = 0, hour = 0, minute = 0, second = 0, micros = 0;
		if(len >= 4) {
			year = (int) r.readFixedLengthInteger(2);
			month = r.read();
			day = r.read();
		}
		if(len >= 7) {
			hour = r.read();
			minute = r.read();
			second = r.read();
		}
		if(len == 11) {
			micros = (int) r.readFixedLengthInteger(4);
		}
		return LocalDateTime.of(year, month, day, hour, minute, second, micros * 1000).toInstant(ZoneOffset.UTC).toEpochMilli();
	}
	
}
