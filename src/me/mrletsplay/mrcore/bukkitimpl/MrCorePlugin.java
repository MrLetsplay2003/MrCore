package me.mrletsplay.mrcore.bukkitimpl;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import me.mrletsplay.mrcore.bukkitimpl.GUIUtils.GUIBuilderMultiPage;
import me.mrletsplay.mrcore.bukkitimpl.GUIUtils.GUIListener;
import me.mrletsplay.mrcore.bukkitimpl.GUIUtils.GUIMultiPage;
import me.mrletsplay.mrcore.bukkitimpl.GUIUtils.ItemSupplier;

public class MrCorePlugin extends JavaPlugin{
	
	private GUIMultiPage<Integer> gui;
	
	@Override
	public void onEnable() {
		getLogger().info("And MrCore is on board as well! :wave:");
		Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
		gui = new GUIBuilderMultiPage<Integer>("tes", 1)
				.addCustomItemSlots(0, 2, 4, 6, 8)
				.setSupplier(new ItemSupplier<Integer>() {
					
					@Override
					public ItemStack toItemStack(Integer item) {
						ItemStack it = new ItemStack(Material.PAPER);
						ItemMeta im = it.getItemMeta();
						im.setDisplayName("#"+item);
						it.setItemMeta(im);
						return it;
					}
					
					@Override
					public List<Integer> getItems() {
						return Arrays.asList(
								1, 2, 3, 4, 5, 6, 7, 8, 9
							);
					}
				})
				.addNextPageItem(1, new ItemStack(Material.RED_ROSE))
				.addPreviousPageItem(3, new ItemStack(Material.INK_SACK))
				.build();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		((Player)sender).openInventory(gui.getForPlayer(((Player)sender), 0));
		return true;
	}
	
}
