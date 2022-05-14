package me.mrletsplay.mrcore.bukkitimpl.gui.event;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.mrletsplay.mrcore.bukkitimpl.gui.GUIHolder;

public class GUIBuildPageItemEvent<T> extends GUIBuildEvent {

	private int page;
	private List<T> items;
	private int absoluteIndex, relativeIndex;
	
	public GUIBuildPageItemEvent(GUIHolder holder, Player player, Inventory inv, int page, List<T> items, int absoluteIndex, int relativeIndex) {
		super(holder, player, inv);
		this.page = page;
		this.items = items;
		this.absoluteIndex = absoluteIndex;
		this.relativeIndex = relativeIndex;
	}
	
	public int getPage() {
		return page;
	}
	
	public List<T> getItems() {
		return items;
	}
	
	public int getAbsoluteIndex() {
		return absoluteIndex;
	}
	
	public int getRelativeIndex() {
		return relativeIndex;
	}
	
}
