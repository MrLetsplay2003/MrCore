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
	V1_13_R1("MC 1.13 Release 1", "v1_13_R1"),
	V1_13_R2("MC 1.13 Release 2", "v1_13_R2"),
	/**
	 * Represents any unknown version of Spigot. This may include any version older than 1.8R1 or newer than 1.13R2
	 */
	UNKNOWN("Unknown", "Unknown version");
	
	private final String friendlyName, version;
	
	private NMSVersion(String friendlyName, String version) {
		this.friendlyName = friendlyName;
		this.version = version;
	}
	
	/**
	 * @return The NMS name of this version (e.g. "v1_8_R1")
	 */
	public String getNMSName() {
		return version;
	}
	
	/**
	 * @return A more user-friendly name for this version (e.g. "MC 1.8 Release 1")
	 */
	public String getFriendlyName() {
		return friendlyName;
	}
	
	private static final NMSVersion CURRENT_VERSION;
	
	static {
		String vRaw = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		CURRENT_VERSION = Arrays.stream(values()).filter(v -> v.version.equalsIgnoreCase(vRaw)).findFirst().orElse(UNKNOWN);
	}
	
	/**
	 * @return The current server version or {@link NMSVersion#UNKNOWN} if unknown
	 */
	public static NMSVersion getCurrentServerVersion() {
		return CURRENT_VERSION;
	}
	
	/**
	 * Returns an array of all the versions from {@code startVersion} to {@code endVersion} (both inclusive)
	 * @param startVersion The starting version (inclusive)
	 * @param endVersion The ending version (inclusive)
	 * @return An array containing all the versions between the boundaries
	 */
	public static NMSVersion[] range(NMSVersion startVersion, NMSVersion endVersion) {
		return Arrays.stream(values()).filter(o -> o.ordinal() >= startVersion.ordinal() && o.ordinal() <= endVersion.ordinal()).toArray(NMSVersion[]::new);
	}
	
	/**
	 * Returns an array of all the 1.8 versions.<br>
	 * @return An array containing all the 1.8 versions
	 */
	public static NMSVersion[] v1_8() {
		return new NMSVersion[] {V1_8_R1, V1_8_R2, V1_8_R3};
	}

	/**
	 * Returns an array of all the 1.9 versions.<br>
	 * @return An array containing all the 1.9 versions
	 */
	public static NMSVersion[] v1_9() {
		return new NMSVersion[] {V1_9_R1, V1_9_R2};
	}

	/**
	 * Returns an array of all the versions from 1.8 to 1.12.<br>
	 * @return An array containing all the 1.8-1.12 versions
	 */
	public static NMSVersion[] v1_8to1_12() {
		return range(NMSVersion.V1_8_R1, NMSVersion.V1_12_R1);
	}

	/**
	 * Returns an array of all the versions from 1.8 to 1.13.<br>
	 * @return An array containing all the 1.8-1.13 versions
	 */
	public static NMSVersion[] v1_8to1_13() {
		return range(NMSVersion.V1_8_R1, NMSVersion.V1_13_R2);
	}

	/**
	 * Returns an array of all the 1.13 versions.<br>
	 * @return An array containing all the 1.13 versions
	 */
	public static NMSVersion[] v1_13() {
		return new NMSVersion[] {V1_13_R1, V1_13_R2};
	}
	
}
