package me.mrletsplay.mrcore.bukkitimpl.versioned;

import static me.mrletsplay.mrcore.bukkitimpl.versioned.NMSRelease.V1_12;
import static me.mrletsplay.mrcore.bukkitimpl.versioned.NMSRelease.V1_13;
import static me.mrletsplay.mrcore.bukkitimpl.versioned.NMSRelease.V1_15;
import static me.mrletsplay.mrcore.bukkitimpl.versioned.NMSRelease.V1_8;
import static me.mrletsplay.mrcore.bukkitimpl.versioned.NMSRelease.V1_9;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

import me.mrletsplay.mrcore.bukkitimpl.versioned.definition.BannerDefinition;
import me.mrletsplay.mrcore.bukkitimpl.versioned.definition.DyeDefinition;
import me.mrletsplay.mrcore.bukkitimpl.versioned.definition.StainedClayDefinition;
import me.mrletsplay.mrcore.bukkitimpl.versioned.definition.StainedGlassDefinition;
import me.mrletsplay.mrcore.bukkitimpl.versioned.definition.StainedGlassPaneDefinition;
import me.mrletsplay.mrcore.bukkitimpl.versioned.definition.TypeDefinition;

/**
 * An enum containing version-independent representations of {@link Material}s
 * @author MrLetsplay2003
 */
public enum VersionedMaterial {

	WHITE_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.WHITE),
			of(new MaterialDefinition("STAINED_CLAY"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("WHITE_TERRACOTTA", 0), V1_13.versionsTo(V1_15))
		),
	ORANGE_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.ORANGE),
			of(new MaterialDefinition("STAINED_CLAY", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("ORANGE_TERRACOTTA", 0), V1_13.versionsTo(V1_15))
		),
	MAGENTA_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.MAGENTA),
			of(new MaterialDefinition("STAINED_CLAY", 2), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MAGENTA_TERRACOTTA", 0), V1_13.versionsTo(V1_15))
		),
	LIGHT_BLUE_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.LIGHT_BLUE),
			of(new MaterialDefinition("STAINED_CLAY", 3), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_BLUE_TERRACOTTA", 0), V1_13.versionsTo(V1_15))
		),
	YELLOW_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.YELLOW),
			of(new MaterialDefinition("STAINED_CLAY", 4), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("YELLOW_TERRACOTTA", 0), V1_13.versionsTo(V1_15))
		),
	LIME_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.LIME),
			of(new MaterialDefinition("STAINED_CLAY", 5), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIME_TERRACOTTA", 0), V1_13.versionsTo(V1_15))
		),
	PINK_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.PINK),
			of(new MaterialDefinition("STAINED_CLAY", 6), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PINK_TERRACOTTA", 0), V1_13.versionsTo(V1_15))
		),
	GRAY_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.GRAY),
			of(new MaterialDefinition("STAINED_CLAY", 7), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GRAY_TERRACOTTA", 0), V1_13.versionsTo(V1_15))
		),
	LIGHT_GRAY_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.LIGHT_GRAY),
			of(new MaterialDefinition("STAINED_CLAY", 8), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_GRAY_TERRACOTTA", 0), V1_13.versionsTo(V1_15))
		),
	CYAN_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.CYAN),
			of(new MaterialDefinition("STAINED_CLAY", 9), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("CYAN_TERRACOTTA", 0), V1_13.versionsTo(V1_15))
		),
	PURPLE_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.PURPLE),
			of(new MaterialDefinition("STAINED_CLAY", 10), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PURPLE_TERRACOTTA", 0), V1_13.versionsTo(V1_15))
		),
	BLUE_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.BLUE),
			of(new MaterialDefinition("STAINED_CLAY", 11), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BLUE_TERRACOTTA", 0), V1_13.versionsTo(V1_15))
		),
	BROWN_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.BROWN),
			of(new MaterialDefinition("STAINED_CLAY", 12), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BROWN_TERRACOTTA", 0), V1_13.versionsTo(V1_15))
		),
	GREEN_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.GREEN),
			of(new MaterialDefinition("STAINED_CLAY", 13), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GREEN_TERRACOTTA", 0), V1_13.versionsTo(V1_15))
		),
	RED_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.RED),
			of(new MaterialDefinition("STAINED_CLAY", 14), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("RED_TERRACOTTA", 0), V1_13.versionsTo(V1_15))
		),
	BLACK_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.BLACK),
			of(new MaterialDefinition("STAINED_CLAY", 15), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BLACK_TERRACOTTA", 0), V1_13.versionsTo(V1_15))
		),
	BLACK_BANNER(
			new BannerDefinition(VersionedDyeColor.BLACK),
			of(new MaterialDefinition("BANNER"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BLACK_BANNER", 0), V1_13.versionsTo(V1_15))
		),
	RED_BANNER(
			new BannerDefinition(VersionedDyeColor.RED),
			of(new MaterialDefinition("BANNER", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("RED_BANNER", 0), V1_13.versionsTo(V1_15))
		),
	GREEN_BANNER(
			new BannerDefinition(VersionedDyeColor.GREEN),
			of(new MaterialDefinition("BANNER", 2), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GREEN_BANNER", 0), V1_13.versionsTo(V1_15))
		),
	BROWN_BANNER(
			new BannerDefinition(VersionedDyeColor.BROWN),
			of(new MaterialDefinition("BANNER", 3), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BROWN_BANNER", 0), V1_13.versionsTo(V1_15))
		),
	BLUE_BANNER(
			new BannerDefinition(VersionedDyeColor.BLUE),
			of(new MaterialDefinition("BANNER", 4), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BLUE_BANNER", 0), V1_13.versionsTo(V1_15))
		),
	PURPLE_BANNER(
			new BannerDefinition(VersionedDyeColor.PURPLE),
			of(new MaterialDefinition("BANNER", 5), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PURPLE_BANNER", 0), V1_13.versionsTo(V1_15))
		),
	CYAN_BANNER(
			new BannerDefinition(VersionedDyeColor.CYAN),
			of(new MaterialDefinition("BANNER", 6), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("CYAN_BANNER", 0), V1_13.versionsTo(V1_15))
		),
	LIGHT_GRAY_BANNER(
			new BannerDefinition(VersionedDyeColor.LIGHT_GRAY),
			of(new MaterialDefinition("BANNER", 7), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_GRAY_BANNER", 0), V1_13.versionsTo(V1_15))
		),
	GRAY_BANNER(
			new BannerDefinition(VersionedDyeColor.GRAY),
			of(new MaterialDefinition("BANNER", 8), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GRAY_BANNER", 0), V1_13.versionsTo(V1_15))
		),
	PINK_BANNER(
			new BannerDefinition(VersionedDyeColor.PINK),
			of(new MaterialDefinition("BANNER", 9), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PINK_BANNER", 0), V1_13.versionsTo(V1_15))
		),
	LIME_BANNER(
			new BannerDefinition(VersionedDyeColor.LIME),
			of(new MaterialDefinition("BANNER", 10), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIME_BANNER", 0), V1_13.versionsTo(V1_15))
		),
	YELLOW_BANNER(
			new BannerDefinition(VersionedDyeColor.YELLOW),
			of(new MaterialDefinition("BANNER", 11), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("YELLOW_BANNER", 0), V1_13.versionsTo(V1_15))
		),
	LIGHT_BLUE_BANNER(
			new BannerDefinition(VersionedDyeColor.LIGHT_BLUE),
			of(new MaterialDefinition("BANNER", 12), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_BLUE_BANNER", 0), V1_13.versionsTo(V1_15))
		),
	MAGENTA_BANNER(
			new BannerDefinition(VersionedDyeColor.MAGENTA),
			of(new MaterialDefinition("BANNER", 13), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MAGENTA_BANNER", 0), V1_13.versionsTo(V1_15))
		),
	ORANGE_BANNER(
			new BannerDefinition(VersionedDyeColor.ORANGE),
			of(new MaterialDefinition("BANNER", 14), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("ORANGE_BANNER", 0), V1_13.versionsTo(V1_15))
		),
	WHITE_BANNER(
			new BannerDefinition(VersionedDyeColor.WHITE),
			of(new MaterialDefinition("BANNER", 15), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("WHITE_BANNER", 0), V1_13.versionsTo(V1_15))
		),
	WHITE_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.WHITE),
			of(new MaterialDefinition("STAINED_GLASS_PANE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("WHITE_STAINED_GLASS_PANE", 0), V1_13.versionsTo(V1_15))
		),
	ORANGE_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.ORANGE),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("ORANGE_STAINED_GLASS_PANE", 0), V1_13.versionsTo(V1_15))
		),
	MAGENTA_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.MAGENTA),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 2), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MAGENTA_STAINED_GLASS_PANE", 0), V1_13.versionsTo(V1_15))
		),
	LIGHT_BLUE_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.LIGHT_BLUE),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 3), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_BLUE_STAINED_GLASS_PANE", 0), V1_13.versionsTo(V1_15))
		),
	YELLOW_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.YELLOW),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 4), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("YELLOW_STAINED_GLASS_PANE", 0), V1_13.versionsTo(V1_15))
		),
	LIME_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.LIME),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 5), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIME_STAINED_GLASS_PANE", 0), V1_13.versionsTo(V1_15))
		),
	PINK_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.PINK),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 6), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PINK_STAINED_GLASS_PANE", 0), V1_13.versionsTo(V1_15))
		),
	GRAY_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.GRAY),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 7), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GRAY_STAINED_GLASS_PANE", 0), V1_13.versionsTo(V1_15))
		),
	LIGHT_GRAY_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.LIGHT_GRAY),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 8), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_GRAY_STAINED_GLASS_PANE", 0), V1_13.versionsTo(V1_15))
		),
	CYAN_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.CYAN),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 9), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("CYAN_STAINED_GLASS_PANE", 0), V1_13.versionsTo(V1_15))
		),
	PURPLE_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.PURPLE),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 10), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PURPLE_STAINED_GLASS_PANE", 0), V1_13.versionsTo(V1_15))
		),
	BLUE_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.BLUE),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 11), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BLUE_STAINED_GLASS_PANE", 0), V1_13.versionsTo(V1_15))
		),
	BROWN_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.BROWN),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 12), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BROWN_STAINED_GLASS_PANE", 0), V1_13.versionsTo(V1_15))
		),
	GREEN_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.GREEN),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 13), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GREEN_STAINED_GLASS_PANE", 0), V1_13.versionsTo(V1_15))
		),
	RED_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.RED),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 14), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("RED_STAINED_GLASS_PANE", 0), V1_13.versionsTo(V1_15))
		),
	BLACK_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.BLACK),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 15), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BLACK_STAINED_GLASS_PANE", 0), V1_13.versionsTo(V1_15))
		),
	WHITE_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.WHITE),
			of(new MaterialDefinition("STAINED_GLASS"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("WHITE_STAINED_GLASS", 0), V1_13.versionsTo(V1_15))
		),
	ORANGE_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.ORANGE),
			of(new MaterialDefinition("STAINED_GLASS", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("ORANGE_STAINED_GLASS", 0), V1_13.versionsTo(V1_15))
		),
	MAGENTA_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.MAGENTA),
			of(new MaterialDefinition("STAINED_GLASS", 2), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MAGENTA_STAINED_GLASS", 0), V1_13.versionsTo(V1_15))
		),
	LIGHT_BLUE_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.LIGHT_BLUE),
			of(new MaterialDefinition("STAINED_GLASS", 3), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_BLUE_STAINED_GLASS", 0), V1_13.versionsTo(V1_15))
		),
	YELLOW_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.YELLOW),
			of(new MaterialDefinition("STAINED_GLASS", 4), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("YELLOW_STAINED_GLASS", 0), V1_13.versionsTo(V1_15))
		),
	LIME_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.LIME),
			of(new MaterialDefinition("STAINED_GLASS", 5), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIME_STAINED_GLASS", 0), V1_13.versionsTo(V1_15))
		),
	PINK_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.PINK),
			of(new MaterialDefinition("STAINED_GLASS", 6), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PINK_STAINED_GLASS", 0), V1_13.versionsTo(V1_15))
		),
	GRAY_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.GRAY),
			of(new MaterialDefinition("STAINED_GLASS", 7), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GRAY_STAINED_GLASS", 0), V1_13.versionsTo(V1_15))
		),
	LIGHT_GRAY_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.LIGHT_GRAY),
			of(new MaterialDefinition("STAINED_GLASS", 8), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_GRAY_STAINED_GLASS", 0), V1_13.versionsTo(V1_15))
		),
	CYAN_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.CYAN),
			of(new MaterialDefinition("STAINED_GLASS", 9), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("CYAN_STAINED_GLASS", 0), V1_13.versionsTo(V1_15))
		),
	PURPLE_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.PURPLE),
			of(new MaterialDefinition("STAINED_GLASS", 10), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PURPLE_STAINED_GLASS", 0), V1_13.versionsTo(V1_15))
		),
	BLUE_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.BLUE),
			of(new MaterialDefinition("STAINED_GLASS", 11), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BLUE_STAINED_GLASS", 0), V1_13.versionsTo(V1_15))
		),
	BROWN_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.BROWN),
			of(new MaterialDefinition("STAINED_GLASS", 12), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BROWN_STAINED_GLASS", 0), V1_13.versionsTo(V1_15))
		),
	GREEN_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.GREEN),
			of(new MaterialDefinition("STAINED_GLASS", 13), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GREEN_STAINED_GLASS", 0), V1_13.versionsTo(V1_15))
		),
	RED_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.RED),
			of(new MaterialDefinition("STAINED_GLASS", 14), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("RED_STAINED_GLASS", 0), V1_13.versionsTo(V1_15))
		),
	BLACK_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.BLACK),
			of(new MaterialDefinition("STAINED_GLASS", 15), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BLACK_STAINED_GLASS", 0), V1_13.versionsTo(V1_15))
		),
	MUSIC_DISC_13(
			of(new MaterialDefinition("GOLD_RECORD"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_13"), V1_13.versionsTo(V1_15))
		),
	MUSIC_DISC_CAT(
			of(new MaterialDefinition("GREEN_RECORD"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_CAT"), V1_13.versionsTo(V1_15))
		),
	MUSIC_DISC_BLOCKS(
			of(new MaterialDefinition("RECORD_3"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_BLOCKS"), V1_13.versionsTo(V1_15))
		),
	MUSIC_DISC_CHIRP(
			of(new MaterialDefinition("RECORD_4"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_CHIRP"), V1_13.versionsTo(V1_15))
		),
	MUSIC_DISC_FAR(
			of(new MaterialDefinition("RECORD_5"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_FAR"), V1_13.versionsTo(V1_15))
		),
	MUSIC_DISC_MALL(
			of(new MaterialDefinition("RECORD_6"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_MALL"), V1_13.versionsTo(V1_15))
		),
	MUSIC_DISC_MELLOHI(
			of(new MaterialDefinition("RECORD_7"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_MELLOHI"), V1_13.versionsTo(V1_15))
		),
	MUSIC_DISC_STAL(
			of(new MaterialDefinition("RECORD_8"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_13"), V1_13.versionsTo(V1_15))
		),
	MUSIC_DISC_STRAD(
			of(new MaterialDefinition("RECORD_9"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_STRAD"), V1_13.versionsTo(V1_15))
		),
	MUSIC_DISC_WARD(
			of(new MaterialDefinition("RECORD_10"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_WARD"), V1_13.versionsTo(V1_15))
		),
	MUSIC_DISC_11(
			of(new MaterialDefinition("RECORD_11"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_11"), V1_13.versionsTo(V1_15))
		),
	MUSIC_DISC_WAIT(
			of(new MaterialDefinition("RECORD_12"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_WAIT"), V1_13.versionsTo(V1_15))
		),
	INK_SACK(
			new DyeDefinition(VersionedDyeColor.BLACK),
			of(new MaterialDefinition("INK_SACK"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("INK_SAC"), V1_13.versionsTo(V1_15))
		),
	ROSE_RED(
			new DyeDefinition(VersionedDyeColor.RED),
			of(new MaterialDefinition("INK_SACK", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("ROSE_RED"), V1_13.versionsTo(V1_15))
		),
	CACTUS_GREEN(
			new DyeDefinition(VersionedDyeColor.GREEN),
			of(new MaterialDefinition("INK_SACK", 2), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("CACTUS_GREEN"), V1_13.versionsTo(V1_15))
		),
	COCOA_BEANS(
			new DyeDefinition(VersionedDyeColor.BROWN),
			of(new MaterialDefinition("INK_SACK", 3), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("COCOA_BEANS"), V1_13.versionsTo(V1_15))
		),
	LAPIS_LAZULI(
			new DyeDefinition(VersionedDyeColor.BLUE),
			of(new MaterialDefinition("INK_SACK", 4), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LAPIS_LAZULI"), V1_13.versionsTo(V1_15))
		),
	PURPLE_DYE(
			new DyeDefinition(VersionedDyeColor.PURPLE),
			of(new MaterialDefinition("INK_SACK", 5), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PURPLE_DYE"), V1_13.versionsTo(V1_15))
		),
	CYAN_DYE(
			new DyeDefinition(VersionedDyeColor.CYAN),
			of(new MaterialDefinition("INK_SACK", 6), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("CYAN_DYE"), V1_13.versionsTo(V1_15))
		),
	LIGHT_GRAY_DYE(
			new DyeDefinition(VersionedDyeColor.LIGHT_GRAY),
			of(new MaterialDefinition("INK_SACK", 7), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_GRAY_DYE"), V1_13.versionsTo(V1_15))
		),
	GRAY_DYE(
			new DyeDefinition(VersionedDyeColor.GRAY),
			of(new MaterialDefinition("INK_SACK", 8), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GRAY_DYE"), V1_13.versionsTo(V1_15))
		),
	PINK_DYE(
			new DyeDefinition(VersionedDyeColor.PINK),
			of(new MaterialDefinition("INK_SACK", 9), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PINK_DYE"), V1_13.versionsTo(V1_15))
		),
	LIME_DYE(
			new DyeDefinition(VersionedDyeColor.LIME),
			of(new MaterialDefinition("INK_SACK", 10), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIME_DYE"), V1_13.versionsTo(V1_15))
		),
	DANDELION_YELLOW(
			new DyeDefinition(VersionedDyeColor.YELLOW),
			of(new MaterialDefinition("INK_SACK", 11), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("DANDELION_YELLOW"), V1_13.versionsTo(V1_15))
		),
	LIGHT_BLUE_DYE(
			new DyeDefinition(VersionedDyeColor.LIGHT_BLUE),
			of(new MaterialDefinition("INK_SACK", 12), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_BLUE_DYE"), V1_13.versionsTo(V1_15))
		),
	MAGENTA_DYE(
			new DyeDefinition(VersionedDyeColor.MAGENTA),
			of(new MaterialDefinition("INK_SACK", 13), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MAGENTA_DYE"), V1_13.versionsTo(V1_15))
		),
	ORANGE_DYE(
			new DyeDefinition(VersionedDyeColor.ORANGE),
			of(new MaterialDefinition("INK_SACK", 14), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("ORANGE_DYE"), V1_13.versionsTo(V1_15))
		),
	BONE_MEAL(
			new DyeDefinition(VersionedDyeColor.WHITE),
			of(new MaterialDefinition("INK_SACK", 15), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BONE_MEAL"), V1_13.versionsTo(V1_15))
		),
	GOLDEN_HELMET(
			of(new MaterialDefinition("GOLD_HELMET"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GOLDEN_HELMET"), V1_13.versionsTo(V1_15))
		),
	GOLDEN_CHESTPLATE(
			of(new MaterialDefinition("GOLD_CHESTPLATE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GOLDEN_CHESTPLATE"), V1_13.versionsTo(V1_15))
		),
	GOLDEN_LEGGINGS(
			of(new MaterialDefinition("GOLD_LEGGINGS"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GOLDEN_LEGGINGS"), V1_13.versionsTo(V1_15))
		),
	GOLDEN_BOOTS(
			of(new MaterialDefinition("GOLD_BOOTS"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GOLDEN_BOOTS"), V1_13.versionsTo(V1_15))
		),
	GOLDEN_SWORD(
			of(new MaterialDefinition("GOLD_SWORD"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GOLDEN_SWORD"), V1_13.versionsTo(V1_15))
		),
	GOLDEN_PICKAXE(
			of(new MaterialDefinition("GOLD_PICKAXE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GOLDEN_PICKAXE"), V1_13.versionsTo(V1_15))
		),
	GOLDEN_AXE(
			of(new MaterialDefinition("GOLD_AXE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GOLDEN_AXE"), V1_13.versionsTo(V1_15))
		),
	GOLDEN_SHOVEL(
			of(new MaterialDefinition("GOLD_SPADE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GOLDEN_SHOVEL"), V1_13.versionsTo(V1_15))
		),
	GOLDEN_HOE(
			of(new MaterialDefinition("GOLD_HOE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GOLDEN_HOE"), V1_13.versionsTo(V1_15))
		),
	WOODEN_SWORD(
			of(new MaterialDefinition("WOOD_SWORD"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("WOODEN_SWORD"), V1_13.versionsTo(V1_15))
		),
	WOODEN_PICKAXE(
			of(new MaterialDefinition("WOOD_PICKAXE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("WOODEN_PICKAXE"), V1_13.versionsTo(V1_15))
		),
	WOODEN_AXE(
			of(new MaterialDefinition("WOOD_AXE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("WOODEN_AXE"), V1_13.versionsTo(V1_15))
		),
	WOODEN_SHOVEL(
			of(new MaterialDefinition("WOOD_SPADE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("WOODEN_SHOVEL"), V1_13.versionsTo(V1_15))
		),
	WOODEN_HOE(
			of(new MaterialDefinition("WOOD_HOE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("WOODEN_HOE"), V1_13.versionsTo(V1_15))
		),
	STONE_SHOVEL(
			of(new MaterialDefinition("STONE_SPADE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("STONE_SHOVEL"), V1_13.versionsTo(V1_15))
		),
	IRON_SHOVEL(
			of(new MaterialDefinition("IRON_SPADE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("IRON_SHOVEL"), V1_13.versionsTo(V1_15))
		),
	DIAMOND_SHOVEL(
			of(new MaterialDefinition("DIAMOND_SPADE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("DIAMOND_SHOVEL"), V1_13.versionsTo(V1_15))
		),
	SKELETON_SKULL(
			of(new MaterialDefinition("SKULL_ITEM", 0), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("SKELETON_SKULL"), V1_13.versionsTo(V1_15))
		),
	WITHER_SKELETON_SKULL(
			of(new MaterialDefinition("SKULL_ITEM", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("WITHER_SKELETON_SKULL"), V1_13.versionsTo(V1_15))
		),
	ZOMBIE_HEAD(
			of(new MaterialDefinition("SKULL_ITEM", 2), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("ZOMBIE_HEAD"), V1_13.versionsTo(V1_15))
		),
	PLAYER_HEAD(
			of(new MaterialDefinition("SKULL_ITEM", 3), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PLAYER_HEAD"), V1_13.versionsTo(V1_15))
		),
	CREEPER_HEAD(
			of(new MaterialDefinition("SKULL_ITEM", 4), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("CREEPER_HEAD"), V1_13.versionsTo(V1_15))
		),
	DRAGON_HEAD(
			of(new MaterialDefinition("SKULL_ITEM", 5), V1_9.versionsTo(V1_12)),
			of(new MaterialDefinition("DRAGON_HEAD"), V1_13.versionsTo(V1_15))
		),
	COMMAND_BLOCK(
			of(new MaterialDefinition("COMMAND"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("COMMAND_BLOCK"), V1_13.versionsTo(V1_15))
		),
	GRASS(
			of(new MaterialDefinition("LONG_GRASS"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GRASS"), V1_13.versionsTo(V1_15))
		),
	GRASS_BLOCK(
			of(new MaterialDefinition("GRASS"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GRASS_BLOCK"), V1_13.versionsTo(V1_15))
		)
	;
	
	private TypeDefinition typeDefinition;
	private Map<NMSVersion, MaterialDefinition> definitions;
	
	@SafeVarargs
	private VersionedMaterial(Map.Entry<NMSVersion, MaterialDefinition>[]... defs) {
		this.definitions = new HashMap<>();
		for(Map.Entry<NMSVersion, MaterialDefinition>[] d : defs) {
			for(Map.Entry<NMSVersion, MaterialDefinition> d2 : d) {
				this.definitions.put(d2.getKey(), d2.getValue());
			}
		}
	}
	
	@SafeVarargs
	private VersionedMaterial(TypeDefinition typeDefinition, Map.Entry<NMSVersion, MaterialDefinition>[]... defs) {
		this.typeDefinition = typeDefinition;
		this.definitions = new HashMap<>();
		for(Map.Entry<NMSVersion, MaterialDefinition>[] d : defs) {
			for(Map.Entry<NMSVersion, MaterialDefinition> d2 : d) {
				this.definitions.put(d2.getKey(), d2.getValue());
			}
		}
	}
	
	/**
	 * @return The {@link TypeDefinition} for this material, can be null
	 */
	public TypeDefinition getTypeDefinition() {
		return typeDefinition;
	}
	
	/**
	 * @param version The version to get the definition for
	 * @return The {@link MaterialDefinition} for that version, null if none / not available for that version
	 */
	public MaterialDefinition getMaterialDefinition(NMSVersion version) {
		if(!definitions.containsKey(version)) throw new UnsupportedVersionException("Unsupported version " + version.name() + " for material " + name());
		return definitions.get(version);
	}
	
	/**
	 * @return The {@link MaterialDefinition} of the material for the current server version
	 */
	public MaterialDefinition getCurrentMaterialDefinition() {
		return getMaterialDefinition(NMSVersion.getCurrentServerVersion());
	}
	
	public boolean isAvailableForCurrentVersion() {
		return isAvailableForVersion(NMSVersion.getCurrentServerVersion());
	}
	
	public boolean isAvailableForVersion(NMSVersion version) {
		return definitions.containsKey(version);
	}
	
	@SuppressWarnings("unchecked")
	private static Map.Entry<NMSVersion, MaterialDefinition>[] of(MaterialDefinition def, NMSVersion... versions) {
		return Arrays.stream(versions)
				.map(v -> new AbstractMap.SimpleEntry<>(v, def))
				.toArray(Map.Entry[]::new);
	}
	
	/**
	 * Finds the corresponding banner material by its color
	 * @param bannerColor The color of the banner
	 * @return The corresponding banner material
	 */
	public static VersionedMaterial getBanner(VersionedDyeColor bannerColor) {
		return Arrays.stream(values())
				.filter(m ->
						m.getTypeDefinition() != null &&
						m.getTypeDefinition().asBanner() != null &&
						m.getTypeDefinition().asBanner().getDyeColor().equals(bannerColor)
					)
				.findFirst().orElse(null);
	}

	/**
	 * Finds the corresponding glass pane material by its color
	 * @param bannerColor The color of the glass pane
	 * @return The corresponding stained glass pane material
	 */
	public static VersionedMaterial getStainedGlassPane(VersionedDyeColor bannerColor) {
		return Arrays.stream(values())
				.filter(m ->
						m.getTypeDefinition() != null &&
						m.getTypeDefinition().asStainedGlassPane() != null &&
						m.getTypeDefinition().asStainedGlassPane().getDyeColor().equals(bannerColor)
					)
				.findFirst().orElse(null);
	}

	/**
	 * Finds the corresponding glass material by its color
	 * @param bannerColor The color of the glass
	 * @return The corresponding stained glass material
	 */
	public static VersionedMaterial getStainedGlass(VersionedDyeColor bannerColor) {
		return Arrays.stream(values())
				.filter(m ->
						m.getTypeDefinition() != null &&
						m.getTypeDefinition().asStainedGlass() != null &&
						m.getTypeDefinition().asStainedGlass().getDyeColor().equals(bannerColor)
					)
				.findFirst().orElse(null);
	}

	/**
	 * Finds the corresponding glass pane material by its color
	 * @param bannerColor The color of the glass pane
	 * @return The corresponding dye material
	 */
	public static VersionedMaterial getDye(VersionedDyeColor bannerColor) {
		return Arrays.stream(values())
				.filter(m ->
						m.getTypeDefinition() != null &&
						m.getTypeDefinition().asDye() != null &&
						m.getTypeDefinition().asDye().getDyeColor().equals(bannerColor)
					)
				.findFirst().orElse(null);
	}

	/**
	 * Finds the corresponding versioned material to its bukkit counterpart
	 * @param bukkitMaterial The bukkit item's material
	 * @param bukkitDamage The bukkit item's damage
	 * @return The versioned material corresponding versioned material
	 */
	public static VersionedMaterial getByBukkitType(Material bukkitMaterial, short bukkitDamage) {
		return Arrays.stream(values())
				.filter(m ->
						m.getCurrentMaterialDefinition().getMaterial().equals(bukkitMaterial) &&
						m.getCurrentMaterialDefinition().getDamage() == bukkitDamage
					)
				.findFirst().orElse(null);
	}
	
}
