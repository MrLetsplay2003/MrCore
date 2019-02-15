package me.mrletsplay.mrcore.bukkitimpl.multiblock;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockEvent;

public class MultiBlockStructureCreatedEvent {

	private BlockEvent triggeringEvent;
	private BuiltMultiBlockStructure builtStructure;
	private Player player;
	
	public MultiBlockStructureCreatedEvent(BlockEvent triggeringEvent, BuiltMultiBlockStructure builtStructure, Player player) {
		this.triggeringEvent = triggeringEvent;
		this.builtStructure = builtStructure;
		this.player = player;
	}
	
	public BlockEvent getTriggeringEvent() {
		return triggeringEvent;
	}
	
	public MultiBlockStructure getStructure() {
		return builtStructure.getStructure();
	}
	
	public BuiltMultiBlockStructure getBuiltStructure() {
		return builtStructure;
	}
	
	public Player getPlayer() {
		return player;
	}
	
}
