package me.mrletsplay.mrcore;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.JSONParseException;
import me.mrletsplay.mrcore.json.JSONParser;
import me.mrletsplay.mrcore.json.JSONType;

public class JSONParserTest {

	private String buildJSONString(int depth) {
		StringBuilder b = new StringBuilder();
		for(int i = 0; i < depth; i++) {
			b.append('[');
		}

		for(int i = 0; i < depth; i++) {
			b.append(']');
		}

		return b.toString();
	}

	@Test
	public void testJSONParser() {
		String jsonString = "{\n"
			+ "  \"string\": \"Hello\\nWorld!\",\n"
			+ "  \"integer\": 12345,\n"
			+ "  \"decimal\": 123.45,\n"
			+ "  \"boolean\": true,\n"
			+ "  \"null\": null,\n"
			+ "  \"object\": {\n"
			+ "    \"a\": 1,\n"
			+ "    \"b\": \"2\"\n"
			+ "  },\n"
			+ "  \"array\": [\n"
			+ "    1,\n"
			+ "    \"2\"\n"
			+ "  ]\n"
			+ "}";

		JSONObject object = new JSONObject(jsonString);
		assertTrue(object.isOfType("string", JSONType.STRING));
		assertTrue(object.isOfType("integer", JSONType.INTEGER));
		assertTrue(object.isOfType("decimal", JSONType.DECIMAL));
		assertTrue(object.isOfType("integer", JSONType.NUMBER));
		assertTrue(object.isOfType("decimal", JSONType.NUMBER));
		assertTrue(object.isOfType("boolean", JSONType.BOOLEAN));
		assertTrue(object.isOfType("null", JSONType.NULL));
		assertTrue(object.isOfType("object", JSONType.OBJECT));
		assertTrue(object.isOfType("array", JSONType.ARRAY));

		assertTrue(object.getJSONArray("array").isOfType(0, JSONType.NUMBER));
	}

	@Test
	public void testJSONParserWrongType() {
		// TODO: implement
	}

	// TODO: add more tests

	@Test
	public void testJSONDepthExceeded() {
		String jsonString = buildJSONString(JSONParser.getMaxDepth());
		assertDoesNotThrow(() -> new JSONArray(jsonString), "Parsing shouldn't throw at max depth");

		String jsonString2 = buildJSONString(JSONParser.getMaxDepth() + 1);
		assertThrows(JSONParseException.class, () -> new JSONArray(jsonString2), "Parsing should throw if max depth is exceeded");
	}

}
