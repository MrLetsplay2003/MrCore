package me.mrletsplay.mrcore.bukkitimpl.versioned;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a version-specific material
 * @author MrLetsplay2003
 */
public class MaterialDefinition {

	private String materialName;
	private short damage;
	
	protected MaterialDefinition(String materialName) {
		this.materialName = materialName;
		this.damage = 0;
	}
	
	protected MaterialDefinition(String materialName, int damage) {
		this.materialName = materialName;
		this.damage = (short) damage;
	}
	
	/**
	 * Gets the material name (for the {@link Material} enum)
	 * @return The material name
	 */
	public String getMaterialName() {
		return materialName;
	}
	
	/**
	 * Gets the {@link Material} using {@link Material#valueOf(String)}
	 * @return The {@link Material} this definition represents
	 */
	public Material getMaterial() {
		return Material.valueOf(materialName);
	}
	
	/**
	 * Gets the damage value for an {@link ItemStack} with that type.<br>
	 * This is used for dye (and similar) in pre-1.13 versions of Spigot
	 * @return The damage value
	 */
	public short getDamage() {
		return damage;
	}
	
	/**
	 * Creates a new {@link ItemStack} with the type represented by this instance
	 * @param amount The amount of items in the new stack
	 * @return An {@link ItemStack} with the type represented by this instance
	 */
	@SuppressWarnings("deprecation")
	public ItemStack newStack(int amount) {
		return new ItemStack(getMaterial(), amount, getDamage());
	}

	/**
	 * Creates a new {@link ItemStack} with the type represented by this instance.<br>
	 * This call is equivalent to calling {@code MaterialDefinition#newStack(1)}
	 * @return An {@link ItemStack} with the type represented by this instance and an item amount of 1
	 */
	public ItemStack newStack() {
		return newStack(1);
	}
	
}
