package me.mrletsplay.mrcore;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import me.mrletsplay.mrcore.misc.StringUtils;

/**
 * Unit test for simple App.
 */
public class StringUtilsTest {

	@Test
	public void testWrapString() {
		String longString = "This is a really really long string";
		assertEquals(Arrays.asList("This is a", "really", "really", "long", "string"), StringUtils.wrapString(longString, 10));

		String anotherString = "Anotherstring";
		assertEquals(Arrays.asList("Anotherstr", "ing"), StringUtils.wrapString(anotherString, 10));
	}

}
