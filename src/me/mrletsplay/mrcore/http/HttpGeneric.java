package me.mrletsplay.mrcore.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpGeneric implements HttpRequest {

	private String requestMethod, url;
	private Map<String, List<String>> queryParameters;
	private Map<String, String> headerParameters;
	private byte[] content;
	private int timeout;
	private Proxy proxy;
	
	/**
	 * Creates a request using the given request method to the specified url
	 * @param requestMethod The request method to use
	 * @param url The url this request should be sent to
	 * @see HttpRequest#createGet(String)
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
		List<String> v = new ArrayList<>();
		v.add(value);
		queryParameters.put(key, v);
		return this;
	}
	
	@Override
	public HttpGeneric addQueryParameter(String key, String value) {
		List<String> v = queryParameters.getOrDefault(key, new ArrayList<>());
		v.add(value);
		queryParameters.put(key, v);
		return this;
	}

	@Override
	public HttpGeneric setHeaderParameter(String key, String value) {
		headerParameters.put(key, value);
		return this;
	}
	
	@Override
	public HttpGeneric setTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}
	
	@Override
	public HttpGeneric setProxy(Proxy proxy) {
		this.proxy = proxy;
		return this;
	}
	
	@Override
	public HttpResult execute() {
		try {
			return HttpResult.retrieveFrom(url, requestMethod, queryParameters, headerParameters, content, timeout, proxy, false);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}
	
	@Override
	public HttpResult executeUntilUnavailable() {
		try {
			return HttpResult.retrieveFrom(url, requestMethod, queryParameters, headerParameters, content, timeout, proxy, true);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}

	@Override
	public InputStream executeAsInputStream() {
		try {
			return HttpResult.retrieveAsInputStreamFrom(url, requestMethod, queryParameters, headerParameters, content, timeout, proxy);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}
	
}
