package me.mrletsplay.mrcore.bukkitimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.md_5.bungee.api.chat.TextComponent;

public class ChatUtils {
	
	public static List<TextComponent> insertTextComponent(String txt, List<KeyedTextComponent> toReplace){
		if(toReplace.isEmpty()) return Arrays.asList(new TextComponent(txt));
		KeyedTextComponent tR = toReplace.remove(0);
		if(!txt.contains(tR.getKey())) return Arrays.asList(new TextComponent(txt));
		List<TextComponent> cs = new ArrayList<>();
		int ind = txt.indexOf(tR.getKey());
		String before = txt.substring(0, ind);
		String after = txt.substring(ind + tR.getKey().length(), txt.length());
		cs.addAll(insertTextComponent(before, new ArrayList<>(toReplace)));
		cs.add(tR.getValue());
		cs.addAll(insertTextComponent(after, new ArrayList<>(toReplace)));
		return cs;
	}
	
	public static class KeyedTextComponent{
		
		private String key;
		private TextComponent value;
		
		public KeyedTextComponent(String k, TextComponent v) {
			this.key = k;
			this.value = v;
		}
		
		public String getKey() {
			return key;
		}
		
		public TextComponent getValue() {
			return value;
		}
	}
	
}
