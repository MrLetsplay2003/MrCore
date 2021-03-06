package me.mrletsplay.mrcore.api.spiget;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.HttpRequest;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.FriendlyException;

public class SpiGet {

	public static final String API_BASE_URL = "https://api.spiget.org/v2/";
	
	public static List<SpiGetResource.Search> searchResource(String query) {
		try {
			return new JSONArray(makeRequest(API_BASE_URL + "search/resources/" + urlEncode(query) + "?size=1000&field=name")).stream()
				.map(res -> new SpiGetResource.Search((JSONObject) res))
				.collect(Collectors.toList());
		}catch(IllegalStateException e) {
			if(e.getCause() instanceof FileNotFoundException)
				return Collections.emptyList();
			throw e;
		}
	}
	
	public static SpiGetResource getResource(int resourceID) {
		return new SpiGetResource(new JSONObject(makeRequest(API_BASE_URL + "resources/" + resourceID)));
	}
	
	protected static String urlEncode(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new FriendlyException(e);
		}
	}
	
	protected static String makeRequest(String url) {
		return HttpRequest.createGet(url).execute().asString();
	}
	
}
