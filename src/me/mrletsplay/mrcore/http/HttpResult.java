package me.mrletsplay.mrcore.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.io.IOUtils;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;

public class HttpResult {

	private byte[] raw;
	
	public HttpResult(byte[] raw) {
		this.raw = raw;
	}
	
	public JSONObject asJSONObject() {
		return new JSONObject(asString());
	}
	
	public JSONArray asJSONArray() {
		return new JSONArray(asString());
	}
	
	public String asString() {
		return new String(raw, StandardCharsets.UTF_8);
	}
	
	public byte[] asRaw() {
		return raw;
	}
	
	public void transferTo(OutputStream out, boolean autoClose) throws IOException {
		out.write(raw);
		if(autoClose) out.close();
	}
	
	public void transferToSafely(OutputStream out, boolean autoClose) {
		try {
			transferTo(out, autoClose);
		} catch (IOException e) {
				try {
					if(autoClose) out.close();
				} catch (IOException e1) {
					throw new HttpException(e);
				}
			throw new HttpException(e);
		}
	}
	
	public void transferTo(File file) throws IOException {
		IOUtils.createFile(file);
		transferTo(new FileOutputStream(file), true);
	}
	
	public void transferToSafely(File file) {
		try {
			transferTo(file);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}
	
	protected static InputStream retrieveAsInputStreamFrom(String url, String method, Map<String, String> queryParams, Map<String, String> headerParams, byte[] postData) throws IOException {
		if(!queryParams.isEmpty()) {
			url += queryParams.entrySet().stream()
				.map(e -> HttpUtils.urlEncode(e.getKey()) + "=" + HttpUtils.urlEncode(e.getValue()))
				.collect(Collectors.joining("&", "?", ""));
		}
		URL u = new URL(url);
		HttpURLConnection con = (HttpURLConnection) u.openConnection();
		con.setRequestMethod(method);
		con.setRequestProperty("charset", "utf-8");
		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		if(postData != null) {
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			con.setRequestProperty("Content-Length", Integer.toString(postData.length));
		}
		headerParams.forEach(con::setRequestProperty);
		con.setUseCaches(false);
		if(postData != null) {
			con.setDoOutput(true);
			OutputStream out = con.getOutputStream();
			out.write(postData, 0, postData.length);
		}
		InputStream in = con.getInputStream();
		return in;
	}
	
	protected static HttpResult retrieveFrom(String url, String method, Map<String, String> queryParams, Map<String, String> headerParams, byte[] postParams) throws IOException {
		InputStream in = retrieveAsInputStreamFrom(url, method, queryParams, headerParams, postParams);
		byte[] raw = IOUtils.readAllBytes(in);
		in.close();
		return new HttpResult(raw);
	}
	
}
