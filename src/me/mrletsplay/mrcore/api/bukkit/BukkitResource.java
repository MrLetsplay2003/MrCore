package me.mrletsplay.mrcore.api.bukkit;

import me.mrletsplay.mrcore.json.JSONObject;

public class BukkitResource {

	private int id;
	private String name, slug, stage;
	
	public BukkitResource(JSONObject bukkitResponse) {
		this.id = bukkitResponse.getInt("id");
		this.name = bukkitResponse.getString("name");
		this.slug = bukkitResponse.getString("slug");
		this.stage = bukkitResponse.getString("stage");
	}
	
	public int getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSlug() {
		return slug;
	}
	
	public String getStage() {
		return stage;
	}
	
	public BukkitFile loadResourceFile() {
		return BukkitAPI.getBukkitFile(id);
	}
	
}
