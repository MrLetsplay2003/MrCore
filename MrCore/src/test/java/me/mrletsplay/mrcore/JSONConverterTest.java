package me.mrletsplay.mrcore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.JSONType;
import me.mrletsplay.mrcore.json.converter.JSONComplexListType;
import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONConverter;
import me.mrletsplay.mrcore.json.converter.JSONConvertible;
import me.mrletsplay.mrcore.json.converter.JSONListType;
import me.mrletsplay.mrcore.json.converter.JSONParameter;
import me.mrletsplay.mrcore.json.converter.JSONPrimitiveConvertible;
import me.mrletsplay.mrcore.json.converter.JSONPrimitiveStringConvertible;
import me.mrletsplay.mrcore.json.converter.JSONValue;

public class JSONConverterTest {

	@Test
	public void testEncodeAndDecode() {
		MyClass obj = MyClass.create();
		assertEquals(obj, JSONConverter.decodeObject(JSONConverter.encodeObject(obj), MyClass.class));
	}

	@Test
	public void testEncodeAndDecodeExtended() {
		MyClass2 obj = MyClass2.create();
		MyClass2 obj2 = JSONConverter.decodeObject(JSONConverter.encodeObject(obj), MyClass2.class);
		assertEquals(obj.time, obj2.time);
	}

	@Test
	public void testEncodeAndDecodeConstructorParam() {
		MyClass3 obj = MyClass3.create();

		JSONObject encoded = JSONConverter.encodeObject(obj);
		assertFalse(encoded.has("someBoolean"), "Value shouldn't be encoded if encode = false");

		MyClass3 obj2 = JSONConverter.decodeObject(encoded, MyClass3.class);
		assertEquals(obj.time, obj2.time);
		assertEquals(obj.val + 2, obj2.val, "Value shouldn't be assigned automatically if decode = false");
	}

	private static class MyClass implements JSONConvertible {

		@JSONValue
		private int iVal;

		@JSONValue
		private long lVal;

		@JSONValue
		private float fVal;

		@JSONValue
		private double dVal;

		@JSONValue
		private String sVal;

		@JSONValue
		private JSONObject oVal;

		@JSONValue
		private JSONArray aVal;

		@JSONValue
		private MyClass cVal;

		@JSONValue
		private MyClass4 cVal2;

		@JSONValue
		private MyClass5 cVal3;

		@JSONValue
		@JSONListType(JSONType.STRING)
		private List<String> slVal;

		@JSONValue
		@JSONComplexListType(MyClass.class)
		private List<MyClass> clVal;

		@JSONConstructor
		private MyClass() {}

		public static MyClass create() {
			MyClass m = new MyClass();
			m.iVal = 420;
			m.lVal = 420_000_000_000L;
			m.fVal = 6.9f;
			m.dVal = 6.9123123123123123d;
			m.sVal = "Hello World!";
			m.oVal = new JSONObject();
			m.oVal.put("one", "two");
			m.aVal = new JSONArray();
			m.aVal.add(123);
			m.cVal = new MyClass();
			m.cVal2 = new MyClass4(123);
			m.cVal3 = new MyClass5("Hello World");
			m.slVal = new ArrayList<>();
			m.slVal.add("Test");
			m.clVal = new ArrayList<>();
			m.clVal.add(new MyClass());
			return m;
		}

		@Override
		public int hashCode() {
			return Objects.hash(aVal, cVal, clVal, dVal, fVal, iVal, lVal, oVal, sVal, slVal);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MyClass other = (MyClass) obj;
			return Objects.equals(aVal, other.aVal) && Objects.equals(cVal, other.cVal)
				&& Objects.equals(clVal, other.clVal)
				&& Double.doubleToLongBits(dVal) == Double.doubleToLongBits(other.dVal)
				&& Float.floatToIntBits(fVal) == Float.floatToIntBits(other.fVal) && iVal == other.iVal
				&& lVal == other.lVal && Objects.equals(oVal, other.oVal) && Objects.equals(sVal, other.sVal)
				&& Objects.equals(slVal, other.slVal);
		}

	}

	private static class MyClass2 implements JSONConvertible {

		private LocalDateTime time;

		@JSONConstructor
		private MyClass2() {}

		@Override
		public void preSerialize(JSONObject object) {
			object.put("time", time.toString());
		}

		@Override
		public void preDeserialize(JSONObject object) {
			this.time = LocalDateTime.parse(object.getString("time"));
		}

		public static MyClass2 create() {
			MyClass2 m = new MyClass2();
			m.time = LocalDateTime.now();
			return m;
		}

	}

	private static class MyClass3 implements JSONConvertible {

		@JSONValue(encode = false, decode = false)
		private boolean someBoolean;

		@JSONValue(decode = false)
		private int val;

		private LocalDateTime time;

		private MyClass3() {}

		@JSONConstructor
		public MyClass3(@JSONParameter("val") int val, @JSONParameter("time") String time) {
			this.val = val + 2;
			this.time = LocalDateTime.parse(time);
		}

		@Override
		public void preSerialize(JSONObject object) {
			object.put("time", time.toString());
		}

		public static MyClass3 create() {
			MyClass3 m = new MyClass3();
			m.time = LocalDateTime.now();
			m.val = 69;
			return m;
		}

	}

	private static class MyClass4 implements JSONPrimitiveConvertible {

		private int value;

		public MyClass4(int value) {
			this.value = value;
		}

		@Override
		public Object toJSONPrimitive() {
			return value;
		}

		@SuppressWarnings("unused")
		public static MyClass4 decodePrimitive(Object value) {
			return new MyClass4((int) value);
		}

		@Override
		public int hashCode() {
			return Objects.hash(value);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MyClass4 other = (MyClass4) obj;
			return value == other.value;
		}

	}

	private static class MyClass5 implements JSONPrimitiveStringConvertible {

		private String value;

		public MyClass5(String value) {
			this.value = value;
		}

		@Override
		public String toJSONPrimitive() {
			return value;
		}

		@SuppressWarnings("unused")
		public static MyClass5 decodePrimitive(String value) {
			return new MyClass5(value);
		}

		@Override
		public int hashCode() {
			return Objects.hash(value);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MyClass5 other = (MyClass5) obj;
			return Objects.equals(value, other.value);
		}

	}

}
