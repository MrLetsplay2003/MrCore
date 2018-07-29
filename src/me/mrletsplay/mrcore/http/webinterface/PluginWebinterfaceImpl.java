package me.mrletsplay.mrcore.http.webinterface;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;

public class PluginWebinterfaceImpl {

	private Plugin plugin;
	private List<PluginTab> tabs;
	
	public PluginWebinterfaceImpl(Plugin plugin) {
		this.plugin = plugin;
		this.tabs = new ArrayList<>();
	}
	
	public void registerTab(PluginTab tab) {
		tabs.add(tab);
	}
	
	public Plugin getPlugin() {
		return plugin;
	}
	
	public List<PluginTab> getTabs() {
		return tabs;
	}
	
	public void register() {
		
	}
	
	public static class PluginTab {
		
		private String name;
		
		public PluginTab(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
	}
	
}
