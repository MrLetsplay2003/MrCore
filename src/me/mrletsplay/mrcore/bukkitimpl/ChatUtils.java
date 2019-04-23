package me.mrletsplay.mrcore.bukkitimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatUtils {

	public static List<BaseComponent> insertBaseComponent(String txt, List<KeyedBaseComponent> toReplace){
		if(toReplace.isEmpty()) return Arrays.asList(new TextComponent(txt));
		KeyedBaseComponent tR = toReplace.remove(0);
		if(!txt.contains(tR.getKey())) return insertBaseComponent(txt, toReplace);
		List<BaseComponent> cs = new ArrayList<>();
		int ind = txt.indexOf(tR.getKey());
		String before = txt.substring(0, ind);
		String after = txt.substring(ind + tR.getKey().length(), txt.length());
		cs.addAll(insertBaseComponent(before, new ArrayList<>(toReplace)));
		cs.add(tR.getValue());
		cs.addAll(insertBaseComponent(after, new ArrayList<>(toReplace)));
		return cs;
	}
	
//	public static List<BaseComponent> fixColor(List<BaseComponent> components){
//		ChatColor lastColor = null;
//		for(BaseComponent bc : components) {
//			if(bc instanceof TextComponent) {
//				TextComponent tc = (TextComponent) bc;
//				String text = tc.getText();
//				int lastIndex = text.lastIndexOf('§');
//				if(lastIndex != -1) {
//					return null;
//				}
//			}else {
//				lastColor = bc.getColor();
//			}
//		}
//		return null;
//	}
	
	public static class KeyedBaseComponent{
		
		private String key;
		private BaseComponent value;
		
		public KeyedBaseComponent(String k, BaseComponent v) {
			this.key = k;
			this.value = v;
		}
		
		public String getKey() {
			return key;
		}
		
		public BaseComponent getValue() {
			return value;
		}
	}
	
}
