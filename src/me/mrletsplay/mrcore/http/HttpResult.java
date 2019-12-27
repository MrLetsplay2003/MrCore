package me.mrletsplay.mrcore.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.io.IOUtils;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;

public class HttpResult {

	private boolean success;
	private Map<String, List<String>> headerFields;
	private byte[] raw;
	private IOException exception;
	private String errorResponse;
	
	public HttpResult(Map<String, List<String>> headerFields, byte[] raw) {
		this.success = true;
		this.headerFields = headerFields;
		this.raw = raw;
	}
	
	public HttpResult(IOException exception, String errorResponse) {
		this.success = false;
		this.exception = exception;
		this.errorResponse = errorResponse;
	}
	
	public Map<String, List<String>> getHeaderFields() {
		requireSuccessful();
		return headerFields;
	}
	
	public JSONObject asJSONObject() {
		return new JSONObject(asString());
	}
	
	public JSONArray asJSONArray() {
		return new JSONArray(asString());
	}
	
	public String asString() {
		return new String(asRaw(), StandardCharsets.UTF_8);
	}
	
	public byte[] asRaw() {
		requireSuccessful();
		return raw;
	}
	
	public void transferTo(OutputStream out, boolean autoClose) throws IOException {
		out.write(asRaw());
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
		requireSuccessful();
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
	
	public void requireSuccessful() throws IllegalStateException {
		if(!success) throw new IllegalStateException("Request was unsuccessful", exception);
	}
	
	public String getErrorResponse() {
		return errorResponse;
	}
	
	public IOException getException() {
		return exception;
	}
	
	protected static HttpURLConnection retrieveRawFrom(String url, String method, Map<String, String> queryParams, Map<String, String> headerParams, byte[] postData, int timeout, Proxy proxy) throws IOException {
		if(!queryParams.isEmpty()) {
			url += queryParams.entrySet().stream()
				.map(e -> HttpUtils.urlEncode(e.getKey()) + "=" + HttpUtils.urlEncode(e.getValue()))
				.collect(Collectors.joining("&", "?", ""));
		}
		URL u = new URL(url);
		HttpURLConnection con = (HttpURLConnection) u.openConnection(proxy == null ? Proxy.NO_PROXY : proxy);
		con.setRequestMethod(method);
		con.setRequestProperty("charset", "utf-8");
		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		if(postData != null) {
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			con.setRequestProperty("Content-Length", Integer.toString(postData.length));
		}
		headerParams.forEach(con::setRequestProperty);
		con.setUseCaches(false);
		con.setConnectTimeout(timeout);
		if(postData != null) {
			con.setDoOutput(true);
			OutputStream out = con.getOutputStream();
			out.write(postData, 0, postData.length);
		}
		return con;
	}
	
	protected static InputStream retrieveAsInputStreamFrom(String url, String method, Map<String, String> queryParams, Map<String, String> headerParams, byte[] postData, int timeout, Proxy proxy) throws IOException {
		return retrieveRawFrom(url, method, queryParams, headerParams, postData, timeout, proxy).getInputStream();
	}
	
	protected static HttpResult retrieveFrom(String url, String method, Map<String, String> queryParams, Map<String, String> headerParams, byte[] postParams, int timeout, Proxy proxy, boolean untilUnavailable) throws IOException {
		HttpURLConnection con = retrieveRawFrom(url, method, queryParams, headerParams, postParams, timeout, proxy);
		try {
			InputStream in = con.getInputStream();
			byte[] raw = untilUnavailable ? IOUtils.readBytesUntilUnavailable(in) : IOUtils.readAllBytes(in);
			in.close();
			return new HttpResult(con.getHeaderFields(), raw);
		} catch(IOException e) {
			String resp = new String(IOUtils.readAllBytesSafely(con.getErrorStream()));
			return new HttpResult(e, resp);
		}
	}
	
}
