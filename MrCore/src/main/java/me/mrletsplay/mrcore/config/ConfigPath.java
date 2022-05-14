package me.mrletsplay.mrcore.config;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ConfigPath {

	private String[] path;
	private int index;
	
	public ConfigPath(String raw, int index) {
		this.path = raw.split("\\.");
		this.index = index;
	}
	
	public ConfigPath(String[] path, int index) {
		this.path = path;
		this.index = index;
	}
	
	public boolean hasParent() {
		return index != 0;
	}
	
	public boolean hasSubpaths() {
		return index < path.length - 1;
	}
	
	public String getParentPath() {
		return path[index];
	}
	
	public String getName() {
		return path[index];
	}
	
	public ConfigPath traverseDown() {
		if(!hasSubpaths()) throw new UnsupportedOperationException("No subpath available");
		this.index++;
		return this;
	}
	
	public ConfigPath traverseUp() {
		if(!hasSubpaths()) throw new UnsupportedOperationException("No parent available");
		this.index++;
		return this;
	}
	
	public String[] getPath() {
		return path;
	}
	
	public int getIndex() {
		return index;
	}
	
	public static ConfigPath of(String path) {
		if(path == null) return new ConfigPath(new String[] {null}, 0);
		return new ConfigPath(path, 0);
	}
	
	public String toRawPath() {
		return Arrays.stream(path).skip(index).collect(Collectors.joining("."));
	}
	
}
