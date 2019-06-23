package me.mrletsplay.mrcore.bukkitimpl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

import me.mrletsplay.mrcore.http.HttpRequest;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.main.MrCore;

public class MrCoreUpdater {

	//TODO: This is just a concept idea, nothing solid
	
	public static void update(String version) {
		String absoluteVersion = version.equalsIgnoreCase("latest") ? getLatestVersion() : version;
		if(MrCore.getVersion().equals(absoluteVersion)) {
			MrCorePlugin.pl.getLogger().info("MrCore is up to date!");
			return;
		}
		MrCorePlugin.pl.getLogger().info("Updating MrCore (current version: "+MrCore.getVersion()+", required version: "+absoluteVersion+"/"+version+")...");
		try {
			String oldProtocol = System.getProperty("http.protocols");
			System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

			File mrCoreFile = new File("plugins/MrCore_"+absoluteVersion+".jar");
			if(mrCoreFile.exists()) {
				MrCorePlugin.pl.getLogger().info("A file named \""+mrCoreFile.getName()+"\" already exists, assuming that MrCore was already loaded");
				return;
			}
			String downloadL = getDownloadLink(version);
			MrCorePlugin.pl.getLogger().info("Downloading from "+downloadL+"...");
			HttpRequest.createGet(downloadL).execute().transferTo(mrCoreFile);
			MrCorePlugin.pl.getLogger().info("Downloaded MrCore jar");
			File currentFile = new File(MrCorePlugin.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			currentFile.deleteOnExit();
			MrCorePlugin.pl.getLogger().info("In order to load the newer version of MrCore, please restart/reload your server");
			
			if(oldProtocol != null) System.setProperty("https.protocols", oldProtocol);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String getDownloadLink(String version) throws MalformedURLException, IOException, ParseException {
		JSONObject release = null;
		if(version.equalsIgnoreCase("latest")) {
			release = HttpRequest.createGet("https://api.github.com/repos/MrLetsplay2003/MrCore/releases/latest").execute().asJSONObject();
		}else {
			JSONArray releases = HttpRequest.createGet("https://api.github.com/repos/MrLetsplay2003/MrCore/releases").execute().asJSONArray();
			for(int i = 0; i < releases.size(); i++) {
				JSONObject rel = releases.getJSONObject(i);
				if(rel.get("tag_name").equals(version)) {
					release = rel;
					break;
				}
			}
			if(release == null) {
				throw new IllegalArgumentException("The specified version ("+version+") doesn't exist");
			}
		}
		JSONArray assets = release.getJSONArray("assets");
		JSONObject asset = assets.getJSONObject(0); // The attached MrCore.jar file
		String downloadL = asset.getString("browser_download_url");
		return downloadL;
	}
	
	private static String getLatestVersion() {
		try {
			JSONObject release = HttpRequest.createGet("https://api.github.com/repos/MrLetsplay2003/MrCore/releases/latest").execute().asJSONObject();
			return release.getString("tag_name");
		}catch(Exception e) {
			return null;
		}
	}
	
}
