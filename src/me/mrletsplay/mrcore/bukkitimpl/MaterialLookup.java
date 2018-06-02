package me.mrletsplay.mrcore.bukkitimpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.mrcore.misc.JSON.JSONArray;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;

public class MaterialLookup {
	
	private static final String ID_LOOKUP_LIST_LINK = "http://minecraft-ids.grahamedgecombe.com/items.json";
	
	private static List<MaterialLookup> lookups;
	
	public static void loadAll() {
		lookups = new ArrayList<>();
		try {
			InputStream in = HttpUtils.httpGet(new URL(ID_LOOKUP_LIST_LINK));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[4096];
			int len;
			while((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			JSONArray allObjects = new JSONArray(out.toString());
			allObjects.forEach(o -> lookups.add(new MaterialLookup((JSONObject) o)));
		}catch(IOException e) {
			MrCorePlugin.getInstance().getLogger().warning("Failed to load material lookup list");
		}
	}

	public static MaterialLookup byMaterial(Material material) {
		return byMaterial(material, (short) 0);
	}

	public static MaterialLookup byMaterial(Material material, short data) {
		return lookups.stream().filter(m -> m.material.equals(material) && m.data == data).findFirst().orElse(null);
	}

	@SuppressWarnings("deprecation")
	public static MaterialLookup byMaterial(MaterialData materialData) {
		return byMaterial(materialData.getItemType(), materialData.getData());
	}

	public static MaterialLookup byID(int id, short data) {
		return lookups.stream().filter(m -> m.typeID == id && m.data == data).findFirst().orElse(null);
	}

	public static MaterialLookup byMinecraftID(String minecraftID) {
		return lookups.stream().filter(m -> m.minecraftID.equalsIgnoreCase(minecraftID)).findFirst().orElse(null);
	}
	
	private Material material;
	private int typeID;
	private byte data;
	private String friendlyName, minecraftID;
	
//	@SuppressWarnings("deprecation")
//	private MaterialLookup(Material material) {
//		this.material = material;
//		this.lookup = lookup(material.getId());
//	}
//	
//	@SuppressWarnings("deprecation")
//	private MaterialLookup(int id, short data) {
//		this(Material.getMaterial(id));
//	}
	
	@SuppressWarnings("deprecation")
	private MaterialLookup(JSONObject lookupData) {
		this.typeID = lookupData.getInt("type");
		this.material = Material.getMaterial(typeID);
		this.data = (byte) lookupData.getInt("meta");
		this.friendlyName = lookupData.getString("name");
		this.minecraftID = lookupData.getString("text_type");
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public int getTypeID() {
		return typeID;
	}
	
	public byte getData() {
		return data;
	}
	
	public String getFriendlyName() {
		return friendlyName;
	}
	
	public String getMinecraftID() {
		return minecraftID;
	}
	
}
