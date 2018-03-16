package main;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class UsefulTools {
	
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
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException("Can't have a negative amount of decimal places");
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
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
