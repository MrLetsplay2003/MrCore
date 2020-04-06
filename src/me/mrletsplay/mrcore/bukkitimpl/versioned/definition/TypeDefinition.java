package me.mrletsplay.mrcore.bukkitimpl.versioned.definition;

public interface TypeDefinition {

	/**
	 * @return The {@link ColoredDefinition} this instance represents or null if this is not a {@link ColoredDefinition}
	 */
	public default ColoredDefinition asColored() {
		return (this instanceof ColoredDefinition ? (ColoredDefinition) this : null);
	}
	
	/**
	 * @return The {@link BannerDefinition} this instance represents or null if this is not a {@link BannerDefinition}
	 */
	public default BannerDefinition asBanner() {
		return (this instanceof BannerDefinition ? (BannerDefinition) this : null);
	}
	
	/**
	 * @return The {@link StainedGlassPaneDefinition} this instance represents or null if this is not a {@link StainedGlassPaneDefinition}
	 */
	public default StainedGlassPaneDefinition asStainedGlassPane() {
		return (this instanceof StainedGlassPaneDefinition ? (StainedGlassPaneDefinition) this : null);
	}
	
	/**
	 * @return The {@link StainedGlassDefinition} this instance represents or null if this is not a {@link StainedGlassDefinition}
	 */
	public default StainedGlassDefinition asStainedGlass() {
		return (this instanceof StainedGlassDefinition ? (StainedGlassDefinition) this : null);
	}
	
	/**
	 * @return The {@link DyeDefinition} this instance represents or null if this is not a {@link DyeDefinition}
	 */
	public default DyeDefinition asDye() {
		return (this instanceof DyeDefinition ? (DyeDefinition) this : null);
	}
	
	/**
	 * @return The {@link StainedClayDefinition} this instance represents or null if this is not a {@link StainedClayDefinition}
	 */
	public default StainedClayDefinition asStainedClay() {
		return (this instanceof StainedClayDefinition ? (StainedClayDefinition) this : null);
	}
	
	/**
	 * @return The {@link WoodenDefinition} this instance represents or null if this is not a {@link WoodenDefinition}
	 */
	public default WoodenDefinition asWooden() {
		return (this instanceof WoodenDefinition ? (WoodenDefinition) this : null);
	}
	
	/**
	 * @return The {@link WoodenPlanksDefinition} this instance represents or null if this is not a {@link WoodenPlanksDefinition}
	 */
	public default WoodenPlanksDefinition asWoodenPlanks() {
		return (this instanceof WoodenPlanksDefinition ? (WoodenPlanksDefinition) this : null);
	}
	
	/**
	 * @return The {@link LeavesDefinition} this instance represents or null if this is not a {@link LeavesDefinition}
	 */
	public default LeavesDefinition asLeaves() {
		return (this instanceof LeavesDefinition ? (LeavesDefinition) this : null);
	}
	
	/**
	 * @return The {@link WoodenLogDefinition} this instance represents or null if this is not a {@link WoodenLogDefinition}
	 */
	public default WoodenLogDefinition asWoodenLog() {
		return (this instanceof WoodenLogDefinition ? (WoodenLogDefinition) this : null);
	}
	
	/**
	 * @return The {@link WoodenSignDefinition} this instance represents or null if this is not a {@link WoodenSignDefinition}
	 */
	public default WoodenSignDefinition asWoodenSign() {
		return (this instanceof WoodenSignDefinition ? (WoodenSignDefinition) this : null);
	}
	
	/**
	 * @return The {@link WoolDefinition} this instance represents or null if this is not a {@link WoolDefinition}
	 */
	public default WoolDefinition asWool() {
		return (this instanceof WoolDefinition ? (WoolDefinition) this : null);
	}
	
}
