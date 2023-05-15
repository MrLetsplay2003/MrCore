package me.mrletsplay.mrcore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONException;
import me.mrletsplay.mrcore.json.JSONObject;

public class JSONTest {

	@Test
	public void testRejectsValidTypes() {
		JSONObject obj = new JSONObject();
		assertThrows(JSONException.class, () -> obj.set("hello", new Object()));

		JSONArray arr = new JSONArray();
		assertThrows(JSONException.class, () -> arr.add(new Object()));
	}

	@Test
	public void testCopyObject() {
		JSONObject obj = new JSONObject();
		obj.put("int", 1);
		obj.put("double", 4.2);
		obj.put("string", "Hello World!");

		JSONObject obj2 = new JSONObject();
		obj.put("obj", new JSONObject());

		JSONArray arr = new JSONArray();
		obj.put("arr", new JSONArray());

		JSONObject copy = obj.copy();
		assertEquals(1, copy.getInt("int"));
		assertEquals(4.2, copy.getDouble("double"));
		assertEquals("Hello World!", copy.getString("string"));
		assertFalse(obj2 == copy.getJSONObject("obj"));
		assertEquals(obj2, copy.getJSONObject("obj"));
		assertFalse(arr == copy.getJSONArray("arr"));
		assertEquals(arr, copy.getJSONArray("arr"));
	}

	@Test
	public void testCopyArray() {
		JSONArray arr = new JSONArray();
		arr.add(1);
		arr.add(4.2);
		arr.add("Hello World!");

		JSONObject obj = new JSONObject();
		arr.add(new JSONObject());

		JSONArray arr2 = new JSONArray();
		arr.add(new JSONArray());

		JSONArray copy = arr.copy();
		assertEquals(1, copy.getInt(0));
		assertEquals(4.2, copy.getDouble(1));
		assertEquals("Hello World!", copy.getString(2));
		assertFalse(obj == copy.getJSONObject(3));
		assertEquals(obj, copy.getJSONObject(3));
		assertFalse(arr2 == copy.getJSONArray(4));
		assertEquals(arr2, copy.getJSONArray(4));
	}

}
