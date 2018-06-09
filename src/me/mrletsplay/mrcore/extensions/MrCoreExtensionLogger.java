package me.mrletsplay.mrcore.extensions;

import java.util.logging.LogRecord;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLogger;

public class MrCoreExtensionLogger extends PluginLogger {

	public MrCoreExtensionLogger(Plugin context) {
		super(context);
	}

	@Override
	public void log(LogRecord logRecord) {
		super.log(logRecord);
	}
	
}
