package me.mrletsplay.mrcore.bukkitimpl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.mrletsplay.mrcore.bukkitimpl.versioned.VersionedDyeColor;
import me.mrletsplay.mrcore.bukkitimpl.versioned.VersionedMaterial;
import me.mrletsplay.mrcore.misc.FriendlyException;

public class ItemUtils {
	
	public static ItemStack createVersioned(VersionedMaterial material, int amount) {
		return material.getCurrentMaterialDefinition().newStack(amount);
	}
	
	public static ItemStack createVersioned(VersionedMaterial material) {
		return material.getCurrentMaterialDefinition().newStack();
	}
	
	public static ItemStack createItem(VersionedMaterial m, int am) {
		return createVersioned(m, am);
	}
	
	public static ItemStack createItem(VersionedMaterial m, int am, String name, String... lore) {
		return createItem(m, am, name, Arrays.asList(lore));
	}
	
	public static ItemStack createItem(VersionedMaterial m, int am, String name, List<String> lore) {
		ItemStack i = createVersioned(m, am);
		ItemMeta me = i.getItemMeta();
		if(name!=null) me.setDisplayName(name);
		me.setLore(lore.stream().filter(l -> !l.isEmpty()).collect(Collectors.toList()));
		i.setItemMeta(me);
		return i;
	}

	public static ItemStack createItem(Material m, int am, int dam, String name, String... lore){
		return createItem(m, am, dam, name, Arrays.asList(lore));
	}

	@SuppressWarnings("deprecation")
	public static ItemStack createItem(Material m, int am, int dam, String name, List<String> lore){
		ItemStack i = new ItemStack(m, am, (short)dam);
		ItemMeta me = i.getItemMeta();
		me.setDisplayName(name);
		me.setLore(lore.stream().filter(l -> !l.isEmpty()).collect(Collectors.toList()));
		i.setItemMeta(me);
		return i;
	}
	
	public static ItemStack createItem(ItemStack it, String name, String... lore){
		return createItem(it, name, Arrays.asList(lore));
	}
	
	public static ItemStack createItem(ItemStack it, String name, List<String> lore){
		ItemStack i = new ItemStack(it);
		ItemMeta me = i.getItemMeta();
		me.setDisplayName(name);
		me.setLore(lore.stream().filter(l -> !l.isEmpty()).collect(Collectors.toList()));
		i.setItemMeta(me);
		return i;
	}

	public static void setDisplayName(ItemStack item, String name) {
		ItemMeta m = item.getItemMeta();
		m.setDisplayName(name);
		item.setItemMeta(m);
	}

	public static void setLore(ItemStack item, List<String> lore) {
		ItemMeta m = item.getItemMeta();
		m.setLore(lore);
		item.setItemMeta(m);
	}

	public static ItemStack blankBanner(VersionedDyeColor color) {
		return createBanner(null, color);
	}

	public static ItemStack arrowRight() {
		return arrowRight(VersionedDyeColor.WHITE);
	}

	public static ItemStack arrowLeft() {
		return arrowLeft(VersionedDyeColor.WHITE);
	}

	public static ItemStack arrowRight(VersionedDyeColor col){
		ItemStack i = createVersioned(VersionedMaterial.getBanner(col));
		BannerMeta m = (BannerMeta)i.getItemMeta();
		m.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		m.addPattern(new Pattern(DyeColor.BLACK, PatternType.RHOMBUS_MIDDLE));
		DyeColor c = col.getBukkitDyeColor();
		m.addPattern(new Pattern(c, PatternType.STRIPE_LEFT));
		m.addPattern(new Pattern(c, PatternType.SQUARE_TOP_LEFT));
		m.addPattern(new Pattern(c, PatternType.SQUARE_BOTTOM_LEFT));
		i.setItemMeta(m);
		return i;
	}
	
	public static ItemStack arrowLeft(VersionedDyeColor col){
		ItemStack i = createVersioned(VersionedMaterial.getBanner(col));
		BannerMeta m = (BannerMeta)i.getItemMeta();
		m.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		m.addPattern(new Pattern(DyeColor.BLACK, PatternType.RHOMBUS_MIDDLE));
		DyeColor c = col.getBukkitDyeColor();
		m.addPattern(new Pattern(c, PatternType.STRIPE_RIGHT));
		m.addPattern(new Pattern(c, PatternType.SQUARE_TOP_RIGHT));
		m.addPattern(new Pattern(c, PatternType.SQUARE_BOTTOM_RIGHT));
		i.setItemMeta(m);
		return i;
	}
	
	public static ItemStack letterC(VersionedDyeColor col){
		ItemStack i = createVersioned(VersionedMaterial.getBanner(col));
		BannerMeta m = (BannerMeta)i.getItemMeta();
		m.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		m.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
		m.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM));
		m.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
		DyeColor c = col.getBukkitDyeColor();
		m.addPattern(new Pattern(c, PatternType.STRIPE_MIDDLE));
		m.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
		m.addPattern(new Pattern(c, PatternType.BORDER));
		i.setItemMeta(m);
		return i;
	}
	
	public static ItemStack createBanner(String name, VersionedDyeColor baseCol, Pattern... patterns){
		ItemStack banner = createVersioned(VersionedMaterial.getBanner(baseCol));
		BannerMeta bMeta = (BannerMeta)banner.getItemMeta();
		bMeta.setDisplayName(name);
		for(Pattern p : patterns){
			bMeta.addPattern(p);
		}
		banner.setItemMeta(bMeta);
		return banner;
	}
	
	public static void addItemOrDrop(Player p, ItemStack item){
		HashMap<Integer,ItemStack> excess = p.getInventory().addItem(item);
		for(Map.Entry<Integer, ItemStack> me : excess.entrySet()){
			p.getWorld().dropItem(p.getLocation(), me.getValue());
		}
	}
	
	@SuppressWarnings("deprecation")
	public static ComparisonResult compareItems(ItemStack item1, ItemStack item2) {
		ComparisonResult res = new ComparisonResult();
		applyComparison(item1, item2, i -> i.getType(), ComparisonParameter.TYPE, res);
		applyComparison(item1, item2, i -> i.getAmount(), ComparisonParameter.AMOUNT, res);
		applyComparison(item1, item2, i -> i.getDurability(), ComparisonParameter.DURABILITY, res);
		if(item1.hasItemMeta() && item2.hasItemMeta()) {
			ItemMeta m1 = item1.getItemMeta(), m2 = item2.getItemMeta();
			applyComparison(m1, m2, m -> m.getDisplayName(), ComparisonParameter.NAME, res);
			applyComparison(m1, m2, m -> m.getLore(), ComparisonParameter.LORE, res);
			applyComparison(m1, m2, m -> m.getEnchants(), ComparisonParameter.ENCHANTS, res);
			
			if(m1 instanceof SkullMeta && m2 instanceof SkullMeta) {
				SkullMeta s1 = (SkullMeta) m1, s2 = (SkullMeta) m2;
				// Using SkullMeta#getOwner() for backwards compatability
				applyComparison(s1, s2, s -> s.getOwner(), ComparisonParameter.SKULL_OWNER, res);
			}
			
			if(m1 instanceof LeatherArmorMeta && m2 instanceof LeatherArmorMeta) {
				LeatherArmorMeta a1 = (LeatherArmorMeta) m1, a2 = (LeatherArmorMeta) m2;
				applyComparison(a1, a2, a -> a.getColor(), ComparisonParameter.LEATHER_ARMOR_COLOR, res);
			}
			
		}
		return res;
	}
	
	private static <T> void applyComparison(T o1, T o2, Function<T, Object> function, ComparisonParameter param, ComparisonResult result) {
		if(compare(o1, o2, function)) {
			result.matches.add(param);
		}else {
			result.doesntMatch.add(param);
		}
	}
	
	private static <T> boolean compare(T o1, T o2, Function<T, Object> function) {
		Object p1 = function.apply(o1), p2 = function.apply(o2);
		if(p1 == p2) return true;
		return p1 != null && p1.equals(p2);
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
	
	public static class ComparisonResult {
		
		private List<ComparisonParameter> matches, doesntMatch;
		
		private ComparisonResult() {
			this.matches = new ArrayList<>();
			this.doesntMatch = new ArrayList<>();
		}
		
		public List<ComparisonParameter> getParametersMatching() {
			return matches;
		}
		
		public List<ComparisonParameter> getParametersNotMatching() {
			return doesntMatch;
		}
		
		public boolean matches(ComparisonParameter... params) {
			return Arrays.stream(params).allMatch(this::matches);
		}
		
		public boolean matchesExplicitly(ComparisonParameter... params) {
			return Arrays.stream(params).allMatch(this::matchesExplicitly);
		}
		
		public boolean matches(ComparisonParameter param) {
			switch(param) {
				case ALL_APPLICABLE:
					return doesntMatch.isEmpty();
				case NONE_APPLICABLE:
					return matches.isEmpty();
				default:
					return !doesntMatch.contains(param);
			}
		}
		
		public boolean matchesExplicitly(ComparisonParameter param) {
			switch(param) {
				case ALL_APPLICABLE:
					return doesntMatch.isEmpty();
				case NONE_APPLICABLE:
					return matches.isEmpty();
				default:
					return matches.contains(param);
			}
		}
		
	}
	
	public static enum ComparisonParameter {
		
		ALL_APPLICABLE(true),
		NONE_APPLICABLE(true),
		
		TYPE(false),
		AMOUNT(false),
		DURABILITY(false),
		NAME(false),
		LORE(false),
		ENCHANTS(false),
		SKULL_OWNER(false),
		LEATHER_ARMOR_COLOR(false);
		
		public final boolean isParameterCollection;
		
		private ComparisonParameter(boolean isCollection) {
			this.isParameterCollection = isCollection;
		}
		
		
	}
	
}
