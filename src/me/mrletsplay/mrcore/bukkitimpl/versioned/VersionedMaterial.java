package me.mrletsplay.mrcore.bukkitimpl.versioned;

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
			of(new MaterialDefinition("STAINED_CLAY"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("WHITE_TERRACOTTA", 0), NMSVersion.v1_13())
		),
	ORANGE_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.ORANGE),
			of(new MaterialDefinition("STAINED_CLAY", 1), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("ORANGE_TERRACOTTA", 0), NMSVersion.v1_13())
		),
	MAGENTA_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.MAGENTA),
			of(new MaterialDefinition("STAINED_CLAY", 2), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("MAGENTA_TERRACOTTA", 0), NMSVersion.v1_13())
		),
	LIGHT_BLUE_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.LIGHT_BLUE),
			of(new MaterialDefinition("STAINED_CLAY", 3), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("LIGHT_BLUE_TERRACOTTA", 0), NMSVersion.v1_13())
		),
	YELLOW_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.YELLOW),
			of(new MaterialDefinition("STAINED_CLAY", 4), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("YELLOW_TERRACOTTA", 0), NMSVersion.v1_13())
		),
	LIME_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.LIME),
			of(new MaterialDefinition("STAINED_CLAY", 5), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("LIME_TERRACOTTA", 0), NMSVersion.v1_13())
		),
	PINK_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.PINK),
			of(new MaterialDefinition("STAINED_CLAY", 6), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("PINK_TERRACOTTA", 0), NMSVersion.v1_13())
		),
	GRAY_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.GRAY),
			of(new MaterialDefinition("STAINED_CLAY", 7), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("GRAY_TERRACOTTA", 0), NMSVersion.v1_13())
		),
	LIGHT_GRAY_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.LIGHT_GRAY),
			of(new MaterialDefinition("STAINED_CLAY", 8), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("LIGHT_GRAY_TERRACOTTA", 0), NMSVersion.v1_13())
		),
	CYAN_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.CYAN),
			of(new MaterialDefinition("STAINED_CLAY", 9), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("CYAN_TERRACOTTA", 0), NMSVersion.v1_13())
		),
	PURPLE_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.PURPLE),
			of(new MaterialDefinition("STAINED_CLAY", 10), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("PURPLE_TERRACOTTA", 0), NMSVersion.v1_13())
		),
	BLUE_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.BLUE),
			of(new MaterialDefinition("STAINED_CLAY", 11), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("BLUE_TERRACOTTA", 0), NMSVersion.v1_13())
		),
	BROWN_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.BROWN),
			of(new MaterialDefinition("STAINED_CLAY", 12), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("BROWN_TERRACOTTA", 0), NMSVersion.v1_13())
		),
	GREEN_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.GREEN),
			of(new MaterialDefinition("STAINED_CLAY", 13), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("GREEN_TERRACOTTA", 0), NMSVersion.v1_13())
		),
	RED_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.RED),
			of(new MaterialDefinition("STAINED_CLAY", 14), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("RED_TERRACOTTA", 0), NMSVersion.v1_13())
		),
	BLACK_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.BLACK),
			of(new MaterialDefinition("STAINED_CLAY", 15), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("BLACK_TERRACOTTA", 0), NMSVersion.v1_13())
		),
	BLACK_BANNER(
			new BannerDefinition(VersionedDyeColor.BLACK),
			of(new MaterialDefinition("BANNER"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("WHITE_BANNER", 0), NMSVersion.v1_13())
		),
	RED_BANNER(
			new BannerDefinition(VersionedDyeColor.RED),
			of(new MaterialDefinition("BANNER", 1), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("RED_BANNER", 0), NMSVersion.v1_13())
		),
	GREEN_BANNER(
			new BannerDefinition(VersionedDyeColor.GREEN),
			of(new MaterialDefinition("BANNER", 2), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("GREEN_BANNER", 0), NMSVersion.v1_13())
		),
	BROWN_BANNER(
			new BannerDefinition(VersionedDyeColor.BROWN),
			of(new MaterialDefinition("BANNER", 3), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("BROWN_BANNER", 0), NMSVersion.v1_13())
		),
	BLUE_BANNER(
			new BannerDefinition(VersionedDyeColor.BLUE),
			of(new MaterialDefinition("BANNER", 4), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("BLUE_BANNER", 0), NMSVersion.v1_13())
		),
	PURPLE_BANNER(
			new BannerDefinition(VersionedDyeColor.PURPLE),
			of(new MaterialDefinition("BANNER", 5), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("PURPLE_BANNER", 0), NMSVersion.v1_13())
		),
	CYAN_BANNER(
			new BannerDefinition(VersionedDyeColor.CYAN),
			of(new MaterialDefinition("BANNER", 6), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("CYAN_BANNER", 0), NMSVersion.v1_13())
		),
	LIGHT_GRAY_BANNER(
			new BannerDefinition(VersionedDyeColor.LIGHT_GRAY),
			of(new MaterialDefinition("BANNER", 7), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("LIGHT_GRAY_BANNER", 0), NMSVersion.v1_13())
		),
	GRAY_BANNER(
			new BannerDefinition(VersionedDyeColor.GRAY),
			of(new MaterialDefinition("BANNER", 8), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("GRAY_BANNER", 0), NMSVersion.v1_13())
		),
	PINK_BANNER(
			new BannerDefinition(VersionedDyeColor.PINK),
			of(new MaterialDefinition("BANNER", 9), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("PINK_BANNER", 0), NMSVersion.v1_13())
		),
	LIME_BANNER(
			new BannerDefinition(VersionedDyeColor.LIME),
			of(new MaterialDefinition("BANNER", 10), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("LIME_BANNER", 0), NMSVersion.v1_13())
		),
	YELLOW_BANNER(
			new BannerDefinition(VersionedDyeColor.YELLOW),
			of(new MaterialDefinition("BANNER", 11), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("YELLOW_BANNER", 0), NMSVersion.v1_13())
		),
	LIGHT_BLUE_BANNER(
			new BannerDefinition(VersionedDyeColor.LIGHT_BLUE),
			of(new MaterialDefinition("BANNER", 12), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("LIGHT_BLUE_BANNER", 0), NMSVersion.v1_13())
		),
	MAGENTA_BANNER(
			new BannerDefinition(VersionedDyeColor.MAGENTA),
			of(new MaterialDefinition("BANNER", 13), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("MAGENTA_BANNER", 0), NMSVersion.v1_13())
		),
	ORANGE_BANNER(
			new BannerDefinition(VersionedDyeColor.ORANGE),
			of(new MaterialDefinition("BANNER", 14), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("ORANGE_BANNER", 0), NMSVersion.v1_13())
		),
	WHITE_BANNER(
			new BannerDefinition(VersionedDyeColor.WHITE),
			of(new MaterialDefinition("BANNER", 15), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("WHITE_BANNER", 0), NMSVersion.v1_13())
		),
	WHITE_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.WHITE),
			of(new MaterialDefinition("STAINED_GLASS_PANE"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("WHITE_STAINED_GLASS_PANE", 0), NMSVersion.v1_13())
		),
	ORANGE_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.ORANGE),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 1), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("ORANGE_STAINED_GLASS_PANE", 0), NMSVersion.v1_13())
		),
	MAGENTA_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.MAGENTA),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 2), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("MAGENTA_STAINED_GLASS_PANE", 0), NMSVersion.v1_13())
		),
	LIGHT_BLUE_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.LIGHT_BLUE),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 3), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("LIGHT_BLUE_STAINED_GLASS_PANE", 0), NMSVersion.v1_13())
		),
	YELLOW_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.YELLOW),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 4), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("YELLOW_STAINED_GLASS_PANE", 0), NMSVersion.v1_13())
		),
	LIME_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.LIME),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 5), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("LIME_STAINED_GLASS_PANE", 0), NMSVersion.v1_13())
		),
	PINK_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.PINK),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 6), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("PINK_STAINED_GLASS_PANE", 0), NMSVersion.v1_13())
		),
	GRAY_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.GRAY),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 7), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("GRAY_STAINED_GLASS_PANE", 0), NMSVersion.v1_13())
		),
	LIGHT_GRAY_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.LIGHT_GRAY),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 8), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("LIGHT_GRAY_STAINED_GLASS_PANE", 0), NMSVersion.v1_13())
		),
	CYAN_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.CYAN),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 9), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("CYAN_STAINED_GLASS_PANE", 0), NMSVersion.v1_13())
		),
	PURPLE_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.PURPLE),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 10), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("PURPLE_STAINED_GLASS_PANE", 0), NMSVersion.v1_13())
		),
	BLUE_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.BLUE),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 11), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("BLUE_STAINED_GLASS_PANE", 0), NMSVersion.v1_13())
		),
	BROWN_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.BROWN),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 12), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("BROWN_STAINED_GLASS_PANE", 0), NMSVersion.v1_13())
		),
	GREEN_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.GREEN),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 13), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("GREEN_STAINED_GLASS_PANE", 0), NMSVersion.v1_13())
		),
	RED_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.RED),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 14), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("RED_STAINED_GLASS_PANE", 0), NMSVersion.v1_13())
		),
	BLACK_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.BLACK),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 15), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("BLACK_STAINED_GLASS_PANE", 0), NMSVersion.v1_13())
		),
	WHITE_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.WHITE),
			of(new MaterialDefinition("STAINED_GLASS"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("WHITE_STAINED_GLASS", 0), NMSVersion.v1_13())
		),
	ORANGE_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.ORANGE),
			of(new MaterialDefinition("STAINED_GLASS", 1), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("ORANGE_STAINED_GLASS", 0), NMSVersion.v1_13())
		),
	MAGENTA_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.MAGENTA),
			of(new MaterialDefinition("STAINED_GLASS", 2), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("MAGENTA_STAINED_GLASS", 0), NMSVersion.v1_13())
		),
	LIGHT_BLUE_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.LIGHT_BLUE),
			of(new MaterialDefinition("STAINED_GLASS", 3), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("LIGHT_BLUE_STAINED_GLASS", 0), NMSVersion.v1_13())
		),
	YELLOW_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.YELLOW),
			of(new MaterialDefinition("STAINED_GLASS", 4), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("YELLOW_STAINED_GLASS", 0), NMSVersion.v1_13())
		),
	LIME_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.LIME),
			of(new MaterialDefinition("STAINED_GLASS", 5), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("LIME_STAINED_GLASS", 0), NMSVersion.v1_13())
		),
	PINK_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.PINK),
			of(new MaterialDefinition("STAINED_GLASS", 6), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("PINK_STAINED_GLASS", 0), NMSVersion.v1_13())
		),
	GRAY_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.GRAY),
			of(new MaterialDefinition("STAINED_GLASS", 7), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("GRAY_STAINED_GLASS", 0), NMSVersion.v1_13())
		),
	LIGHT_GRAY_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.LIGHT_GRAY),
			of(new MaterialDefinition("STAINED_GLASS", 8), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("LIGHT_GRAY_STAINED_GLASS", 0), NMSVersion.v1_13())
		),
	CYAN_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.CYAN),
			of(new MaterialDefinition("STAINED_GLASS", 9), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("CYAN_STAINED_GLASS", 0), NMSVersion.v1_13())
		),
	PURPLE_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.PURPLE),
			of(new MaterialDefinition("STAINED_GLASS", 10), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("PURPLE_STAINED_GLASS", 0), NMSVersion.v1_13())
		),
	BLUE_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.BLUE),
			of(new MaterialDefinition("STAINED_GLASS", 11), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("BLUE_STAINED_GLASS", 0), NMSVersion.v1_13())
		),
	BROWN_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.BROWN),
			of(new MaterialDefinition("STAINED_GLASS", 12), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("BROWN_STAINED_GLASS", 0), NMSVersion.v1_13())
		),
	GREEN_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.GREEN),
			of(new MaterialDefinition("STAINED_GLASS", 13), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("GREEN_STAINED_GLASS", 0), NMSVersion.v1_13())
		),
	RED_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.RED),
			of(new MaterialDefinition("STAINED_GLASS", 14), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("RED_STAINED_GLASS", 0), NMSVersion.v1_13())
		),
	BLACK_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.BLACK),
			of(new MaterialDefinition("STAINED_GLASS", 15), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("BLACK_STAINED_GLASS", 0), NMSVersion.v1_13())
		),
	MUSIC_DISC_13(
			of(new MaterialDefinition("GOLD_RECORD"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("MUSIC_DISC_13"), NMSVersion.v1_13())
		),
	MUSIC_DISC_CAT(
			of(new MaterialDefinition("GREEN_RECORD"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("MUSIC_DISC_CAT"), NMSVersion.v1_13())
		),
	MUSIC_DISC_BLOCKS(
			of(new MaterialDefinition("RECORD_3"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("MUSIC_DISC_BLOCKS"), NMSVersion.v1_13())
		),
	MUSIC_DISC_CHIRP(
			of(new MaterialDefinition("RECORD_4"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("MUSIC_DISC_CHIRP"), NMSVersion.v1_13())
		),
	MUSIC_DISC_FAR(
			of(new MaterialDefinition("RECORD_5"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("MUSIC_DISC_FAR"), NMSVersion.v1_13())
		),
	MUSIC_DISC_MALL(
			of(new MaterialDefinition("RECORD_6"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("MUSIC_DISC_MALL"), NMSVersion.v1_13())
		),
	MUSIC_DISC_MELLOHI(
			of(new MaterialDefinition("RECORD_7"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("MUSIC_DISC_MELLOHI"), NMSVersion.v1_13())
		),
	MUSIC_DISC_STAL(
			of(new MaterialDefinition("RECORD_8"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("MUSIC_DISC_13"), NMSVersion.v1_13())
		),
	MUSIC_DISC_STRAD(
			of(new MaterialDefinition("RECORD_9"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("MUSIC_DISC_STRAD"), NMSVersion.v1_13())
		),
	MUSIC_DISC_WARD(
			of(new MaterialDefinition("RECORD_10"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("MUSIC_DISC_WARD"), NMSVersion.v1_13())
		),
	MUSIC_DISC_11(
			of(new MaterialDefinition("RECORD_11"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("MUSIC_DISC_11"), NMSVersion.v1_13())
		),
	MUSIC_DISC_WAIT(
			of(new MaterialDefinition("RECORD_12"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("MUSIC_DISC_WAIT"), NMSVersion.v1_13())
		),
	INK_SACK(
			new DyeDefinition(VersionedDyeColor.BLACK),
			of(new MaterialDefinition("INK_SACK"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("INK_SAC"), NMSVersion.v1_13())
		),
	ROSE_RED(
			new DyeDefinition(VersionedDyeColor.RED),
			of(new MaterialDefinition("INK_SACK", 1), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("ROSE_RED"), NMSVersion.v1_13())
		),
	CACTUS_GREEN(
			new DyeDefinition(VersionedDyeColor.GREEN),
			of(new MaterialDefinition("INK_SACK", 2), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("CACTUS_GREEN"), NMSVersion.v1_13())
		),
	COCOA_BEANS(
			new DyeDefinition(VersionedDyeColor.BROWN),
			of(new MaterialDefinition("INK_SACK", 3), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("COCOA_BEANS"), NMSVersion.v1_13())
		),
	LAPIS_LAZULI(
			new DyeDefinition(VersionedDyeColor.BLUE),
			of(new MaterialDefinition("INK_SACK", 4), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("LAPIS_LAZULI"), NMSVersion.v1_13())
		),
	PURPLE_DYE(
			new DyeDefinition(VersionedDyeColor.PURPLE),
			of(new MaterialDefinition("INK_SACK", 5), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("PURPLE_DYE"), NMSVersion.v1_13())
		),
	CYAN_DYE(
			new DyeDefinition(VersionedDyeColor.CYAN),
			of(new MaterialDefinition("INK_SACK", 6), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("CYAN_DYE"), NMSVersion.v1_13())
		),
	LIGHT_GRAY_DYE(
			new DyeDefinition(VersionedDyeColor.LIGHT_GRAY),
			of(new MaterialDefinition("INK_SACK", 7), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("LIGHT_GRAY_DYE"), NMSVersion.v1_13())
		),
	GRAY_DYE(
			new DyeDefinition(VersionedDyeColor.GRAY),
			of(new MaterialDefinition("INK_SACK", 8), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("GRAY_DYE"), NMSVersion.v1_13())
		),
	PINK_DYE(
			new DyeDefinition(VersionedDyeColor.PINK),
			of(new MaterialDefinition("INK_SACK", 9), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("PINK_DYE"), NMSVersion.v1_13())
		),
	LIME_DYE(
			new DyeDefinition(VersionedDyeColor.LIME),
			of(new MaterialDefinition("INK_SACK", 10), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("LIME_DYE"), NMSVersion.v1_13())
		),
	DANDELION_YELLOW(
			new DyeDefinition(VersionedDyeColor.YELLOW),
			of(new MaterialDefinition("INK_SACK", 11), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("DANDELION_YELLOW"), NMSVersion.v1_13())
		),
	LIGHT_BLUE_DYE(
			new DyeDefinition(VersionedDyeColor.LIGHT_BLUE),
			of(new MaterialDefinition("INK_SACK", 12), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("LIGHT_BLUE_DYE"), NMSVersion.v1_13())
		),
	MAGENTA_DYE(
			new DyeDefinition(VersionedDyeColor.MAGENTA),
			of(new MaterialDefinition("INK_SACK", 13), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("MAGENTA_DYE"), NMSVersion.v1_13())
		),
	ORANGE_DYE(
			new DyeDefinition(VersionedDyeColor.ORANGE),
			of(new MaterialDefinition("INK_SACK", 14), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("ORANGE_DYE"), NMSVersion.v1_13())
		),
	BONE_MEAL(
			new DyeDefinition(VersionedDyeColor.WHITE),
			of(new MaterialDefinition("INK_SACK", 15), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("BONE_MEAL"), NMSVersion.v1_13())
		),
	GOLDEN_HELMET(
			of(new MaterialDefinition("GOLD_HELMET"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("GOLDEN_HELMET"), NMSVersion.v1_13())
		),
	GOLDEN_CHESTPLATE(
			of(new MaterialDefinition("GOLD_CHESTPLATE"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("GOLDEN_CHESTPLATE"), NMSVersion.v1_13())
		),
	GOLDEN_LEGGINGS(
			of(new MaterialDefinition("GOLD_LEGGINGS"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("GOLDEN_LEGGINGS"), NMSVersion.v1_13())
		),
	GOLDEN_BOOTS(
			of(new MaterialDefinition("GOLD_BOOTS"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("GOLDEN_BOOTS"), NMSVersion.v1_13())
		),
	GOLDEN_SWORD(
			of(new MaterialDefinition("GOLD_SWORD"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("GOLDEN_SWORD"), NMSVersion.v1_13())
		),
	GOLDEN_PICKAXE(
			of(new MaterialDefinition("GOLD_PICKAXE"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("GOLDEN_PICKAXE"), NMSVersion.v1_13())
		),
	GOLDEN_AXE(
			of(new MaterialDefinition("GOLD_AXE"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("GOLDEN_AXE"), NMSVersion.v1_13())
		),
	GOLDEN_SHOVEL(
			of(new MaterialDefinition("GOLD_SPADE"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("GOLDEN_SHOVEL"), NMSVersion.v1_13())
		),
	GOLDEN_HOE(
			of(new MaterialDefinition("GOLD_HOE"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("GOLDEN_HOE"), NMSVersion.v1_13())
		),
	WOODEN_SWORD(
			of(new MaterialDefinition("WOOD_SWORD"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("WOODEN_SWORD"), NMSVersion.v1_13())
		),
	WOODEN_PICKAXE(
			of(new MaterialDefinition("WOOD_PICKAXE"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("WOODEN_PICKAXE"), NMSVersion.v1_13())
		),
	WOODEN_AXE(
			of(new MaterialDefinition("WOOD_AXE"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("WOODEN_AXE"), NMSVersion.v1_13())
		),
	WOODEN_SHOVEL(
			of(new MaterialDefinition("WOOD_SPADE"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("WOODEN_SHOVEL"), NMSVersion.v1_13())
		),
	WOODEN_HOE(
			of(new MaterialDefinition("WOOD_HOE"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("WOODEN_HOE"), NMSVersion.v1_13())
		),
	STONE_SHOVEL(
			of(new MaterialDefinition("STONE_SPADE"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("STONE_SHOVEL"), NMSVersion.v1_13())
		),
	IRON_SHOVEL(
			of(new MaterialDefinition("IRON_SPADE"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("IRON_SHOVEL"), NMSVersion.v1_13())
		),
	DIAMOND_SHOVEL(
			of(new MaterialDefinition("DIAMOND_SPADE"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("DIAMOND_SHOVEL"), NMSVersion.v1_13())
		),
	WITHER_SKELETON_SKULL(
			of(new MaterialDefinition("SKULL_ITEM", 1), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("WITHER_SKELETON_SKULL"), NMSVersion.v1_13())
		),
	ZOMBIE_HEAD(
			of(new MaterialDefinition("SKULL_ITEM", 2), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("ZOMBIE_HEAD"), NMSVersion.v1_13())
		),
	PLAYER_HEAD(
			of(new MaterialDefinition("SKULL_ITEM", 3), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("PLAYER_HEAD"), NMSVersion.v1_13())
		),
	CREEPER_HEAD(
			of(new MaterialDefinition("SKULL_ITEM", 4), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("CREEPER_HEAD"), NMSVersion.v1_13())
		),
	DRAGON_HEAD(
			of(new MaterialDefinition("SKULL_ITEM", 5), NMSVersion.v1_9to1_12()),
			of(new MaterialDefinition("DRAGON_HEAD"), NMSVersion.v1_13())
		),
	COMMAND_BLOCK(
			of(new MaterialDefinition("COMMAND"), NMSVersion.v1_8to1_12()),
			of(new MaterialDefinition("COMMAND_BLOCK"), NMSVersion.v1_13())
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
		return getMaterialDefinition(version) != null;
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
	 * @return The corresponding glass pane material
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
	 * @return The corresponding glass material
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
	 * @return The corresponding glass pane material
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
	 * @return
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
