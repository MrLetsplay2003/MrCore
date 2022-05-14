package me.mrletsplay.mrcore.bukkitimpl.ui;

import java.util.Arrays;

import me.mrletsplay.mrcore.bukkitimpl.ui.event.UIBuildEvent;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class UILayoutParser {
	
	@SuppressWarnings("deprecation")
	public static BaseComponent[] parseElement(UIBuildEvent event, UIElement element, String instanceID, String elementID) {
		BaseComponent[] tcs = element.getLayout(event);
		if(element.getHoverText() != null)
			Arrays.stream(tcs).forEach(tc -> tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(element.getHoverText()).create())));
		if(element.getAction() != null)
			Arrays.stream(tcs).forEach(tc -> tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mrcoreui "+instanceID+" "+elementID)));
		return tcs;
	}
	
}
