package me.mrletsplay.mrcore.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

public class HttpUtils {

	/**
	 * Constructs a Http POST request with the given (POST) parameters
	 * @param url The url to send the request to
	 * @param params The POST parameters for this request
	 * @return An InputStream to that given url
	 * @throws IOException If an IO error occurs while making the request
	 * @deprecated Has been replaced by {@link HttpPost}
	 * @see {@link HttpRequest#createPost(String)}
	 */
	@Deprecated
	public static InputStream httpPost(URL url, String... params) throws IOException {
		String urlParameters = Arrays.stream(params).collect(Collectors.joining("&"));
		byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
		int postDataLength = postData.length;
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();           
		conn.setDoOutput(true);
		conn.setInstanceFollowRedirects(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
		conn.setRequestProperty("charset", "utf-8");
		conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
		conn.setUseCaches(false);
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.write(postData);
		InputStream in = conn.getInputStream();
		return in;
	}
	
	/**
	 * Constructs a Http GET request with the given (GET) parameters
	 * @param url The url to send the request to
	 * @param params The GET parameters for this request
	 * @return An InputStream to that given url
	 * @throws IOException If an IO error occurs while making the request
	 * @deprecated Has been replaced by {@link HttpGet}
	 * @see {@link HttpRequest#createGet(String)}
	 */
	@Deprecated
	public static InputStream httpGet(URL url, String... params) throws IOException {
		URL url2 = new URL(url.toString()+(params.length == 0 ? "" : "?"+Arrays.stream(params).collect(Collectors.joining("&"))));
		InputStream in = url2.openStream();
		return in;
	}
	
	/**
	 * Downloads a file from a given url
	 * @param url The url to download from
	 * @param file The file to save to
	 * @return The amount of bytes downloaded
	 * @throws IOException If an IO error occurs while downloading the file
	 */
	@Deprecated
	public static long download(URL url, File file) throws IOException {
		file.getParentFile().mkdirs();
		ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		FileOutputStream fos = new FileOutputStream(file);
		long b = fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
		return b;
	}
	
	public static String urlEncode(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new HttpException(e);
		}
	}

}
