package me.mrletsplay.mrcore.bukkitimpl.versioned;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialDefinition {

	private String materialName;
	private short damage;
	
	public MaterialDefinition(String materialName) {
		this.materialName = materialName;
		this.damage = 0;
	}
	
	public MaterialDefinition(String materialName, int damage) {
		this.materialName = materialName;
		this.damage = (short) damage;
	}
	
	public String getMaterialName() {
		return materialName;
	}
	
	public Material getMaterial() {
		return Material.valueOf(materialName);
	}
	
	public short getDamage() {
		return damage;
	}
	
	public ItemStack newStack(int amount) {
		return new ItemStack(getMaterial(), amount, getDamage());
	}
	
	public ItemStack newStack() {
		return newStack(1);
	}
	
}
