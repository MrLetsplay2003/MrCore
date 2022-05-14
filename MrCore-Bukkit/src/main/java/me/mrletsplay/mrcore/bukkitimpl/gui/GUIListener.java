package me.mrletsplay.mrcore.bukkitimpl.gui;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import me.mrletsplay.mrcore.bukkitimpl.gui.event.GUIPutItemEvent;
import me.mrletsplay.mrcore.bukkitimpl.gui.event.GUITakeItemEvent;

/**
 * Do NOT register this listener yourself. It will be handled by MrCore and registering it will cause double calls to GUIs
 * @author MrLetsplay2003
 */
public class GUIListener implements Listener {

	@EventHandler
	public void onInvDrag(InventoryDragEvent e) {
		if(e.getInventory() == null) return;
		GUIHolder holder = GUI.getGUIHolder(e.getInventory());
		if(holder != null) {
			e.setCancelled(true);
			GUI gui = holder.getGUI();
			if(e.getNewItems().size() == 1) {
				if(gui.getPutItemListener() != null) {
					int slot = e.getNewItems().keySet().iterator().next();
					if(slot >= e.getInventory().getSize()) {
						e.setCancelled(false);
						return; // Player inv
					}
					ItemStack tbp = e.getNewItems().get(slot).clone();

					GUIPutItemEvent p = new GUIPutItemEvent((Player) e.getWhoClicked(), e, e.getInventory(), gui, holder, tbp, tbp, slot);
					gui.getPutItemListener().onPutItem(p);
					if(!p.isCancelled()) {
						e.setCancelled(false);
						p.getCallback().run();
					}
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getClickedInventory() == null) return;
		Inventory inv = e.getInventory();
		Player player = (Player) e.getWhoClicked();
		if(inv.getHolder() instanceof GUIHolder) {
			e.setResult(Result.ALLOW);
			GUIHolder holder = (GUIHolder) inv.getHolder();
			GUI gui = holder.getGUI();

			if(e.getInventory().equals(e.getClickedInventory())) {
				//Gui clicked
				int slot = e.getSlot();
				GUIElement elClicked = gui.getElementBySlot(slot);
				e.setCancelled(true);
				if(elClicked != null && elClicked.getAction() != null) {
					GUIElementActionEvent event = new GUIElementActionEvent(player, inv, gui, holder, e);
					elClicked.getAction().onAction(event);
					if(!event.isCancelled()) e.setCancelled(false);
				}

				switch(e.getAction()) {
					case HOTBAR_SWAP:
					case HOTBAR_MOVE_AND_READD:
					{
						if(gui.getTakeItemListener() == null || gui.getPutItemListener() == null) break;

						Runnable take = null;
						if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
							ItemStack current = e.getCurrentItem() != null ? e.getCurrentItem().clone() : null;
							GUITakeItemEvent t = new GUITakeItemEvent(player, e, inv, gui, holder, current, null, slot);
							gui.getTakeItemListener().onTakeItem(t);
							if(t.isCancelled()) break;
							e.setCancelled(false);
							take = t.getCallback();
						}

						ItemStack hb = e.getView().getBottomInventory().getItem(e.getHotbarButton());
						if(hb != null && hb.getType() != Material.AIR) {
							ItemStack tbp = hb.clone();
							ItemStack after = hb.clone();

							GUIPutItemEvent p = new GUIPutItemEvent(player, e, inv, gui, holder, tbp, after, slot);
							gui.getPutItemListener().onPutItem(p);
							if(!p.isCancelled()) {
								e.setCancelled(false);
								if(take != null) take.run();
								p.runCallback();
							}else {
								e.setCancelled(true);
								break;
							}
						}else {
							if(take != null) take.run();
						}
						break;
					}
					case MOVE_TO_OTHER_INVENTORY:
					case PICKUP_ALL:
					{
						if(gui.getTakeItemListener() != null) {
							ItemStack current = e.getCurrentItem() != null ? e.getCurrentItem().clone() : null;
							GUITakeItemEvent t = new GUITakeItemEvent(player, e, inv, gui, holder, current, null, slot);
							gui.getTakeItemListener().onTakeItem(t);
							if(!t.isCancelled()) {
								e.setCancelled(false);
								t.runCallback();
							}
						}
						break;
					}
					case PICKUP_HALF:
					{
						if(gui.getTakeItemListener() != null) {
							int take = (int) Math.ceil(e.getCurrentItem().getAmount() / 2D);

							ItemStack tbt = e.getCurrentItem().clone();
							tbt.setAmount(take);

							ItemStack after = e.getCurrentItem().clone();
							after.setAmount(after.getAmount() - take);

							GUITakeItemEvent t = new GUITakeItemEvent(player, e, inv, gui, holder, tbt, after, slot);
							gui.getTakeItemListener().onTakeItem(t);
							if(!t.isCancelled()) {
								e.setCancelled(false);
								t.runCallback();
							}
						}
						break;
					}
					case PLACE_ALL:
					{
						if(gui.getPutItemListener() != null) {
							ItemStack tbp = e.getCursor().clone();
							ItemStack after = e.getCursor().clone();
							if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
								after.setAmount(after.getAmount() + e.getCurrentItem().getAmount());
							}

							GUIPutItemEvent p = new GUIPutItemEvent(player, e, inv, gui, holder, tbp, after, slot);
							gui.getPutItemListener().onPutItem(p);
							if(!p.isCancelled()) {
								e.setCancelled(false);
								p.runCallback();
							}
						}
						break;
					}
					case PLACE_ONE:
					{
						if(gui.getPutItemListener() != null && e.getCursor() != null) {
							ItemStack tbp = e.getCursor().clone();
							tbp.setAmount(1);

							ItemStack after = tbp.clone();
							if(e.getCurrentItem() != null) {
								after.setAmount(after.getAmount() + e.getCurrentItem().getAmount());
							}

							GUIPutItemEvent p = new GUIPutItemEvent(player, e, inv, gui, holder, tbp, after, slot);
							gui.getPutItemListener().onPutItem(p);
							if(!p.isCancelled()) {
								e.setCancelled(false);
								p.runCallback();
							}
						}
						break;
					}
					case PLACE_SOME:
					{
						if(gui.getPutItemListener() != null) {
							int tp = e.getCurrentItem().getMaxStackSize() - e.getCurrentItem().getAmount();

							ItemStack tbp = e.getCursor().clone();
							tbp.setAmount(tp);

							ItemStack after = e.getCursor().clone();
							if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
								after.setAmount(after.getAmount() + e.getCurrentItem().getAmount());
							}

							GUIPutItemEvent p = new GUIPutItemEvent(player, e, inv, gui, holder, tbp, after, slot);
							gui.getPutItemListener().onPutItem(p);
							if(!p.isCancelled()) {
								e.setCancelled(false);
								p.runCallback();
							}
						}
						break;
					}
					case SWAP_WITH_CURSOR:
					{
						Runnable take = null;
						if(gui.getTakeItemListener() != null && e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
							ItemStack current = e.getCurrentItem() != null ? e.getCurrentItem().clone() : null;
							GUITakeItemEvent t = new GUITakeItemEvent(player, e, inv, gui, holder, current, null, slot);
							gui.getTakeItemListener().onTakeItem(t);
							if(t.isCancelled()) break;
							e.setCancelled(false);
							take = t.getCallback();
						}

						if(gui.getPutItemListener() != null) {
							ItemStack tbp = e.getCursor().clone();
							ItemStack after = e.getCursor().clone();

							GUIPutItemEvent p = new GUIPutItemEvent(player, e, inv, gui, holder, tbp, after, slot);
							gui.getPutItemListener().onPutItem(p);
							if(!p.isCancelled()) {
								e.setCancelled(false);
								if(take != null) take.run();
								p.runCallback();
							}else {
								e.setCancelled(true);
								break;
							}
						}else {
							if(take != null) take.run();
						}
						break;
					}
					case CLONE_STACK:
					case COLLECT_TO_CURSOR:
					case DROP_ALL_CURSOR:
					case DROP_ALL_SLOT:
					case DROP_ONE_CURSOR:
					case DROP_ONE_SLOT:
					case NOTHING:
					case PICKUP_ONE:
					case PICKUP_SOME:
					case UNKNOWN:
					default:
						// Ignored and cancelled by default
						break;
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

				if(gui.getDragDropListener() != null) {
					// Use old behavior for legacy plugins. TODO remove at some point
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

				if(gui.getPutItemListener() != null) {
					e.setCancelled(false);

					if(e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && e.getCurrentItem() != null) {
						e.setCancelled(true);

						ItemStack toPutDown = e.getCurrentItem().clone();
						for(int i = 0; i < inv.getSize(); i++) {
							ItemStack it = inv.getItem(i);
							if(toPutDown.isSimilar(it) && it.getAmount() < it.getMaxStackSize()) {
								int pd = Math.min(it.getMaxStackSize() - it.getAmount(), toPutDown.getAmount());

								ItemStack tbp = toPutDown.clone();
								tbp.setAmount(pd);

								ItemStack after = it.clone();
								after.setAmount(it.getAmount() + pd);

								GUIPutItemEvent p = new GUIPutItemEvent(player, e, inv, gui, holder, tbp, after, i);
								gui.getPutItemListener().onPutItem(p);
								if(!p.isCancelled()) {
									it.setAmount(it.getAmount() + pd);
									toPutDown.setAmount(toPutDown.getAmount() - pd);
									p.runCallback();
									player.updateInventory();
									if(toPutDown.getAmount() <= 0) break;
								}
							}
						}

						if(toPutDown.getAmount() > 0) {
							for(int i = 0; i < inv.getSize(); i++) {
								ItemStack it = inv.getItem(i);
								if(it == null || it.getType() == Material.AIR) {
									GUIPutItemEvent p = new GUIPutItemEvent(player, e, inv, gui, holder, toPutDown.clone(), toPutDown.clone(), i);
									gui.getPutItemListener().onPutItem(p);
									if(!p.isCancelled()) {
										inv.setItem(i, toPutDown);
										toPutDown.setAmount(0);
										p.runCallback();
										break;
									}
								}
							}
						}

						e.setCurrentItem(toPutDown);
					}else if(e.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
						e.setCancelled(true);
					}
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
