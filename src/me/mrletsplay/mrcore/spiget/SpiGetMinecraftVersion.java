package me.mrletsplay.mrcore.spiget;

import java.util.Arrays;

public enum SpiGetMinecraftVersion {

	v1_7("1.7"),
	v1_8("1.8"),
	v1_9("1.9"),
	v1_10("1.10"),
	v1_11("1.11"),
	v1_12("1.12"),
	;
	
	private final String name;
	
	private SpiGetMinecraftVersion(String name) {
		this.name = name;
	}
	
	public String getSpiGetName() {
		return name;
	}
	
	public static SpiGetMinecraftVersion getByName(String spiGetName) {
		return Arrays.stream(values()).filter(v -> v.getSpiGetName().equalsIgnoreCase(spiGetName)).findFirst().orElse(null);
	}
	
}
