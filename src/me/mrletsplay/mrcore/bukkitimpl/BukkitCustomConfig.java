package me.mrletsplay.mrcore.bukkitimpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.mrletsplay.mrcore.config.ConfigExpansions.ExpandableCustomConfig;
import me.mrletsplay.mrcore.misc.OtherTools.FriendlyException;

public class BukkitCustomConfig extends ExpandableCustomConfig {
	
	public BukkitCustomConfig(File configFile, ConfigSaveProperty... defaultSaveProperties) {
		super(configFile, defaultSaveProperties);
		setFormatter(new BukkitConfigFormatter(this));
		registerMappers();
	}
	
	public BukkitCustomConfig(URL configURL, ConfigSaveProperty... defaultSaveProperties) {
		super(configURL, defaultSaveProperties);
		setFormatter(new BukkitConfigFormatter(this));
		registerMappers();
	}
	
	@Override
	public BukkitCustomConfig loadConfig() throws IOException {
		super.loadConfig();
		return this;
	}
	
	@Override
	public BukkitCustomConfig loadConfigSafely() {
		super.loadConfigSafely();
		return this;
	}
	
	@Override
	public BukkitCustomConfig loadConfig(InputStream in) throws IOException {
		super.loadConfig(in);
		return this;
	}
	
	private void registerMappers() {
		registerMapper(new ObjectMapper<Location>(Location.class) {

			@Override
			public Map<String, Object> mapObject(Location loc) {
				HashMap<String, Object> map = new HashMap<>();
				map.put("world", loc.getWorld().getName());
				map.put("x", loc.getX());
				map.put("y", loc.getY());
				map.put("z", loc.getZ());
				map.put("pitch", loc.getPitch());
				map.put("yaw", loc.getYaw());
				return map;
			}

			@Override
			public Location constructObject(Map<String, Object> map) {
				if(!requireKeys(map, "world", "x", "y", "z")) return null;
				return new Location(
							Bukkit.getWorld((String) map.get("world")),
							castGeneric(map.get("x"), Double.class),
							castGeneric(map.get("y"), Double.class),
							castGeneric(map.get("z"), Double.class),
							castGeneric(map.get("yaw"), Float.class),
							castGeneric(map.get("pitch"), Float.class)
						);
			}
			
		});
		
		registerMapper(new ObjectMapper<ItemStack>(ItemStack.class) {

			@SuppressWarnings("deprecation")
			@Override
			public Map<String, Object> mapObject(ItemStack it) {
				Map<String, Object> map = new HashMap<>();
				map.put("type", it.getType());
				map.put("amount", it.getAmount());
				map.put("durability", it.getDurability());
				if(it.hasItemMeta()) {
					ItemMeta m = it.getItemMeta();
					map.put("name", m.getDisplayName());
					map.put("lore", m.getLore());
					if(!m.getEnchants().isEmpty()) {
						HashMap<String, Integer> enchMap = new HashMap<>();
						for(Map.Entry<Enchantment, Integer> ench : m.getEnchants().entrySet()) {
							enchMap.put(ench.getKey().getName(), ench.getValue());
						}
						map.put("enchantments", enchMap);
					}
					if(!m.getItemFlags().isEmpty()) {
						map.put("flags", m.getItemFlags().stream().map(f -> f.name()).collect(Collectors.toList()));
					}
					
					if(m instanceof SkullMeta) {
						SkullMeta sM = (SkullMeta) m;
						if(sM.hasOwner()) {
							// Using SkullMeta#getOwner for backwards compatibility
							map.put("skull-owner", sM.getOwner());
						}else {
							String texture = getTexture(sM);
							if(texture != null) map.put("skull-texture", texture);
						}
					}
					
					if(m instanceof LeatherArmorMeta) {
						LeatherArmorMeta lM = (LeatherArmorMeta) m;
						map.put("leather-armor-color", Integer.toHexString(lM.getColor().asRGB()));
					}
					
				}
				return map;
			}

			@SuppressWarnings({ "unchecked", "deprecation" })
			@Override
			public ItemStack constructObject(Map<String, Object> map) {
				if(!map.containsKey("type")) return null;
				ItemStack it = new ItemStack(Material.getMaterial((String) map.get("type")),
						castGeneric(map.getOrDefault("amount", 1), Integer.class),
						castGeneric(map.getOrDefault("durability", 0), Short.class));
				ItemMeta m = it.getItemMeta();
				m.setDisplayName((String) map.get("name"));
				m.setLore((List<String>) map.getOrDefault("lore", new ArrayList<>()));
				Map<String, Integer> enchs = castGenericMap((Map<String, ?>) map.getOrDefault("enchantments", new HashMap<>()), Integer.class);
				enchs.forEach((en, lvl) -> m.addEnchant(Enchantment.getByName(en), lvl, true));
				List<String> flags = (List<String>) map.getOrDefault("flags", new ArrayList<>());
				flags.forEach(f -> m.addItemFlags(ItemFlag.valueOf(f)));
				if(requireKeys(map, "skull-owner")) {
					SkullMeta sM = (SkullMeta) m;
					// Using SkullMeta#setOwner for backwards compatability
					sM.setOwner((String) map.get("skull-owner"));
				}
				
				if(requireKeys(map, "skull-texture")) {
					SkullMeta sM = (SkullMeta) m;
					setTexture(sM, (String) map.get("skull-texture"));
				}
				
				if(requireKeys(map, "skull-hash")) {
					SkullMeta sM = (SkullMeta) m;
					setRawTexture(sM, (String) map.get("skull-hash"));
				}
				
				if(requireKeys(map, "leather-armor-color")) {
					LeatherArmorMeta lM = (LeatherArmorMeta) m;
					lM.setColor(Color.fromRGB(Integer.parseInt((String) map.get("leather-armor-color"), 16)));
				}
				
				it.setItemMeta(m);
				return it;
			}
			
		});
		
		// Registers an object mapper for ConfigurationSerializable with priority -100 to first use other mappers (if available)
		registerMapper(new ObjectMapper<ConfigurationSerializable>(-100, ConfigurationSerializable.class) {

			@Override
			public Map<String, Object> mapObject(ConfigurationSerializable object) {
				Map<String, Object> map = new HashMap<>();
				map.put("serializable-class", object.getClass().getName());
				map.put("serializable-data", object.serialize());
				return map;
			}

			@Override
			public ConfigurationSerializable constructObject(Map<String, Object> map) {
				if(!requireKeys(map, "serializable-class", "serializable-data")) return null;
				try {
					Class<?> clazz = Class.forName((String) map.get("serializable-class"));
					try {
						Method deserialize = clazz.getDeclaredMethod("deserialize", Map.class);
						deserialize.setAccessible(true);
						return (ConfigurationSerializable) deserialize.invoke(null, map);
					} catch (Exception e) {
						try {
							Method deserialize = clazz.getDeclaredMethod("valueOf", Map.class);
							deserialize.setAccessible(true);
							return (ConfigurationSerializable) deserialize.invoke(null, map);
						} catch (Exception e1) {
							try {
								Constructor<?> constr = clazz.getDeclaredConstructor(Map.class);
								constr.setAccessible(true);
								return (ConfigurationSerializable) constr.newInstance(map);
							} catch (Exception e2) {
								throw new FriendlyException(e2);
							}
						}
					}
				} catch (ClassNotFoundException e) {
					throw new FriendlyException(e);
				}
			}
			
		});
	}
	
	public static class BukkitConfigFormatter extends ExpandableConfigFormatter {

		private BukkitCustomConfig config;
		
		public BukkitConfigFormatter(BukkitCustomConfig config) {
			super(config);
			this.config = config;
		}
		
		@Override
		public FormattedProperty formatObject(Object o) {
			FormattedProperty fp = super.formatObject(o);
			if(fp.isSpecific()) return fp;
			
//			if(o instanceof ConfigurationSerializable) {
//				return FormattedProperty.map(((ConfigurationSerializable) o).serialize());
//			}
			
			return fp;
		}
		
		@Override
		public BukkitCustomConfig getConfig() {
			return config;
		}
		
	}
	
	public Location getLocation(String key) {
		return getMappable(key, Location.class);
	}
	
	public Location getLocation(String key, Location defaultVal, boolean applyDefault) {
		Location o = getMappable(key, Location.class);
		if(o != null) return o;
		if(applyDefault) set(key, defaultVal);
		return defaultVal;
	}
	
	public List<Location> getLocationList(String key) {
		return getMappableList(key, Location.class);
	}
	
	public List<Location> getLocationList(String key, List<Location> defaultVal, boolean applyDefault) {
		List<Location> o = getMappableList(key, Location.class);
		if(o != null) return o;
		if(applyDefault) set(key, defaultVal);
		return defaultVal;
	}
	
	public ItemStack getItemStack(String key) {
		return getMappable(key, ItemStack.class);
	}
	
	public ItemStack getItemStack(String key, ItemStack defaultVal, boolean applyDefault) {
		ItemStack o = getMappable(key, ItemStack.class);
		if(o != null) return o;
		if(applyDefault) set(key, defaultVal);
		return defaultVal;
	}
	
	public List<ItemStack> getItemStackList(String key) {
		return getMappableList(key, ItemStack.class);
	}
	
	public List<ItemStack> getItemStackList(String key, List<ItemStack> defaultVal, boolean applyDefault) {
		List<ItemStack> o = getMappableList(key, ItemStack.class);
		if(o != null) return o;
		if(applyDefault) set(key, defaultVal);
		return defaultVal;
	}
	
	public static void setTexture(SkullMeta im, String url) {
		try {
			Class<?> gameProfileClass = Class.forName("com.mojang.authlib.GameProfile");
			Class<?> propertyClass = Class.forName("com.mojang.authlib.properties.Property");
			Object gameProfile = gameProfileClass.getConstructor(UUID.class, String.class).newInstance(UUID.randomUUID(), null);
			Object propertyMap = gameProfileClass.getMethod("getProperties").invoke(gameProfile);
			propertyMap.getClass().getMethod("put", Object.class, Object.class)
				.invoke(propertyMap, "textures",
						propertyClass.getConstructor(String.class, String.class)
						.newInstance("textures", Base64.getEncoder().encodeToString(("{textures:{SKIN:{url:\""+url+"\"}}}").getBytes())));
			Field profileField = im.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(im, gameProfile);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
	
	public static void setRawTexture(SkullMeta im, String raw) {
		try {
			Class<?> gameProfileClass = Class.forName("com.mojang.authlib.GameProfile");
			Class<?> propertyClass = Class.forName("com.mojang.authlib.properties.Property");
			Object gameProfile = gameProfileClass.getConstructor(UUID.class, String.class).newInstance(UUID.randomUUID(), null);
			Object propertyMap = gameProfileClass.getMethod("getProperties").invoke(gameProfile);
			propertyMap.getClass().getMethod("put", Object.class, Object.class)
				.invoke(propertyMap, "textures",
						propertyClass.getConstructor(String.class, String.class)
						.newInstance("textures", raw));
			Field profileField = im.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(im, gameProfile);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
	
	public static String getTexture(SkullMeta im) {
		try {
			Field profileField = im.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			Object gameProfile = profileField.get(im);
			if(gameProfile != null) {
				Object propertyMap = gameProfile.getClass().getMethod("getProperties").invoke(gameProfile);
				Collection<?> propertyCollection = (Collection<?>) propertyMap.getClass().getMethod("get", Object.class).invoke(propertyMap, "textures");
				Iterator<?> propertyIterator = propertyCollection.iterator();
				
				if(propertyIterator.hasNext()) {
					Object property = propertyIterator.next();
					String rawTexture = new String(Base64.getDecoder().decode(((String)property.getClass().getMethod("getValue").invoke(property)).getBytes()));
					String texture = rawTexture.substring("{\"textures\":{\"SKIN\":{\"url\":\"".length(), rawTexture.length() - "\"}}}".length());
					return texture;
				}
			}
			return null;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			throw new FriendlyException(e);
		}
	}
	
//	public static void setTexture(SkullMeta im, String url) {
//		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
//		profile.getProperties().put("textures", new com.mojang.authlib.properties.Property("textures", new String(Base64.getEncoder().encode(("{textures:{SKIN:{url:\""+url+"\"}}}").getBytes()))));
//		Field profileField = null;
//		try {
//			profileField = im.getClass().getDeclaredField("profile");
//			profileField.setAccessible(true);
//			profileField.set(im, profile);
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//	}
//	
//	public static void setRawTexture(SkullMeta im, String raw) {
//		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
//		profile.getProperties().put("textures", new com.mojang.authlib.properties.Property("textures", raw));
//		Field profileField = null;
//		try {
//			profileField = im.getClass().getDeclaredField("profile");
//			profileField.setAccessible(true);
//			profileField.set(im, profile);
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//	}
//	
//	public static String getRawTexture(SkullMeta m) {
//		try {
//			Field profileField = m.getClass().getDeclaredField("profile");
//			profileField.setAccessible(true);
//			GameProfile profile = (GameProfile) profileField.get(m);
//			if(profile != null) {
//				Collection<com.mojang.authlib.properties.Property> txt = profile.getProperties().get("textures");
//				Iterator<com.mojang.authlib.properties.Property> it = txt.iterator();
//				if(it.hasNext()) {
//					return it.next().getValue();
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
}
