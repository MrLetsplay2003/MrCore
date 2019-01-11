package me.mrletsplay.mrcore.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpGeneric implements HttpRequest {

	private String requestMethod, url;
	private Map<String, String> queryParameters, headerParameters;
	private byte[] content;
	
	/**
	 * Creates a request using the given request method to the specified url
	 * @param url The url this request should be sent to
	 * @see {@link HttpRequest#createGet(String)}
	 */
	public HttpGeneric(String requestMethod, String url) {
		this.requestMethod = requestMethod;
		this.url = url;
		this.queryParameters = new HashMap<>();
		this.headerParameters = new HashMap<>();
	}
	
	public HttpGeneric setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
		return this;
	}
	
	public HttpGeneric setContent(byte[] content) {
		this.content = content;
		return this;
	}
	
	@Override
	public HttpGeneric setQueryParameter(String key, String value) {
		queryParameters.put(key, value);
		return this;
	}

	@Override
	public HttpGeneric setHeaderParameter(String key, String value) {
		headerParameters.put(key, value);
		return this;
	}
	
	@Override
	public HttpResult execute() {
		try {
			return HttpResult.retrieveFrom(url, requestMethod, queryParameters, headerParameters, content, false);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}
	
	@Override
	public HttpResult executeUntilUnavailable() {
		try {
			return HttpResult.retrieveFrom(url, requestMethod, queryParameters, headerParameters, content, true);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}

	@Override
	public InputStream executeAsInputStream() {
		try {
			return HttpResult.retrieveAsInputStreamFrom(url, requestMethod, queryParameters, headerParameters, content);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}
	
}
