package me.mrletsplay.mrcore.spiget;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;
import me.mrletsplay.mrcore.misc.OtherTools.FriendlyException;

public class SpiGet {

	public static final String API_BASE_URL = "https://api.spiget.org/v2/";
	
	public static SpiGetResource getResource(String resourceID) {
		try {
			InputStream stream = HttpUtils.httpGet(new URL(API_BASE_URL + "/resources/" + resourceID));
			byte[] buf = new byte[4096];
			int len;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			while((len = stream.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			String response = new String(out.toByteArray());
			return new SpiGetResource(new JSONObject(response));
		} catch (IOException e) {
			throw new FriendlyException(e);
		}
	}
	
}
