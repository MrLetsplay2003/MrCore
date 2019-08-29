package me.mrletsplay.mrcore.bukkitimpl.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mrletsplay.mrcore.bukkitimpl.ui.event.UIElementActionEvent;

public class UIListener implements CommandExecutor {
	
	public static List<UI> uis = new ArrayList<>();
	public static HashMap<String, UIInstance> instances = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!command.getName().equals("mrcoreui")) return true;
		if(!(sender instanceof Player)) {
			sender.sendMessage("UIs are only available to players");
			return true;
		}
		Player p = (Player) sender;
		if(!requireArgs(p, args, 2)) return true;
		String insID = args[0];
		String elID = args[1];
		UIInstance instance = UIListener.instances.get(insID);
		if(instance == null) {
			p.sendMessage("Invalid instance");
			return true;
		}
		HashMap<String, UIElement> elements = (HashMap<String, UIElement>) instance.getProperties().get("elements");
		UIElement el = elements.get(elID);
		if(el == null) {
			p.sendMessage("Invalid element");
			return true;
		}
		if(el.getAction() == null) {
			p.sendMessage("Invalid action");
			return true;
		}
		el.getAction().action(new UIElementActionEvent(instance, p));
		return true;
	}
	
	private boolean requireArgs(Player p, String[] args, int n) {
		if(args.length >= n) return true;
		p.sendMessage("Invalid syntax");
		return false;
	}
	
}
