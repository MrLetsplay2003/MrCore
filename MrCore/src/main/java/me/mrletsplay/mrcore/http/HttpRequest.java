package me.mrletsplay.mrcore.http;

import java.io.InputStream;
import java.net.Proxy;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.time.temporal.TemporalUnit;

import me.mrletsplay.mrcore.http.data.RequestData;

public interface HttpRequest {

	public static final HttpClient DEFAULT_CLIENT = HttpClient.newBuilder().followRedirects(Redirect.NORMAL).build();

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
	 * Adds a query parameter for this request<br>
	 * Query parameters are appended after the url in the format:<br>
	 * {@code ?query-key=query-value&query-key2=query-value2&query-key3=...}
	 * @param key The key of the query parameter
	 * @param value The value of the query parameter
	 * @return This request
	 */
	public HttpRequest addQueryParameter(String key, String value);

	/**
	 * Sets a header parameter for this request<br>
	 * Header parameters are sent as part of the HTTP header<br>
	 * These may include: Authorization, User-Agent, ...
	 * @param key The key of the header parameter
	 * @param value The value of the header paramezer
	 * @return This request
	 */
	@Deprecated
	public HttpRequest setHeaderParameter(String key, String value);

	/**
	 * Sets a header for this request<br>
	 * Headers are sent as part of the HTTP header<br>
	 * These may include: Authorization, User-Agent, ...
	 * @param key The key of the header parameter
	 * @param value The value of the header paramezer
	 * @return This request
	 */
	public HttpRequest setHeader(String key, String value);

	/**
	 * Sets the data for this request
	 * @param data The data
	 * @return This request
	 */
	public HttpRequest setData(RequestData data);

	/**
	 * Sets the maximum connection timeout for this request
	 * @deprecated Use {@link #setTimeout(long, TemporalUnit)} instead
	 * @param timeout The timeout to set
	 * @return This request
	 * @see #setTimeout(long, TemporalUnit)
	 */
	@Deprecated
	public HttpRequest setTimeout(int timeout);

	/**
	 * Sets the maximum connection timeout for this request
	 * @param amount The timeout to set
	 * @param unit The unit of the timeout
	 * @return This request
	 */
	public HttpRequest setTimeout(long amount, TemporalUnit unit);

	/**
	 * Sets the proxy for this request.<br>
	 * Note: This won't work if you set a custom client for this request using {@link #setClient(HttpClient)}
	 * @param proxy The proxy to use
	 * @return This request
	 */
	public HttpRequest setProxy(Proxy proxy);

	/**
	 * Sets the client for this request
	 * @param client The client to use
	 * @return This request
	 */
	public HttpRequest setClient(HttpClient client);

	/**
	 * Executes the request, reads all bytes and returns the result as an {@link HttpResult}
	 * @return The result received after execution
	 * @throws HttpException If an I/O error occurs while executing the request
	 */
	public HttpResult execute();

	/**
	 * Executes the request, reads all bytes until unavailable and returns the result as an {@link HttpResult}
	 * @deprecated As of MrCore 3.6, this is identical to {@link #execute()}
	 * @return The result received after execution
	 * @throws HttpException If an I/O error occurs while executing the request
	 * @see #execute()
	 */
	@Deprecated
	public HttpResult executeUntilUnavailable();

	/**
	 * Executes the request and returns the result as an {@link InputStream}
	 * @return The InputStream of the result received after execution
	 * @throws HttpException If an I/O error occurs while executing the request
	 */
	public InputStream executeAsInputStream();

	/**
	 * Creates a generic request represented by an {@link HttpGeneric} instance
	 * @param requestMethod The request method to use
	 * @param url The url the request should be sent to
	 * @return The request
	 */
	public static HttpGeneric createGeneric(String requestMethod, String url) {
		return new HttpGeneric(requestMethod, url);
	}

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
