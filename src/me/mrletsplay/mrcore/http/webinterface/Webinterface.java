package me.mrletsplay.mrcore.http.webinterface;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.mrletsplay.mrcore.http.server.HttpClientPoll;
import me.mrletsplay.mrcore.http.server.HttpConnection;
import me.mrletsplay.mrcore.http.server.HttpServer;
import me.mrletsplay.mrcore.http.server.css.CSSStyleElement;
import me.mrletsplay.mrcore.http.server.css.CSSStylesheet;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;
import me.mrletsplay.mrcore.http.server.js.JSFunctionConsumingCallable;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class Webinterface {

	private static HttpServer server;
	private static Map<String, WebinterfaceAccount> loggedInSessions;
	private static Map<String, JSONObject> awaitingConfirmation;
	
	public static void start() {
		WebinterfaceDataManager.init();
		
		loggedInSessions = new HashMap<>();
		awaitingConfirmation = new HashMap<>();
		
		server = new HttpServer(WebinterfaceDataManager.getWebinterfacePort());
		
		HTMLDocument home = new HTMLDocument();
		
		home.appendDocument(initHome());
		
		server.addPage("/", home);
		server.addPage("/register", initRegister());
		server.start();
	}
	
	public static HttpServer getServer() {
		return server;
	}
	
	public static WebinterfaceAccount confirmRegistration(Player player, HttpConnection connection) {
		if(!awaitingConfirmation.containsKey(connection.getSessionID())) return null;
		JSONObject d = awaitingConfirmation.remove(connection.getSessionID());
		WebinterfaceAccount acc = WebinterfaceDataManager.createAccount(player.getUniqueId(), d.getString("password"));
		connection.addPoll(HttpClientPoll.redirect("/"));
		loggedInSessions.put(connection.getSessionID(), acc);
		return acc;
	}
	
	public static void stop() {
		server.stop();
	}
	
	private static HTMLElement addHeader(HTMLDocument doc) {
		CSSStylesheet style = doc.getStyle();
		HTMLElement headerDiv = HTMLElement.div().setID("page-header");
		
		headerDiv.css()
			.addProperty("position", "absolute")
			.addProperty("top", "0")
			.addProperty("left", "0")
			.addProperty("width", "100%")
			.addProperty("height", "75px")
			.addProperty("opacity", "0.7")
			.addProperty("background-color", "hsl(0, 0%, 20%)");
		
		HTMLElement leftContent = HTMLElement.div().setID("left-content");
		
		leftContent.css()
			.addProperty("position", "absolute")
			.addProperty("left", "0")
			.addProperty("top", "0")
			.addProperty("height", "100%")
			.addProperty("width", "25%");
		
		style.getElement("left-content:after")
			.addProperty("content", "\"\"")
			.addProperty("background", "white")
			.addProperty("position", "absolute")
			.addProperty("top", "5%")
			.addProperty("right", "0")
			.addProperty("height", "90%")
			.addProperty("width", "2px");
		
		HTMLElement img = HTMLElement.img(Var.ICON).setID("header-img");
		
		img.css()
			.addProperty("position", "absolute")
			.addProperty("top", "2%")
			.addProperty("right", "5%")
			.addProperty("height", "96%");
		
//		HTMLElement nameContainer = HTMLElement.a(Var.NAME, "/").setID("name-container");
//		
//		nameContainer.css()
//			.addProperty("position", "absolute")
//			.addProperty("top", "0")
//			.addProperty("left", "65px")
//			.addProperty("width", "calc(100% - 65px)")
//			.addProperty("height", "100%")
//			.addProperty("display", "inline-flex")
//			.addProperty("justify-content", "center")
//			.addProperty("align-items", "center")
//			.addProperty("font-family", "'Ek Mukta'")
//			.addProperty("color", "white")
//			.addProperty("font-size", "30px")
//			.addProperty("font-weight", "bold");
		
		leftContent.addChild(img);
//		leftContent.addChild(nameContainer);
		
		HTMLElement rightContent = HTMLElement.div().setID("right-content");
		
		rightContent.css()
			.addProperty("position", "absolute")
			.addProperty("right", "0")
			.addProperty("top", "0")
			.addProperty("height", "100%")
			.addProperty("width", "25%")
			.addProperty("text-align", "center");
	
//		style.getElement("right-content:before")
//			.addProperty("content", "\"\"")
//			.addProperty("background", "white")
//			.addProperty("position", "absolute")
//			.addProperty("top", "5%")
//			.addProperty("left", "0")
//			.addProperty("height", "90%")
//			.addProperty("width", "2px");
		
		HTMLElement login = HTMLElement.a("Login").setID("login");
		
		login.css()
			.addProperty("height", "100%")
			.addProperty("display", "inline-flex")
			.addProperty("justify-content", "center")
			.addProperty("align-items", "center")
			.addProperty("font-family", "'Ek Mukta'")
			.addProperty("color", "white")
			.addProperty("font-size", "30px")
			.addProperty("margin-right", "5%");
		
		HTMLElement register = HTMLElement.a("Register", "/register").setID("register");
		
		register.css()
			.addProperty("height", "100%")
			.addProperty("display", "inline-flex")
			.addProperty("justify-content", "center")
			.addProperty("align-items", "center")
			.addProperty("font-family", "'Ek Mukta'")
			.addProperty("color", "white")
			.addProperty("font-size", "30px")
			.addProperty("margin-left", "5%");
		
		rightContent.addChild(login);
		rightContent.addChild(register);
		
		HTMLElement middleContent = HTMLElement.div().setID("middle-content");
		
		middleContent.css()
			.addProperty("position", "absolute")
			.addProperty("left", "25%")
			.addProperty("top", "0")
			.addProperty("width", "50%")
			.addProperty("height", "100%")
			.addProperty("text-align", "center")
			.addProperty("display", "inline-flex")
			.addProperty("justify-content", "center")
			.addProperty("align-items", "center");
		
		HTMLElement.OnHover hover = new HTMLElement.OnHover();
		hover.css()
			.addProperty("border-bottom", "1px solid white")
			.addProperty("cursor", "pointer");
		
		HTMLElement.OnClicked click = new HTMLElement.OnClicked();
		click.css().addProperty("box-shadow", "none");
		
		CSSStyleElement e = new CSSStyleElement()
			.addProperty("width", "40%");
		
		middleContent.addChild(HTMLElement.a("", "/lol").addChild(HTMLElement.img(Var.DEMO_ICON).css(e).onHover(hover).onClicked(click)));
		middleContent.addChild(HTMLElement.a("", "/lol").addChild(HTMLElement.img(Var.DEMO_ICON).css(e).onHover(hover).onClicked(click)));
		middleContent.addChild(HTMLElement.a("", "/lol").addChild(HTMLElement.img(Var.DEMO_ICON).css(e).onHover(hover).onClicked(click)));
		middleContent.addChild(HTMLElement.a("", "/lol").addChild(HTMLElement.img(Var.DEMO_ICON).css(e).onHover(hover).onClicked(click)));
		middleContent.addChild(HTMLElement.a("", "/lol").addChild(HTMLElement.img(Var.DEMO_ICON).css(e).onHover(hover).onClicked(click)));
		
		style.get("#middle-content > a > img")
			.addProperty("height", "90%")
			.addProperty("margin-left", "5px")
			.addProperty("margin-right", "5px");
		
		headerDiv.addChild(leftContent);
		headerDiv.addChild(middleContent);
		headerDiv.addChild(rightContent);
		return headerDiv;
	}
	
	private static HTMLDocument initHome() {
		HTMLDocument home = new HTMLDocument();
		CSSStylesheet style = home.getStyle();
		
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
		
		HTMLElement pageContentDiv = HTMLElement.div().setID("page-content");
		
		pageContentDiv.css()
			.addProperty("position", "absolute")
			.addProperty("top", "65px")
			.addProperty("left", "0")
			.addProperty("width", "100%")
			.addProperty("height", "calc(100% - 65px)")
			.addProperty("background-color", "hsl(0, 0%, 20%)");
		
		div.addChild(addHeader(home));
		pageContentDiv.addChild(HTMLElement.img(Var.HEAD_IMG));
		div.addChild(pageContentDiv);
		
		home.addElement(div);
		return home;
	}
	
	private static HTMLDocument initRegister() {
		HTMLDocument registerPage = new HTMLDocument();
		CSSStylesheet style = registerPage.getStyle();
		
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
					awaitingConfirmation.put(event.getConnection().getSessionID(), params);
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

		div.addChild(addHeader(registerPage));
		div.addChild(HTMLElement.img(Var.HEAD_IMG));
		div.addChild(name);
		div.addChild(pass);
		div.addChild(passConf);
		div.addChild(confirm);
		
		registerPage.addElement(div);
		
		return registerPage;
	}
	
}
