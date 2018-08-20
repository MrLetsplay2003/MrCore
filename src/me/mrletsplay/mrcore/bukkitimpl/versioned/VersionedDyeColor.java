package me.mrletsplay.mrcore.bukkitimpl.versioned;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.DyeColor;

public enum VersionedDyeColor {

	BLACK(of("BLACK", NMSVersion.v1_8to1_13())),
	RED(of("RED", NMSVersion.v1_8to1_13())),
	GREEN(of("GREEN", NMSVersion.v1_8to1_13())),
	BROWN(of("BROWN", NMSVersion.v1_8to1_13())),
	BLUE(of("BLUE", NMSVersion.v1_8to1_13())),
	PURPLE(of("PURPLE", NMSVersion.v1_8to1_13())),
	CYAN(of("CYAN", NMSVersion.v1_8to1_13())),
	LIGHT_GRAY(
			of("SILVER", NMSVersion.v1_8to1_12()),
			of("LIGHT_GRAY", NMSVersion.V1_13_R1)
		),
	GRAY(of("GRAY", NMSVersion.v1_8to1_13())),
	PINK(of("PINK", NMSVersion.v1_8to1_13())),
	LIME(of("LIME", NMSVersion.v1_8to1_13())),
	YELLOW(of("YELLOW", NMSVersion.v1_8to1_13())),
	LIGHT_BLUE(of("LIGHT_BLUE", NMSVersion.v1_8to1_13())),
	MAGENTA(of("MAGENTA", NMSVersion.v1_8to1_13())),
	ORANGE(of("ORANGE", NMSVersion.v1_8to1_13())),
	WHITE(of("WHITE", NMSVersion.v1_8to1_13()));
	
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
	
	public DyeColor getBukkitDyeColor() {
		return DyeColor.valueOf(getCurrentBukkitName());
	}
	
	public String getBukkitName(NMSVersion version) {
		return definitions.get(version);
	}
	
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
