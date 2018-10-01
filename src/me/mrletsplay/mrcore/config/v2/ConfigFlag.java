package me.mrletsplay.mrcore.config.v2;

public enum ConfigFlag {

	/**
	 * When saving the config, all config properties will be sorted alphabetically<br>
	 * This is only available to non-external configs (as stated by {@link CustomConfig#isExternal()})
	 */
	SORT_ALPHABETICALLY,
	
	/**
	 * When saving the config, properties with comments will have one empty line added before and after them<br>
	 * Currently does nothing
	 */
	SPACE_COMMENTED_PROPERTIES;
	
}
