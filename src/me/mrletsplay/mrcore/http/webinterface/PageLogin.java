package me.mrletsplay.mrcore.http.webinterface;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.mrletsplay.mrcore.http.server.HttpClientPoll;
import me.mrletsplay.mrcore.http.server.css.CSSStylesheet;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;
import me.mrletsplay.mrcore.http.server.js.JSFunctionConsumingCallable;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;

public class PageLogin {

	private static HTMLDocument login;
	
	static {
		login = new HTMLDocument();
		login.setName("MrCore | Login");
		login.setIcon("/_internals/img/MrCore.png");
		CSSStylesheet style = login.getStyle();
		
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
		
		HTMLElement div = HTMLElement.div().setID("page-content");
		
//		div.css()
//			.addProperty("position", "absolute")
//			.addProperty("top", "0")
//			.addProperty("left", "0")
//			.addProperty("width", "100%")
//			.addProperty("height", "100%")
//			.addProperty("background-color", "hsl(0, 0%, 20%)")
//			.addProperty("overflow", "hidden");
		
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
		
		confirm.onHover().css()
			.addProperty("cursor", "pointer");
		
		confirm.onClicked()
			.function(new JSFunctionConsumingCallable() {
				
				@SuppressWarnings("deprecation")
				@Override
				public void invoke(JSFunctionConsumingInvokedEvent event) {
					JSONObject params = event.getParameters();
					if(!requireParams(params, "name", "password")) {
						event.getConnection().addPoll(HttpClientPoll.alert("Name and password are required"));
						return;
					}
					OfflinePlayer player = Bukkit.getOfflinePlayer(params.getString("name"));
					WebinterfaceAccount acc = WebinterfaceDataManager.getByMinecraftPlayer(player);
					if(acc == null) {
						event.getConnection().addPoll(HttpClientPoll.alert("Player isn't registered"));
						return;
					}
					if(!acc.matchesPassword(params.getString("password"))) {
						event.getConnection().addPoll(HttpClientPoll.alert("Invalid password"));
						return;
					}
					event.getConnection().addPoll(HttpClientPoll.redirect("/"));
					Webinterface.loggedInSessions.put(event.getConnection().getSessionID(), acc);
				}
				
				@Override
				public String getParamsMethod() {
					return 
							"let pw = $(\"#password-field\").val();" +
							"return {name: $(\"#name-field\").val(), password: pw};";
				}
			});
		
		div.addChild(HTMLElement.img(WebinterfaceUtils.HEAD_IMG));
		div.addChild(name);
		div.addChild(pass);
		div.addChild(confirm);

		login.addElement(WebinterfaceUtils.addLoading(login));
		login.addElement(WebinterfaceUtils.addHeader(login));
		login.addElement(div);
	}
	
	public static HTMLDocument getPage() {
		return login;
	}
	
}
