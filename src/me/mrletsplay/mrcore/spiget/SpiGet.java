package me.mrletsplay.mrcore.spiget;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.mrcore.misc.JSON.JSONArray;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;
import me.mrletsplay.mrcore.misc.OtherTools.FriendlyException;

public class SpiGet {

	public static final String API_BASE_URL = "https://api.spiget.org/v2/";
	
	public static List<SpiGetResource.Search> searchResource(String query) {
		return new JSONArray(makeRequest(API_BASE_URL + "search/resources/" + urlEncode(query))).stream()
			.map(res -> new SpiGetResource.Search((JSONObject) res))
			.collect(Collectors.toList());
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
		try {
			InputStream stream = HttpUtils.httpGet(new URL(url));
			byte[] buf = new byte[4096];
			int len;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			while((len = stream.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			String response = new String(out.toByteArray());
			return response;
		} catch (FileNotFoundException e) { // HTTP 404
			return null;
		} catch (IOException e) {
			throw new FriendlyException(e);
		}
	}
	
}
