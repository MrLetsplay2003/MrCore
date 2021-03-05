 package me.mrletsplay.mrcore.main;

public class MrCore {
	
	private static final String VERSION = "2.11";
	
	/**
	 * @deprecated There's no real use for plugins and I keep forgetting to update its value. Will be removed or replaced by something more robust in a future version
	 * @return The current version of the MrCore, only "2.11" after v2.11 of MrCore
	 */
	@Deprecated
	public static String getVersion() {
		return VERSION;
	}
	
}
