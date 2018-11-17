package me.mrletsplay.mrcore.bukkitimpl.config;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Base64;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.mrletsplay.mrcore.bukkitimpl.ItemUtils;
import me.mrletsplay.mrcore.config.v2.JSONObjectMapper;
import me.mrletsplay.mrcore.config.v2.builder.JSONMapperBuilder;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.mrcore.misc.FriendlyException;

public class BukkitConfigMappers {
	
	public static final JSONObjectMapper<Location> LOCATION_MAPPER = new JSONMapperBuilder<>(Location.class,
			(sec, json) -> new Location(
						Bukkit.getWorld(json.getString("world")),
						json.getDouble("x"),
						json.getDouble("y"),
						json.getDouble("z"),
						json.has("pitch") ? (float) json.getDouble("pitch") : 0f,
						json.has("yaw") ? (float) json.getDouble("yaw") : 0f
					)
			)
			.mapDouble("x", Location::getX, null).then()
			.mapDouble("y", Location::getY, null).then()
			.mapDouble("z", Location::getZ, null).then()
			.mapDouble("pitch", l -> (double) l.getPitch(), null).then()
			.mapDouble("yaw", l -> (double) l.getYaw(), null).then()
			.create();
	
	/*
	 * TODO: BlockStateMeta, KnowledgeBookMeta, TropicalFishBucketMeta
	 */
	
	@SuppressWarnings("deprecation")
	public static final JSONObjectMapper<ItemStack> ITEM_MAPPER = new JSONMapperBuilder<>(ItemStack.class,
			(sec, json) -> new ItemStack(Material.valueOf(json.getString("type").toUpperCase()), json.getInt("amount")))
			.mapString("type", it -> it.getType().name(), null).then()
			.mapInteger("amount", ItemStack::getAmount, null).then()
			.mapInteger("durability", i -> (int) i.getDurability(), (i, v) -> i.setDurability(v.shortValue())).then()
			.mapString("name", i -> i.getItemMeta().getDisplayName(), ItemUtils::setDisplayName).onlyMapIf(ItemStack::hasItemMeta).then()
			.mapJSONArray("lore", i -> new JSONArray(i.getItemMeta().getLore()), (i, a) -> i.getItemMeta().setLore(Complex.castList(a, String.class).get())).onlyMapIf(ItemStack::hasItemMeta).then()
			.mapJSONObject("enchantments", i -> {
				JSONObject o = new JSONObject();
				for(Map.Entry<Enchantment, Integer> ench : i.getItemMeta().getEnchants().entrySet()) {
					o.put(ench.getKey().getName(), ench.getValue());
				}
				return o;
			}, (i, e) -> {
				 ItemMeta m = i.getItemMeta();
				 for(String k : e.keySet()) {
					 m.addEnchant(Enchantment.getByName(k), e.getInt(k), true);
				 }
				 i.setItemMeta(m);
			}).onlyMapIf(ItemStack::hasItemMeta).then()
			.mapJSONArray("flags",
					i -> new JSONArray(i.getItemMeta().getItemFlags().stream().map(ItemFlag::name).collect(Collectors.toList())),
					(i, a) -> a.forEach(f -> i.getItemMeta().addItemFlags(ItemFlag.valueOf(((String) f).toUpperCase()))))
				.onlyMapIf(ItemStack::hasItemMeta).then()
			.mapJSONObject("banner", i -> {
				BannerMeta m = (BannerMeta) i.getItemMeta();
				JSONObject b = new JSONObject();
				b.set("color", m.getBaseColor().name());
				JSONObject patterns = new JSONObject();
				for(Pattern p : m.getPatterns()) {
					b.set(p.getPattern().name(), p.getColor().name());
				}
				b.set("patterns", patterns);
				return b;
			}, (i, j) -> {
				BannerMeta m = (BannerMeta) i.getItemMeta();
				if(j.has("color")) {
					m.setBaseColor(DyeColor.valueOf(j.getString("color").toUpperCase()));
				}
				if(j.has("patterns")) {
					JSONObject ps = j.getJSONObject("patterns");
					for(String pK : ps.keySet()) {
						m.addPattern(new Pattern(DyeColor.valueOf(ps.getString(pK).toUpperCase()), PatternType.valueOf(ps.getString(pK).toUpperCase())));
					}
				}
				i.setItemMeta(m);
			}).onlyMapIf(i -> i.getItemMeta() instanceof BannerMeta).then()
			.mapJSONObject("book", i -> {
				BookMeta m = (BookMeta) i.getItemMeta();
				JSONObject b = new JSONObject();
				b.set("author", m.getAuthor());
				b.set("generation", m.getGeneration().name());
				b.set("title", m.getTitle());
				b.set("pages", new JSONArray(m.getPages()));
				return b;
			}, (i, j) -> {
				BookMeta m = (BookMeta) i.getItemMeta();
				if(j.has("author")) m.setAuthor(j.getString("author"));
				if(j.has("generation")) m.setGeneration(Generation.valueOf(j.getString("generation").toUpperCase()));
				if(j.has("title")) m.setTitle(j.getString("title"));
				if(j.has("pages")) m.setPages(Complex.castList(j.getJSONArray("pages"), String.class).get());
			}).onlyMapIf(i -> i.getItemMeta() instanceof BookMeta).then()
			.mapJSONObject("enchantment-storage", i -> {
				EnchantmentStorageMeta m = (EnchantmentStorageMeta) i.getItemMeta();
				JSONObject o = new JSONObject();
				for(Map.Entry<Enchantment, Integer> ench : m.getStoredEnchants().entrySet()) {
					o.put(ench.getKey().getName(), ench.getValue());
				}
				return o;
			}, (i, j) -> {
				EnchantmentStorageMeta m = (EnchantmentStorageMeta) i.getItemMeta();
				for(String k : j.keySet()) {
					m.addStoredEnchant(Enchantment.getByName(k), j.getInt(k), true);
				}
				i.setItemMeta(m);
			}).onlyMapIf(i -> i.getItemMeta() instanceof EnchantmentStorageMeta).then()
			.mapJSONObject("firework-effect", i -> {
				FireworkEffectMeta m = (FireworkEffectMeta) i.getItemMeta();
				JSONObject f = new JSONObject();
				if(m.hasEffect()) {
					FireworkEffect eff = m.getEffect();
					f.set("colors", eff.getColors().stream().map(c -> Integer.toHexString(c.asRGB())).collect(Collectors.toList()));
					f.set("fade-colors", eff.getFadeColors().stream().map(c -> Integer.toHexString(c.asRGB())).collect(Collectors.toList()));
					f.set("type", eff.getType().name());
					f.set("flicker", eff.hasFlicker());
					f.set("trail", eff.hasTrail());
				}
				return f;
			}, (i, j) -> {
				FireworkEffectMeta m = (FireworkEffectMeta) i.getItemMeta();
				FireworkEffect.Builder b = FireworkEffect.builder();
				if(j.has("colors")) b.withColor(j.getJSONArray("colors").stream().map(c -> Color.fromRGB(Integer.parseInt((String) c, 16))).collect(Collectors.toList()));
				if(j.has("fade-colors")) b.withFade(j.getJSONArray("fade-colors").stream().map(c -> Color.fromRGB(Integer.parseInt((String) c, 16))).collect(Collectors.toList()));
				if(j.has("type")) b.with(Type.valueOf(j.getString("type").toUpperCase()));
				if(j.has("flicker")) b.flicker(j.getBoolean("flicker"));
				if(j.has("trail")) b.trail(j.getBoolean("trail"));
				m.setEffect(b.build());
				i.setItemMeta(m);
			}).onlyMapIf(i -> i.getItemMeta() instanceof FireworkEffectMeta).then()
			.mapJSONObject("firework", i -> {
				FireworkMeta m = (FireworkMeta) i.getItemMeta();
				JSONObject f = new JSONObject();
				f.set("power", m.getPower());
				if(m.hasEffects()) {
					JSONArray es = new JSONArray();
					for(FireworkEffect eff : m.getEffects()) {
						JSONObject e = new JSONObject();
						e.set("colors", eff.getColors().stream().map(c -> Integer.toHexString(c.asRGB())).collect(Collectors.toList()));
						e.set("fade-colors", eff.getFadeColors().stream().map(c -> Integer.toHexString(c.asRGB())).collect(Collectors.toList()));
						e.set("type", eff.getType().name());
						e.set("flicker", eff.hasFlicker());
						e.set("trail", eff.hasTrail());
						es.add(e);
					}
					f.set("effects", es);
				}
				return f;
			}, (i, j) -> {
				FireworkMeta m = (FireworkMeta) i.getItemMeta();
				if(j.has("power")) m.setPower(j.getInt("power"));
				if(j.has("effects")) {
					JSONArray efs = j.getJSONArray("effects");
					for(JSONObject e : Complex.castList(efs, JSONObject.class).get()) {
						FireworkEffect.Builder b = FireworkEffect.builder();
						if(e.has("colors")) b.withColor(e.getJSONArray("colors").stream().map(c -> Color.fromRGB(Integer.parseInt((String) c, 16))).collect(Collectors.toList()));
						if(e.has("fade-colors")) b.withFade(e.getJSONArray("fade-colors").stream().map(c -> Color.fromRGB(Integer.parseInt((String) c, 16))).collect(Collectors.toList()));
						if(e.has("type")) b.with(Type.valueOf(e.getString("type").toUpperCase()));
						if(e.has("flicker")) b.flicker(e.getBoolean("flicker"));
						if(e.has("trail")) b.trail(e.getBoolean("trail"));
						m.addEffect(b.build());
					}
				}
				i.setItemMeta(m);
			}).onlyMapIf(i -> i.getItemMeta() instanceof FireworkMeta).then()
			.mapJSONObject("leather-armor", i -> {
				LeatherArmorMeta m = (LeatherArmorMeta) i.getItemMeta();
				JSONObject l = new JSONObject();
				l.set("color", Integer.toHexString(m.getColor().asRGB()));
				return l;
			}, (i, j) -> {
				LeatherArmorMeta m = (LeatherArmorMeta) i.getItemMeta();
				if(j.has("color")) m.setColor(Color.fromRGB(Integer.parseInt(j.getString("color"), 16)));
				i.setItemMeta(m);
			}).onlyMapIf(i -> i.getItemMeta() instanceof LeatherArmorMeta).then()
			.mapJSONObject("map", i -> {
				MapMeta m = (MapMeta) i.getItemMeta();
				JSONObject l = new JSONObject();
				l.set("color", Integer.toHexString(m.getColor().asRGB()));
				l.set("location-name", m.getLocationName());
				l.set("map-id", m.getMapId());
				return l;
			}, (i, j) -> {
				MapMeta m = (MapMeta) i.getItemMeta();
				if(j.has("color")) m.setColor(Color.fromRGB(Integer.parseInt(j.getString("color"), 16)));
				if(j.has("location-name")) m.setLocationName(j.getString("location-name"));
				if(j.has("map-id")) m.setMapId(j.getInt("map-id"));
				i.setItemMeta(m);
			}).onlyMapIf(i -> i.getItemMeta() instanceof MapMeta).then()
			.mapJSONObject("potion", i -> {
				PotionMeta m = (PotionMeta) i.getItemMeta();
				JSONObject f = new JSONObject();
				f.set("color", Integer.toHexString(m.getColor().asRGB()));
				if(m.hasCustomEffects()) {
					JSONArray es = new JSONArray();
					for(PotionEffect eff : m.getCustomEffects()) {
						JSONObject e = new JSONObject();
						e.set("amplifier", eff.getAmplifier());
						e.set("duration", eff.getDuration());
						e.set("type", eff.getType().getName());
						e.set("particles", eff.hasParticles());
						e.set("icon", eff.hasIcon());
						e.set("ambient", eff.isAmbient());
						es.add(e);
					}
					f.set("effects", es);
				}
				return f;
			}, (i, j) -> {
				PotionMeta m = (PotionMeta) i.getItemMeta();
				if(j.has("color")) m.setColor(Color.fromRGB(Integer.parseInt(j.getString("color"), 16)));
				if(j.has("effects")) {
					JSONArray efs = j.getJSONArray("effects");
					for(JSONObject e : Complex.castList(efs, JSONObject.class).get()) {
						PotionEffectType type = PotionEffectType.getByName(e.getString("type"));
						int duration = j.getInt("duration");
						int amplifier = j.getInt("amplifier");
						boolean ambient = true, particles = true, icon = true;
						if(e.has("ambient")) ambient = e.getBoolean("ambient");
						if(e.has("particles")) ambient = e.getBoolean("particles");
						if(e.has("icon")) ambient = e.getBoolean("icon");
						m.addCustomEffect(new PotionEffect(type, duration, amplifier, ambient, particles, icon), true);
					}
				}
				i.setItemMeta(m);
			}).onlyMapIf(i -> i.getItemMeta() instanceof PotionMeta).then()
			.mapJSONObject("spawn-egg", i -> {
				SpawnEggMeta m = (SpawnEggMeta) i.getItemMeta();
				JSONObject l = new JSONObject();
				l.set("spawned-type", m.getSpawnedType().name());
				return l;
			}, (i, j) -> {
				SpawnEggMeta m = (SpawnEggMeta) i.getItemMeta();
				if(j.has("spawned-type")) m.setSpawnedType(EntityType.valueOf(j.getString("spawned-type").toUpperCase()));
				i.setItemMeta(m);
			}).onlyMapIf(i -> i.getItemMeta() instanceof SpawnEggMeta).then()
			.mapJSONObject("skull", i -> {
				SkullMeta m = (SkullMeta) i.getItemMeta();
				JSONObject s = new JSONObject();
				if(m.hasOwner()) {
					// Using SkullMeta#getOwner for backwards compatibility
					s.set("owner", m.getOwner());
				}else {
					String texture = getTexture(m);
					if(texture != null) s.set("texture", texture);
				}
				return s;
			}, (i, j) -> {
				SkullMeta m = (SkullMeta) i.getItemMeta();
				if(j.has("owner")) {
					// Using SkullMeta#setOwner for backwards compatability
					m.setOwner(j.getString("owner"));
				}
				if(j.has("texture")) {
					setTexture(m, j.getString("texture"));
				}
				if(j.has("hash")) {
					setRawTexture(m, j.getString("hash"));
				}
				i.setItemMeta(m);
			}).onlyMapIf(i -> i.getItemMeta() instanceof SkullMeta).then()
			.create();
	
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
	
}
