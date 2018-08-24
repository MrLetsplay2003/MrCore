package me.mrletsplay.mrcore.bukkitimpl.versioned.definition;

import me.mrletsplay.mrcore.bukkitimpl.versioned.VersionedDyeColor;

/**
 * Represents a colored material (e.g. dye, stained glass etc.)
 * @author MrLetsplay2003
 */
public class ColoredDefinition implements TypeDefinition {

	private VersionedDyeColor dyeColor;
	
	public ColoredDefinition(VersionedDyeColor dyeColor) {
		this.dyeColor = dyeColor;
	}
	
	public VersionedDyeColor getDyeColor() {
		return dyeColor;
	}
	
}
