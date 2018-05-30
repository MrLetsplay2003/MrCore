package me.mrletsplay.mrcore.bukkitimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

public class ItemUtils {

	public static ItemStack createItem(Material m, int am, int dam, String name, String... lore){
		ItemStack i = new ItemStack(m, am, (short)dam);
		ItemMeta me = i.getItemMeta();
		me.setDisplayName(name);
		List<String> s = new ArrayList<>();
		for(String l:lore){
			s.add(l);
		}
		me.setLore(s);
		i.setItemMeta(me);
		return i;
	}

	public static ItemStack createItem(Material m, int am, int dam, String name, List<String> lore){
		ItemStack i = new ItemStack(m, am, (short)dam);
		ItemMeta me = i.getItemMeta();
		me.setDisplayName(name);
		List<String> s = new ArrayList<>();
		for(String l:lore){
			s.add(l);
		}
		me.setLore(s);
		i.setItemMeta(me);
		return i;
	}
	
	public static ItemStack createItem(ItemStack it, String name, String... lore){
		ItemStack i = new ItemStack(it);
		ItemMeta me = i.getItemMeta();
		me.setDisplayName(name);
		List<String> s = new ArrayList<>();
		for(String l:lore){
			s.add(l);
		}
		me.setLore(s);
		i.setItemMeta(me);
		return i;
	}
	
	public static ItemStack createItem(ItemStack it, String name, List<String> lore){
		ItemStack i = new ItemStack(it);
		ItemMeta me = i.getItemMeta();
		me.setDisplayName(name);
		List<String> s = new ArrayList<>();
		for(String l:lore){
			s.add(l);
		}
		me.setLore(s);
		i.setItemMeta(me);
		return i;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack arrowRight(DyeColor col){
		ItemStack i = new ItemStack(Material.BANNER);
		BannerMeta m = (BannerMeta)i.getItemMeta();
		m.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		m.setBaseColor(col);
		m.addPattern(new Pattern(DyeColor.BLACK, PatternType.RHOMBUS_MIDDLE));
		m.addPattern(new Pattern(col, PatternType.STRIPE_LEFT));
		m.addPattern(new Pattern(col, PatternType.SQUARE_TOP_LEFT));
		m.addPattern(new Pattern(col, PatternType.SQUARE_BOTTOM_LEFT));
		i.setItemMeta(m);
		return i;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack arrowLeft(DyeColor col){
		ItemStack i = new ItemStack(Material.BANNER);
		BannerMeta m = (BannerMeta)i.getItemMeta();
		m.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		m.setBaseColor(col);
		m.addPattern(new Pattern(DyeColor.BLACK, PatternType.RHOMBUS_MIDDLE));
		m.addPattern(new Pattern(col, PatternType.STRIPE_RIGHT));
		m.addPattern(new Pattern(col, PatternType.SQUARE_TOP_RIGHT));
		m.addPattern(new Pattern(col, PatternType.SQUARE_BOTTOM_RIGHT));
		i.setItemMeta(m);
		return i;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack letterC(DyeColor col){
		ItemStack i = new ItemStack(Material.BANNER);
		BannerMeta m = (BannerMeta)i.getItemMeta();
		m.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		m.setBaseColor(col);
		m.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
		m.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM));
		m.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
		m.addPattern(new Pattern(col, PatternType.STRIPE_MIDDLE));
		m.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
		m.addPattern(new Pattern(col, PatternType.BORDER));
		i.setItemMeta(m);
		return i;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack createBanner(String name, DyeColor baseCol, Pattern... patterns){
		ItemStack banner = new ItemStack(Material.BANNER);
		BannerMeta bMeta = (BannerMeta)banner.getItemMeta();
		bMeta.setDisplayName(name);
		bMeta.setBaseColor(baseCol);
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
			return Arrays.stream(params).allMatch(p -> matches(p));
		}
		
		public boolean matchesExplicitly(ComparisonParameter... params) {
			return Arrays.stream(params).allMatch(p -> matchesExplicitly(p));
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
//		SKULL_TEXTURE,
		LEATHER_ARMOR_COLOR(false);
		
		public final boolean isParameterCollection;
		
		private ComparisonParameter(boolean isCollection) {
			this.isParameterCollection = isCollection;
		}
		
		
	}
	
}
