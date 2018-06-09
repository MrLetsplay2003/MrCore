package me.mrletsplay.mrcore.extensions;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.UnknownDependencyException;

import me.mrletsplay.mrcore.bukkitimpl.MrCorePlugin;
import me.mrletsplay.mrcore.misc.OtherTools.FriendlyException;

public class ExtensionManager {
	
	public static MrCoreExtension loadExtension(File file) {
		return enableExtension(preLoad(file));
	}
	
	public static MrCoreExtension enableExtension(MrCoreExtension ex) {
		MrCorePluginLoader.getInstance().enablePlugin(ex);
		return ex;
	}
	
	public static MrCoreExtension disableExtension(MrCoreExtension ex) {
		MrCorePluginLoader.getInstance().disablePlugin(ex);
		return ex;
	}
	
	public static void unloadExtension(MrCoreExtension ex) {
		if(ex.isEnabled()) MrCorePluginLoader.getInstance().disablePlugin(ex);
		try {
			ex.getLoader().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected static MrCoreExtension preLoad(File f) {
		JARLoader loader = null;
		MrCorePlugin.pl.getLogger().info("Pre-loading plugin @ "+f);
		try {
			loader = new JARLoader(f);
			
			Class<?> mainClazz = loader.getBukkitMainClass();
			if(MrCoreExtension.class.isAssignableFrom(mainClazz)) {
				MrCoreExtension ex = (MrCoreExtension) mainClazz.newInstance();
				ex.init(loader);
				MrCorePluginLoader.getInstance().addPlugin(ex);
				return ex;
			}else {
				loader.close();
				throw new FriendlyException("Failed to load plugin. Invalid type of main class ("+mainClazz.getName()+")");
			}
		} catch (IOException | InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | SecurityException | URISyntaxException e) {
			try {
				if(loader != null) loader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			throw new FriendlyException("Failed to load plugin", e);
		}
	}
	
	public static List<MrCoreExtension> loadExtensions(File folder) {
		List<MrCoreExtension> extensions = new ArrayList<>();

        Map<String, MrCoreExtension> plugins = new HashMap<>();
        Set<String> loadedPlugins = new HashSet<>(Arrays.asList("MrCore_BukkitImpl")); // MrCore is loaded by default (obviously)
        Map<String, Collection<String>> dependencies = new HashMap<String, Collection<String>>();
        Map<String, Collection<String>> softDependencies = new HashMap<String, Collection<String>>();
		
        for (File file : folder.listFiles()) {
        	if(file.isDirectory()) continue;
            PluginDescriptionFile description = null;
        	MrCoreExtension preLoaded = preLoad(file);
            description = preLoaded.getDescription();
			String name = description.getName();
			if (name.equalsIgnoreCase("bukkit") || name.equalsIgnoreCase("minecraft") || name.equalsIgnoreCase("mojang")) {
				MrCorePlugin.pl.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + folder.getPath() + "': Restricted Name");
			    continue;
			}

            MrCoreExtension replacedFile = plugins.put(description.getName(), preLoaded);
            if (replacedFile != null) {
                MrCorePlugin.pl.getLogger().severe(String.format(
	                    "Ambiguous plugin name `%s' for files `%s' and `%s' in `%s'",
	                    description.getName(),
	                    file.getPath(),
	                    replacedFile.getLoader().getFile().getPath(),
	                    folder.getPath()
                    ));
            }

            Collection<String> softDependencySet = description.getSoftDepend();
            if (softDependencySet != null && !softDependencySet.isEmpty()) {
                if (softDependencies.containsKey(description.getName())) {
                    // Duplicates do not matter, they will be removed together if applicable
                    softDependencies.get(description.getName()).addAll(softDependencySet);
                } else {
                    softDependencies.put(description.getName(), new LinkedList<String>(softDependencySet));
                }
            }

            Collection<String> dependencySet = description.getDepend();
            if (dependencySet != null && !dependencySet.isEmpty()) {
                dependencies.put(description.getName(), new LinkedList<String>(dependencySet));
            }

            Collection<String> loadBeforeSet = description.getLoadBefore();
            if (loadBeforeSet != null && !loadBeforeSet.isEmpty()) {
                for (String loadBeforeTarget : loadBeforeSet) {
                    if (softDependencies.containsKey(loadBeforeTarget)) {
                        softDependencies.get(loadBeforeTarget).add(description.getName());
                    } else {
                        // softDependencies is never iterated, so 'ghost' plugins aren't an issue
                        Collection<String> shortSoftDependency = new LinkedList<String>();
                        shortSoftDependency.add(description.getName());
                        softDependencies.put(loadBeforeTarget, shortSoftDependency);
                    }
                }
            }
        }
        
        while (!plugins.isEmpty()) {
            boolean missingDependency = true;
            Iterator<String> pluginIterator = plugins.keySet().iterator();

            while (pluginIterator.hasNext()) {
                String plugin = pluginIterator.next();

                if (dependencies.containsKey(plugin)) {
                    Iterator<String> dependencyIterator = dependencies.get(plugin).iterator();

                    while (dependencyIterator.hasNext()) {
                        String dependency = dependencyIterator.next();

                        // Dependency loaded
                        if (loadedPlugins.contains(dependency)) {
                            dependencyIterator.remove();

                        // We have a dependency not found
                        } else if (!plugins.containsKey(dependency)) {
                            missingDependency = false;
                            MrCoreExtension file = plugins.get(plugin);
                            pluginIterator.remove();
                            softDependencies.remove(plugin);
                            dependencies.remove(plugin);

                            MrCorePlugin.pl.getLogger().log(
                                Level.SEVERE,
                                "Could not load '" + file.getLoader().getFile().getPath() + "' in folder '" + folder.getPath() + "'",
                                new UnknownDependencyException(dependency));
                            break;
                        }
                    }

                    if (dependencies.containsKey(plugin) && dependencies.get(plugin).isEmpty()) {
                        dependencies.remove(plugin);
                    }
                }
                if (softDependencies.containsKey(plugin)) {
                    Iterator<String> softDependencyIterator = softDependencies.get(plugin).iterator();

                    while (softDependencyIterator.hasNext()) {
                        String softDependency = softDependencyIterator.next();

                        // Soft depend is no longer around
                        if (!plugins.containsKey(softDependency)) {
                            softDependencyIterator.remove();
                        }
                    }

                    if (softDependencies.get(plugin).isEmpty()) {
                        softDependencies.remove(plugin);
                    }
                }
                if (!(dependencies.containsKey(plugin) || softDependencies.containsKey(plugin)) && plugins.containsKey(plugin)) {
                    // We're clear to load, no more soft or hard dependencies left
                    MrCoreExtension file = plugins.get(plugin);
                    pluginIterator.remove();
                    missingDependency = false;

                    extensions.add(file);
					loadedPlugins.add(plugin);
					continue;
                }
            }

            if (missingDependency) {
                // We now iterate over plugins until something loads
                // This loop will ignore soft dependencies
                pluginIterator = plugins.keySet().iterator();

                while (pluginIterator.hasNext()) {
                    String plugin = pluginIterator.next();

                    if (!dependencies.containsKey(plugin)) {
                        softDependencies.remove(plugin);
                        missingDependency = false;
                        MrCoreExtension file = plugins.get(plugin);
                        pluginIterator.remove();
                        
                    	extensions.add(file);
                        loadedPlugins.add(plugin);
                        break;
                    }
                }
                // We have no plugins left without a depend
                if (missingDependency) {
                    softDependencies.clear();
                    dependencies.clear();
                    Iterator<MrCoreExtension> failedPluginIterator = plugins.values().iterator();

                    while (failedPluginIterator.hasNext()) {
                        MrCoreExtension file = failedPluginIterator.next();
                        failedPluginIterator.remove();
                        MrCorePlugin.pl.getLogger().log(Level.SEVERE, "Could not load '" + file.getLoader().getFile().getPath() + "' in folder '" + folder.getPath() + "': circular dependency detected");
                    }
                }
            }
        }
        
        extensions.forEach(ExtensionManager::enableExtension);
        
		return extensions;
	}
	
}
