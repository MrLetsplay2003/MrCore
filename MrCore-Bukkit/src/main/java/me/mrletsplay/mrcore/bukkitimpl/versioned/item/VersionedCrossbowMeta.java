package me.mrletsplay.mrcore.bukkitimpl.versioned.item;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class VersionedCrossbowMeta implements VersionedItemMeta {
	
	private static final String BUKKIT_CLASS_NAME = "org.bukkit.inventory.meta.CrossbowMeta";
	
	private ItemMeta meta;
	
	public VersionedCrossbowMeta(ItemMeta meta) {
		this.meta = meta;
	}
	
	public boolean hasChargedProjectiles() {
		return (boolean) invoke(getMethod("hasChargedProjectiles"));
	}
	
	@SuppressWarnings("unchecked")
	public List<ItemStack> getChargedProjectiles() {
		return (List<ItemStack>) invoke(getMethod("getChargedProjectiles"));
	}
	
	public void setChargedProjectiles(List<ItemStack> projectiles) {
		invoke(getMethod("setChargedProjectiles", List.class), projectiles);
	}
	
	public void addChargedProjectile(ItemStack projectile) {
		invoke(getMethod("addChargedProjectiles", ItemStack.class), projectile);
	}
	
	@Override
	public ItemMeta getBukkit() {
		return meta;
	}
	
	public static boolean isInstance(ItemMeta meta) {
		return VersionedItemMeta.isInstance(meta, BUKKIT_CLASS_NAME);
	}

}
