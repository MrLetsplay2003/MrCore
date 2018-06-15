package me.mrletsplay.mrcore.spiget;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import me.mrletsplay.mrcore.misc.JSON.JSONArray;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;
import me.mrletsplay.mrcore.misc.OtherTools.FriendlyException;

public class SpiGetResource {

	private String description, downloadURL, name, tagLine;
	private boolean external;
	private double fileSize;
	private String fileSizeUnit, fileExtension;
	private int likes, id;
	private List<SpiGetMinecraftVersion> testedVersions;
	private double price;
	private String priceCurrency;
	private BufferedImage icon;
	private String iconURL;
	private long releaseDate, lastUpdateDate;
	private int downloads, latestVersionID;
	private boolean premium;
	private String supportedLanguages;
	private List<SpiGetResourceVersion> versions;
	
	public SpiGetResource(JSONObject spigetResponse) {
		description = new String(Base64.getDecoder().decode(spigetResponse.getString("description")));
		JSONObject file = spigetResponse.getJSONObject("file");
		downloadURL = SpiGet.API_BASE_URL + file.getString("url");
		fileExtension = file.getString("type");
		fileSize = file.getDouble("size");
		fileSizeUnit = file.getString("sizeUnit");
		likes = spigetResponse.getInt("likes");
		testedVersions = spigetResponse.has("testedVersions") ? spigetResponse.getJSONArray("testedVersions").stream().map(o -> SpiGetMinecraftVersion.getByName((String)o)).collect(Collectors.toList()) : new ArrayList<>();
		name = spigetResponse.getString("name");
		tagLine = spigetResponse.has("tag") ? spigetResponse.getString("tag") : null;
		JSONObject icon = spigetResponse.getJSONObject("icon");
		iconURL = icon.getString("url");
		byte[] imgData = Base64.getDecoder().decode(icon.getString("data"));
		try {
			this.icon = ImageIO.read(new ByteArrayInputStream(imgData));
		} catch (IOException e) {
			throw new FriendlyException(e);
		}
		releaseDate = spigetResponse.getLong("releaseDate");
		lastUpdateDate = spigetResponse.getLong("updateDate");
		downloads = spigetResponse.getInt("downloads");
		premium = spigetResponse.has("premium") ? spigetResponse.getBoolean("premium") : false;
		price = spigetResponse.has("price") ? spigetResponse.getDouble("price") : 0;
		priceCurrency = spigetResponse.has("currency") ? spigetResponse.getString("currency") : null;
		supportedLanguages = spigetResponse.has("supportedLanguages") ? spigetResponse.getString("supportedLanguages") : null;
		id = spigetResponse.getInt("id");
		versions = new JSONArray(SpiGet.makeRequest(SpiGet.API_BASE_URL + "/resources/" + id + "/versions")).stream()
					.map(r -> new SpiGetResourceVersion((JSONObject) r))
					.collect(Collectors.toList());
	}
	
	public String getName() {
		return name;
	}
	
	public int getID() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getDownloads() {
		return downloads;
	}
	
	public String getDownloadURL() {
		return downloadURL;
	}
	
	public String getFileExtension() {
		return fileExtension;
	}
	
	public double getFileSize() {
		return fileSize;
	}
	
	public String getFileSizeUnit() {
		return fileSizeUnit;
	}
	
	public BufferedImage getIcon() {
		return icon;
	}
	
	public String getIconURL() {
		return iconURL;
	}
	
	public long getLastUpdateDate() {
		return lastUpdateDate;
	}
	
	public int getLikes() {
		return likes;
	}
	
	public double getPrice() {
		return price;
	}
	
	public String getPriceCurrency() {
		return priceCurrency;
	}
	
	public long getReleaseDate() {
		return releaseDate;
	}
	
	public String getSupportedLanguages() {
		return supportedLanguages;
	}
	
	public String getTagLine() {
		return tagLine;
	}
	
	public List<SpiGetMinecraftVersion> getTestedVersions() {
		return testedVersions;
	}
	
	public boolean isPremium() {
		return premium;
	}
	
	public boolean isFileExternal() {
		return external;
	}
	
	public List<SpiGetResourceVersion> getVersions() {
		return versions;
	}
	
	public int getLatestVersionID() {
		return latestVersionID;
	}
	
	public SpiGetResourceVersion getLatestVersion() {
		return getVersions().stream().filter(v -> v.getID() == latestVersionID).findFirst().orElse(null);
	}
	
	public static class Search {
		
		private String name, tagLine;
		private int id;
		
		public Search(JSONObject spigetResponse) {
			this.name = spigetResponse.getString("name");
			this.tagLine = spigetResponse.getString("tag");
			this.id = spigetResponse.getInt("id");
		}
		
		public String getName() {
			return name;
		}
		
		public String getTagLine() {
			return tagLine;
		}
		
		public int getID() {
			return id;
		}
		
		public SpiGetResource loadResource() {
			return SpiGet.getResource(id);
		}
		
	}
	
}
