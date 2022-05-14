package me.mrletsplay.mrcore.bukkitimpl.versioned.definition;

public class WoodenDefinition implements TypeDefinition {
	
	private WoodType woodType;

	public WoodenDefinition(WoodType woodType) {
		this.woodType = woodType;
	}
	
	public WoodType getWoodType() {
		return woodType;
	}

}
