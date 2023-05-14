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
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import me.mrletsplay.mrcore.bukkitimpl.ItemUtils;
import me.mrletsplay.mrcore.bukkitimpl.versioned.NMSVersion;
import me.mrletsplay.mrcore.bukkitimpl.versioned.item.Pre113ItemMeta;
import me.mrletsplay.mrcore.bukkitimpl.versioned.item.VersionedCrossbowMeta;
import me.mrletsplay.mrcore.config.ConfigValueType;
import me.mrletsplay.mrcore.config.mapper.JSONObjectMapper;
import me.mrletsplay.mrcore.config.mapper.builder.JSONMapperBuilder;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.JSONType;
import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.mrcore.misc.FriendlyException;

public class BukkitConfigMappers {

	public static final JSONObjectMapper<Location> LOCATION_MAPPER = new JSONMapperBuilder<>(Location.class,
			(sec, json) -> new Location(
						Bukkit.getWorld(json.getString("world")),
						json.getDouble("x"),
						json.getDouble("y"),
						json.getDouble("z"),
						json.has("yaw") ? json.getFloat("yaw") : 0f,
						json.has("pitch") ? json.getFloat("pitch") : 0f
					)
			)
			.mapString("world", l -> l.getWorld().getName(), null).then()
			.mapDouble("x", Location::getX, null).then()
			.mapDouble("y", Location::getY, null).then()
			.mapDouble("z", Location::getZ, null).then()
			.mapDouble("pitch", l -> (double) l.getPitch(), null).then()
			.mapDouble("yaw", l -> (double) l.getYaw(), null).then()
			.create();

	@SuppressWarnings("deprecation")
	public static final JSONObjectMapper<ItemStack> ITEM_MAPPER = new JSONMapperBuilder<>(ItemStack.class,
			(sec, json) -> {
				return new ItemStack(Material.valueOf(json.getString("type").toUpperCase()), json.has("amount") ? json.getInt("amount"): 1);
			})
			.mapString("type", it -> it.getType().name(), null).then()
			.mapInteger("amount", ItemStack::getAmount, null).then()
			.mapInteger("durability", i -> (int) i.getDurability(), (i, v) -> i.setDurability(v.shortValue()))
				.onlyConstructIfNotNull()
				.then()
			.mapString("name", i -> i.getItemMeta().getDisplayName(), ItemUtils::setDisplayName)
				.onlyMapIf(ItemStack::hasItemMeta)
				.onlyConstructIfExists()
				.then()
			.mapJSONArray("lore", i -> new JSONArray(i.getItemMeta().getLore()), (i, a) -> ItemUtils.setLore(i, Complex.castList(a.toList(), String.class).get()))
				.onlyMapIf(ItemStack::hasItemMeta)
				.onlyMapIf(i -> i.getItemMeta().hasLore())
				.onlyConstructIfNotNull()
				.then()
			.mapJSONObject("enchantments", i -> {
				JSONObject o = new JSONObject();
				for(Map.Entry<Enchantment, Integer> ench : i.getItemMeta().getEnchants().entrySet()) {
					o.put(ench.getKey().getName(), ench.getValue());
				}
				return o;
			}, (i, e) -> {
				 ItemMeta m = i.getItemMeta();
				 for(String k : e.keys()) {
					 m.addEnchant(Enchantment.getByName(k), e.getInt(k), true);
				 }
				 i.setItemMeta(m);
			}).onlyMapIf(ItemStack::hasItemMeta).onlyConstructIfNotNull().then()
			.mapJSONArray("flags",
					i -> new JSONArray(i.getItemMeta().getItemFlags().stream().map(ItemFlag::name).collect(Collectors.toList())),
					(i, a) -> a.forEach(f -> i.getItemMeta().addItemFlags(ItemFlag.valueOf(((String) f).toUpperCase()))))
				.onlyMapIf(ItemStack::hasItemMeta).onlyConstructIfNotNull().then()
			.mapBoolean("unbreakable", i -> {
				if(NMSVersion.getCurrentServerVersion().isOlderThan(NMSVersion.V1_13_R1))
					return new Pre113ItemMeta(i.getItemMeta()).isUnbreakable();
				return i.getItemMeta().isUnbreakable();
			}, (i, v) -> {
				ItemMeta m = i.getItemMeta();
				if(NMSVersion.getCurrentServerVersion().isOlderThan(NMSVersion.V1_13_R1)) {
					new Pre113ItemMeta(m).setUnbreakable(v);
					return;
				}
				m.setUnbreakable(v);
				i.setItemMeta(m);
			})
				.onlyMapIf(ItemStack::hasItemMeta)
				.onlyConstructIfNotNull()
				.then()
			.mapInteger("custom-model-data", i -> i.getItemMeta().getCustomModelData(), (i, v) -> i.getItemMeta().setCustomModelData(v))
				.onlyMapIf(i -> NMSVersion.getCurrentServerVersion().isNewerThanOrEqualTo(NMSVersion.V1_14_R1))
				.onlyMapIf(i -> i.hasItemMeta() && i.getItemMeta().hasCustomModelData())
				.onlyConstructIfNotNull()
				.then()
			.mapJSONObject("banner", i -> {
				BannerMeta m = (BannerMeta) i.getItemMeta();
				JSONObject b = new JSONObject();
				JSONArray patterns = new JSONArray();
				for(Pattern p : m.getPatterns()) {
					JSONObject pattern = new JSONObject();
					pattern.set("pattern", p.getPattern().name());
					pattern.set("color", p.getColor().name());
					patterns.add(pattern);
				}
				b.set("patterns", patterns);
				return b;
			}, (i, j) -> {
				BannerMeta m = (BannerMeta) i.getItemMeta();
				if(j.has("color") && NMSVersion.getCurrentServerVersion().isOlderThan(NMSVersion.V1_13_R1)) {
					m.setBaseColor(DyeColor.valueOf(j.getString("color").toUpperCase()));
				}
				if(j.has("patterns")) {
					JSONArray ps = j.getJSONArray("patterns");
					for(JSONObject pK : Complex.castList(ps.toList(), JSONObject.class).get()) {
						m.addPattern(new Pattern(DyeColor.valueOf(pK.getString("color").toUpperCase()), PatternType.valueOf(pK.getString("pattern").toUpperCase())));
					}
				}
				i.setItemMeta(m);
			})
				.onlyMapIf(i -> i.getItemMeta() instanceof BannerMeta)
				.onlyConstructIfNotNull()
				.then()
			.mapJSONObject("book", i -> {
				BookMeta m = (BookMeta) i.getItemMeta();
				JSONObject b = new JSONObject();
				b.set("author", m.getAuthor());
				if(NMSVersion.getCurrentServerVersion().isNewerThan(NMSVersion.V1_8_R3) && m.hasGeneration()) b.set("generation", m.getGeneration().name());
				b.set("title", m.getTitle());
				b.set("pages", new JSONArray(m.getPages()));
				return b;
			}, (i, j) -> {
				BookMeta m = (BookMeta) i.getItemMeta();
				if(j.has("author")) m.setAuthor(j.getString("author"));
				if(NMSVersion.getCurrentServerVersion().isNewerThan(NMSVersion.V1_8_R3) && j.has("generation")) m.setGeneration(org.bukkit.inventory.meta.BookMeta.Generation.valueOf(j.getString("generation").toUpperCase()));
				if(j.has("title")) m.setTitle(j.getString("title"));
				if(j.has("pages")) m.setPages(Complex.castList(j.getJSONArray("pages").toList(), String.class).get());
				i.setItemMeta(m);
			})
				.onlyMapIf(i -> i.getItemMeta() instanceof BookMeta)
				.onlyConstructIfNotNull()
				.then()
			.mapJSONObject("enchantment-storage", i -> {
				EnchantmentStorageMeta m = (EnchantmentStorageMeta) i.getItemMeta();
				JSONObject o = new JSONObject();
				for(Map.Entry<Enchantment, Integer> ench : m.getStoredEnchants().entrySet()) {
					o.put(ench.getKey().getName(), ench.getValue());
				}
				return o;
			}, (i, j) -> {
				EnchantmentStorageMeta m = (EnchantmentStorageMeta) i.getItemMeta();
				for(String k : j.keys()) {
					m.addStoredEnchant(Enchantment.getByName(k), j.getInt(k), true);
				}
				i.setItemMeta(m);
			})
				.onlyMapIf(i -> i.getItemMeta() instanceof EnchantmentStorageMeta)
				.onlyConstructIfNotNull()
				.then()
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
			})
				.onlyMapIf(i -> i.getItemMeta() instanceof FireworkEffectMeta)
				.onlyConstructIfNotNull()
				.then()
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
					for(JSONObject e : Complex.castList(efs.toList(), JSONObject.class).get()) {
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
			})
				.onlyMapIf(i -> i.getItemMeta() instanceof FireworkMeta)
				.onlyConstructIfNotNull()
				.then()
			.mapJSONObject("knowledge-book", i -> {
				JSONObject o = new JSONObject(i.getItemMeta().serialize());
				o.set("==", "ItemMeta");
				return new JSONObject(o);
			}, (i, j) -> {
				ItemMeta m = (ItemMeta) ConfigurationSerialization.deserializeObject(j.toMap());
				i.setItemMeta(m);
			})
				.onlyMapIf(i -> NMSVersion.getCurrentServerVersion().isNewerThanOrEqualTo(NMSVersion.V1_12_R1))
				.onlyConstructIf((a, b, c, d) -> NMSVersion.getCurrentServerVersion().isNewerThanOrEqualTo(NMSVersion.V1_12_R1))
				.onlyMapIf(i -> i.getItemMeta() instanceof org.bukkit.inventory.meta.KnowledgeBookMeta)
				.onlyConstructIfNotNull()
				.then()
			.mapJSONObject("leather-armor", i -> {
				LeatherArmorMeta m = (LeatherArmorMeta) i.getItemMeta();
				JSONObject l = new JSONObject();
				l.set("color", Integer.toHexString(m.getColor().asRGB()));
				return l;
			}, (i, j) -> {
				LeatherArmorMeta m = (LeatherArmorMeta) i.getItemMeta();
				if(j.has("color")) m.setColor(Color.fromRGB(Integer.parseInt(j.getString("color"), 16)));
				i.setItemMeta(m);
			})
				.onlyMapIf(i -> i.getItemMeta() instanceof LeatherArmorMeta)
				.onlyConstructIfNotNull()
				.then()
			.mapJSONObject("map", i -> {
				MapMeta m = (MapMeta) i.getItemMeta();
				JSONObject l = new JSONObject();
				if(m.hasColor()) l.set("color", Integer.toHexString(m.getColor().asRGB()));
				if(m.hasLocationName()) l.set("location-name", m.getLocationName());
				if(m.hasMapId()) l.set("map-id", m.getMapId());
				return l;
			}, (i, j) -> {
				MapMeta m = (MapMeta) i.getItemMeta();
				if(j.has("color")) m.setColor(Color.fromRGB(Integer.parseInt(j.getString("color"), 16)));
				if(j.has("location-name")) m.setLocationName(j.getString("location-name"));
				if(j.has("map-id")) m.setMapId(j.getInt("map-id"));
				i.setItemMeta(m);
			})
				.onlyMapIf(i -> i.getItemMeta() instanceof MapMeta)
				.onlyConstructIfNotNull()
				.then()
			.mapJSONObject("potion", i -> {
				PotionMeta m = (PotionMeta) i.getItemMeta();
				JSONObject f = new JSONObject();
				if(m.hasColor()) f.set("color", Integer.toHexString(m.getColor().asRGB()));
				f.set("type", m.getBasePotionData().getType().name());
				f.set("extended", m.getBasePotionData().isExtended());
				f.set("upgraded", m.getBasePotionData().isUpgraded());
				if(m.hasCustomEffects()) {
					JSONArray es = new JSONArray();
					for(PotionEffect eff : m.getCustomEffects()) {
						JSONObject e = new JSONObject();
						e.set("amplifier", eff.getAmplifier());
						e.set("duration", eff.getDuration());
						e.set("type", eff.getType().getName());
						e.set("particles", eff.hasParticles());
						if(NMSVersion.getCurrentServerVersion().isNewerThanOrEqualTo(NMSVersion.V1_13_R1)) e.set("icon", eff.hasIcon());
						e.set("ambient", eff.isAmbient());
						es.add(e);
					}
					f.set("custom-effects", es);
				}
				return f;
			}, (i, j) -> {
				PotionMeta m = (PotionMeta) i.getItemMeta();
				if(j.has("color")) m.setColor(Color.fromRGB(Integer.parseInt(j.getString("color"), 16)));
				PotionData d = new PotionData(PotionType.valueOf(j.getString("type")), j.getBoolean("extended"), j.getBoolean("upgraded"));
				m.setBasePotionData(d);
				if(j.has("custom-effects")) {
					JSONArray efs = j.getJSONArray("custom-effects");
					for(JSONObject e : Complex.castList(efs.toList(), JSONObject.class).get()) {
						PotionEffectType type = PotionEffectType.getByName(e.getString("type"));
						int duration = j.getInt("duration");
						int amplifier = j.getInt("amplifier");
						boolean ambient = true, particles = true, icon = true;
						if(e.has("ambient")) ambient = e.getBoolean("ambient");
						if(e.has("particles")) particles = e.getBoolean("particles");
						if(e.has("icon")) icon = e.getBoolean("icon");
						if(NMSVersion.getCurrentServerVersion().isNewerThanOrEqualTo(NMSVersion.V1_13_R1)) {
							m.addCustomEffect(new PotionEffect(type, duration, amplifier, ambient, particles, icon), true);
						}else {
							m.addCustomEffect(new PotionEffect(type, duration, amplifier, ambient, particles), true);
						}
					}
				}
				i.setItemMeta(m);
			})
				.onlyMapIf(i -> i.getItemMeta() instanceof PotionMeta)
				.onlyConstructIfNotNull()
				.then()
			.mapJSONObject("spawn-egg", i -> {
				org.bukkit.inventory.meta.SpawnEggMeta m = (org.bukkit.inventory.meta.SpawnEggMeta) i.getItemMeta();
				JSONObject l = new JSONObject();
				l.set("spawned-type", m.getSpawnedType().name());
				return l;
			}, (i, j) -> {
				org.bukkit.inventory.meta.SpawnEggMeta m = (org.bukkit.inventory.meta.SpawnEggMeta) i.getItemMeta();
				if(j.has("spawned-type")) m.setSpawnedType(EntityType.valueOf(j.getString("spawned-type").toUpperCase()));
				i.setItemMeta(m);
			})
				.onlyMapIf(i -> NMSVersion.getCurrentServerVersion().isNewerThan(NMSVersion.V1_10_R1))
				.onlyMapIf(i -> NMSVersion.getCurrentServerVersion().isOlderThan(NMSVersion.V1_13_R1))
				.onlyMapIf(i -> i.getItemMeta() instanceof org.bukkit.inventory.meta.SpawnEggMeta)
				.onlyConstructIfNotNull()
				.then()
			.mapJSONObject("skull", i -> {
				SkullMeta m = (SkullMeta) i.getItemMeta();
				JSONObject s = new JSONObject();
				if(m.hasOwner()) {
					if(NMSVersion.getCurrentServerVersion().isOlderThan(NMSVersion.V1_12_R1)) {
						s.set("owner", m.getOwner());
					}else {
						s.set("owner", m.getOwningPlayer().getUniqueId().toString());
					}
				}else {
					String texture = getTexture(m);
					if(texture != null) s.set("texture", texture);
				}
				return s;
			}, (i, j) -> {
				SkullMeta m = (SkullMeta) i.getItemMeta();
				if(j.has("owner")) {
					if(NMSVersion.getCurrentServerVersion().isOlderThan(NMSVersion.V1_12_R1)) {
						m.setOwner(j.getString("owner"));
					}else {
						m.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(j.getString("owner"))));
					}
				}
				if(j.has("texture")) {
					setTexture(m, j.getString("texture"));
				}
				if(j.has("hash")) {
					setRawTexture(m, j.getString("hash"));
				}
				i.setItemMeta(m);
			})
				.onlyMapIf(i -> i.getItemMeta() instanceof SkullMeta)
				.onlyConstructIfNotNull()
				.then()
			.mapJSONObject("crossbow", (s, i) -> {
				VersionedCrossbowMeta m = new VersionedCrossbowMeta(i.getItemMeta());
				JSONObject b = new JSONObject();
				if(m.hasChargedProjectiles()) b.set("charged-projectiles", new JSONArray(m.getChargedProjectiles().stream().map(k -> ConfigValueType.createCompatible(s, k).get()).collect(Collectors.toList())));
				return b;
			}, (s, i, j) -> {
				VersionedCrossbowMeta m = new VersionedCrossbowMeta(i.getItemMeta());
				if(j.has("charged-projectiles")) {
					JSONArray ps = j.getJSONArray("charged-projectiles");
					m.setChargedProjectiles(Complex.castList(ps.toList(), JSONObject.class).get().stream().map(p -> s.castType(p, ItemStack.class, Complex.value(ItemStack.class)).get()).collect(Collectors.toList()));
				}
				i.setItemMeta(m.getBukkit());
			})
				.onlyMapIf(i -> NMSVersion.getCurrentServerVersion().isNewerThanOrEqualTo(NMSVersion.V1_14_R1))
				.onlyConstructIf((a, b, c, d) -> NMSVersion.getCurrentServerVersion().isNewerThanOrEqualTo(NMSVersion.V1_14_R1))
				.onlyMapIf(i -> VersionedCrossbowMeta.isInstance(i.getItemMeta()))
				.onlyConstructIfNotNull()
				.then()
			.mapJSONObject("tropical-fish-bucket", i -> {
				JSONObject o = new JSONObject(i.getItemMeta().serialize());
				o.set("==", "ItemMeta");
				return new JSONObject(o);
			}, (i, j) -> {
				if(j.isOfType("fish-variant", JSONType.INTEGER)) j.set("fish-variant", j.getInt("fish-variant")); // Cast to int
				ItemMeta m = (ItemMeta) ConfigurationSerialization.deserializeObject(j.toMap());
				i.setItemMeta(m);
			})
				.onlyMapIf(i -> NMSVersion.getCurrentServerVersion().isNewerThanOrEqualTo(NMSVersion.V1_13_R1))
				.onlyConstructIf((a, b, c, d) -> NMSVersion.getCurrentServerVersion().isNewerThanOrEqualTo(NMSVersion.V1_13_R1))
				.onlyMapIf(i -> i.getItemMeta() instanceof org.bukkit.inventory.meta.TropicalFishBucketMeta)
				.onlyConstructIfNotNull()
				.then()
			.mapJSONObject("block-state", i -> {
				JSONObject o = new JSONObject(i.getItemMeta().serialize());
				o.set("==", "ItemMeta");
				return new JSONObject(o);
			}, (i, j) -> {
				ItemMeta m = (ItemMeta) ConfigurationSerialization.deserializeObject(j.toMap());
				i.setItemMeta(m);
			})
				.onlyMapIf(i -> NMSVersion.getCurrentServerVersion().isNewerThanOrEqualTo(NMSVersion.V1_9_R1))
				.onlyConstructIf((a, b, c, d) -> NMSVersion.getCurrentServerVersion().isNewerThanOrEqualTo(NMSVersion.V1_9_R1))
				.onlyMapIf(i -> i.getItemMeta() instanceof org.bukkit.inventory.meta.BlockStateMeta)
				.onlyConstructIfNotNull()
				.then()
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
