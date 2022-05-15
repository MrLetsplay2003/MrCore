package me.mrletsplay.mrcore.misc;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtils {

	/**
	 * Rounds a double to a given amount of decimals using the {@link RoundingMode#HALF_UP} method
	 * @param value The number to round
	 * @param places The number of decimals to round to
	 * @return The number rounded to the specified amount of decimals
	 * @throws IllegalArgumentException - If the specified amount of decimals is less than 0
	 */
	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException("Can't have a negative amount of decimal places");
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
