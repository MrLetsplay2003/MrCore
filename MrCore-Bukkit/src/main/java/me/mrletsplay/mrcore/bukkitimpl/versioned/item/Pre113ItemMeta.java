package me.mrletsplay.mrcore.bukkitimpl.versioned.item;

import org.bukkit.inventory.meta.ItemMeta;

public class Pre113ItemMeta implements VersionedItemMeta {

	private ItemMeta meta;
	
	public Pre113ItemMeta(ItemMeta meta) {
		this.meta = meta;
	}
	
	public boolean isUnbreakable() {
		Object spigot = invoke(getMethod("spigot"));
		return (boolean) VersionedItemMeta.invoke(spigot, VersionedItemMeta.getMethod(spigot, "isUnbreakable"));
	}
	
	public void setUnbreakable(boolean unbreakable) {
		Object spigot = invoke(getMethod("spigot"));
		VersionedItemMeta.invoke(spigot, VersionedItemMeta.getMethod(spigot, "setUnbreakable", boolean.class), unbreakable);
	}
	
	@Override
	public ItemMeta getBukkit() {
		return meta;
	}
	
}
