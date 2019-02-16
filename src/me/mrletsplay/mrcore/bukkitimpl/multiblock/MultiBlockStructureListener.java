package me.mrletsplay.mrcore.bukkitimpl.multiblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class MultiBlockStructureListener implements Listener {

	private static Map<MultiBlockStructure, List<Consumer<MultiBlockStructureCreatedEvent>>> createdListeners = new HashMap<>();
	private static Map<MultiBlockStructure, List<Consumer<MultiBlockStructureBrokenEvent>>> brokenListeners = new HashMap<>();

	@EventHandler
	private void onBlockPlaced(BlockPlaceEvent event) {
//		for(Map.Entry<MultiBlockStructure, List<Consumer<MultiBlockStructureBrokenEvent>>> en : brokenListeners.entrySet()) { TODO: Detect when structure is destroyed by placing a block
//			BuiltMultiBlockStructure bM = en.getKey().checkAt(event.getBlock().getLocation());
//			if(bM != null) {
//				MultiBlockStructureBrokenEvent ev = new MultiBlockStructureBrokenEvent(event, bM, event.getPlayer());
//				en.getValue().forEach(c -> c.accept(ev));
//			}
//		}
		for(Map.Entry<MultiBlockStructure, List<Consumer<MultiBlockStructureCreatedEvent>>> en : createdListeners.entrySet()) {
			if(en.getKey().getLayers().stream().anyMatch(l -> l.getTypeMappings().values().contains(event.getBlock().getType()))) {
				BuiltMultiBlockStructure b = en.getKey().checkAt(event.getBlock().getLocation());
				if(b != null) {
					MultiBlockStructureCreatedEvent ev = new MultiBlockStructureCreatedEvent(event, b, event.getPlayer());
					en.getValue().forEach(c -> c.accept(ev));
				}
			}
		}
	}

	@EventHandler
	private void onBlockBroken(BlockBreakEvent event) {
		for (Map.Entry<MultiBlockStructure, List<Consumer<MultiBlockStructureBrokenEvent>>> en : brokenListeners.entrySet()) {
			BuiltMultiBlockStructure bM = en.getKey().checkAt(event.getBlock().getLocation());
			if (bM != null) {
				MultiBlockStructureBrokenEvent ev = new MultiBlockStructureBrokenEvent(event, bM, event.getPlayer());
				en.getValue().forEach(c -> c.accept(ev));
			}
		}
		for (Map.Entry<MultiBlockStructure, List<Consumer<MultiBlockStructureCreatedEvent>>> en : createdListeners
				.entrySet()) {
			if (en.getKey().getLayers().stream().anyMatch(l -> l.getTypeMappings().values().contains(Material.AIR))) {
				event.getBlock().setType(Material.AIR);
				BuiltMultiBlockStructure b = en.getKey().checkAt(event.getBlock().getLocation());
				if (b != null) {
					MultiBlockStructureCreatedEvent ev = new MultiBlockStructureCreatedEvent(event, b,
							event.getPlayer());
					en.getValue().forEach(c -> c.accept(ev));
				}
			}
		}
	}

	public static void addCreatedListener(MultiBlockStructure structure,
			Consumer<MultiBlockStructureCreatedEvent> listener) {
		List<Consumer<MultiBlockStructureCreatedEvent>> ls = createdListeners.getOrDefault(structure,
				new ArrayList<>());
		ls.add(listener);
		createdListeners.put(structure, ls);
	}

	public static void addBrokenListener(MultiBlockStructure structure,
			Consumer<MultiBlockStructureBrokenEvent> listener) {
		List<Consumer<MultiBlockStructureBrokenEvent>> ls = brokenListeners.getOrDefault(structure, new ArrayList<>());
		ls.add(listener);
		brokenListeners.put(structure, ls);
	}

}
