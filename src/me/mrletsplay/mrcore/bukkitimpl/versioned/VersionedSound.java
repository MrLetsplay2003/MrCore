package me.mrletsplay.mrcore.bukkitimpl.versioned;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Sound;

public enum VersionedSound {

	BLOCK_NOTE_BLOCK_HARP(
			of("NOTE_PIANO", NMSVersion.v1_8()),
			of("BLOCK_NOTE_HARP", NMSVersion.range(NMSVersion.V1_9_R1, NMSVersion.V1_12_R1)),
			of("BLOCK_NOTE_BLOCK_HARP", NMSVersion.v1_13())
		),
	BLOCK_NOTE_BLOCK_BASS(
			of("NOTE_BASS_GUITAR", NMSVersion.v1_8()),
			of("BLOCK_NOTE_BASS", NMSVersion.range(NMSVersion.V1_9_R1, NMSVersion.V1_12_R1)),
			of("BLOCK_NOTE_BLOCK_BASS", NMSVersion.v1_13())
		),
	BLOCK_NOTE_BLOCK_BASEDRUM(
			of("NOTE_BASS_DRUM", NMSVersion.v1_8()),
			of("BLOCK_NOTE_BASEDRUM", NMSVersion.range(NMSVersion.V1_9_R1, NMSVersion.V1_12_R1)),
			of("BLOCK_NOTE_BLOCK_BASEDRUM", NMSVersion.v1_13())
		),
	BLOCK_NOTE_BLOCK_SNARE(
			of("NOTE_SNARE_DRUM", NMSVersion.v1_8()),
			of("BLOCK_NOTE_SNARE", NMSVersion.range(NMSVersion.V1_9_R1, NMSVersion.V1_12_R1)),
			of("BLOCK_NOTE_BLOCK_SNARE", NMSVersion.v1_13())
		),
	BLOCK_NOTE_BLOCK_HAT(
			of("NOTE_STICKS", NMSVersion.v1_8()),
			of("BLOCK_NOTE_HAT", NMSVersion.range(NMSVersion.V1_9_R1, NMSVersion.V1_12_R1)),
			of("BLOCK_NOTE_BLOCK_HAT", NMSVersion.v1_13())
		),
	BLOCK_NOTE_BLOCK_PLING(
			of("NOTE_PLING", NMSVersion.v1_8()),
			of("BLOCK_NOTE_PLING", NMSVersion.range(NMSVersion.V1_9_R1, NMSVersion.V1_12_R1)),
			of("BLOCK_NOTE_BLOCK_PLING", NMSVersion.v1_13())
		),
	BLOCK_NOTE_BLOCK_GUITAR(
			of("BLOCK_NOTE_GUITAR", NMSVersion.V1_12_R1),
			of("BLOCK_NOTE_BLOCK_GUITAR", NMSVersion.v1_13())
		),
	BLOCK_NOTE_BLOCK_FLUTE(
			of("BLOCK_NOTE_FLUTE", NMSVersion.V1_12_R1),
			of("BLOCK_NOTE_BLOCK_FLUTE", NMSVersion.v1_13())
		),
	BLOCK_NOTE_BLOCK_BELL(
			of("BLOCK_NOTE_BELL", NMSVersion.V1_12_R1),
			of("BLOCK_NOTE_BLOCK_BELL", NMSVersion.v1_13())
		),
	BLOCK_NOTE_BLOCK_CHIME(
			of("BLOCK_NOTE_CHIME", NMSVersion.V1_12_R1),
			of("BLOCK_NOTE_BLOCK_CHIME", NMSVersion.v1_13())
		),
	BLOCK_NOTE_BLOCK_XYLOPHONE(
			of("BLOCK_NOTE_XYLOPHONE", NMSVersion.V1_12_R1),
			of("BLOCK_NOTE_BLOCK_XYLOPHONE", NMSVersion.v1_13())
		);
	
	private Map<NMSVersion, String> definitions;
	
	@SafeVarargs
	private VersionedSound(Map.Entry<NMSVersion, String>[]... defs) {
		this.definitions = new HashMap<>();
		for(Map.Entry<NMSVersion, String>[] d : defs) {
			for(Map.Entry<NMSVersion, String> d2 : d) {
				this.definitions.put(d2.getKey(), d2.getValue());
			}
		}
	}
	
	public Sound getBukkitSound() {
		return Sound.valueOf(getCurrentBukkitName());
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
