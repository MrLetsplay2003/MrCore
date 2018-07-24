package me.mrletsplay.mrcore.http.webinterface;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.mrletsplay.mrcore.bukkitimpl.BukkitCustomConfig;
import me.mrletsplay.mrcore.bukkitimpl.MrCorePlugin;
import me.mrletsplay.mrcore.config.ConfigExpansions.ExpandableCustomConfig.ObjectMapper;
import me.mrletsplay.mrcore.config.ConfigLoader;

public class WebinterfaceDataManager {
	
	private static BukkitCustomConfig data, config;
	
	private static int port;
	
	private static List<WebinterfaceAccount> accounts;
	
	public static void init() {
		data = ConfigLoader.loadBukkitConfig(new File(MrCorePlugin.getBaseFolder()+"/webinterface", "data.yml"));
		
		data.registerMapper(new ObjectMapper<WebinterfaceAccount>(WebinterfaceAccount.class) {

			@Override
			public Map<String, Object> mapObject(WebinterfaceAccount object) {
				Map<String, Object> map = new HashMap<>();
				map.put("mc-uuid", object.getMinecraftUUID());
				map.put("permissions", object.getPermissions());
				return map;
			}
			
			@Override
			public WebinterfaceAccount constructObject(Map<String, Object> map) {
				if(!requireKeys(map, "mc-uuid")) return null;
				UUID mcUUID = UUID.fromString((String) map.get("mc-uuid"));
				List<String> perms = map.containsKey("permissions") ? castGenericList(castGeneric(map.get("permissions"), List.class), String.class) : new ArrayList<>();
				return new WebinterfaceAccount(mcUUID, perms);
			}
			
		});
		
		accounts = data.getGenericList("accounts", WebinterfaceAccount.class);
		
		config = ConfigLoader.loadBukkitConfig(new File(MrCorePlugin.getBaseFolder()+"/webinterface", "config.yml"));
		
		port = config.getInt("port", 9090, true);
		
		config.saveConfigSafely();
	}
	
	public static WebinterfaceAccount createAccount(UUID mcUUID, String password) {
		WebinterfaceAccount acc = new WebinterfaceAccount(mcUUID, new ArrayList<>());
		data.set("passwords."+acc.getMinecraftUUID(), password);
		accounts.add(acc);
		saveAccounts();
		return acc;
	}
	
	public static void deleteAccount(WebinterfaceAccount account) {
		accounts.remove(account);
		saveAccounts();
	}
	
	public static void saveAccounts() {
		data.set("accounts", accounts);
		data.saveConfigSafely();
	}
	
	@SuppressWarnings("deprecation")
	public static boolean isRegistered(String username) {
		return isRegistered(Bukkit.getOfflinePlayer(username).getUniqueId());
	}
	
	public static boolean isRegistered(UUID mcUUID) {
		return accounts.stream().anyMatch(a -> a.getMinecraftUUID().equals(mcUUID));
	}
	
	public static boolean isRegistered(OfflinePlayer player) {
		return isRegistered(player.getUniqueId());
	}
	
	public static WebinterfaceAccount getByMinecraftPlayer(OfflinePlayer player) {
		return accounts.stream().filter(a -> {
			System.out.println(a);
			System.out.println(a.getMinecraftUUID());
			return a.getMinecraftUUID().equals(player.getUniqueId());
		}).findFirst().orElse(null);
	}
	
	public static WebinterfaceAccount getByMinecraftUUID(UUID mcUUID) {
		return accounts.stream().filter(a -> a.getMinecraftUUID().equals(mcUUID)).findFirst().orElse(null);
	}
	
	public static List<WebinterfaceAccount> getAllAccounts() {
		return accounts;
	}
	
	public static boolean matchesPassword(WebinterfaceAccount account, String password) {
		return data.getString("passwords."+account.getMinecraftUUID()).equals(password);
	}
	
	public static int getWebinterfacePort() {
		return port;
	}
	
}
