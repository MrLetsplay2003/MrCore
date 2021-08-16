module mrcore {
	exports me.mrletsplay.mrcore.misc.classfile;
	exports me.mrletsplay.mrcore.command.properties;
	exports me.mrletsplay.mrcore.bukkitimpl.command;
	exports me.mrletsplay.mrcore.command;
	exports me.mrletsplay.mrcore.config.mapper.builder;
	exports me.mrletsplay.mrcore.config.mapper;
	exports me.mrletsplay.mrcore.misc.classfile.pool.entry;
	exports me.mrletsplay.mrcore.api.spiget;
	exports me.mrletsplay.mrcore.io;
	exports me.mrletsplay.mrcore.json;
	exports me.mrletsplay.mrcore.command.parser;
	exports me.mrletsplay.mrcore.misc;
	exports me.mrletsplay.mrcore.command.provider;
	exports me.mrletsplay.mrcore.bukkitimpl.ui;
	exports me.mrletsplay.mrcore.misc.classfile.attribute.stackmap;
	exports me.mrletsplay.mrcore.misc.classfile.util;
	exports me.mrletsplay.mrcore.misc.classfile.attribute.stackmap.verification;
	exports me.mrletsplay.mrcore.bukkitimpl.gui;
	exports me.mrletsplay.mrcore.command.event.filter;
	exports me.mrletsplay.mrcore.api.bukkit;
	exports me.mrletsplay.mrcore.bukkitimpl.gui.event;
	exports me.mrletsplay.mrcore.bukkitimpl.config;
	exports me.mrletsplay.mrcore.command.option.impl;
	exports me.mrletsplay.mrcore.locale;
	exports me.mrletsplay.mrcore.bukkitimpl.ui.event;
	exports me.mrletsplay.mrcore.http;
	exports me.mrletsplay.mrcore.command.completer;
	exports me.mrletsplay.mrcore.command.properties.auto;
	exports me.mrletsplay.mrcore.bukkitimpl;
	exports me.mrletsplay.mrcore.locale.file;
	exports me.mrletsplay.mrcore.misc.classfile.annotation;
	exports me.mrletsplay.mrcore.misc.classfile.attribute;
	exports me.mrletsplay.mrcore.main;
	exports me.mrletsplay.mrcore.bukkitimpl.versioned.item;
	exports me.mrletsplay.mrcore.permission;
	exports me.mrletsplay.mrcore.command.provider.impl;
	exports me.mrletsplay.mrcore.bukkitimpl.versioned;
	exports me.mrletsplay.mrcore.command.event;
	exports me.mrletsplay.mrcore.misc.classfile.annotation.value;
	exports me.mrletsplay.mrcore.misc.classfile.signature;
	exports me.mrletsplay.mrcore.config;
	exports me.mrletsplay.mrcore.bukkitimpl.multiblock;
	exports me.mrletsplay.mrcore.json.converter;
	exports me.mrletsplay.mrcore.command.option;
	exports me.mrletsplay.mrcore.config.locale;
	exports me.mrletsplay.mrcore.config.impl;
	exports me.mrletsplay.mrcore.misc.classfile.pool;
	exports me.mrletsplay.mrcore.bukkitimpl.versioned.definition;
	
	requires transitive java.desktop;
	requires transitive java.logging;
	requires transitive java.sql;

	requires transitive static bungeecord.chat;
	requires transitive static org.bukkit;
	
	requires static gson;
}