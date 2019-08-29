package me.mrletsplay.mrcore.bukkitimpl.ui.event;

import java.util.List;

import org.bukkit.entity.Player;

import me.mrletsplay.mrcore.bukkitimpl.ui.UIInstance;

public class UIBuildPageItemEvent<T> extends UIBuildEvent {
	
	private int page;
	private List<T> items;
	private int absoluteIndex, relativeIndex;
	
	public UIBuildPageItemEvent(UIInstance ui, Player p, int page, List<T> items, int absoluteIndex,
			int relativeIndex) {
		super(ui, p);
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
