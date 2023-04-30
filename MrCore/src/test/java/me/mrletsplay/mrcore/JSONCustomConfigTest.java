package me.mrletsplay.mrcore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import me.mrletsplay.mrcore.config.ConfigException;
import me.mrletsplay.mrcore.config.impl.json.JSONCustomConfig;

public class JSONCustomConfigTest {

	@Test
	public void testConfigLoad() {
		String config = "{\"prop1\":\"Hello World!\",\"prop2\":42,\"prop3\":{\"prop4\":\"Test\",\"prop5\":[0,\"1\",2.0]},\"prop6\":1.5,\"prop7\":null,\"prop8\":true}";
		JSONCustomConfig cc = new JSONCustomConfig();
		cc.load(new ByteArrayInputStream(config.getBytes(StandardCharsets.UTF_8)));
		assertEquals("Hello World!", cc.getString("prop1"));
		assertEquals(42, cc.getInt("prop2"));
		assertEquals("Test", cc.getString("prop3.prop4"));
		assertEquals(Arrays.asList(0L /* int gets treated as long */, "1", 2.0), cc.getList("prop3.prop5"));
		assertEquals(1.5, cc.getDouble("prop6"), 0);
		assertEquals(null, cc.getString("prop7"));
		assertEquals(true, cc.getBoolean("prop8"));
		assertThrows(ConfigException.class, () -> cc.set("hello.world", this));
	}

}
