package me.mrletsplay.mrcore.api.bukkit;

import me.mrletsplay.mrcore.json.JSONObject;

public class BukkitFile {

	private int projectId;
	private String downloadURL, fileName, fileURL, gameVersion, md5, name, releaseType;
	
	public BukkitFile(JSONObject bukkitResponse) {
		this.projectId = bukkitResponse.getInt("projectId");
		this.downloadURL = bukkitResponse.getString("downloadUrl");
		this.fileName = bukkitResponse.getString("fileName");
		this.fileURL = bukkitResponse.getString("fileUrl");
		this.gameVersion = bukkitResponse.getString("gameVersion");
		this.md5 = bukkitResponse.getString("md5");
		this.name = bukkitResponse.getString("name");
		this.releaseType = bukkitResponse.getString("releaseType");
	}
	
	public int getProjectID() {
		return projectId;
	}
	
	public String getDownloadURL() {
		return downloadURL;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getFileURL() {
		return fileURL;
	}
	
	public String getGameVersion() {
		return gameVersion;
	}
	
	public String getMD5() {
		return md5;
	}
	
	public String getName() {
		return name;
	}
	
	public String getReleaseType() {
		return releaseType;
	}
	
}
