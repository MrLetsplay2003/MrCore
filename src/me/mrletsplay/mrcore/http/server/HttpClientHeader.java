package me.mrletsplay.mrcore.http.server;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.server.impl.DefaultHttpClientHeader;
import me.mrletsplay.mrcore.http.server.impl.DefaultHttpHeaderFields;

public interface HttpClientHeader {
	
	public static final Pattern
			HEADER_TOP_PATTERN = Pattern.compile("(?<method>[A-Z]+) (?<path>.+) (?<protocol>HTTP\\/(?<version>.+))\\z"),
			HEADER_FIELD_PATTERN = Pattern.compile("(?<name>.+?):\\s*(?<value>.+?)\\s*\\z"),
			HEADER_COOKIE_PATTERN = Pattern.compile("\\s*(?<name>.+?)=(?<value>.+?)");

	public String getRequestMethod();
	
	public String getRawRequestedPath();
	
	public String getProtocol();
	
	public String getProtocolVersion();
	
	public HttpHeaderFields getFields();
	
	public byte[] getBody();
	
	public default String getUserAgent() {
		return getFields().getLast("User-Agent");
	}
	
	public default Map<String, String> getCookies() throws IllegalArgumentException {
		Map<String, String> cookies = new LinkedHashMap<>();
		for(String cookieStr : getFields().get("Cookie")) {
			for(String cookie : cookieStr.split(";")) {
				Matcher m = HEADER_COOKIE_PATTERN.matcher(cookie);
				if(!m.matches()) throw new IllegalArgumentException("Invalid cookie format: " + cookie);
				cookies.put(m.group("name"), m.group("value"));
			}
		}
		return cookies;
	}
	
	public default boolean hasCookie(String cookieName) {
		return getCookies().containsKey(cookieName);
	}
	
	public default String getCookie(String cookieName) {
		return getCookies().get(cookieName);
	}
	
	public static HttpClientHeader parse(String headerString) {
		if(headerString.isEmpty()) throw new IllegalArgumentException("Empty header");
		List<String> headerLines = Arrays.asList(headerString.split("\n"));
		String top = headerLines.get(0).trim();
		Matcher tM = HEADER_TOP_PATTERN.matcher(top);
		if(!tM.matches()) throw new IllegalArgumentException("Invalid header format: " + top);
		HttpHeaderFields fields = new DefaultHttpHeaderFields();
		int i;
		for(i = 1; i < headerLines.size(); i++) {
			Matcher m = HEADER_FIELD_PATTERN.matcher(headerLines.get(i).trim());
			if(!m.matches()) break;
			fields.add(m.group("name"), m.group("value"));
		}
		String remainder = headerLines.stream().skip(i).collect(Collectors.joining("\n"));
		return new DefaultHttpClientHeader(tM.group("method"), tM.group("path"), tM.group("protocol"), tM.group("version"), fields, remainder.getBytes(StandardCharsets.UTF_8)); // TODO: Remainder
	}
	
}
