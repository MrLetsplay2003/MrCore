package me.mrletsplay.mrcore.extensions;

import org.bukkit.plugin.Plugin;

public interface MrCoreExtension extends Plugin {

	public void init(JARLoader loader);
	
	public JARLoader getLoader();
	
	public ClassLoader getClassLoader();

	PluginCommandWrapper getCommand(String name);

	void setEnabled(boolean enabled);
	
	@Override
	public default MrCorePluginLoader getPluginLoader() {
		return MrCorePluginLoader.getInstance();
	}
	
}
