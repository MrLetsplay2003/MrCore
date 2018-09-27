package me.mrletsplay.mrcore.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpGet implements HttpRequest {

	private String url;
	private Map<String, String> queryParameters, headerParameters;
	
	/**
	 * Creates a GET request to the specified url
	 * @param url The url this request should be sent to
	 * @see {@link HttpRequest#createGet(String)}
	 */
	public HttpGet(String url) {
		this.url = url;
		this.queryParameters = new HashMap<>();
		this.headerParameters = new HashMap<>();
	}
	
	@Override
	public HttpGet setQueryParameter(String key, String value) {
		queryParameters.put(key, value);
		return this;
	}

	@Override
	public HttpGet setHeaderParameter(String key, String value) {
		headerParameters.put(key, value);
		return this;
	}

	@Override
	public HttpResult execute() {
		try {
			return HttpResult.retrieveFrom(url, "GET", queryParameters, headerParameters, new HashMap<>());
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}

	@Override
	public InputStream executeAsInputStream() {
		try {
			return HttpResult.retrieveAsInputStreamFrom(url, "GET", queryParameters, headerParameters, new HashMap<>());
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}

}
