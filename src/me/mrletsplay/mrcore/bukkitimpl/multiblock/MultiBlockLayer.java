package me.mrletsplay.mrcore.bukkitimpl.multiblock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.bukkit.Material;

public class MultiBlockLayer {

	private String[] shape;
	private Map<Character, Material> typeMappings;
	
	public MultiBlockLayer() {
		this.typeMappings = new HashMap<>();
	}
	
	public MultiBlockLayer shape(String... shape) {
		this.shape = shape;
		return this;
	}
	
	public MultiBlockLayer setType(char key, Material type) {
		this.typeMappings.put(key, type);
		return this;
	}
	
	public String[] getShape() {
		return shape;
	}
	
	public Map<Character, Material> getTypeMappings() {
		return typeMappings;
	}
	
	public Character getKeyAt(int x, int y) {
		if(x < 0 || y < 0 || y >= shape.length || y >= shape[y].length()) return null;
		return shape[y].charAt(x);
	}
	
	public int getWidth() {
		if(shape == null) throw new IllegalStateException("You need to set a shape first");
		return Arrays.stream(shape).mapToInt(String::length).max().getAsInt();
	}
	
	public int getHeight() {
		if(shape == null) throw new IllegalStateException("You need to set a shape first");
		return shape.length;
	}
	
	public boolean isValid() {
		return shape != null && Arrays.stream(shape).noneMatch(Objects::isNull);
	}
	
}
