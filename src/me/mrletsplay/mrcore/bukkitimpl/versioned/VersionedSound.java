package me.mrletsplay.mrcore.bukkitimpl.versioned;

import static me.mrletsplay.mrcore.bukkitimpl.versioned.NMSRelease.V1_12;
import static me.mrletsplay.mrcore.bukkitimpl.versioned.NMSRelease.V1_13;
import static me.mrletsplay.mrcore.bukkitimpl.versioned.NMSRelease.V1_14;
import static me.mrletsplay.mrcore.bukkitimpl.versioned.NMSRelease.V1_16;
import static me.mrletsplay.mrcore.bukkitimpl.versioned.NMSRelease.V1_8;
import static me.mrletsplay.mrcore.bukkitimpl.versioned.NMSRelease.V1_9;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Sound;

public enum VersionedSound {

	BLOCK_NOTE_BLOCK_HARP(
			of("NOTE_PIANO", V1_8.getVersions()),
			of("BLOCK_NOTE_HARP", V1_9.versionsTo(V1_12)),
			of("BLOCK_NOTE_BLOCK_HARP", V1_13.versionsTo(V1_16))
		),
	BLOCK_NOTE_BLOCK_BASS(
			of("NOTE_BASS_GUITAR", V1_8.getVersions()),
			of("BLOCK_NOTE_BASS", V1_9.versionsTo(V1_12)),
			of("BLOCK_NOTE_BLOCK_BASS", V1_13.versionsTo(V1_16))
		),
	BLOCK_NOTE_BLOCK_BASEDRUM(
			of("NOTE_BASS_DRUM", V1_8.getVersions()),
			of("BLOCK_NOTE_BASEDRUM", V1_9.versionsTo(V1_12)),
			of("BLOCK_NOTE_BLOCK_BASEDRUM", V1_13.versionsTo(V1_16))
		),
	BLOCK_NOTE_BLOCK_SNARE(
			of("NOTE_SNARE_DRUM", V1_8.getVersions()),
			of("BLOCK_NOTE_SNARE", V1_9.versionsTo(V1_12)),
			of("BLOCK_NOTE_BLOCK_SNARE", V1_13.versionsTo(V1_16))
		),
	BLOCK_NOTE_BLOCK_HAT(
			of("NOTE_STICKS", V1_8.getVersions()),
			of("BLOCK_NOTE_HAT", V1_9.versionsTo(V1_12)),
			of("BLOCK_NOTE_BLOCK_HAT", V1_13.versionsTo(V1_16))
		),
	BLOCK_NOTE_BLOCK_PLING(
			of("NOTE_PLING", V1_8.getVersions()),
			of("BLOCK_NOTE_PLING", V1_9.versionsTo(V1_12)),
			of("BLOCK_NOTE_BLOCK_PLING", V1_13.versionsTo(V1_16))
		),
	BLOCK_NOTE_BLOCK_GUITAR(
			of("BLOCK_NOTE_GUITAR", V1_12.getVersions()),
			of("BLOCK_NOTE_BLOCK_GUITAR", V1_13.versionsTo(V1_16))
		),
	BLOCK_NOTE_BLOCK_FLUTE(
			of("BLOCK_NOTE_FLUTE", V1_12.getVersions()),
			of("BLOCK_NOTE_BLOCK_FLUTE", V1_13.versionsTo(V1_16))
		),
	BLOCK_NOTE_BLOCK_BELL(
			of("BLOCK_NOTE_BELL", V1_12.getVersions()),
			of("BLOCK_NOTE_BLOCK_BELL", V1_13.versionsTo(V1_16))
		),
	BLOCK_NOTE_BLOCK_CHIME(
			of("BLOCK_NOTE_CHIME", V1_12.getVersions()),
			of("BLOCK_NOTE_BLOCK_CHIME", V1_13.versionsTo(V1_16))
		),
	BLOCK_NOTE_BLOCK_XYLOPHONE(
			of("BLOCK_NOTE_XYLOPHONE", V1_12.getVersions()),
			of("BLOCK_NOTE_BLOCK_XYLOPHONE", V1_13.versionsTo(V1_16))
		),
	BLOCK_NOTE_BLOCK_IRON_XYLOPHONE(
			of("BLOCK_NOTE_BLOCK_IRON_XYLOPHONE", V1_14.versionsTo(V1_16))
		),
	BLOCK_NOTE_BLOCK_COW_BELL(
			of("BLOCK_NOTE_BLOCK_COW_BELL", V1_14.versionsTo(V1_16))
		),
	BLOCK_NOTE_BLOCK_DIDGERIDOO(
			of("BLOCK_NOTE_BLOCK_DIDGERIDOO", V1_14.versionsTo(V1_16))
		),
	BLOCK_NOTE_BLOCK_BIT(
			of("BLOCK_NOTE_BLOCK_BIT", V1_14.versionsTo(V1_16))
		),
	BLOCK_NOTE_BLOCK_BANJO(
			of("BLOCK_NOTE_BLOCK_BANJO", V1_14.versionsTo(V1_16))
		),
	;
	
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
		if(!definitions.containsKey(version)) throw new UnsupportedVersionException("Unsupported version " + version.name() + " for sound " + name());
		return definitions.get(version);
	}
	
	public String getCurrentBukkitName() {
		return getBukkitName(NMSVersion.getCurrentServerVersion());
	}
	
	public boolean isAvailableForCurrentVersion() {
		return isAvailableForVersion(NMSVersion.getCurrentServerVersion());
	}
	
	public boolean isAvailableForVersion(NMSVersion version) {
		return definitions.containsKey(version);
	}
	
	@SuppressWarnings("unchecked")
	private static Map.Entry<NMSVersion, String>[] of(String def, NMSVersion... versions) {
		return Arrays.stream(versions)
				.map(v -> new AbstractMap.SimpleEntry<>(v, def))
				.toArray(Map.Entry[]::new);
	}
	
}
