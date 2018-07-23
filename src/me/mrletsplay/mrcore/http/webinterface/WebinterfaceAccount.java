package me.mrletsplay.mrcore.http.webinterface;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class WebinterfaceAccount {

	private UUID minecraftUUID;
	private List<String> permissions;
	
	public WebinterfaceAccount(UUID minecraftUUID, List<String> permissions) {
		this.minecraftUUID = minecraftUUID;
		this.permissions = permissions;
	}
	
	public UUID getMinecraftUUID() {
		return minecraftUUID;
	}
	
	public OfflinePlayer getMinecraftPlayer() {
		return Bukkit.getPlayer(minecraftUUID);
	}
	
	public List<String> getPermissions() {
		return permissions;
	}
	
	public boolean matchesPassword(String password) {
		return WebinterfaceDataManager.matchesPassword(this, password);
	}
	
}
