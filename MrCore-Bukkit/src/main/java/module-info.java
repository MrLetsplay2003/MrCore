module me.mrletsplay.mrcore.bukkit {
	exports me.mrletsplay.mrcore.bukkitimpl.command;
	exports me.mrletsplay.mrcore.bukkitimpl.ui;
	exports me.mrletsplay.mrcore.bukkitimpl.gui;
	exports me.mrletsplay.mrcore.bukkitimpl.gui.event;
	exports me.mrletsplay.mrcore.bukkitimpl.config;
	exports me.mrletsplay.mrcore.bukkitimpl.ui.event;
	exports me.mrletsplay.mrcore.bukkitimpl;
	exports me.mrletsplay.mrcore.bukkitimpl.versioned.item;
	exports me.mrletsplay.mrcore.bukkitimpl.versioned;
	exports me.mrletsplay.mrcore.bukkitimpl.multiblock;
	exports me.mrletsplay.mrcore.bukkitimpl.versioned.definition;

	requires transitive me.mrletsplay.mrcore;
	requires transitive org.bukkit;
	requires transitive bungeecord.chat;

	requires java.logging;
}