package me.mrletsplay.mrcore.main;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OtherTools {
	
	/**
	 * @param ms The amount of milliseconds
	 * @param formatting Example format: "s,min,h,d"
	 * @return A formatted string
	 */
	public static String formatTime(int ms, String formatting) {
		String[] formats = formatting.split(",");
		if(ms<60000) {
			return ms/1000+formats[0];
		}else if(ms<60*60000) {
			return ms/60000+formats[1];
		}else if(ms<60*60000*24){
			return ms/(60*60000)+formats[2];
		}else{
			return ms/(60*60000*24)+formats[3];
		}
	}
	
	/**
	 * Rounds a double to a given amount of decimals using the {@link java.math.RoundingMode#HALF_UP HALF_UP} method
	 * @param value The number to round
	 * @param places The number of decimals to round to
	 * @return The number rounded to the specified amount of decimals
	 * @throws IllegalArgumentException - If the specified amount of decimals is < 0
	 */
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException("Can't have a negative amount of decimal places");
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	/**
	 * Splits a string into multiple lines by the following rules:
	 * - A string will never be longer than *length*
	 * - The string will be cut on the end of a word
	 * - If that's not possible, the string will be cut exactly at *length*
	 * @param string The string to cut into lines
	 * @param len The maximum length of a line
	 * @return An array of lines cut into properly-sized pieces
	 */
	public static List<String> advSplit(String string, int length) {
		List<String> words = new ArrayList<>();
		Collections.addAll(words, string.split(" ", -1));
		List<String> lineBuf = new ArrayList<>();
		List<String> buf = new ArrayList<>();
		boolean a = true;
		while(!words.isEmpty()) {
			if(a) buf.add(words.remove(0));
			a = true;
			if(toString(buf).length()>length) {
				String o = buf.remove(buf.size()-1);
				if(buf.isEmpty()) {
					List<String> buf2 = new ArrayList<>();
					while(o.length()>length) {
						String sub = o.substring(0, length);
						o = o.substring(length);
						buf2.add(sub);
					}
					buf.add(o);
					lineBuf.addAll(buf2);
				}else {
					lineBuf.add(toString(buf));
					buf.clear();
					buf.add(o);
					a = false;
				}
			}
		}
		if(!buf.isEmpty())lineBuf.add(toString(buf));
		return lineBuf;
	}
	
	private static String toString(List<String> list) {
		return list.stream().collect(Collectors.joining(" "));
	}
	
	public static class UnitConverter {
		
		private String[] formatting;
		private int unitFactor, decimalPlaces;
		private boolean useDecimals;
		
		public UnitConverter(String formatting, int factor) {
			this.formatting = formatting.split("\\|");
			this.unitFactor = factor;
			this.useDecimals = false;
			this.decimalPlaces = 0;
		}
		
		public void setDecimalPlaces(int decimalPlaces) {
			this.decimalPlaces = decimalPlaces;
		}
		
		public void setUseDecimals(boolean useDecimals) {
			this.useDecimals = useDecimals;
		}
		
		public String format(long baseAmount) {
			long fac = 1;
			int a = 0;
			while(baseAmount/fac>=unitFactor) {
				fac*=unitFactor;
				a++;
			}
			String f = formatting[a>=formatting.length?formatting.length-1:a];
			if(!useDecimals) {
				return baseAmount/fac+f;
			}else {
				return round(baseAmount/(double)fac, decimalPlaces)+f;
			}
		}
		
		public String format(long amount, int unitSteps) {
			return format(amount*((long)Math.pow(unitFactor, unitSteps)));
		}
		
	}
	
}
