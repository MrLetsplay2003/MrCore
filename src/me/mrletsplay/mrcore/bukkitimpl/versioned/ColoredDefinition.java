package me.mrletsplay.mrcore.bukkitimpl.versioned;

public class ColoredDefinition implements TypeDefinition {

	private VersionedDyeColor dyeColor;
	
	public ColoredDefinition(VersionedDyeColor dyeColor) {
		this.dyeColor = dyeColor;
	}
	
	public VersionedDyeColor getDyeColor() {
		return dyeColor;
	}
	
}
