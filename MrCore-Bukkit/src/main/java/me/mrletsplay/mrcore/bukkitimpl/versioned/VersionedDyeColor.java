package me.mrletsplay.mrcore.bukkitimpl.versioned;

import static me.mrletsplay.mrcore.bukkitimpl.versioned.NMSRelease.V1_12;
import static me.mrletsplay.mrcore.bukkitimpl.versioned.NMSRelease.V1_13;
import static me.mrletsplay.mrcore.bukkitimpl.versioned.NMSRelease.V1_8;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.DyeColor;

/**
 * An enum containing version-independent representations of {@link DyeColor}s
 * @author MrLetsplay2003
 */
public enum VersionedDyeColor {

	BLACK(of("BLACK", V1_8.versionsTo(NMSRelease.getLatestSupportedRelease()))),
	RED(of("RED", V1_8.versionsTo(NMSRelease.getLatestSupportedRelease()))),
	GREEN(of("GREEN", V1_8.versionsTo(NMSRelease.getLatestSupportedRelease()))),
	BROWN(of("BROWN", V1_8.versionsTo(NMSRelease.getLatestSupportedRelease()))),
	BLUE(of("BLUE", V1_8.versionsTo(NMSRelease.getLatestSupportedRelease()))),
	PURPLE(of("PURPLE", V1_8.versionsTo(NMSRelease.getLatestSupportedRelease()))),
	CYAN(of("CYAN", V1_8.versionsTo(NMSRelease.getLatestSupportedRelease()))),
	LIGHT_GRAY(
			of("SILVER", V1_8.versionsTo(V1_12)),
			of("LIGHT_GRAY", V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GRAY(of("GRAY", V1_8.versionsTo(NMSRelease.getLatestSupportedRelease()))),
	PINK(of("PINK", V1_8.versionsTo(NMSRelease.getLatestSupportedRelease()))),
	LIME(of("LIME", V1_8.versionsTo(NMSRelease.getLatestSupportedRelease()))),
	YELLOW(of("YELLOW", V1_8.versionsTo(NMSRelease.getLatestSupportedRelease()))),
	LIGHT_BLUE(of("LIGHT_BLUE", V1_8.versionsTo(NMSRelease.getLatestSupportedRelease()))),
	MAGENTA(of("MAGENTA", V1_8.versionsTo(NMSRelease.getLatestSupportedRelease()))),
	ORANGE(of("ORANGE", V1_8.versionsTo(NMSRelease.getLatestSupportedRelease()))),
	WHITE(of("WHITE", V1_8.versionsTo(NMSRelease.getLatestSupportedRelease())));
	
	private Map<NMSVersion, String> definitions;
	
	@SafeVarargs
	private VersionedDyeColor(Map.Entry<NMSVersion, String>[]... defs) {
		this.definitions = new HashMap<>();
		for(Map.Entry<NMSVersion, String>[] d : defs) {
			for(Map.Entry<NMSVersion, String> d2 : d) {
				this.definitions.put(d2.getKey(), d2.getValue());
			}
		}
	}
	
	/**
	 * @return The bukkit DyeColor for the current server version using {@link DyeColor#valueOf(String)}
	 */
	public DyeColor getBukkitDyeColor() {
		return DyeColor.valueOf(getCurrentBukkitName());
	}
	
	/**
	 * @param version The version to get the name for
	 * @return The name for that version, null if none
	 */
	public String getBukkitName(NMSVersion version) {
		return definitions.get(version);
	}
	
	/**
	 * @return The {@link DyeColor} enum name for the color for the current server version
	 */
	public String getCurrentBukkitName() {
		return getBukkitName(NMSVersion.getCurrentServerVersion());
	}
	
	@SuppressWarnings("unchecked")
	private static Map.Entry<NMSVersion, String>[] of(String def, NMSVersion... versions) {
		return Arrays.stream(versions)
				.map(v -> new AbstractMap.SimpleEntry<>(v, def))
				.toArray(Map.Entry[]::new);
	}
	
}
