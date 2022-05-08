package me.mrletsplay.mrcore.http;

import java.io.InputStream;
import java.net.Proxy;
import java.net.http.HttpClient;
import java.time.temporal.TemporalUnit;

import me.mrletsplay.mrcore.http.data.RequestData;

public class HttpGet extends HttpGeneric {

	/**
	 * Creates a GET request to the specified url
	 * @param url The url this request should be sent to
	 * @see HttpRequest#createGet(String)
	 */
	public HttpGet(String url) {
		super("GET", url);
	}
	
	@Override
	public HttpGet setQueryParameter(String key, String value) {
		super.setQueryParameter(key, value);
		return this;
	}
	
	@Override
	public HttpGet addQueryParameter(String key, String value) {
		super.addQueryParameter(key, value);
		return this;
	}

	@Deprecated
	@Override
	public HttpGet setHeaderParameter(String key, String value) {
		super.setHeaderParameter(key, value);
		return this;
	}

	@Override
	public HttpGet setHeader(String key, String value) {
		super.setHeader(key, value);
		return this;
	}
	
	@Override
	public HttpGet setData(RequestData data) {
		super.setData(data);
		return this;
	}
	
	@Deprecated
	@Override
	public HttpGet setTimeout(int timeout) {
		super.setTimeout(timeout);
		return this;
	}
	
	@Override
	public HttpGet setTimeout(long amount, TemporalUnit unit) {
		super.setTimeout(amount, unit);
		return this;
	}
	
	@Override
	public HttpGet setProxy(Proxy proxy) {
		super.setProxy(proxy);
		return this;
	}
	
	@Override
	public HttpGet setClient(HttpClient client) {
		super.setClient(client);
		return this;
	}

	@Override
	public HttpResult execute() {
		return super.execute();
	}

	@Deprecated
	@Override
	public HttpResult executeUntilUnavailable() {
		return super.executeUntilUnavailable();
	}

	@Override
	public InputStream executeAsInputStream() {
		return super.executeAsInputStream();
	}

}
