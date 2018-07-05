package me.mrletsplay.mrcore.http.server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class ParsedURL {

	private String path;
	private Map<String, String> getParams;
	private String query;
	
	public ParsedURL(String path, Map<String, String> getParams, String query) {
		this.path = path;
		this.getParams = getParams;
		this.query = query;
	}
	
	public String getPath() {
		return path;
	}
	
	public Map<String, String> getGetParameters() {
		return getParams;
	}
	
	public String getQuery() {
		return query;
	}
	
	public static ParsedURL parse(String raw) {
		int qMInd = raw.indexOf('?');
		String query = null;
		Map<String, String> getParams = new HashMap<>();
		if(qMInd >= 0) {
			String gP = raw.substring(qMInd + 1);
			for(String qPair : gP.split("&")) {
				String[] kV = qPair.split("=", 2);
				try {
					getParams.put(kV[0],kV.length==2?URLDecoder.decode(kV[1], "UTF-8"):null);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			raw = raw.substring(0, qMInd);
		}
		return new ParsedURL(raw, getParams, query);
	}
	
}
