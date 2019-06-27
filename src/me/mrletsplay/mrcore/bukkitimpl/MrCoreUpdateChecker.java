package me.mrletsplay.mrcore.bukkitimpl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

import me.mrletsplay.mrcore.http.HttpRequest;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.main.MrCore;

public class MrCoreUpdateChecker {

	public static void checkForUpdate(String version){
		String absoluteVersion = version.equalsIgnoreCase("latest") ? getLatestVersion() : version;
		if(!MrCore.getVersion().equals(absoluteVersion)) {
			try {
				String newestLink = getDownloadLink(version);
				MrCorePlugin.pl.getLogger().info("The current version (" + MrCore.getVersion() + ") does not match the configured one (" + version + "/" + absoluteVersion + ")");
				MrCorePlugin.pl.getLogger().info("You can download the required version here: "+newestLink);
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static String getLatestVersion() {
		try {
			JSONObject release = HttpRequest.createGet("https://api.github.com/repos/MrLetsplay2003/MrCore/releases/latest").execute().asJSONObject();
			return release.getString("tag_name");
		}catch(Exception e) {
			return null;
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
	
}
