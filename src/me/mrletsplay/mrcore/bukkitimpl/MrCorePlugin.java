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
		if(command.getName().equals("mrcore")) {
			UIMultiPage<String> ui = new UIBuilderMultiPage<String>().setLayout(new UILayoutMultiPage())
				.addElement("player_name", new UIElement() {
					
					@Override
					public String getLayout(Player p) {
						return p.getName();
					}
				}.setAction(new UIElementAction() {
					
					@Override
					public void action(UIElementActionEvent event) {
						System.out.println(event.getPlayer().getName());
					}
				}).setHoverText("Click me!"))
				.setLayout(new UILayoutMultiPage()
						.addText("Some ui!")
						.newLine()
						.addElement("player_name")
						.addPageElements(10, true))
				.setSupplier(new ItemSupplier<String>() {
					
					@Override
					public UIElement toUIElement(Player p, String item) {
						return new StaticUIElement(item.toString()).setHoverText("Hover "+item).setAction(new UIElementAction() {
							
							@Override
							public void action(UIElementActionEvent event) {
								event.getPlayer().sendMessage("You clicked: "+item);
							}
						});
					}
					
					@Override
					public List<String> getItems() {
						return Arrays.asList("Some string 1", "Some string 2");
					}
				})
				.build();
			ui.sendToPlayer((Player)sender);
		}
		return true;
	}
	
}
