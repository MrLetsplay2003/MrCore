package me.mrletsplay.mrcore.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.data.RawData;
import me.mrletsplay.mrcore.http.data.RequestData;

public class HttpGeneric implements HttpRequest {

	private String requestMethod, url;
	private Map<String, List<String>> queryParameters;
	private Map<String, String> headers;
	protected RequestData data;
	private Duration timeout;
	private Proxy proxy;
	private HttpClient client;
	
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
		this.headers = new HashMap<>();
	}
	
	@Deprecated
	public HttpGeneric setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
		return this;
	}
	
	@Deprecated
	public HttpGeneric setContent(byte[] content) {
		this.data = new RawData("application/x-www-form-urlencoded", content);
		return this;
	}
	
	@Override
	public HttpGeneric setData(RequestData data) {
		this.data = data;
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

	@Deprecated
	@Override
	public HttpGeneric setHeaderParameter(String key, String value) {
		headers.put(key, value);
		return this;
	}

	@Override
	public HttpGeneric setHeader(String key, String value) {
		headers.put(key, value);
		return this;
	}
	
	@Deprecated
	@Override
	public HttpGeneric setTimeout(int timeout) {
		this.timeout = Duration.ofMillis(timeout);
		return this;
	}
	
	@Override
	public HttpGeneric setTimeout(long amount, TemporalUnit unit) {
		this.timeout = Duration.of(amount, unit);
		return this;
	}
	
	@Override
	public HttpGeneric setProxy(Proxy proxy) {
		this.proxy = proxy;
		return this;
	}
	
	@Override
	public HttpGeneric setClient(HttpClient client) {
		this.client = client;
		return this;
	}
	
	private HttpClient getClient() {
		if(client != null) return client;
		if(proxy == null) return HttpRequest.DEFAULT_CLIENT;
		return HttpClient.newBuilder()
				.proxy(new ProxySelector() {
					
					@Override
					public List<Proxy> select(URI uri) {
						return Collections.singletonList(proxy);
					}
					
					@Override
					public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
						
					}
				})
				.build();
	}
	
	private java.net.http.HttpRequest createRequest() {
		String reqURL = url + queryParameters.entrySet().stream()
			.map(e -> e.getValue().stream()
				.map(v -> HttpUtils.urlEncode(e.getKey()) + "=" + HttpUtils.urlEncode(v))
				.collect(Collectors.joining("&")))
			.collect(Collectors.joining("&", "?", ""));
		
		var builder = java.net.http.HttpRequest.newBuilder(URI.create(reqURL));
		if(timeout != null) builder.timeout(timeout);
		builder.method(requestMethod, data == null ? BodyPublishers.noBody() : BodyPublishers.ofByteArray(data.createPayload()));
		headers.forEach(builder::header);
		if(data != null) builder.header("Content-Type", data.getMimeType());
		return builder.build();
	}
	
	@Override
	public HttpResult execute() {
		try {
			return new HttpResult(getClient().send(createRequest(), BodyHandlers.ofByteArray()));
		} catch (IOException | InterruptedException e) {
			throw new HttpException(e);
		}
	}
	
	@Deprecated
	@Override
	public HttpResult executeUntilUnavailable() {
		return execute();
	}

	@Override
	public InputStream executeAsInputStream() {
		try {
			return getClient().send(createRequest(), BodyHandlers.ofInputStream()).body();
		} catch (IOException | InterruptedException e) {
			throw new HttpException(e);
		}
	}
	
}
