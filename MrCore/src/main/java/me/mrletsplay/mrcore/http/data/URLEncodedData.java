package me.mrletsplay.mrcore.http.data;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.HttpUtils;

/**
 * Provides data for requests encoded using the <code>application/x-www-form-urlencoded</code> MIME type
 * @author MrLetsplay2003
 */
public class URLEncodedData implements RequestData {
	
	private Map<String, String> data;
	
	private URLEncodedData(Map<String, String> data) {
		this.data = new LinkedHashMap<>(data);
	}
	
	public URLEncodedData() {
		this.data = new LinkedHashMap<>();
	}
	
	/**
	 * Sets a key to a value
	 * @param key The key
	 * @param value The value
	 * @return This {@link URLEncodedData} object for chaining convenience
	 */
	public URLEncodedData set(String key, String value) {
		data.put(key, value);
		return this;
	}
	
	@Override
	public byte[] createPayload() {
		return data.entrySet().stream()
			.map(e -> HttpUtils.urlEncode(e.getKey()) + "=" + HttpUtils.urlEncode(e.getValue()))
			.collect(Collectors.joining("&")).getBytes(StandardCharsets.UTF_8);
	}
	
	@Override
	public String getMimeType() {
		return "application/x-www-form-urlencoded";
	}
	
	/**
	 * Creates a {@link URLEncodedData} object from the provided map of key-value pairs
	 * @param data The map of key-value pairs
	 * @return A {@link URLEncodedData} object initialized with the provided data
	 */
	public static URLEncodedData of(Map<String, String> data) {
		return new URLEncodedData(data);
	}

}
