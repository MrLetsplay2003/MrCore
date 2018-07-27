package me.mrletsplay.mrcore.http.webinterface;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.mrletsplay.mrcore.http.server.HttpClientPoll;
import me.mrletsplay.mrcore.http.server.css.CSSStylesheet;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;
import me.mrletsplay.mrcore.http.server.js.JSFunctionConsumingCallable;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class PageRegister {

	private static HTMLDocument register;
	
	static {
		register = new HTMLDocument();
		CSSStylesheet style = register.getStyle();
		
		style.getType("body")
			.addProperty("background-color", "#ecf0f5");
		
		style.get("a:link")
			.addProperty("text-decoration", "none")
			.addProperty("color", "white");
		style.get("a:active")
			.addProperty("text-decoration", "none")
			.addProperty("color", "white");
		style.get("a:visited")
			.addProperty("text-decoration", "none")
			.addProperty("color", "white");
		
		HTMLElement div = HTMLElement.div();
		
		div.css()
			.addProperty("position", "absolute")
			.addProperty("top", "0")
			.addProperty("left", "0")
			.addProperty("width", "100%")
			.addProperty("height", "100%")
			.addProperty("background-color", "hsl(0, 0%, 20%)")
			.addProperty("overflow", "hidden");
		
		HTMLElement name = HTMLElement.inputText("Minecraft Name").setID("name-field");

		name.css()
			.addProperty("position", "absolute")
			.addProperty("top", "30%")
			.addProperty("left", "10%")
			.addProperty("width", "30%")
			.addProperty("height", "7%")
			.addProperty("border", "1px solid lightgray")
			.addProperty("color", "black")
			.addProperty("font-family", "'Ek Mukta'")
			.addProperty("background-color", "#fafafa")
			.addProperty("padding-left", "10px")
			.addProperty("font-size", "30px");
		
		HTMLElement pass = HTMLElement.inputPassword("Password").setID("password-field");

		pass.css()
			.addProperty("position", "absolute")
			.addProperty("top", "45%")
			.addProperty("left", "10%")
			.addProperty("width", "30%")
			.addProperty("height", "7%")
			.addProperty("border", "1px solid lightgray")
			.addProperty("color", "black")
			.addProperty("font-family", "'Ek Mukta'")
			.addProperty("background-color", "#fafafa")
			.addProperty("padding-left", "10px")
			.addProperty("font-size", "30px");
		
		HTMLElement passConf = HTMLElement.inputPassword("Confirm Password").setID("password-confirm-field");

		passConf.css()
			.addProperty("position", "absolute")
			.addProperty("top", "55%")
			.addProperty("left", "10%")
			.addProperty("width", "30%")
			.addProperty("height", "7%")
			.addProperty("border", "1px solid lightgray")
			.addProperty("color", "black")
			.addProperty("font-family", "'Ek Mukta'")
			.addProperty("background-color", "#fafafa")
			.addProperty("padding-left", "10px")
			.addProperty("font-size", "30px");
		
		HTMLElement awaitingDiv = HTMLElement.div();
		
		awaitingDiv.css()
			.addProperty("position", "absolute")
			.addProperty("width", "40%")
			.addProperty("height", "50%")
			.addProperty("right", "8%")
			.addProperty("text-align", "left")
			.addProperty("color", "white")
			.addProperty("top", "25%")
			.addProperty("display", "none");
		
		HTMLElement awaitingHead = HTMLElement.a("Awaiting confirmation...");
		
		awaitingHead.css()
			.addProperty("font-size", "30px")
			.addProperty("font-weight", "bold");
		
		HTMLElement awaitingText = HTMLElement.a("</br></br>Go into your minecraft client and click</br>on the message we have send to you.");
		
		awaitingText.css()
			.addProperty("position", "absolute")
			.addProperty("top", "7%")
			.addProperty("left", "20px")
			.addProperty("font-size", "20px");
		
		awaitingDiv.addChild(awaitingHead);
		awaitingDiv.addChild(awaitingText);
		
		HTMLElement confirm = HTMLElement.button("Confirm");
		
		confirm.css()
			.addProperty("position", "absolute")
			.addProperty("top", "65%")
			.addProperty("left", "10%")
			.addProperty("width", "30%")
			.addProperty("height", "7%")
			.addProperty("background-color", "#57bc54")
			.addProperty("border", "none")
			.addProperty("color", "white")
			.addProperty("font-family", "'Ek Mukta'")
			.addProperty("font-size", "25px")
			.addProperty("border-radius", "4px")
			.addProperty("padding-left", "10px")
			.addProperty("box-shadow", "2px 4px 10px rgba(0,0,0,0.1)");
		
		confirm.onClicked()
			.function(new JSFunctionConsumingCallable() {
				
				@Override
				public void invoke(JSFunctionInvokedEvent event, JSONObject params) {
					if(!requireParams(params, "name", "password")) {
						event.getConnection().addPoll(HttpClientPoll.alert("Name and password are required"));
						return;
					}
					Player player = Bukkit.getPlayer(params.getString("name"));
					if(player == null) {
						event.getConnection().addPoll(HttpClientPoll.alert("Player isn't online"));
						return;
					}
					if(WebinterfaceDataManager.isRegistered(player)) {
						event.getConnection().addPoll(HttpClientPoll.alert("Player already registered"));
						return;
					}
					Webinterface.awaitingConfirmation.put(event.getConnection().getSessionID(), params);
					event.getConnection().addPoll(HttpClientPoll.alert("Waiting for registration"));
					player.spigot().sendMessage(new ComponentBuilder("Click here").color(ChatColor.GRAY).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mrcore web register-confirm " + event.getConnection().getSessionID())).create());
				}
				
				@Override
				public String getParamsMethod() {
					return 
							"let pw1 = $(\"#password-field\").val();" +
							"let pw2 = $(\"#password-confirm-field\").val();" +
							"if(pw1 != pw2)  {" +
							"alert(\"Passwords don't match\");" +
							"return null;" +
							"}" +
							"return {name: $(\"#name-field\").val(), password: pw1};";
				}
			});

		div.addChild(Webinterface.addHeader(register));
		div.addChild(HTMLElement.img(Var.HEAD_IMG));
		div.addChild(name);
		div.addChild(pass);
		div.addChild(passConf);
		div.addChild(confirm);
		div.addChild(awaitingDiv);
		
		register.addElement(Webinterface.addLoading(register));
		
		register.addElement(div);
	}
	
	public static HTMLDocument getPage() {
		return register;
	}
	
}
