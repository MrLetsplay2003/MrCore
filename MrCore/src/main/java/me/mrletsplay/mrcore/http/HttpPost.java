package me.mrletsplay.mrcore.http;

import java.io.InputStream;
import java.net.Proxy;
import java.net.http.HttpClient;
import java.time.temporal.TemporalUnit;

import me.mrletsplay.mrcore.http.data.RequestData;
import me.mrletsplay.mrcore.http.data.URLEncodedData;

public class HttpPost extends HttpGeneric {
	
	/**
	 * Creates a POST request to the specified url
	 * @param url The url this request should be sent to
	 * @see HttpRequest#createPost(String)
	 */
	public HttpPost(String url) {
		super("POST", url);
	}
	
	@Override
	public HttpPost setQueryParameter(String key, String value) {
		super.setQueryParameter(key, value);
		return this;
	}
	
	@Override
	public HttpPost addQueryParameter(String key, String value) {
		super.addQueryParameter(key, value);
		return this;
	}

	@Deprecated
	@Override
	public HttpPost setHeaderParameter(String key, String value) {
		super.setHeaderParameter(key, value);
		return this;
	}

	@Override
	public HttpPost setHeader(String key, String value) {
		super.setHeader(key, value);
		return this;
	}
	
	@Override
	public HttpPost setData(RequestData data) {
		super.setData(data);
		return this;
	}
	
	@Deprecated
	@Override
	public HttpPost setTimeout(int timeout) {
		super.setTimeout(timeout);
		return this;
	}
	
	@Override
	public HttpPost setTimeout(long amount, TemporalUnit unit) {
		super.setTimeout(amount, unit);
		return this;
	}
	
	@Override
	public HttpPost setProxy(Proxy proxy) {
		super.setProxy(proxy);
		return this;
	}
	
	@Override
	public HttpPost setClient(HttpClient client) {
		super.setClient(client);
		return this;
	}
	
	/**
	 * Sets a post parameter for this request
	 * @deprecated Use {@link #setData(RequestData)} with {@link URLEncodedData} instead
	 * @param key The key of the post parameter
	 * @param value The value of the post parameter
	 * @return This request
	 */
	@Deprecated
	public HttpPost setPostParameter(String key, String value) {
		if(data == null) data = new URLEncodedData();
		if(!(data instanceof URLEncodedData)) throw new UnsupportedOperationException("Data is not url encoded");
		((URLEncodedData) data).set(key, value);
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
