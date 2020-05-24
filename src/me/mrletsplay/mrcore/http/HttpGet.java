package me.mrletsplay.mrcore.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpGet implements HttpRequest {

	private String url;
	private Map<String, List<String>> queryParameters;
	private Map<String, String> headerParameters;
	private int timeout;
	private Proxy proxy;
	
	/**
	 * Creates a GET request to the specified url
	 * @param url The url this request should be sent to
	 * @see HttpRequest#createGet(String)
	 */
	public HttpGet(String url) {
		this.url = url;
		this.queryParameters = new HashMap<>();
		this.headerParameters = new HashMap<>();
	}
	
	@Override
	public HttpGet setQueryParameter(String key, String value) {
		List<String> v = new ArrayList<>();
		v.add(value);
		queryParameters.put(key, v);
		return this;
	}
	
	@Override
	public HttpGet addQueryParameter(String key, String value) {
		List<String> v = queryParameters.getOrDefault(key, new ArrayList<>());
		v.add(value);
		queryParameters.put(key, v);
		return this;
	}

	@Override
	public HttpGet setHeaderParameter(String key, String value) {
		headerParameters.put(key, value);
		return this;
	}
	
	@Override
	public HttpGet setTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}
	
	@Override
	public HttpGet setProxy(Proxy proxy) {
		this.proxy = proxy;
		return this;
	}

	@Override
	public HttpResult execute() {
		try {
			return HttpResult.retrieveFrom(url, "GET", queryParameters, headerParameters, null, timeout, proxy, false);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}

	@Override
	public HttpResult executeUntilUnavailable() {
		try {
			return HttpResult.retrieveFrom(url, "GET", queryParameters, headerParameters, null, timeout, proxy, true);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}

	@Override
	public InputStream executeAsInputStream() {
		try {
			return HttpResult.retrieveAsInputStreamFrom(url, "GET", queryParameters, headerParameters, null, timeout, proxy);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}

}
