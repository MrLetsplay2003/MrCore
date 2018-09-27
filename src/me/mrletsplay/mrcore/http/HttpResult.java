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
import me.mrletsplay.mrcore.misc.JSON.JSONArray;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;

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
	
	public void transferTo(OutputStream out) throws IOException {
		out.write(raw);
	}
	
	public void transferToSafely(OutputStream out) {
		try {
			transferTo(out);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}
	
	public void transferTo(File file) throws IOException {
		IOUtils.createFile(file);
		transferTo(new FileOutputStream(file));
	}
	
	public void transferToSafely(File file) {
		try {
			transferTo(file);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}
	
	protected static InputStream retrieveAsInputStreamFrom(String url, String method, Map<String, String> queryParams, Map<String, String> headerParams, Map<String, String> postParams) throws IOException {
		if(!queryParams.isEmpty()) {
			url += queryParams.entrySet().stream()
					.map(e -> HttpUtils.urlEncode(e.getKey()) + "=" + HttpUtils.urlEncode(e.getValue()))
					.collect(Collectors.joining("&", "?", ""));
		}
		URL u = new URL(url);
		HttpURLConnection con = (HttpURLConnection) u.openConnection();
		con.setRequestMethod(method);
		con.setDoOutput(true);
		con.setRequestProperty("charset", "utf-8");
		byte[] postData = postParams.entrySet().stream()
				.map(e -> HttpUtils.urlEncode(e.getKey()) + "=" + HttpUtils.urlEncode(e.getValue()))
				.collect(Collectors.joining("&")).getBytes(StandardCharsets.UTF_8);
		if(postData.length > 0) {
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			con.setRequestProperty("Content-Length", Integer.toString(postData.length));
		}
		headerParams.forEach(con::setRequestProperty);
		con.setUseCaches(false);
		if(postData.length > 0) {
			OutputStream out = con.getOutputStream();
			out.write(postData, 0, postData.length);
		}
		InputStream in = con.getInputStream();
		return in;
	}
	
	protected static HttpResult retrieveFrom(String url, String method, Map<String, String> queryParams, Map<String, String> headerParams, Map<String, String> postParams) throws IOException {
		InputStream in = retrieveAsInputStreamFrom(url, method, queryParams, headerParams, postParams);
		byte[] raw = IOUtils.readAllBytes(in);
		in.close();
		return new HttpResult(raw);
	}
	
}
