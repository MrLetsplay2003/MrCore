package me.mrletsplay.mrcore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import me.mrletsplay.mrcore.misc.Result;

public class ResultTest {

	public static Result<Integer, NumberFormatException> parse(String wow) {
		try {
			return Result.of(Integer.parseInt(wow));
		}catch(NumberFormatException e) {
			return Result.err(e);
		}
	}

	public static Result<String, NumberFormatException> myThingy(String wow) {
		var res = parse(wow);
		if(res.isErr()) return res.up();
		int val = res.get();
		String string = String.format("%05d", val);
		return Result.of(string);
	}

	@Test
	public void testErr() {
		assertEquals("00001", myThingy("1").get());
		assertThrows(NumberFormatException.class, () -> myThingy("a").get());
	}

}
