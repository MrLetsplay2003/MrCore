package me.mrletsplay.mrcore.bukkitimpl.gui;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.mrletsplay.mrcore.bukkitimpl.MrCorePlugin;
import me.mrletsplay.mrcore.bukkitimpl.gui.event.GUIActionEvent;
import me.mrletsplay.mrcore.bukkitimpl.gui.event.GUIClosedEvent;
import me.mrletsplay.mrcore.bukkitimpl.gui.event.GUIDragDropEvent;
import me.mrletsplay.mrcore.bukkitimpl.gui.event.GUIElementActionEvent;

/**
 * Do NOT register this listener yourself. It will be handled by MrCore and registering it will cause double calls to GUIs
 * @author MrLetsplay2003
 */
public class GUIListener implements Listener {
	
	@EventHandler
	public void onInvDrag(InventoryDragEvent e) {
		if(e.getInventory() == null) return;
		if(GUI.getGUI(e.getInventory()) != null) {
			e.setCancelled(true);
			return;
		}
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getClickedInventory() == null) return;
		Inventory inv = e.getInventory();
		Player player = (Player) e.getWhoClicked();
		if(inv.getHolder() instanceof GUIHolder) {
			e.setResult(Result.ALLOW);
			GUIHolder holder = (GUIHolder) inv.getHolder();
			GUI gui = holder.getGUI();
			if(!e.isLeftClick() && !e.isRightClick()) {
				e.setCancelled(true);
				return;
			}
			if(e.getClickedInventory().equals(e.getInventory())) {
				//Gui clicked
				int slot = e.getSlot();
				GUIElement elClicked = gui.getElementBySlot(slot);
				e.setCancelled(true);
				if(elClicked != null && elClicked.getAction() != null) {
					GUIElementActionEvent event = new GUIElementActionEvent(player, inv, gui, holder, e);
					elClicked.getAction().onAction(event);
					if(!event.isCancelled()) e.setCancelled(false);
				}
				if(gui.getBuilder().getActionListener() != null) {
					GUIActionEvent event = new GUIActionEvent(player, elClicked, e.getClickedInventory(), gui, holder, e);
					gui.getBuilder().getActionListener().onAction(event);
					if(!event.isCancelled()) e.setCancelled(false);
				}
				if(gui instanceof GUIMultiPage<?>) {
					Map<Integer, GUIElement> pageElements = (Map<Integer, GUIElement>) holder.getProperties().get("page-elements");
					GUIElement pElClicked = pageElements.get(slot);
					if(pElClicked != null && pElClicked.getAction() != null) {
						GUIElementActionEvent event = new GUIElementActionEvent(player, inv, gui, holder, e);
						pElClicked.getAction().onAction(event);
						if(!event.isCancelled()) e.setCancelled(false);
					}
				}
			} else {
				//Player inv clicked
				
				if(e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
					e.setCancelled(true);
					return;
				}
				
				if(gui.getBuilder().getDragDropListener() != null) {
					ItemStack pickedUp = e.getCurrentItem();
					if(pickedUp!=null) {
						GUIDragDropEvent event = new GUIDragDropEvent(player, pickedUp, e.getClickedInventory(), gui, holder, e);
						gui.getBuilder().getDragDropListener().onDragDrop(event);
						if(event.isCancelled()) {
							e.setCancelled(true);
						}
					}
				}else {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onInvClose(InventoryCloseEvent e) {
		Inventory inv = e.getInventory();
		Player player = (Player) e.getPlayer();
		if(inv.getHolder() instanceof GUIHolder) {
			GUIHolder holder = (GUIHolder) inv.getHolder();
			GUI gui = holder.getGUI();
			if(gui.getBuilder().getClosedListener() != null) {
				GUIClosedEvent ev = new GUIClosedEvent(holder, player, inv);
				gui.getBuilder().getClosedListener().onClosed(ev);
				if(ev.isCancelled()) {
					holder.cps();
					Bukkit.getScheduler().runTaskLater(MrCorePlugin.pl, () -> {
						player.openInventory(inv);
					}, 1);
				}
			}
		}
	}
	
}
