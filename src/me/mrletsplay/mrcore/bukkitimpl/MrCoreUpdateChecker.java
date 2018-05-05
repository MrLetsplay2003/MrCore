package me.mrletsplay.mrcore.bukkitimpl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import me.mrletsplay.mrcore.main.MrCore;

public class MrCoreUpdateChecker {

	public static void checkForUpdate(String version){
		String absoluteVersion = version.equalsIgnoreCase("latest") ? getLatestVersion() : version;
		if(!MrCore.getVersion().equals(absoluteVersion)) {
			try {
				String newestLink = getDownloadLink(version);
				MrCorePlugin.pl.getLogger().info("The current version ("+MrCore.getVersion()+") does not match the required one ("+version+"/"+absoluteVersion+")");
				MrCorePlugin.pl.getLogger().info("You can download the required version here: "+newestLink);
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static String getLatestVersion() {
		try {
			JSONObject release = (JSONObject) new JSONParser().parse(new InputStreamReader(new URL("https://api.github.com/repos/MrLetsplay2003/MrCore/releases/latest").openStream()));
			return (String) release.get("tag_name");
		}catch(Exception e) {
			return null;
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
