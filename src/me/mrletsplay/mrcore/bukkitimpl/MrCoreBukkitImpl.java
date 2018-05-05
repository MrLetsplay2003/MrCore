package me.mrletsplay.mrcore.bukkitimpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MrCoreBukkitImpl {
	
	public static final String MRCORE_PLUGIN_NAME = "MrCore_BukkitImpl";

	/**
	 * This will load (or download) the MrCore lib<br>
	 * Version "latest" is to be used with caution, because i'm horrible at sustaining backwards-compatability
	 * @param plugin The plugin to load it for
	 * @param version The version to load ("latest" for the latest version). This should equal the version specified in the maven dependency
	 * @param autoUpdate Whether the MrCore should be updated automatically (only if version is "latest")
	 */
	public static void loadMrCore(JavaPlugin plugin, String version, boolean autoUpdate) {
		if(Bukkit.getPluginManager().isPluginEnabled(MRCORE_PLUGIN_NAME)) {
			return;
		}
		plugin.getLogger().info("Couldn't find "+MRCORE_PLUGIN_NAME+", seems like we need to download it from GitHub...");
		try {
			String oldProtocol = System.getProperty("http.protocols");
			System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
			
			File mrCoreFile = new File("plugins/"+MRCORE_PLUGIN_NAME+".jar");
			if(mrCoreFile.exists()) {
				plugin.getLogger().info("A file named \""+mrCoreFile.getName()+"\" already exists, assuming that MrCore was already loaded");
				return;
			}
			String downloadL = getDownloadLink(version);
			plugin.getLogger().info("Downloading from "+downloadL+"...");
			download(new URL(downloadL), mrCoreFile);
			Bukkit.getPluginManager().loadPlugin(mrCoreFile);
			plugin.getLogger().info("Down-/loaded MrCore successfully");
			if(oldProtocol != null) System.setProperty("https.protocols", oldProtocol);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	private static void download(URL url, File file) {
		try {
			file.getParentFile().mkdirs();
			ReadableByteChannel rbc = Channels.newChannel(url.openStream());
			FileOutputStream fos = new FileOutputStream(file);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String getDownloadLink(String version) throws MalformedURLException, IOException, ParseException {
		JSONObject release = null;
		if(version.equalsIgnoreCase("latest")) {
			release = (JSONObject) new JSONParser().parse(new InputStreamReader(new URL("https://api.github.com/repos/MrLetsplay2003/MrCore/releases/latest").openStream()));
		}else {
			JSONArray releases = (JSONArray) new JSONParser().parse(new InputStreamReader(new URL("https://api.github.com/repos/MrLetsplay2003/MrCore/releases").openStream()));
			for(int i = 0; i < releases.size(); i++) {
				JSONObject rel = (JSONObject) releases.get(i);
				if(rel.get("tag_name").equals(version)) {
					release = rel;
					break;
				}
			}
			if(release == null) {
				throw new IllegalArgumentException("The specified version ("+version+") doesn't exist");
			}
		}
		JSONArray assets = (JSONArray) release.get("assets");
		JSONObject asset = (JSONObject) assets.get(0); // The attached MrCore.jar file
		String downloadL = (String) asset.get("browser_download_url");
		return downloadL;
	}
	
}
