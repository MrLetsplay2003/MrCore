package me.mrletsplay.mrcore.bukkitimpl;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import me.mrletsplay.mrcore.bukkitimpl.versioned.MaterialDefinition;
import me.mrletsplay.mrcore.bukkitimpl.versioned.NMSVersion;
import me.mrletsplay.mrcore.bukkitimpl.versioned.VersionedMaterial;

public class BlockUtils {
	
	@SuppressWarnings("deprecation")
	public static void placeBlock(Location location, Material material, byte data) {
		Block block = location.getBlock();
		block.setType(material);
		if(NMSVersion.getCurrentServerVersion().isOlderThan(NMSVersion.V1_13_R1)) block.getState().setRawData(data);
	}
	
	public static void placeBlock(Location location, VersionedMaterial material) {
		MaterialDefinition def = material.getCurrentMaterialDefinition();
		placeBlock(location, def.getMaterial(), (byte) def.getDamage());
	}

}
