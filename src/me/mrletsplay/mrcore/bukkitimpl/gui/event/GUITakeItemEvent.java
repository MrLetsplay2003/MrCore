package me.mrletsplay.mrcore.bukkitimpl.gui.event;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.mrletsplay.mrcore.bukkitimpl.gui.GUI;
import me.mrletsplay.mrcore.bukkitimpl.gui.GUIHolder;

public class GUITakeItemEvent {
	
	private Player player;
	private InventoryInteractEvent cause;
	private Inventory inventory;
	private GUI gui;
	private GUIHolder guiHolder;
	private ItemStack toBeTaken;
	private ItemStack slotAfter;
	private int slot;
	private boolean cancelled;
	
	private Runnable callback;
	
	public GUITakeItemEvent(Player player, InventoryInteractEvent cause, Inventory inventory, GUI gui, GUIHolder guiHolder, ItemStack toBeTaken, ItemStack slotAfter, int slot) {
		this.player = player;
		this.cause = cause;
		this.inventory = inventory;
		this.gui = gui;
		this.guiHolder = guiHolder;
		this.toBeTaken = toBeTaken;
		this.slotAfter = slotAfter;
		this.slot = slot;
		this.cancelled = true;
	}

	public Player getPlayer() {
		return player;
	}

	public InventoryInteractEvent getCause() {
		return cause;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public GUI getGui() {
		return gui;
	}

	public GUIHolder getGUIHolder() {
		return guiHolder;
	}

	public ItemStack getToBeTaken() {
		return toBeTaken;
	}
	
	public ItemStack getSlotAfter() {
		return slotAfter;
	}

	public int getSlot() {
		return slot;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCallback(Runnable callback) {
		this.callback = callback;
	}
	
	public Runnable getCallback() {
		return callback;
	}
	
	public void runCallback() {
		if(callback != null) callback.run();
	}
	
}
