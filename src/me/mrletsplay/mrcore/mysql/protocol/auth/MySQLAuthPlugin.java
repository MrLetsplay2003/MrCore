package me.mrletsplay.mrcore.mysql.protocol.auth;

import java.util.Arrays;

public enum MySQLAuthPlugin {

	CACHING_SHA2_PASSWORD("caching_sha2_password", MySQLCachingSha2Auth.class),
	MYSQL_NATIVE_PASSWORD("mysql_native_password", MySQLNative41PasswordAuth.class),
	SHA256_PASSWORD("sha256_password", null);
	
	private String name;
	private Class<? extends MySQLAuthPluginBase> clazz;
	
	private MySQLAuthPlugin(String name, Class<? extends MySQLAuthPluginBase> clazz) {
		this.name = name;
		this.clazz = clazz;
	}
	
	public String getName() {
		return name;
	}
	
	public Class<? extends MySQLAuthPluginBase> getAuthPluginClass() {
		return clazz;
	}
	
	public MySQLAuthPluginBase newInstance() {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static MySQLAuthPlugin getByName(String name) {
		return Arrays.stream(values()).filter(pl -> pl.getName().equals(name)).findFirst().orElse(null);
	}
	
}