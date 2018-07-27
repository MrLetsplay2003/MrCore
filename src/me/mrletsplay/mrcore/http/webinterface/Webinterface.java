package me.mrletsplay.mrcore.http.webinterface;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import me.mrletsplay.mrcore.http.server.HttpClientPoll;
import me.mrletsplay.mrcore.http.server.HttpConnection;
import me.mrletsplay.mrcore.http.server.HttpServer;
import me.mrletsplay.mrcore.http.server.css.CSSStyleElement;
import me.mrletsplay.mrcore.http.server.css.CSSStylesheet;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;

public class Webinterface {

	private static HttpServer server;
	protected static Map<String, WebinterfaceAccount> loggedInSessions;
	protected static Map<String, JSONObject> awaitingConfirmation;
	
	public static void start() {
		loggedInSessions = new HashMap<>();
		awaitingConfirmation = new HashMap<>();
		
		server = new HttpServer(WebinterfaceDataManager.getWebinterfacePort());
		
		server.addPage("/", PageHome.getPage());
		server.addPage("/register", PageRegister.getPage());
		server.addPage("/login", PageLogin.getPage());
		server.addPage("/logout", PageLogout.getPage());
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
	
	public static boolean isLoggedIn(HttpConnection con) {
		return loggedInSessions.containsKey(con.getSessionID());
	}
	
	public static boolean isLoggedIn(String sessionID) {
		return loggedInSessions.containsKey(sessionID);
	}
	
	public static boolean isLoggedIn(WebinterfaceAccount acc) {
		return loggedInSessions.containsValue(acc);
	}
	
	public static WebinterfaceAccount getLoggedInAccount(HttpConnection con) {
		return loggedInSessions.get(con.getSessionID());
	}
	
	public static void stop() {
		if(server != null) server.stop();
	}
	
	protected static HTMLElement addLoading(HTMLDocument doc) {
		HTMLElement loadingDiv = HTMLElement.div().setID("loading-div");
		
		loadingDiv.css()
			.addProperty("position", "absolute")
			.addProperty("top", "0")
			.addProperty("left", "0")
			.addProperty("width", "100%")
			.addProperty("height", "100%")
			.addProperty("z-index", "100")
			.addProperty("background-color", "rgb(50, 50, 50)")
			.addProperty("display", "inline-flex")
			.addProperty("align-items", "center")
			.addProperty("justify-content", "center");
		
		doc.addElement(HTMLElement.script(
					"if(document.readyState === 'complete') {" + 
					"	document.getElementById(\"loading-div\").style.display = \"none\";" + 
					"}else{" + 
					"	var interval = setInterval(function() {" + 
					"		if(document.readyState === 'complete') {" + 
					"			clearInterval(interval);" + 
					"			$(\"#loading-div\").animate({" + 
					"				opacity: 0" + 
					"			}, 300, function() {" +
					"				$(\"#loading-div\").hide();" +
					"			});" + 
					"		}" + 
					"	}, 100);" + 
					"}"
				));
		
		HTMLElement loadingGif = HTMLElement.img("https://upload.wikimedia.org/wikipedia/commons/7/7a/Ajax_loader_metal_512.gif");
		
		loadingGif.css()
			.addProperty("width", "100px");
		
		loadingDiv.addChild(loadingGif);
		
		return loadingDiv;
	}
	
	protected static HTMLElement addHeader(HTMLDocument doc) {
		CSSStylesheet style = doc.getStyle();
		HTMLElement headerDiv = HTMLElement.div().setID("page-header");
		
		headerDiv.css()
			.addProperty("position", "absolute")
			.addProperty("top", "0")
			.addProperty("left", "0")
			.addProperty("width", "100%")
			.addProperty("height", "65px")
			.addProperty("opacity", "1")
			.addProperty("background-color", "hsl(0, 0%, 20%)");
		
		style.get("a:link")
			.addProperty("text-decoration", "none")
			.addProperty("color", "white");
		style.get("a:active")
			.addProperty("text-decoration", "none")
			.addProperty("color", "white");
		style.get("a:visited")
			.addProperty("text-decoration", "none")
			.addProperty("color", "white");
		
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
		
		leftContent.addChild(img);
		
		HTMLElement rightContent = HTMLElement.div().setID("right-content");
		
		rightContent.css()
			.addProperty("position", "absolute")
			.addProperty("right", "0")
			.addProperty("top", "0")
			.addProperty("height", "100%")
			.addProperty("width", "25%")
			.addProperty("text-align", "center");
		
		HTMLElement login = HTMLElement.a("Login", "/login").setID("login").condition(event -> {
			return !isLoggedIn(event.getConnection());
		});
		
		login.css()
			.addProperty("height", "100%")
			.addProperty("display", "inline-flex")
			.addProperty("justify-content", "center")
			.addProperty("align-items", "center")
			.addProperty("font-family", "'Ek Mukta'")
			.addProperty("color", "white")
			.addProperty("font-size", "20px")
			.addProperty("margin-right", "5%");
		
		HTMLElement register = HTMLElement.a("Register", "/register").setID("register").condition(event -> {
			return !isLoggedIn(event.getConnection());
		});
		
		register.css()
			.addProperty("height", "100%")
			.addProperty("display", "inline-flex")
			.addProperty("justify-content", "center")
			.addProperty("align-items", "center")
			.addProperty("font-family", "'Ek Mukta'")
			.addProperty("color", "white")
			.addProperty("font-size", "20px")
			.addProperty("margin-left", "5%");
		
		HTMLElement name = HTMLElement.dynamic(HTMLElement.a(null), event -> {
			return Webinterface
					.getLoggedInAccount(event.getConnection())
					.getMinecraftPlayer()
					.getName();
		}).setID("name").condition(event -> {
			return isLoggedIn(event.getConnection());
		});
		
		name.css()
			.addProperty("height", "100%")
			.addProperty("display", "inline-flex")
			.addProperty("justify-content", "center")
			.addProperty("align-items", "center")
			.addProperty("font-family", "'Ek Mukta'")
			.addProperty("color", "white")
			.addProperty("font-size", "20px")
			.addProperty("margin-left", "5%");
		
		HTMLElement logout = HTMLElement.a("Logout", "/logout").setID("logout").condition(event -> {
			return isLoggedIn(event.getConnection());
		});
		
		logout.css()
			.addProperty("height", "100%")
			.addProperty("display", "inline-flex")
			.addProperty("justify-content", "center")
			.addProperty("align-items", "center")
			.addProperty("font-family", "'Ek Mukta'")
			.addProperty("color", "white")
			.addProperty("font-size", "20px")
			.addProperty("margin-left", "5%");
		
		rightContent.addChild(login);
		rightContent.addChild(register);
		rightContent.addChild(name);
		rightContent.addChild(logout);
		
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
		
		middleContent.addChild(HTMLElement.a("", "/").addChild(HTMLElement.img(Var.HOME_ICON).css(e).onHover(hover).onClicked(click)));
		middleContent.addChild(HTMLElement.a("", "/lol").addChild(HTMLElement.img(Var.DOCUMENTATION_ICON).css(e).onHover(hover).onClicked(click)));
		middleContent.addChild(HTMLElement.a("", "/lol").addChild(HTMLElement.img(Var.USERS_ICON).css(e).onHover(hover).onClicked(click)));
		
		style.get("#middle-content > a > img")
			.addProperty("height", "90%")
			.addProperty("margin-left", "10px");
		
		headerDiv.addChild(leftContent);
		headerDiv.addChild(middleContent);
		headerDiv.addChild(rightContent);
		return headerDiv;
	}
	
}
