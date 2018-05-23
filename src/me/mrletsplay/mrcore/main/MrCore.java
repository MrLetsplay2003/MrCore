package me.mrletsplay.mrcore.main;

import java.io.File;

import me.mrletsplay.mrcore.config.ConfigLoader;
import me.mrletsplay.mrcore.config.CustomConfig;

public class MrCore {

	/*
	 * TODO:
	 * - Color chat message parser (for BaseComponents)
	 * - Load/save items (CC)
	 * - NMS
	 * - CC custom parser
	 * - Command system
	 * - Chat GUIs (via commands)
	 * - JSON (?)
	 * - MySQL
	 * - Multi-Plugin ("Modules")
	 * 
	 * shift click (gui)
	 */
	
	private static final String VERSION = "1.9";
	
	public static String getVersion() {
		return VERSION;
	}
	
	public static void main(String[] args) {
		CustomConfig cc = ConfigLoader.loadCompactConfig(new File("TEST/abc.yml"));
		System.out.println(cc.getProperties());
	}
	
}
