package me.mrletsplay.mrcore.bukkitimpl.versioned;

import java.util.Arrays;

import org.bukkit.Bukkit;

import me.mrletsplay.mrcore.bukkitimpl.MrCorePlugin;

public enum NMSVersion {

	V1_8_R1("MC 1.8 Release 1", "v1_8_R1"),
	V1_8_R2("MC 1.8 Release 2", "v1_8_R2"),
	V1_8_R3("MC 1.8 Release 3", "v1_8_R3", "1.8"),
	V1_9_R1("MC 1.9 Release 1", "v1_9_R1"),
	V1_9_R2("MC 1.9 Release 2", "v1_9_R2", "1.9"),
	V1_10_R1("MC 1.10", "v1_10_R1", "1.10"),
	V1_11_R1("MC 1.11", "v1_11_R1", "1.11"),
	V1_12_R1("MC 1.12", "v1_12_R1", "1.12"),
	V1_13_R1("MC 1.13 Release 1", "v1_13_R1"),
	V1_13_R2("MC 1.13 Release 2", "v1_13_R2", "1.13"),
	V1_14_R1("MC 1.14", "v1_14_R1", "1.14"),
	V1_15_R1("MC 1.15", "v1_15_R1", "1.15"),
	V1_16_R1("MC 1.16 Release 1", "v1_16_R1"),
	V1_16_R2("MC 1.16 Release 2", "v1_16_R2"),
	V1_16_R3("MC 1.16 Release 3", "v1_16_R3", "1.16"),
	V1_17_R1("MC 1.17 Release 1", "v1_17_R1", "1.17"),
	V1_18_R1("MC 1.18 Release 1", "v1_18_R1"),
	V1_18_R2("MC 1.18 Release 2", "v1_18_R2", "1.18"),
	V1_19_R1("MC 1.19 Release 1", "v1_19_R1"),
	V1_19_R2("MC 1.19 Release 2", "v1_19_R2"),
	V1_19_R3("MC 1.19 Release 3", "v1_19_R3", "1.19"),
	V1_20_R1("MC 1.20 Release 1", "v1_20_R1"),
	V1_20_R2("MC 1.20 Release 2", "v1_20_R2"),
	v1_20_R3("MC 1.20 Release 3", "v1_20_R3", "1.20"),
	v1_21_R1("MC 1.21 Release 1", null, "1.21"),
	/**
	 * Represents any unknown version of Spigot. This may include any version older than 1.8R1 or newer than the highest supported version
	 */
	UNKNOWN("Unknown", "Unknown version");

	private final String friendlyName, version, versionName;

	private NMSVersion(String friendlyName, String version, String versionName) {
		this.friendlyName = friendlyName;
		this.version = version;
		this.versionName = versionName;
	}

	private NMSVersion(String friendlyName, String version) {
		this(friendlyName, version, null);
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

	public boolean isNewerThan(NMSVersion other) {
		return other.ordinal() < ordinal();
	}

	public boolean isOlderThan(NMSVersion other) {
		return other.ordinal() > ordinal();
	}

	public boolean isNewerThanOrEqualTo(NMSVersion other) {
		return other.ordinal() <= ordinal();
	}

	public boolean isOlderThanOrEqualTo(NMSVersion other) {
		return other.ordinal() >= ordinal();
	}

	private static final NMSVersion CURRENT_VERSION;

	static {
		NMSVersion ver = null;

		String configuredVersion = MrCorePlugin.getMrCoreConfig().getNMSVersion();
		if(!configuredVersion.equalsIgnoreCase("auto")) {
			try {
				ver = NMSVersion.valueOf(configuredVersion.toUpperCase());
			}catch(IllegalArgumentException e) {
				MrCorePlugin.getInstance().getLogger().warning("Invalid NMS version \"" + configuredVersion + "\" in config, falling back to default");
			}
		}

		if(ver == null) {
			String packageName = Bukkit.getServer().getClass().getPackage().getName();
			String bukkitVersion = Bukkit.getBukkitVersion();

			MrCorePlugin.getInstance().getLogger().info("Package name is: " + packageName);
			MrCorePlugin.getInstance().getLogger().info("Bukkit version name is: " + bukkitVersion);

			if(packageName.startsWith("net.minecraft.server.v1_") || packageName.startsWith("org.bukkit.craftbukkit.v1_")) {
				String vRaw = packageName.split("\\.")[3];
				ver = Arrays.stream(values()).filter(v -> v.version != null && v.version.equalsIgnoreCase(vRaw)).findFirst().orElse(UNKNOWN);
			}else {
				ver = Arrays.stream(values()).filter(v -> v.versionName != null && bukkitVersion.startsWith(v.versionName)).findFirst().orElse(UNKNOWN);
			}
		}

		CURRENT_VERSION = ver;
	}

	/**
	 * @return The current server version or {@link NMSVersion#UNKNOWN} if unknown
	 */
	public static NMSVersion getCurrentServerVersion() {
		return CURRENT_VERSION;
	}

	/**
	 * @return The latest version supported by this version of MrCore
	 */
	public static NMSVersion getLatestSupportedVersion() {
		return values()[values().length - 1];
	}

	/**
	 * Returns an array of all the versions from {@code startVersion} to {@code endVersion} (both inclusive)
	 * @param startVersion The starting version (inclusive)
	 * @param endVersion The ending version (inclusive)
	 * @return An array containing all the versions between the boundaries
	 */
	public static NMSVersion[] range(NMSVersion startVersion, NMSVersion endVersion) {
		return Arrays.stream(values())
				.filter(o -> o.ordinal() >= startVersion.ordinal() && o.ordinal() <= endVersion.ordinal())
				.toArray(NMSVersion[]::new);
	}

	/**
	 * @deprecated Use {@link NMSRelease#getVersions()} instead
	 * Returns an array of all the 1.8 versions.<br>
	 * @return An array containing all the 1.8 versions
	 */
	@Deprecated
	public static NMSVersion[] v1_8() {
		return new NMSVersion[] {V1_8_R1, V1_8_R2, V1_8_R3};
	}

	/**
	 * @deprecated Use {@link NMSRelease#getVersions()} instead
	 * Returns an array of all the 1.9 versions.<br>
	 * @return An array containing all the 1.9 versions
	 */
	@Deprecated
	public static NMSVersion[] v1_9() {
		return new NMSVersion[] {V1_9_R1, V1_9_R2};
	}

	/**
	 * @deprecated Use {@link NMSRelease#versionsTo(NMSRelease)} instead
	 * Returns an array of all the versions from 1.8 to 1.12.<br>
	 * @return An array containing all the 1.8-1.12 versions
	 */
	@Deprecated
	public static NMSVersion[] v1_8to1_12() {
		return range(NMSVersion.V1_8_R1, NMSVersion.V1_12_R1);
	}

	/**
	 * @deprecated Use {@link NMSRelease#versionsTo(NMSRelease)} instead
	 * Returns an array of all the versions from 1.9 to 1.12.<br>
	 * @return An array containing all the 1.9-1.12 versions
	 */
	@Deprecated
	public static NMSVersion[] v1_9to1_12() {
		return range(NMSVersion.V1_9_R1, NMSVersion.V1_12_R1);
	}

	/**
	 * @deprecated Use {@link NMSRelease#versionsTo(NMSRelease)} instead
	 * Returns an array of all the versions from 1.8 to 1.13.<br>
	 * @return An array containing all the 1.8-1.13 versions
	 */
	@Deprecated
	public static NMSVersion[] v1_8to1_13() {
		return range(NMSVersion.V1_8_R1, NMSVersion.V1_13_R2);
	}

	/**
	 * @deprecated Use {@link NMSRelease#getVersions()} instead
	 * Returns an array of all the 1.13 versions.<br>
	 * @return An array containing all the 1.13 versions
	 */
	@Deprecated
	public static NMSVersion[] v1_13() {
		return new NMSVersion[] {V1_13_R1, V1_13_R2};
	}

}
