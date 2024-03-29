package me.mrletsplay.mrcore;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import me.mrletsplay.mrcore.misc.ByteUtils;

public class ByteUtilsTest {

	@Test
	public void testBytesToHex() {
		assertEquals("abcdef012345", ByteUtils.bytesToHex(new byte[] { (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, 0x01, 0x23, 0x45 }));
	}

	@Test
	public void testHexToBytes() {
		assertArrayEquals(new byte[] { (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, 0x01, 0x23, 0x45 }, ByteUtils.hexToBytes("ABCDEF012345"));
		assertThrows(IllegalArgumentException.class, () -> ByteUtils.hexToBytes("123"));
		assertThrows(IllegalArgumentException.class, () -> ByteUtils.hexToBytes("test"));
	}

}
