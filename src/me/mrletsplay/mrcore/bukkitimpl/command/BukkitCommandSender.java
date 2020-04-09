package me.mrletsplay.mrcore.bukkitimpl.command;

import me.mrletsplay.mrcore.command.CommandSender;

public class BukkitCommandSender implements CommandSender {

	private org.bukkit.command.CommandSender bukkitSender;
	
	public BukkitCommandSender(org.bukkit.command.CommandSender bukkitSender) {
		this.bukkitSender = bukkitSender;
	}

	@Override
	public void sendMessage(String message) {
		bukkitSender.sendMessage(message);
	}
	
	public org.bukkit.command.CommandSender getBukkitSender() {
		return bukkitSender;
	}

}
