package me.mrletsplay.mrcore.api.bukkit;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.HttpRequest;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.FriendlyException;

public class BukkitAPI {
	
	private static final String
		PROJECTS_URL = "https://api.curseforge.com/servermods/projects?search=",
		FILES_URL = "https://api.curseforge.com/servermods/files?projectIds=";

	public static List<BukkitResource> searchResource(String query) {
		return new JSONArray(makeRequest(PROJECTS_URL + urlEncode(query))).stream()
				.map(r -> new BukkitResource((JSONObject) r))
				.collect(Collectors.toList());
	}
	
	public static BukkitFile getBukkitFile(int projectId) {
		return new JSONArray(makeRequest(FILES_URL + projectId)).stream()
				.map(r -> new BukkitFile((JSONObject) r))
				.collect(Collectors.toList()).get(0);
	}
	
	private static String urlEncode(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new FriendlyException(e);
		}
	}
	
	private static String makeRequest(String url) {
		return HttpRequest.createGet(url).execute().asString();
	}
	
}
