package me.mrletsplay.mrcore.bukkitimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

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
	
	@Deprecated //TODO
	public static ComparisonResult compareItems(ItemStack item1, ItemStack item2) {
		return null;
//		if(item1 == null || item2 == null) {
//			if(item1 == item2) {
//				return new ComparisonResult(Arrays.asList(ComparisonParameter.NULL));
//			}else {
//				return new ComparisonResult(Arrays.asList(ComparisonParameter.NONE_APPLICABLE));
//			}
//		}else {
//			List<ComparisonParameter> params = new ArrayList<>();
//			List<ComparisonParameter> antiParams = new ArrayList<>();
//			if(item1.getType().equals(item2.getType())) {
//				params.add(ComparisonParameter.TYPE);
//			}
//			if(item1.getDurability() == item2.getDurability()) {
//				params.add(ComparisonParameter.DURABILITY);
//			}
//			if(item1.hasItemMeta() == item2.hasItemMeta()) {
//				ItemMeta m1 = item1.getItemMeta();
//				ItemMeta m2 = item2.getItemMeta();
//				if(m1.hasDisplayName() && m2.hasDisplayName()) {
//					if(m1.getDisplayName().equals(m2.getDisplayName())) {
//						params.add(ComparisonParameter.NAME);
//					}
//				}
//				
//				if(equalsIncludeNull(m1.getDisplayName(), m2.getDisplayName())) {
//					
//				}
//				
//				if(m1.hasLore() && m2.hasLore()) {
//					if(m1.getLore().equals(m2.getLore())) {
//						params.add(ComparisonParameter.LORE);
//					}else {
//						antiParams.add(ComparisonParameter.LORE);
//					}
//				}
//				
//				if(m1.getEnchants().equals(m2.getEnchants())) {
//					params.add(ComparisonParameter.ENCHANTS);
//				}else {
//					antiParams.add(ComparisonParameter.ENCHANTS);
//				}
//				
//				if(m1 instanceof SkullMeta && m2 instanceof SkullMeta) {
//					SkullMeta s1 = (SkullMeta) m1;
//					SkullMeta s2 = (SkullMeta) m2;
//					if(s1.hasOwner() == s2.hasOwner()) {
//						if(s1.getOwningPlayer().equals(s2.getOwningPlayer())) {
//							params.add(ComparisonParameter.SKULL_OWNER);
//						}else {
//							antiParams.add(ComparisonParameter.SKULL_OWNER);
//						}
//					}
//				}else if(m1 instanceof LeatherArmorMeta && m2 instanceof LeatherArmorMeta) {
//					LeatherArmorMeta l1 = (LeatherArmorMeta) m1;
//					LeatherArmorMeta l2 = (LeatherArmorMeta) m2;
//					if(l1.getColor().equals(l2.getColor())) {
//						params.add(ComparisonParameter.LEATHER_ARMOR_COLOR);
//					}
//				}
//			}
//			return new ComparisonResult(params);
//		}
	}
	
	private static boolean equalsIncludeNull(Object a, Object b) {
		if(a == b) return true;
		return(a != null && b.equals(a));
	}
	
	public static class ComparisonResult {
		
//		private List<ComparisonParameter> parametersApply;
//		private List<ComparisonParameter> parametersDontApply;
//		
//		public ComparisonResult(List<ComparisonParameter> params) {
//			this.parameters = params;
//		}
//		
//		public List<ComparisonParameter> getParameters() {
//			return parameters;
//		}
//		
//		public boolean matches(ComparisonParameter... params) {
//			return parameters.containsAll(Arrays.asList(params));
//		}
		
	}
	
	public static enum ComparisonParameter {
		
		ALL_APPLICABLE,
		ALL_BASIC,
		NONE_APPLICABLE,
		
		NULL,
		
		TYPE,
		DURABILITY,
		NAME,
		LORE,
		ENCHANTS,
		SKULL_OWNER,
//		SKULL_TEXTURE,
		LEATHER_ARMOR_COLOR
		
	}
	
}
