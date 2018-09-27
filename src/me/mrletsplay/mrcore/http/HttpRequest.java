package me.mrletsplay.mrcore.http;

import java.io.InputStream;

public interface HttpRequest {
	
	/**
	 * Sets a query parameter for this request<br>
	 * Query parameters are appended after the url in the format:<br>
	 * {@code ?query-key=query-value&query-key2=query-value2&query-key3=...}
	 * @param key The key of the query parameter
	 * @param value The value of the query parameter
	 * @return This request
	 */
	public HttpRequest setQueryParameter(String key, String value);
	
	/**
	 * Sets a header parameter for this request<br>
	 * Header parameters are sent as part of the HTTP header<br>
	 * These may include: Authorization, User-Agent, ...
	 * @param key The key of the header parameter
	 * @param value The value of the header paramezer
	 * @return This request
	 */
	public HttpRequest setHeaderParameter(String key, String value);

	/**
	 * Executes the request and returns the result as an {@link HttpResult}
	 * @return The result received after execution
	 * @throws HttpException If an I/O error occurs while executing the request
	 */
	public HttpResult execute();

	/**
	 * Executes the request and returns the result as an {@link InputStream}
	 * @return The InputStream of the result received after execution
	 * @throws HttpException If an I/O error occurs while executing the request
	 */
	public InputStream executeAsInputStream();
	
	/**
	 * Creates a POST request represented by an {@link HttpPost} instance
	 * @param url The url the request should be sent to
	 * @return The request
	 */
	public static HttpPost createPost(String url) {
		return new HttpPost(url);
	}

	/**
	 * Creates a GET request represented by an {@link HttpGet} instance
	 * @param url The url the request should be sent to
	 * @return The request
	 */
	public static HttpGet createGet(String url) {
		return new HttpGet(url);
	}
	
}
