package me.mrletsplay.mrcore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import me.mrletsplay.mrcore.config.ConfigException;
import me.mrletsplay.mrcore.config.impl.yaml.YAMLCustomConfig;

public class YAMLCustomConfigTest {

	@Test
	public void testConfigLoad() {
		String config =
			  "### CustomConfig version: 2.0\r\n"
			+ "prop1: \"Hello World!\"\r\n"
			+ "prop2: 42\r\n"
			+ "prop3:\r\n"
			+ "  prop4: \"Test\"\r\n"
			+ "  prop5:\r\n"
			+ "    - 0\r\n"
			+ "    - \"1\"\r\n"
			+ "    - 2.0\r\n"
			+ "prop6: 1.5\r\n"
			+ "prop7: null\r\n"
			+ "prop8: true\r\n";

		YAMLCustomConfig cc = new YAMLCustomConfig();
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
