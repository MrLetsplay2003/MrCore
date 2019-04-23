package me.mrletsplay.mrcore.config;

import java.util.Arrays;

public enum ConfigVersion {

	v1_0("1.0"),
	v1_1("1.1"),
	v2_0("2.0"),
	v1_0_compact("1.0_compact");
	
	public final String name;
	
	private ConfigVersion(String name) {
		this.name = name;
	}
	
	public static ConfigVersion getByName(String name) {
		return Arrays.stream(values()).filter(c -> c.name.equals(name)).findFirst().orElse(null);
	}
	
}
