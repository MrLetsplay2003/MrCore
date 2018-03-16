package me.mrletsplay.mrcore.main;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

public class HttpUtils {

	public static String httpPost(URL url, String... params) throws IOException {
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
		byte[] dat = new byte[1024];
		in.read(dat);
		return new String(dat).trim();
	}
	
	public static String httpGet(URL url, String... params) throws IOException {
		URL url2 = new URL(url.toString()+"?"+Arrays.stream(params).collect(Collectors.joining("&")));
		InputStream in = url2.openStream();
		byte[] dat = new byte[1024];
		in.read(dat);
		return new String(dat).trim();
	}

}
