package me.mrletsplay.mrcore.http.data;

import java.nio.charset.StandardCharsets;

import me.mrletsplay.mrcore.json.JSONObject;

/**
 * Provides data for requests encoded using the <code>application/json</code> MIME type
 * @author MrLetsplay2003
 */
public class JSONObjectData implements RequestData {
	
	private JSONObject data;

	private JSONObjectData(JSONObject data) {
		this.data = data;
	}
	
	public JSONObjectData() {
		this.data = new JSONObject();
	}
	
	/**
	 * Sets a key to a value
	 * @param key The key
	 * @param value The value
	 * @return This {@link JSONObjectData} object for chaining convenience
	 */
	public JSONObjectData set(String key, Object value) {
		data.set(key, value);
		return this;
	}
	
	@Override
	public byte[] createPayload() {
		return data.toString().getBytes(StandardCharsets.UTF_8);
	}
	
	@Override
	public String getMimeType() {
		return "application/json";
	}
	
	/**
	 * Creates a {@link URLEncodedData} object from the provided map of key-value pairs
	 * @param data The map of key-value pairs
	 * @return A {@link URLEncodedData} object initialized with the provided data
	 */
	public static JSONObjectData of(JSONObject object) {
		return new JSONObjectData(object);
	}
	
}
