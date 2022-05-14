package me.mrletsplay.mrcore.bukkitimpl.multiblock;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class BuiltMultiBlockStructure {

	private MultiBlockStructure structure;
	private Location origin;
	private BlockFace direction;
	
	public BuiltMultiBlockStructure(MultiBlockStructure structure, Location origin, BlockFace direction) {
		this.structure = structure;
		this.origin = origin;
		this.direction = direction;
	}
	
	public MultiBlockStructure getStructure() {
		return structure;
	}
	
	public Location getOrigin() {
		return origin;
	}
	
	public BlockFace getDirection() {
		return direction;
	}
	
	public List<Location> getBlockLocations() {
		return structure.getBlockOffsets().stream()
				.map(off -> MultiBlockStructure.offsetLoc(origin, origin.clone(), off.getBlockX(), off.getBlockY(), off.getBlockZ(), direction))
				.collect(Collectors.toList());
	}
	
}
