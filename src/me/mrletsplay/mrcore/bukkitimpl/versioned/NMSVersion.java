package me.mrletsplay.mrcore.bukkitimpl.versioned;

import java.util.Arrays;

import org.bukkit.Bukkit;

public enum NMSVersion {

	V1_8_R1("MC 1.8 Release 1", "v1_8_R1"),
	V1_8_R2("MC 1.8 Release 2", "v1_8_R2"),
	V1_8_R3("MC 1.8 Release 3", "v1_8_R3"),
	V1_9_R1("MC 1.9 Release 1", "v1_9_R1"),
	V1_9_R2("MC 1.9 Release 2", "v1_9_R2"),
	V1_10_R1("MC 1.10", "v1_10_R1"),
	V1_11_R1("MC 1.11", "v1_11_R1"),
	V1_12_R1("MC 1.12", "v1_12_R1"),
	V1_13_R1("MC 1.13", "v1_13_R1"),
	UNKNOWN("Unknown", null);
	
	public final String friendlyName, version;
	
	private NMSVersion(String friendlyName, String version) {
		this.friendlyName = friendlyName;
		this.version = version;
	}
	
	private static final NMSVersion CURRENT_VERSION;
	
	static {
		String vRaw = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		CURRENT_VERSION = Arrays.stream(values()).filter(v -> v.version.equalsIgnoreCase(vRaw)).findFirst().orElse(UNKNOWN);
	}
	
	public static NMSVersion getCurrentServerVersion() {
		return CURRENT_VERSION;
	}
	
	public static NMSVersion[] v1_8to1_12() {
		return new NMSVersion[] {V1_8_R1, V1_8_R2, V1_8_R3, V1_9_R1, V1_9_R2, V1_10_R1, V1_11_R1, V1_12_R1};
	}
	
	public static NMSVersion[] v1_8to1_13() {
		return new NMSVersion[] {V1_8_R1, V1_8_R2, V1_8_R3, V1_9_R1, V1_9_R2, V1_10_R1, V1_11_R1, V1_12_R1, V1_13_R1};
	}
	
}
