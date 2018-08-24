package me.mrletsplay.mrcore.bukkitimpl.versioned;

public interface TypeDefinition {

	/**
	 * @return The {@link ColoredDefinition} this instance represents or null if this is not a ColoredDefinition
	 */
	public default ColoredDefinition asColored() {
		return (this instanceof ColoredDefinition ? (ColoredDefinition) this : null);
	}
	
}
