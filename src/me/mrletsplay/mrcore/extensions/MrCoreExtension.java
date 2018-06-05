package me.mrletsplay.mrcore.extensions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginLogger;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;

import me.mrletsplay.mrcore.misc.OtherTools.FriendlyException;

public class MrCoreExtension implements Plugin {

	private JARLoader loader;
	private PluginDescriptionFile desc;
	private boolean enabled, naggable;
	private File configFile;
	private YamlConfiguration config;
	private File dataFolder;
	
	public MrCoreExtension() {
		if(!(getClassLoader() instanceof JARLoader)) {
			throw new FriendlyException("Unsupported ClassLoader: "+getClassLoader().getClass().getName());
		}
	}
	
	public void init(JARLoader loader) {
		this.loader = loader;
		try {
			desc = new PluginDescriptionFile(new InputStreamReader(loader.getResource("plugin.yml").openStream()));
		} catch (InvalidDescriptionException | IOException e) {
			e.printStackTrace();
		}
		this.dataFolder = new File(MrCorePluginLoader.getInstance().getPluginDataFolder(), getName());
		this.configFile = new File(getDataFolder(), "config.yml");
	}
	
	public ClassLoader getClassLoader() {
		return getClass().getClassLoader();
	}
	
	public JARLoader getLoader() {
		return loader;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		return false;
	}

	@Override
	public FileConfiguration getConfig() {
		if(config == null) reloadConfig();
		return config;
	}

	/**
	 * Snacked from bukkit's JavaPlugin class
	 */
	@Override
	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = getResource("config.yml");
        if (defConfigStream == null) {
            return;
        }

        final YamlConfiguration defConfig;
        final byte[] contents;
        defConfig = new YamlConfiguration();
        try {
            contents = ByteStreams.toByteArray(defConfigStream);
        } catch (final IOException e) {
            getLogger().log(Level.SEVERE, "Unexpected failure reading config.yml", e);
            return;
        }

        final String text = new String(contents, Charset.defaultCharset());
        if (!text.equals(new String(contents, Charsets.UTF_8))) {
            getLogger().warning("Default system encoding may have misread config.yml from plugin jar");
        }

        try {
            defConfig.loadFromString(text);
        } catch (final InvalidConfigurationException e) {
            getLogger().log(Level.SEVERE, "Cannot load configuration from jar", e);
        }

        config.setDefaults(defConfig);
	}

	@Override
	public void saveConfig() {
		try {
			config.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveDefaultConfig() {
		if(!configFile.exists()) {
			saveResource("config.yml", false);
		}
	}

	@Override
	public File getDataFolder() {
		return dataFolder;
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String world, String uuid) {
		return null;
	}

	@Override
	public PluginDescriptionFile getDescription() {
		return desc;
	}

	@Override
	public Logger getLogger() {
		return new PluginLogger(this);
	}

	@Override
	public String getName() {
		return desc.getName();
	}

	@Override
	public PluginLoader getPluginLoader() {
		return MrCorePluginLoader.getInstance();
	}

	@Override
	public InputStream getResource(String filename) {
		if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }

        try {
            URL url = getClassLoader().getResource(filename);

            if (url == null) {
                return null;
            }

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ex) {
            return null;
        }
	}

	@Override
	public Server getServer() {
		return Bukkit.getServer();
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if(enabled) {
			onEnable();
		}else {
			onDisable();
		}
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean isNaggable() {
		return naggable;
	}

	@Override
	public void onDisable() {
		
	}

	@Override
	public void onEnable() {
		
	}

	@Override
	public void onLoad() {
		
	}

	@Override
	public void saveResource(String resourcePath, boolean replace) {
		if (resourcePath == null || resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResource(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + getName());
        }

        File outFile = new File(dataFolder, resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(dataFolder, resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists() || replace) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {
                getLogger().log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException ex) {
        	getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, ex);
        }	
	}

	@Override
	public void setNaggable(boolean naggable) {
		this.naggable = naggable;
	}
	
	public PluginCommand getCommand(String name) {
        String alias = name.toLowerCase();
        PluginCommand command = getServer().getPluginCommand(alias);

        if (command == null /*|| command.getPlugin() != this*/) {
            command = getServer().getPluginCommand(getDescription().getName().toLowerCase() + ":" + alias);
        }
		
        if (command != null /*&& command.getPlugin() == this*/) {
            return command;
        } else {
            return null;
        }
    }
	
}
