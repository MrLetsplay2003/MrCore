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
	
	public static void addItem(Player p, ItemStack item){
		HashMap<Integer,ItemStack> excess = p.getInventory().addItem(item);
		for(Map.Entry<Integer, ItemStack> me : excess.entrySet()){
			p.getWorld().dropItem(p.getLocation(), me.getValue());
		}
	}
	
}
