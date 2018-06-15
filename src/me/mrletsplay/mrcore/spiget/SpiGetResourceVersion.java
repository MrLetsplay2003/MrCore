package me.mrletsplay.mrcore.spiget;

import me.mrletsplay.mrcore.misc.JSON.JSONObject;

public class SpiGetResourceVersion {

	private int id, downloads;
	private String name;
	private long releaseDate;
	
	public SpiGetResourceVersion(JSONObject spigetResponse) {
		this.id = spigetResponse.getInt("id");
		this.downloads = spigetResponse.getInt("downloads");
		this.name = spigetResponse.getString("name");
		releaseDate = spigetResponse.getLong("releaseDate");
	}
	
	public int getID() {
		return id;
	}
	
	public int getDownloads() {
		return downloads;
	}
	
	public String getName() {
		return name;
	}
	
	public long getReleaseDate() {
		return releaseDate;
	}
	
}
