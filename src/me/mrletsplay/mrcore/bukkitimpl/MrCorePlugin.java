package me.mrletsplay.mrcore.bukkitimpl;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.mrletsplay.mrcore.bukkitimpl.ChatUI.ItemSupplier;
import me.mrletsplay.mrcore.bukkitimpl.ChatUI.StaticUIElement;
import me.mrletsplay.mrcore.bukkitimpl.ChatUI.UIBuilderMultiPage;
import me.mrletsplay.mrcore.bukkitimpl.ChatUI.UIElement;
import me.mrletsplay.mrcore.bukkitimpl.ChatUI.UIElementAction;
import me.mrletsplay.mrcore.bukkitimpl.ChatUI.UIElementActionEvent;
import me.mrletsplay.mrcore.bukkitimpl.ChatUI.UILayoutMultiPage;
import me.mrletsplay.mrcore.bukkitimpl.ChatUI.UIListener;
import me.mrletsplay.mrcore.bukkitimpl.ChatUI.UIMultiPage;
import me.mrletsplay.mrcore.bukkitimpl.GUIUtils.GUIListener;

public class MrCorePlugin extends JavaPlugin{
	
	@Override
	public void onEnable() {
		getLogger().info("And MrCore is on board as well! :wave:");
		Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
		getCommand("mrcoreui").setExecutor(new UIListener());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return true;
	}
	
}
