package me.mrletsplay.mrcore.bukkitimpl.versioned;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrletsplay.mrcore.bukkitimpl.ItemUtils;

public class VersionedUtils {

	/**
	 * @see ItemUtils#createItem(VersionedMaterial, int, String, String...)
	 */
	public static ItemStack createItem(VersionedMaterial material, int amount, String name, String... lore) {
		return ItemUtils.createItem(material, amount, name, lore);
	}
	
	public static void playSound(VersionedSound sound, Location location, float volume, float pitch) {
		location.getWorld().playSound(location, sound.getBukkitSound(), volume, pitch);
	}
	
	public static void playSound(VersionedSound sound, Player player, float volume, float pitch) {
		player.playSound(player.getLocation(), sound.getBukkitSound(), volume, pitch);
	}
	
}
