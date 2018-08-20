package me.mrletsplay.mrcore.bukkitimpl.versioned;

public interface TypeDefinition {

	public default BannerDefinition bannerDefinition() {
		return (this instanceof BannerDefinition ? (BannerDefinition) this : null);
	}

	public default ColoredDefinition coloredDefinition() {
		return (this instanceof ColoredDefinition ? (ColoredDefinition) this : null);
	}
	
}
