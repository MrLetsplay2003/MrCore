package me.mrletsplay.mrcore.bukkitimpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.mrcore.main.MrCore;

public class MrCoreUpdater {

	public static void update(String version) {
		String absoluteVersion = version.equalsIgnoreCase("latest") ? getLatestVersion() : version;
		if(MrCore.getVersion().equals(absoluteVersion)) {
			MrCorePlugin.pl.getLogger().info("MrCore is up to date!");
			return;
		}
		MrCorePlugin.pl.getLogger().info("Updating MrCore (current version: "+MrCore.getVersion()+", required version: "+version+")...");
		try {
			String oldProtocol = System.getProperty("http.protocols");
			System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

			File mrCoreFile = new File("plugins/MrCore_"+version+".jar");
			if(mrCoreFile.exists()) {
				MrCorePlugin.pl.getLogger().info("A file named \""+mrCoreFile.getName()+"\" already exists, assuming that MrCore was already loaded");
				return;
			}
			String downloadL = getDownloadLink(version);
			MrCorePlugin.pl.getLogger().info("Downloading from "+downloadL+"...");
			HttpUtils.download(new URL(downloadL), mrCoreFile);
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
	
	private static String getLatestVersion() {
		try {
			JSONObject release = (JSONObject) new JSONParser().parse(new InputStreamReader(new URL("https://api.github.com/repos/MrLetsplay2003/MrCore/releases/latest").openStream()));
			return (String) release.get("tag_name");
		}catch(Exception e) {
			return null;
		}
	}
	
}
