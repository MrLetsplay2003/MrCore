package me.mrletsplay.mrcore.bukkitimpl.gui.event;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.mrletsplay.mrcore.bukkitimpl.gui.GUI;
import me.mrletsplay.mrcore.bukkitimpl.gui.GUIHolder;

public class GUIPutItemEvent {
	
	private Player player;
	private InventoryInteractEvent cause;
	private Inventory inventory;
	private GUI gui;
	private GUIHolder guiHolder;
	private ItemStack toBePut;
	private ItemStack slotAfter;
	private int slot;
	private boolean cancelled;
	
	private Runnable callback;
	
	public GUIPutItemEvent(Player player, InventoryInteractEvent cause, Inventory inventory, GUI gui, GUIHolder guiHolder, ItemStack toBePut, ItemStack slotAfter, int slot) {
		this.player = player;
		this.cause = cause;
		this.inventory = inventory;
		this.gui = gui;
		this.guiHolder = guiHolder;
		this.toBePut = toBePut;
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

	public ItemStack getToBePut() {
		return toBePut;
	}

	public int getSlot() {
		return slot;
	}
	
	public ItemStack getSlotAfter() {
		return slotAfter;
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
