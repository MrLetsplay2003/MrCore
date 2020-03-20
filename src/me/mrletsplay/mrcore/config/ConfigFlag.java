package me.mrletsplay.mrcore.config;

public enum ConfigFlag {

	/**
	 * When saving the config, all config properties will be sorted alphabetically<br>
	 */
	@Deprecated
	SORT_ALPHABETICALLY,
	
	/**
	 * When saving the config, properties with comments will have one empty line added before and after them<br>
	 * Currently does nothing
	 */
	@Deprecated
	SPACE_COMMENTED_PROPERTIES,
	
	/**
	 * When saving the config, always create the file, even if there wouldn't be anything in it
	 */
	CREATE_EMPTY_FILE;
	
}
