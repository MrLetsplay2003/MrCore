package me.mrletsplay.mrcore.bukkitimpl.command;

import me.mrletsplay.mrcore.command.properties.CommandProperties;

public class BukkitCommandProperties implements CommandProperties {
	
	/**
	 * This is the default value for a permission.<br>
	 * See {@link #setPermission(String)} for the default behavior.
	 */
	public static final String PERMISSION_DEFAULT = "perm-default";
	
	private String permission = PERMISSION_DEFAULT;
	
	/**
	 * Sets the (explicit) permission required to use this command.<br>
	 * If a player doesn't have this permission, they will get an error message and the command will not be executed.<br>
	 * Set it to <code>null</code> to explicitly require no permission.<br>
	 * By default, a player will require the permission of the nearest parent command (up the chain) that has an explicit permission set to use this command.<br>
	 * Use {@link #PERMISSION_DEFAULT} to reset it to the default behavior.
	 * @param permission The permission
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	/**
	 * Returns the permission required to execute this command.<br>
	 * May return <code>null</code> if none is set.
	 * @return The permission
	 */
	public String getPermission() {
		return permission;
	}
	
}
