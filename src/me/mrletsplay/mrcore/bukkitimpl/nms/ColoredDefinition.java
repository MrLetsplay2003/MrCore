package me.mrletsplay.mrcore.bukkitimpl.nms;

public class ColoredDefinition implements TypeDefinition {

	private NMSDyeColor dyeColor;
	
	public ColoredDefinition(NMSDyeColor dyeColor) {
		this.dyeColor = dyeColor;
	}
	
	public NMSDyeColor getDyeColor() {
		return dyeColor;
	}
	
}
