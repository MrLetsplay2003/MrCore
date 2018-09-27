package me.mrletsplay.mrcore.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpPost implements HttpRequest {

	private String url;
	private Map<String, String> queryParameters, headerParameters, postParameters;

	/**
	 * Creates a POST request to the specified url
	 * @param url The url this request should be sent to
	 * @see {@link HttpRequest#createPost(String)}
	 */
	public HttpPost(String url) {
		this.url = url;
		this.queryParameters = new HashMap<>();
		this.headerParameters = new HashMap<>();
		this.postParameters = new HashMap<>();
	}
	
	@Override
	public HttpPost setQueryParameter(String key, String value) {
		queryParameters.put(key, value);
		return this;
	}

	@Override
	public HttpPost setHeaderParameter(String key, String value) {
		headerParameters.put(key, value);
		return this;
	}
	
	/**
	 * Sets a post parameter for this request
	 * @param key The key of the post parameter
	 * @param value The value of the post parameter
	 * @return This request
	 */
	public HttpPost setPostParameter(String key, String value) {
		postParameters.put(key, value);
		return this;
	}

	@Override
	public HttpResult execute() {
		try {
			return HttpResult.retrieveFrom(url, "POST", queryParameters, headerParameters, postParameters);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}

	@Override
	public InputStream executeAsInputStream() {
		try {
			return HttpResult.retrieveAsInputStreamFrom(url, "POST", queryParameters, headerParameters, postParameters);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}

}
