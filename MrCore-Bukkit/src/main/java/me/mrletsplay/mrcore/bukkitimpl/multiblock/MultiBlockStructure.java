package me.mrletsplay.mrcore.bukkitimpl.multiblock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class MultiBlockStructure {

	public static final Set<BlockFace> ALL_DIRECTIONS = Collections.unmodifiableSet(EnumSet.of(BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH, BlockFace.EAST));
	
	private NamespacedKey key;
	private List<MultiBlockLayer> layers;
	
	public MultiBlockStructure(NamespacedKey key) {
		this.key = key;
		this.layers = new ArrayList<>();
	}
	
	public MultiBlockStructure addLayer(MultiBlockLayer layer) {
		this.layers.add(layer);
		return this;
	}
	
	public NamespacedKey getKey() {
		return key;
	}
	
	public List<MultiBlockLayer> getLayers() {
		return layers;
	}
	
	public BuiltMultiBlockStructure checkAt(Location loc) {
		return checkAt(loc, ALL_DIRECTIONS);
	}
	
	public List<Vector> getBlockOffsets() {
		List<Vector> offs = new ArrayList<>();
		for(int h = 0; h < layers.size(); h++) {
			MultiBlockLayer layer = layers.get(h);
			for(int x = 0; x < layer.getWidth(); x++) {
				for(int y = 0;  y < layer.getHeight(); y++) {
					Character key = layer.getKeyAt(x, y);
					if(key == null) continue;
					Material mapping = layer.getTypeMappings().get(key);
					if(mapping == null) continue; // No mapping => placeholder, any block
					offs.add(new Vector(x, h, y));
				}
			}
		}
		return offs;
	}
	
	public BuiltMultiBlockStructure checkAt(Location loc, Set<BlockFace> possibleDirections) {
		Location cLoc = loc.clone();
		for(int h = 0; h < layers.size(); h++) {
			MultiBlockLayer layer = layers.get(h);
			for(int x = 0; x < layer.getWidth(); x++) {
				for(int y = 0;  y < layer.getHeight(); y++) {
					Character key = layer.getKeyAt(x, y);
					if(key == null) continue;
					Material mapping = layer.getTypeMappings().get(key);
					if(mapping == null) continue; // No mapping => placeholder, any block
					if(mapping.equals(loc.getBlock().getType())) {
						for(BlockFace dir : possibleDirections) {
							Location originLoc = offsetLoc(loc, cLoc, -x, -h, -y, dir);
							BuiltMultiBlockStructure built = checkOriginAt(originLoc, Collections.singleton(dir));
							if(built != null) return built;
						}
					}
				}
			}
		}
		return null;
	}
	
	public BuiltMultiBlockStructure checkOriginAt(Location loc) {
		return checkOriginAt(loc, ALL_DIRECTIONS);
	}
	
	public BuiltMultiBlockStructure checkOriginAt(Location loc, Set<BlockFace> possibleDirections) {
		if(!isValid()) throw new IllegalStateException("Multiblock structure is invalid");
		Location cLoc = loc.clone();
		dl: for(BlockFace dir : possibleDirections) {
			for(int h = 0; h < layers.size(); h++) {
				MultiBlockLayer layer = layers.get(h);
				for(int x = 0; x < layer.getWidth(); x++) {
					for(int y = 0;  y < layer.getHeight(); y++) {
						Character key = layer.getKeyAt(x, y);
						if(key == null) continue;
						Material mapping = layer.getTypeMappings().get(key);
						if(mapping == null) continue; // No mapping => placeholder, any block
						Location chLoc = offsetLoc(loc, cLoc, x, h, y, dir);
						if(!chLoc.getBlock().getType().equals(mapping)) {
							continue dl;
						}
					}
				}
			}
			return new BuiltMultiBlockStructure(this, loc, dir);
		}
		return null;
	}
	
	public boolean isValid() {
		return !layers.isEmpty() && layers.stream().allMatch(MultiBlockLayer::isValid);
	}
	
	public MultiBlockStructure addBuiltListener(Consumer<MultiBlockStructureCreatedEvent> listener) {
		MultiBlockStructureListener.addCreatedListener(this, listener);
		return this;
	}
	
	public MultiBlockStructure addBrokenListener(Consumer<MultiBlockStructureBrokenEvent> listener) {
		MultiBlockStructureListener.addBrokenListener(this, listener);
		return this;
	}
	
	public static Location offsetLoc(Location originalLoc, Location newLoc, double xOff, double yOff, double zOff, BlockFace direction) {
		double 
			x = originalLoc.getBlockX(),
			y = originalLoc.getBlockY() + yOff,
			z = originalLoc.getBlockZ();
		switch(direction) {
			case NORTH:
				x += xOff;
				z += zOff;
				break;
			case SOUTH:
				x -= xOff;
				z -= zOff;
				break;
			case WEST:
				x += xOff;
				z -= zOff;
				break;
			case EAST:
				x -= xOff;
				z += zOff;
				break;
			default:
				throw new IllegalArgumentException("Unsupported direction " + direction + ", must be one of NORTH, WEST, SOUTH or EAST");
		}
		newLoc.setX(x);
		newLoc.setY(y);
		newLoc.setZ(z);
		return newLoc;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof MultiBlockStructure)) return false;
		return key.equals(((MultiBlockStructure) obj).getKey());
	}
	
}
