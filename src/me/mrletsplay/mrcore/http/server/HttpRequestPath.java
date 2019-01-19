package me.mrletsplay.mrcore.http.server;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.mrletsplay.mrcore.http.HttpUtils;

public class HttpRequestPath {

	public static final Pattern
			PATH_PATTERN = Pattern.compile("(?<path>(?:\\/[^&=?]*+))(?:\\?(?<get>[^&=]++(?:=[^&]*+|)(?:\\&[^&=]++(?:=[^&]*+|))*))?\\z");
	
	private String path;
	private Map<String, String> getParameters;

	public HttpRequestPath(String path, Map<String, String> getParameters) {
		this.path = path;
		this.getParameters = getParameters;
	}
	
	public String getPath() {
		return path;
	}
	
	public Map<String, String> getGetParameters() {
		return getParameters;
	}
	
	public static HttpRequestPath parse(String rawPath) {
		Matcher m = PATH_PATTERN.matcher(rawPath);
		if(!m.matches()) throw new IllegalArgumentException("Invalid path format");
		String path = m.group("path");
		String get = m.group("get");
		Map<String, String> getParameters = new LinkedHashMap<>();
		if(get != null) {
			for(String gP : get.split("&")) {
				String[] spl = gP.split("=", 2);
				getParameters.put(spl[0], HttpUtils.urlDecode(spl[1]));
			}
		}
		return new HttpRequestPath(path, getParameters);
	}
	
}
