package me.mrletsplay.mrcore.bukkitimpl.versioned;

import java.util.Arrays;

public enum NMSRelease {

	V1_8(NMSVersion.V1_8_R1, NMSVersion.V1_8_R2, NMSVersion.V1_8_R3),
	V1_9(NMSVersion.V1_9_R1, NMSVersion.V1_9_R2),
	V1_10(NMSVersion.V1_10_R1),
	V1_11(NMSVersion.V1_11_R1),
	V1_12(NMSVersion.V1_12_R1),
	V1_13(NMSVersion.V1_13_R1, NMSVersion.V1_13_R2),
	V1_14(NMSVersion.V1_14_R1),
	V1_15(NMSVersion.V1_15_R1),
	V1_16(NMSVersion.V1_16_R1, NMSVersion.V1_16_R2, NMSVersion.V1_16_R3),
	V1_17(NMSVersion.V1_17_R1),
	V1_18(NMSVersion.V1_18_R1, NMSVersion.V1_18_R2),
	;
	
	private NMSVersion[] versions;
	
	private NMSRelease(NMSVersion... versions) {
		this.versions = versions;
	}
	
	public NMSVersion[] getVersions() {
		return versions;
	}
	
	public NMSVersion getFirstVersion() {
		return versions[0];
	}
	
	public NMSVersion getLastVersion() {
		return versions[versions.length - 1];
	}
	
	public NMSRelease[] to(NMSRelease other) {
		return Arrays.stream(values())
				.filter(o -> o.ordinal() >= ordinal() && o.ordinal() <= other.ordinal())
				.toArray(NMSRelease[]::new);
	}
	
	public NMSVersion[] versionsTo(NMSRelease other) {
		return Arrays.stream(to(other))
				.map(NMSRelease::getVersions)
				.flatMap(Arrays::stream)
				.toArray(NMSVersion[]::new);
	}
	
	/**
	 * @return The latest release supported by this version of MrCore
	 */
	public static NMSRelease getLatestSupportedRelease() {
		return values()[values().length - 1];
	}
	
}
