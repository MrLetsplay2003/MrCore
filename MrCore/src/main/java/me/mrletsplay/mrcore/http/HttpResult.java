package me.mrletsplay.mrcore.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import me.mrletsplay.mrcore.io.IOUtils;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;

public class HttpResult {

	private HttpResponse<byte[]> response;

	public HttpResult(HttpResponse<byte[]> response) {
		this.response = response;
	}

	@Deprecated
	public Map<String, List<String>> getHeaderFields() {
		requireSuccessful(); // Old behavior
		return response.headers().map();
	}

	public HttpHeaders getHeaders() {
		return response.headers();
	}

	public boolean isSuccess() {
		int type = response.statusCode() / 100;
		return type == 1 || type == 2 || type == 3; // 1xx, 2xx, 3xx status code
	}

	public int getStatusCode() {
		return response.statusCode();
	}

	public JSONObject asJSONObject() {
		return new JSONObject(asString());
	}

	public JSONArray asJSONArray() {
		return new JSONArray(asString());
	}

	public String asString() {
		return new String(response.body(), StandardCharsets.UTF_8);
	}

	public byte[] asRaw() {
		return response.body();
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
		if(!isSuccess()) throw new IllegalStateException("Request was unsuccessful", getException());
	}

	@Deprecated
	public String getErrorResponse() {
		if(!isSuccess()) return null;
		return asString();
	}

	@Deprecated
	public IOException getException() {
		if(!isSuccess()) return null;
		return new IOException("Got status " + getStatusCode());
	}

}
