package me.mrletsplay.mrcore.bukkitimpl;

import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mrletsplay.mrcore.misc.JSON.JSONArray;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class ExtraChatComponents {
	
	/**
	 * @author MrLetsplay2003
	 */
	public static class ItemStackComponent {
		
		private ItemStack item;
		
		public ItemStackComponent(ItemStack item) {
			this.item = item;
		}
		
		public ItemStack getItem() {
			return item;
		}
		
		@SuppressWarnings("deprecation")
		public BaseComponent[] toBase() {
			JSONObject o = new JSONObject();
			MaterialLookup lookup = MaterialLookup.byMaterial(item.getType());
			o.set("id", lookup.getMinecraftID());
			o.set("Count", item.getAmount());
			if(item.hasItemMeta()) {
				ItemMeta meta = item.getItemMeta();
				JSONObject tag = new JSONObject();
				if(meta.hasDisplayName() || meta.hasLore()) {
					JSONObject display = new JSONObject();
					if(meta.hasDisplayName()) display.set("Name", meta.getDisplayName());
					if(meta.hasLore()) display.set("Lore", new JSONArray(meta.getLore()));
					tag.set("display", display);
				}
				if(meta.hasEnchants()) {
					JSONArray ench = new JSONArray();
					for(Map.Entry<Enchantment, Integer> en : meta.getEnchants().entrySet()) {
						JSONObject e = new JSONObject();
						e.set("id", en.getKey().getId());
						e.set("lvl", en.getValue());
						ench.add(e);
					}
					tag.set("ench", ench);
				}
				o.set("tag", tag);
			}
			BaseComponent[] b = new ComponentBuilder(o.toString()).create();
			return b;
		}
		
	}
	
}
