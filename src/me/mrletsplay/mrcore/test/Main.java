package me.mrletsplay.mrcore.test;

import me.mrletsplay.mrcore.main.UsefulTools;
import me.mrletsplay.mrcore.main.UsefulTools.UnitConverter;

public class Main {

	public static void main(String[] args) {
		System.out.println(UsefulTools.formatTime(123424, " second(s), minute(s), hour(s), day(s)"));
		UnitConverter c = new UsefulTools.UnitConverter(" b| kB| mB| GB| PB", 1024);
		c.setUseDecimals(true);
		c.setDecimalPlaces(2);
		System.out.println(c.format(10000000));
	}
	
}
