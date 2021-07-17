package me.mrletsplay.mrcore.bukkitimpl.versioned;

import static me.mrletsplay.mrcore.bukkitimpl.versioned.NMSRelease.V1_12;
import static me.mrletsplay.mrcore.bukkitimpl.versioned.NMSRelease.V1_13;
import static me.mrletsplay.mrcore.bukkitimpl.versioned.NMSRelease.V1_14;
import static me.mrletsplay.mrcore.bukkitimpl.versioned.NMSRelease.V1_8;
import static me.mrletsplay.mrcore.bukkitimpl.versioned.NMSRelease.V1_9;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

import me.mrletsplay.mrcore.bukkitimpl.versioned.definition.BannerDefinition;
import me.mrletsplay.mrcore.bukkitimpl.versioned.definition.DyeDefinition;
import me.mrletsplay.mrcore.bukkitimpl.versioned.definition.LeavesDefinition;
import me.mrletsplay.mrcore.bukkitimpl.versioned.definition.StainedClayDefinition;
import me.mrletsplay.mrcore.bukkitimpl.versioned.definition.StainedGlassDefinition;
import me.mrletsplay.mrcore.bukkitimpl.versioned.definition.StainedGlassPaneDefinition;
import me.mrletsplay.mrcore.bukkitimpl.versioned.definition.TypeDefinition;
import me.mrletsplay.mrcore.bukkitimpl.versioned.definition.WoodType;
import me.mrletsplay.mrcore.bukkitimpl.versioned.definition.WoodenLogDefinition;
import me.mrletsplay.mrcore.bukkitimpl.versioned.definition.WoodenPlanksDefinition;
import me.mrletsplay.mrcore.bukkitimpl.versioned.definition.WoodenSignDefinition;
import me.mrletsplay.mrcore.bukkitimpl.versioned.definition.WoolDefinition;

/**
 * An enum containing version-independent representations of {@link Material}s
 * @author MrLetsplay2003
 */
public enum VersionedMaterial {

	WHITE_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.WHITE),
			of(new MaterialDefinition("STAINED_CLAY"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("WHITE_TERRACOTTA"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	ORANGE_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.ORANGE),
			of(new MaterialDefinition("STAINED_CLAY", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("ORANGE_TERRACOTTA"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	MAGENTA_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.MAGENTA),
			of(new MaterialDefinition("STAINED_CLAY", 2), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MAGENTA_TERRACOTTA"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	LIGHT_BLUE_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.LIGHT_BLUE),
			of(new MaterialDefinition("STAINED_CLAY", 3), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_BLUE_TERRACOTTA"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	YELLOW_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.YELLOW),
			of(new MaterialDefinition("STAINED_CLAY", 4), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("YELLOW_TERRACOTTA"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	LIME_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.LIME),
			of(new MaterialDefinition("STAINED_CLAY", 5), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIME_TERRACOTTA"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	PINK_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.PINK),
			of(new MaterialDefinition("STAINED_CLAY", 6), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PINK_TERRACOTTA"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GRAY_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.GRAY),
			of(new MaterialDefinition("STAINED_CLAY", 7), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GRAY_TERRACOTTA"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	LIGHT_GRAY_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.LIGHT_GRAY),
			of(new MaterialDefinition("STAINED_CLAY", 8), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_GRAY_TERRACOTTA"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	CYAN_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.CYAN),
			of(new MaterialDefinition("STAINED_CLAY", 9), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("CYAN_TERRACOTTA"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	PURPLE_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.PURPLE),
			of(new MaterialDefinition("STAINED_CLAY", 10), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PURPLE_TERRACOTTA"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BLUE_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.BLUE),
			of(new MaterialDefinition("STAINED_CLAY", 11), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BLUE_TERRACOTTA"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BROWN_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.BROWN),
			of(new MaterialDefinition("STAINED_CLAY", 12), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BROWN_TERRACOTTA"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GREEN_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.GREEN),
			of(new MaterialDefinition("STAINED_CLAY", 13), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GREEN_TERRACOTTA"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	RED_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.RED),
			of(new MaterialDefinition("STAINED_CLAY", 14), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("RED_TERRACOTTA"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BLACK_STAINED_CLAY(
			new StainedClayDefinition(VersionedDyeColor.BLACK),
			of(new MaterialDefinition("STAINED_CLAY", 15), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BLACK_TERRACOTTA"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BLACK_BANNER(
			new BannerDefinition(VersionedDyeColor.BLACK),
			of(new MaterialDefinition("BANNER"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BLACK_BANNER"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	RED_BANNER(
			new BannerDefinition(VersionedDyeColor.RED),
			of(new MaterialDefinition("BANNER", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("RED_BANNER"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GREEN_BANNER(
			new BannerDefinition(VersionedDyeColor.GREEN),
			of(new MaterialDefinition("BANNER", 2), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GREEN_BANNER"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BROWN_BANNER(
			new BannerDefinition(VersionedDyeColor.BROWN),
			of(new MaterialDefinition("BANNER", 3), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BROWN_BANNER"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BLUE_BANNER(
			new BannerDefinition(VersionedDyeColor.BLUE),
			of(new MaterialDefinition("BANNER", 4), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BLUE_BANNER"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	PURPLE_BANNER(
			new BannerDefinition(VersionedDyeColor.PURPLE),
			of(new MaterialDefinition("BANNER", 5), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PURPLE_BANNER"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	CYAN_BANNER(
			new BannerDefinition(VersionedDyeColor.CYAN),
			of(new MaterialDefinition("BANNER", 6), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("CYAN_BANNER"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	LIGHT_GRAY_BANNER(
			new BannerDefinition(VersionedDyeColor.LIGHT_GRAY),
			of(new MaterialDefinition("BANNER", 7), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_GRAY_BANNER"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GRAY_BANNER(
			new BannerDefinition(VersionedDyeColor.GRAY),
			of(new MaterialDefinition("BANNER", 8), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GRAY_BANNER"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	PINK_BANNER(
			new BannerDefinition(VersionedDyeColor.PINK),
			of(new MaterialDefinition("BANNER", 9), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PINK_BANNER"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	LIME_BANNER(
			new BannerDefinition(VersionedDyeColor.LIME),
			of(new MaterialDefinition("BANNER", 10), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIME_BANNER"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	YELLOW_BANNER(
			new BannerDefinition(VersionedDyeColor.YELLOW),
			of(new MaterialDefinition("BANNER", 11), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("YELLOW_BANNER"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	LIGHT_BLUE_BANNER(
			new BannerDefinition(VersionedDyeColor.LIGHT_BLUE),
			of(new MaterialDefinition("BANNER", 12), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_BLUE_BANNER"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	MAGENTA_BANNER(
			new BannerDefinition(VersionedDyeColor.MAGENTA),
			of(new MaterialDefinition("BANNER", 13), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MAGENTA_BANNER"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	ORANGE_BANNER(
			new BannerDefinition(VersionedDyeColor.ORANGE),
			of(new MaterialDefinition("BANNER", 14), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("ORANGE_BANNER"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	WHITE_BANNER(
			new BannerDefinition(VersionedDyeColor.WHITE),
			of(new MaterialDefinition("BANNER", 15), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("WHITE_BANNER"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	WHITE_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.WHITE),
			of(new MaterialDefinition("STAINED_GLASS_PANE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("WHITE_STAINED_GLASS_PANE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	ORANGE_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.ORANGE),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("ORANGE_STAINED_GLASS_PANE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	MAGENTA_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.MAGENTA),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 2), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MAGENTA_STAINED_GLASS_PANE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	LIGHT_BLUE_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.LIGHT_BLUE),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 3), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_BLUE_STAINED_GLASS_PANE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	YELLOW_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.YELLOW),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 4), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("YELLOW_STAINED_GLASS_PANE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	LIME_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.LIME),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 5), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIME_STAINED_GLASS_PANE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	PINK_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.PINK),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 6), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PINK_STAINED_GLASS_PANE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GRAY_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.GRAY),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 7), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GRAY_STAINED_GLASS_PANE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	LIGHT_GRAY_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.LIGHT_GRAY),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 8), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_GRAY_STAINED_GLASS_PANE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	CYAN_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.CYAN),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 9), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("CYAN_STAINED_GLASS_PANE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	PURPLE_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.PURPLE),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 10), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PURPLE_STAINED_GLASS_PANE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BLUE_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.BLUE),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 11), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BLUE_STAINED_GLASS_PANE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BROWN_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.BROWN),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 12), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BROWN_STAINED_GLASS_PANE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GREEN_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.GREEN),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 13), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GREEN_STAINED_GLASS_PANE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	RED_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.RED),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 14), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("RED_STAINED_GLASS_PANE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BLACK_STAINED_GLASS_PANE(
			new StainedGlassPaneDefinition(VersionedDyeColor.BLACK),
			of(new MaterialDefinition("STAINED_GLASS_PANE", 15), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BLACK_STAINED_GLASS_PANE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	WHITE_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.WHITE),
			of(new MaterialDefinition("STAINED_GLASS"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("WHITE_STAINED_GLASS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	ORANGE_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.ORANGE),
			of(new MaterialDefinition("STAINED_GLASS", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("ORANGE_STAINED_GLASS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	MAGENTA_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.MAGENTA),
			of(new MaterialDefinition("STAINED_GLASS", 2), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MAGENTA_STAINED_GLASS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	LIGHT_BLUE_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.LIGHT_BLUE),
			of(new MaterialDefinition("STAINED_GLASS", 3), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_BLUE_STAINED_GLASS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	YELLOW_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.YELLOW),
			of(new MaterialDefinition("STAINED_GLASS", 4), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("YELLOW_STAINED_GLASS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	LIME_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.LIME),
			of(new MaterialDefinition("STAINED_GLASS", 5), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIME_STAINED_GLASS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	PINK_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.PINK),
			of(new MaterialDefinition("STAINED_GLASS", 6), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PINK_STAINED_GLASS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GRAY_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.GRAY),
			of(new MaterialDefinition("STAINED_GLASS", 7), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GRAY_STAINED_GLASS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	LIGHT_GRAY_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.LIGHT_GRAY),
			of(new MaterialDefinition("STAINED_GLASS", 8), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_GRAY_STAINED_GLASS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	CYAN_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.CYAN),
			of(new MaterialDefinition("STAINED_GLASS", 9), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("CYAN_STAINED_GLASS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	PURPLE_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.PURPLE),
			of(new MaterialDefinition("STAINED_GLASS", 10), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PURPLE_STAINED_GLASS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BLUE_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.BLUE),
			of(new MaterialDefinition("STAINED_GLASS", 11), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BLUE_STAINED_GLASS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BROWN_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.BROWN),
			of(new MaterialDefinition("STAINED_GLASS", 12), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BROWN_STAINED_GLASS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GREEN_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.GREEN),
			of(new MaterialDefinition("STAINED_GLASS", 13), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GREEN_STAINED_GLASS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	RED_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.RED),
			of(new MaterialDefinition("STAINED_GLASS", 14), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("RED_STAINED_GLASS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BLACK_STAINED_GLASS(
			new StainedGlassDefinition(VersionedDyeColor.BLACK),
			of(new MaterialDefinition("STAINED_GLASS", 15), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BLACK_STAINED_GLASS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	MUSIC_DISC_13(
			of(new MaterialDefinition("GOLD_RECORD"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_13"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	MUSIC_DISC_CAT(
			of(new MaterialDefinition("GREEN_RECORD"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_CAT"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	MUSIC_DISC_BLOCKS(
			of(new MaterialDefinition("RECORD_3"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_BLOCKS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	MUSIC_DISC_CHIRP(
			of(new MaterialDefinition("RECORD_4"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_CHIRP"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	MUSIC_DISC_FAR(
			of(new MaterialDefinition("RECORD_5"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_FAR"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	MUSIC_DISC_MALL(
			of(new MaterialDefinition("RECORD_6"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_MALL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	MUSIC_DISC_MELLOHI(
			of(new MaterialDefinition("RECORD_7"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_MELLOHI"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	MUSIC_DISC_STAL(
			of(new MaterialDefinition("RECORD_8"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_13"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	MUSIC_DISC_STRAD(
			of(new MaterialDefinition("RECORD_9"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_STRAD"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	MUSIC_DISC_WARD(
			of(new MaterialDefinition("RECORD_10"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_WARD"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	MUSIC_DISC_11(
			of(new MaterialDefinition("RECORD_11"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_11"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	MUSIC_DISC_WAIT(
			of(new MaterialDefinition("RECORD_12"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MUSIC_DISC_WAIT"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	/**
	 * @deprecated {@link #INK_SAC}
	 */
	@Deprecated
	INK_SACK(
			of(new MaterialDefinition("INK_SACK"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("INK_SAC"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	INK_SAC(
			of(new MaterialDefinition("INK_SACK"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("INK_SAC"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BLACK_DYE(
			new DyeDefinition(VersionedDyeColor.BLACK),
			of(new MaterialDefinition("INK_SACK"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("INK_SAC"), V1_13.getVersions()),
			of(new MaterialDefinition("BLACK_DYE"), V1_14.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	/**
	 * @deprecated {@link #RED_DYE}
	 */
	@Deprecated
	ROSE_RED(
			new DyeDefinition(VersionedDyeColor.RED),
			of(new MaterialDefinition("INK_SACK", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("ROSE_RED"), V1_13.getVersions()),
			of(new MaterialDefinition("RED_DYE"), V1_14.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	RED_DYE(
			new DyeDefinition(VersionedDyeColor.RED),
			of(new MaterialDefinition("INK_SACK", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("ROSE_RED"), V1_13.getVersions()),
			of(new MaterialDefinition("RED_DYE"), V1_14.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	/**
	 * @deprecated {@link #GREEN_DYE}
	 */
	@Deprecated
	CACTUS_GREEN(
			new DyeDefinition(VersionedDyeColor.GREEN),
			of(new MaterialDefinition("INK_SACK", 2), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("CACTUS_GREEN"), V1_13.getVersions()),
			of(new MaterialDefinition("GREEN_DYE"), V1_14.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GREEN_DYE(
			new DyeDefinition(VersionedDyeColor.GREEN),
			of(new MaterialDefinition("INK_SACK", 2), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("CACTUS_GREEN"), V1_13.getVersions()),
			of(new MaterialDefinition("GREEN_DYE"), V1_14.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	COCOA_BEANS(
			of(new MaterialDefinition("INK_SACK", 3), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("COCOA_BEANS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BROWN_DYE(
			new DyeDefinition(VersionedDyeColor.BROWN),
			of(new MaterialDefinition("INK_SACK", 3), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("COCOA_BEANS"), V1_13.getVersions()),
			of(new MaterialDefinition("BROWN_DYE"), V1_14.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	LAPIS_LAZULI(
			of(new MaterialDefinition("INK_SACK", 4), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LAPIS_LAZULI"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BLUE_DYE(
			new DyeDefinition(VersionedDyeColor.BLUE),
			of(new MaterialDefinition("INK_SACK", 4), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LAPIS_LAZULI"), V1_13.getVersions()),
			of(new MaterialDefinition("BLUE_DYE"), V1_14.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	PURPLE_DYE(
			new DyeDefinition(VersionedDyeColor.PURPLE),
			of(new MaterialDefinition("INK_SACK", 5), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PURPLE_DYE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	CYAN_DYE(
			new DyeDefinition(VersionedDyeColor.CYAN),
			of(new MaterialDefinition("INK_SACK", 6), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("CYAN_DYE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	LIGHT_GRAY_DYE(
			new DyeDefinition(VersionedDyeColor.LIGHT_GRAY),
			of(new MaterialDefinition("INK_SACK", 7), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_GRAY_DYE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GRAY_DYE(
			new DyeDefinition(VersionedDyeColor.GRAY),
			of(new MaterialDefinition("INK_SACK", 8), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GRAY_DYE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	PINK_DYE(
			new DyeDefinition(VersionedDyeColor.PINK),
			of(new MaterialDefinition("INK_SACK", 9), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PINK_DYE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	LIME_DYE(
			new DyeDefinition(VersionedDyeColor.LIME),
			of(new MaterialDefinition("INK_SACK", 10), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIME_DYE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	/**
	 * @deprecated {@link #YELLOW_DYE}
	 */
	@Deprecated
	DANDELION_YELLOW(
			new DyeDefinition(VersionedDyeColor.YELLOW),
			of(new MaterialDefinition("INK_SACK", 11), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("DANDELION_YELLOW"), V1_13.getVersions()),
			of(new MaterialDefinition("YELLOW_DYE"), V1_14.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	YELLOW_DYE(
			new DyeDefinition(VersionedDyeColor.YELLOW),
			of(new MaterialDefinition("INK_SACK", 11), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("DANDELION_YELLOW"), V1_13.getVersions()),
			of(new MaterialDefinition("YELLOW_DYE"), V1_14.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	LIGHT_BLUE_DYE(
			new DyeDefinition(VersionedDyeColor.LIGHT_BLUE),
			of(new MaterialDefinition("INK_SACK", 12), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_BLUE_DYE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	MAGENTA_DYE(
			new DyeDefinition(VersionedDyeColor.MAGENTA),
			of(new MaterialDefinition("INK_SACK", 13), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MAGENTA_DYE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	ORANGE_DYE(
			new DyeDefinition(VersionedDyeColor.ORANGE),
			of(new MaterialDefinition("INK_SACK", 14), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("ORANGE_DYE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BONE_MEAL(
			new DyeDefinition(VersionedDyeColor.WHITE),
			of(new MaterialDefinition("INK_SACK", 15), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BONE_MEAL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GOLDEN_HELMET(
			of(new MaterialDefinition("GOLD_HELMET"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GOLDEN_HELMET"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GOLDEN_CHESTPLATE(
			of(new MaterialDefinition("GOLD_CHESTPLATE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GOLDEN_CHESTPLATE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GOLDEN_LEGGINGS(
			of(new MaterialDefinition("GOLD_LEGGINGS"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GOLDEN_LEGGINGS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GOLDEN_BOOTS(
			of(new MaterialDefinition("GOLD_BOOTS"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GOLDEN_BOOTS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GOLDEN_SWORD(
			of(new MaterialDefinition("GOLD_SWORD"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GOLDEN_SWORD"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GOLDEN_PICKAXE(
			of(new MaterialDefinition("GOLD_PICKAXE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GOLDEN_PICKAXE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GOLDEN_AXE(
			of(new MaterialDefinition("GOLD_AXE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GOLDEN_AXE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GOLDEN_SHOVEL(
			of(new MaterialDefinition("GOLD_SPADE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GOLDEN_SHOVEL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GOLDEN_HOE(
			of(new MaterialDefinition("GOLD_HOE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GOLDEN_HOE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	WOODEN_SWORD(
			of(new MaterialDefinition("WOOD_SWORD"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("WOODEN_SWORD"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	WOODEN_PICKAXE(
			of(new MaterialDefinition("WOOD_PICKAXE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("WOODEN_PICKAXE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	WOODEN_AXE(
			of(new MaterialDefinition("WOOD_AXE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("WOODEN_AXE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	WOODEN_SHOVEL(
			of(new MaterialDefinition("WOOD_SPADE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("WOODEN_SHOVEL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	WOODEN_HOE(
			of(new MaterialDefinition("WOOD_HOE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("WOODEN_HOE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	STONE_SHOVEL(
			of(new MaterialDefinition("STONE_SPADE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("STONE_SHOVEL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	IRON_SHOVEL(
			of(new MaterialDefinition("IRON_SPADE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("IRON_SHOVEL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	DIAMOND_SHOVEL(
			of(new MaterialDefinition("DIAMOND_SPADE"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("DIAMOND_SHOVEL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	SKELETON_SKULL(
			of(new MaterialDefinition("SKULL_ITEM", 0), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("SKELETON_SKULL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	WITHER_SKELETON_SKULL(
			of(new MaterialDefinition("SKULL_ITEM", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("WITHER_SKELETON_SKULL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	ZOMBIE_HEAD(
			of(new MaterialDefinition("SKULL_ITEM", 2), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("ZOMBIE_HEAD"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	PLAYER_HEAD(
			of(new MaterialDefinition("SKULL_ITEM", 3), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PLAYER_HEAD"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	CREEPER_HEAD(
			of(new MaterialDefinition("SKULL_ITEM", 4), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("CREEPER_HEAD"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	DRAGON_HEAD(
			of(new MaterialDefinition("SKULL_ITEM", 5), V1_9.versionsTo(V1_12)),
			of(new MaterialDefinition("DRAGON_HEAD"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	COMMAND_BLOCK(
			of(new MaterialDefinition("COMMAND"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("COMMAND_BLOCK"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GRASS(
			of(new MaterialDefinition("LONG_GRASS"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GRASS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GRASS_BLOCK(
			of(new MaterialDefinition("GRASS"), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GRASS_BLOCK"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	OAK_PLANKS(
			new WoodenPlanksDefinition(WoodType.OAK),
			of(new MaterialDefinition("WOOD", 0), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("OAK_PLANKS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	SPRUCE_PLANKS(
			new WoodenPlanksDefinition(WoodType.SPRUCE),
			of(new MaterialDefinition("WOOD", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("SPRUCE_PLANKS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BIRCH_PLANKS(
			new WoodenPlanksDefinition(WoodType.BIRCH),
			of(new MaterialDefinition("WOOD", 2), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BIRCH_PLANKS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	JUNGLE_PLANKS(
			new WoodenPlanksDefinition(WoodType.JUNGLE),
			of(new MaterialDefinition("WOOD", 3), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("JUNGLE_PLANKS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	ACACIA_PLANKS(
			new WoodenPlanksDefinition(WoodType.ACACIA),
			of(new MaterialDefinition("WOOD", 4), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("ACACIA_PLANKS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	DARK_OAK_PLANKS(
			new WoodenPlanksDefinition(WoodType.DARK_OAK),
			of(new MaterialDefinition("WOOD", 5), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("DARK_OAK_PLANKS"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	OAK_LEAVES(
			new LeavesDefinition(WoodType.OAK),
			of(new MaterialDefinition("LEAVES", 0), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("OAK_LEAVES"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	SPRUCE_LEAVES(
			new LeavesDefinition(WoodType.SPRUCE),
			of(new MaterialDefinition("LEAVES", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("SPRUCE_LEAVES"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BIRCH_LEAVES(
			new LeavesDefinition(WoodType.BIRCH),
			of(new MaterialDefinition("LEAVES", 2), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BIRCH_LEAVES"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	JUNGLE_LEAVES(
			new LeavesDefinition(WoodType.JUNGLE),
			of(new MaterialDefinition("LEAVES", 3), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("JUNGLE_LEAVES"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	ACACIA_LEAVES(
			new LeavesDefinition(WoodType.ACACIA),
			of(new MaterialDefinition("LEAVES_2", 0), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("ACACIA_LEAVES"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	DARK_OAK_LEAVES(
			new LeavesDefinition(WoodType.DARK_OAK),
			of(new MaterialDefinition("LEAVES_2", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("DARK_OAK_LEAVES"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	OAK_LOG(
			new WoodenLogDefinition(WoodType.OAK),
			of(new MaterialDefinition("LOG", 0), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("OAK_LOG"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	SPRUCE_LOG(
			new WoodenLogDefinition(WoodType.SPRUCE),
			of(new MaterialDefinition("LOG", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("SPRUCE_LOG"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BIRCH_LOG(
			new WoodenLogDefinition(WoodType.BIRCH),
			of(new MaterialDefinition("LOG", 2), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BIRCH_LOG"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	JUNGLE_LOG(
			new WoodenLogDefinition(WoodType.JUNGLE),
			of(new MaterialDefinition("LOG", 3), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("JUNGLE_LOG"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	ACACIA_LOG(
			new WoodenLogDefinition(WoodType.ACACIA),
			of(new MaterialDefinition("LOG2", 0), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("ACACIA_LOG"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	DARK_OAK_LOG(
			new WoodenLogDefinition(WoodType.DARK_OAK),
			of(new MaterialDefinition("LOG_2", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("DARK_OAK_LOG"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	OAK_SIGN(
			new WoodenSignDefinition(WoodType.OAK),
			of(new MaterialDefinition("SIGN", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("OAK_SIGN"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	OAK_SIGN_BLOCK(
			new WoodenSignDefinition(WoodType.OAK),
			of(new MaterialDefinition("SIGN_POST", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("OAK_SIGN"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	OAK_WALL_SIGN(
			new WoodenSignDefinition(WoodType.OAK),
			of(new MaterialDefinition("WALL_SIGN", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("OAK_WALL_SIGN"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	WHITE_WOOL(
			new WoolDefinition(VersionedDyeColor.WHITE),
			of(new MaterialDefinition("WOOL", 0), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("WHITE_WOOL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	ORANGE_WOOL(
			new WoolDefinition(VersionedDyeColor.ORANGE),
			of(new MaterialDefinition("WOOL", 1), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("ORANGE_WOOL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	MAGENTA_WOOL(
			new WoolDefinition(VersionedDyeColor.MAGENTA),
			of(new MaterialDefinition("WOOL", 2), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("MAGENTA_WOOL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	LIGHT_BLUE_WOOL(
			new WoolDefinition(VersionedDyeColor.LIGHT_BLUE),
			of(new MaterialDefinition("WOOL", 3), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_BLUE_WOOL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	YELLOW_WOOL(
			new WoolDefinition(VersionedDyeColor.YELLOW),
			of(new MaterialDefinition("WOOL", 4), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("YELLOW_WOOL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	LIME_WOOL(
			new WoolDefinition(VersionedDyeColor.LIME),
			of(new MaterialDefinition("WOOL", 5), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIME_WOOL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	PINK_WOOL(
			new WoolDefinition(VersionedDyeColor.PINK),
			of(new MaterialDefinition("WOOL", 6), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PINK_WOOL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GRAY_WOOL(
			new WoolDefinition(VersionedDyeColor.GRAY),
			of(new MaterialDefinition("WOOL", 7), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GRAY_WOOL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	LIGHT_GRAY_WOOL(
			new WoolDefinition(VersionedDyeColor.LIGHT_GRAY),
			of(new MaterialDefinition("WOOL", 8), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_GRAY_WOOL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	CYAN_WOOL(
			new WoolDefinition(VersionedDyeColor.CYAN),
			of(new MaterialDefinition("WOOL", 9), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("CYAN_WOOL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	PURPLE_WOOL(
			new WoolDefinition(VersionedDyeColor.PURPLE),
			of(new MaterialDefinition("WOOL", 10), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("PURPLE_WOOL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BLUE_WOOL(
			new WoolDefinition(VersionedDyeColor.BLUE),
			of(new MaterialDefinition("WOOL", 11), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BLUE_WOOL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BROWN_WOOL(
			new WoolDefinition(VersionedDyeColor.BROWN),
			of(new MaterialDefinition("WOOL", 12), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BROWN_WOOL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	GREEN_WOOL(
			new WoolDefinition(VersionedDyeColor.GREEN),
			of(new MaterialDefinition("WOOL", 13), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("GREEN_WOOL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	RED_WOOL(
			new WoolDefinition(VersionedDyeColor.RED),
			of(new MaterialDefinition("WOOL", 14), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("RED_WOOL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	BLACK_WOOL(
			new WoolDefinition(VersionedDyeColor.BLACK),
			of(new MaterialDefinition("WOOL", 15), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("BLACK_WOOL"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	REPEATER(
			of(new MaterialDefinition("DIODE", 0), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("REPEATER"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	COMPARATOR(
			of(new MaterialDefinition("REDSTONE_COMPARATOR", 0), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("COMPARATOR"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	STONE_PRESSURE_PLATE(
			of(new MaterialDefinition("STONE_PLATE", 0), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("STONE_PRESSURE_PLATE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	OAK_PRESSURE_PLATE(
			of(new MaterialDefinition("WOOD_PLATE", 0), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("OAK_PRESSURE_PLATE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	LIGHT_WEIGHTED_PRESSURE_PLATE(
			of(new MaterialDefinition("GOLD_PLATE", 0), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("LIGHT_WEIGHTED_PRESSURE_PLATE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	HEAVY_WEIGHTED_PRESSURE_PLATE(
			of(new MaterialDefinition("IRON_PLATE", 0), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("HEAVY_WEIGHTED_PRESSURE_PLATE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
	END_STONE(
			of(new MaterialDefinition("ENDER_STONE", 0), V1_8.versionsTo(V1_12)),
			of(new MaterialDefinition("END_STONE"), V1_13.versionsTo(NMSRelease.getLatestSupportedRelease()))
		),
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
	 * @param glassColor The color of the glass pane
	 * @return The corresponding stained glass pane material
	 */
	public static VersionedMaterial getStainedGlassPane(VersionedDyeColor glassColor) {
		return Arrays.stream(values())
				.filter(m ->
						m.getTypeDefinition() != null &&
						m.getTypeDefinition().asStainedGlassPane() != null &&
						m.getTypeDefinition().asStainedGlassPane().getDyeColor().equals(glassColor)
					)
				.findFirst().orElse(null);
	}

	/**
	 * Finds the corresponding glass material by its color
	 * @param glassColor The color of the glass
	 * @return The corresponding stained glass material
	 */
	public static VersionedMaterial getStainedGlass(VersionedDyeColor glassColor) {
		return Arrays.stream(values())
				.filter(m ->
						m.getTypeDefinition() != null &&
						m.getTypeDefinition().asStainedGlass() != null &&
						m.getTypeDefinition().asStainedGlass().getDyeColor().equals(glassColor)
					)
				.findFirst().orElse(null);
	}

	/**
	 * Finds the corresponding glass pane material by its color
	 * @param dyeColor The color of the glass pane
	 * @return The corresponding dye material
	 */
	public static VersionedMaterial getDye(VersionedDyeColor dyeColor) {
		return Arrays.stream(values())
				.filter(m ->
						m.getTypeDefinition() != null &&
						m.getTypeDefinition().asDye() != null &&
						m.getTypeDefinition().asDye().getDyeColor().equals(dyeColor)
					)
				.filter(m -> {
					try {
						Deprecated d = VersionedMaterial.class.getField(m.name()).getAnnotation(Deprecated.class);
						return d == null;
					} catch (NoSuchFieldException | SecurityException e) {
						return false;
					}
				})
				.findFirst().orElse(null);
	}

	/**
	 * Finds the corresponding wooden planks material by its wood type
	 * @param woodType The wood type of the planks
	 * @return The corresponding dye material
	 */
	public static VersionedMaterial getWoodenPlanks(WoodType woodType) {
		return Arrays.stream(values())
				.filter(m ->
						m.getTypeDefinition() != null &&
						m.getTypeDefinition().asWoodenPlanks() != null &&
						m.getTypeDefinition().asWoodenPlanks().getWoodType().equals(woodType)
					)
				.findFirst().orElse(null);
	}

	/**
	 * Finds the corresponding leaves material by its wood type
	 * @param woodType The type of the leaves
	 * @return The corresponding dye material
	 */
	public static VersionedMaterial getLeaves(WoodType woodType) {
		return Arrays.stream(values())
				.filter(m ->
						m.getTypeDefinition() != null &&
						m.getTypeDefinition().asLeaves() != null &&
						m.getTypeDefinition().asLeaves().getWoodType().equals(woodType)
					)
				.findFirst().orElse(null);
	}

	/**
	 * Finds the corresponding wooden log material by its wood type
	 * @param woodType The wood type of the log
	 * @return The corresponding dye material
	 */
	public static VersionedMaterial getWoodenLog(WoodType woodType) {
		return Arrays.stream(values())
				.filter(m ->
						m.getTypeDefinition() != null &&
						m.getTypeDefinition().asWoodenLog() != null &&
						m.getTypeDefinition().asWoodenLog().getWoodType().equals(woodType)
					)
				.findFirst().orElse(null);
	}

	/**
	 * Finds the corresponding wooden sign material by its wood type
	 * @param woodType The wood type of the sign
	 * @return The corresponding dye material
	 */
	public static VersionedMaterial getWoodenSign(WoodType woodType) {
		return Arrays.stream(values())
				.filter(m ->
						m.getTypeDefinition() != null &&
						m.getTypeDefinition().asWoodenSign() != null &&
						m.getTypeDefinition().asWoodenSign().getWoodType().equals(woodType)
					)
				.findFirst().orElse(null);
	}

	/**
	 * Finds the corresponding wool material by its color
	 * @param woolColor The color of the wool
	 * @return The corresponding dye material
	 */
	public static VersionedMaterial getWool(VersionedDyeColor woolColor) {
		return Arrays.stream(values())
				.filter(m ->
						m.getTypeDefinition() != null &&
						m.getTypeDefinition().asWool() != null &&
						m.getTypeDefinition().asWool().getDyeColor().equals(woolColor)
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
