package me.mrletsplay.mrcore.http.webinterface.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import me.mrletsplay.mrcore.bukkitimpl.MrCorePlugin;

public class MapRenderer {

	private static Queue<PrioritizedChunk> chunkQueue = new PriorityQueue<>(Comparator.comparingInt(PrioritizedChunk::getPriority).reversed());
	private static Map<Long, BufferedImage> drawnChunks = new HashMap<>();
	
	public static void init() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MrCorePlugin.pl, () -> {
			int perTick = 50;
			for(int i = 0; i < perTick; i++) {
				PrioritizedChunk ch = chunkQueue.poll();
				if(ch != null) {
					drawChunk(ch.getChunkX(), ch.getChunkZ());
				}else {
					chunkQueue.addAll(getNextRenderableChunks(perTick * 2));
				}
			}
		}, 1, 1);
	}
	
	public static void drawChunk(int cX, int cZ) {
		BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = img.createGraphics();
		for(int x = 0; x < 16; x++) {
			for(int z = 0; z < 16; z++) {
				Block b = Bukkit.getWorld("world").getHighestBlockAt(cX * 16 + x, cZ * 16 + z).getRelative(BlockFace.DOWN);
				g2d.setColor(Color.RED);
				if(b.getType().equals(Material.GRASS)) {
					g2d.setColor(Color.GREEN);
				}else if(b.getType().equals(Material.LEAVES) || b.getType().equals(Material.LEAVES_2)) {
					g2d.setColor(Color.PINK);
				}else if(b.getType().equals(Material.WATER) || b.getType().equals(Material.STATIONARY_WATER)) {
					g2d.setColor(Color.BLUE);
				}else if(b.getType().equals(Material.STONE)) {
					g2d.setColor(Color.GRAY);
				}
				g2d.fillRect(x, z, 1, 1);
			}
		}
		drawnChunks.put(chunkCoordsAsInt(cX, cZ), img);
	}

	public static BufferedImage getChunkAt(int cX, int cZ) {
		return drawnChunks.get(chunkCoordsAsInt(cX, cZ));
	}
	
	private static long chunkCoordsAsInt(int cX, int cZ) {
		return (((long) cX) << 32) + cZ;
	}
	
	public static List<PrioritizedChunk> getNextRenderableChunks(int count) {
		int x = 0, y = 0, tX = 0, tY = 0, n = 1, dir = 0;
		boolean a = false;
		List<PrioritizedChunk> chunks = new ArrayList<>();
		while(chunks.size() < count) {
			while(drawnChunks.containsKey(chunkCoordsAsInt(x, y)) && Bukkit.getWorld("world").getChunkAt(x, y) != null) {
				if(x > tX) x--;
				if(x < tX) x++;
				if(y > tY) y--;
				if(y < tY) y++;
				if(x == tX && y == tY) {
					switch(dir) {
						case 0:
							tX+=n;
							break;
						case 1:
							tY+=n;
							break;
						case 2:
							tX-=n;
							break;
						case 3:
							tY-=n;
							break;
					}
					dir++;
					if(dir>3) dir = 0;
					n = a?n+1:n;
					a = !a;
				}
			}
			chunks.add(new PrioritizedChunk(0, x, y));
		}
		return chunks;
	}
	
	public static void queueChunk(int priority, int chunkX, int chunkZ) {
		chunkQueue.add(new PrioritizedChunk(priority, chunkX, chunkZ));
	}
	
	public static void queueChunk(int priority, Chunk chunk) {
		chunkQueue.add(new PrioritizedChunk(priority, chunk.getX(), chunk.getZ()));
	}
	
	public static void queueChunk(int chunkX, int chunkZ) {
		chunkQueue.add(new PrioritizedChunk(0, chunkX, chunkZ));
	}
	
	public static void queueChunk(Chunk chunk) {
		chunkQueue.add(new PrioritizedChunk(0, chunk.getX(), chunk.getZ()));
	}
	
	private static class PrioritizedChunk {
		
		private int priority, chunkX, chunkZ;
		
		public PrioritizedChunk(int priority, int chunkX, int chunkZ) {
			this.priority = priority;
			this.chunkX = chunkX;
			this.chunkZ = chunkZ;
		}
		
		public int getPriority() {
			return priority;
		}
		
		public int getChunkX() {
			return chunkX;
		}
		
		public int getChunkZ() {
			return chunkZ;
		}
		
	}
	
}
