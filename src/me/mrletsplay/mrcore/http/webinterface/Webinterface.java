package me.mrletsplay.mrcore.http.webinterface;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
		server.addPage("/login", initLogin());
		server.addPage("/logout", initLogout());
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
		server.stop();
	}
	
	private static HTMLElement addLoading(HTMLDocument doc) {
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
					"			$(\"#loading-div\").delay(1000).animate({" + 
					"				opacity: 0" + 
					"			}, 1000, function() {" +
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
	
	private static HTMLElement addHeader(HTMLDocument doc) {
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
			.addProperty("background-color", "white")
			.addProperty("overflow", "hidden");
		
		HTMLElement about = HTMLElement.div().setID("about-container");
		
		about.css()
			.addProperty("position", "absolute")
			.addProperty("width", "100%")
			.addProperty("height", "30%")
			.addProperty("left", "0")
			.addProperty("top", "25%")
			.addProperty("color", "white")
			.addProperty("text-align", "center")
			.addProperty("font-size", "20px");
		
		HTMLElement aboutTextHeader = HTMLElement.a("About testname");
		
		aboutTextHeader.css()
			.addProperty("font-size", "35px")
			.addProperty("text-decoration", "underline")
			.addProperty("font-weight", "bold");
		
		HTMLElement aboutText = HTMLElement.a("</br></br>A very nice schlong dong text.</br>"
											+ "Made with <3 by the SchlongDong GmbH.</br>"
											+ "Da is ja da G�nther und der Heinz Dieter</br>"
											+ "Dieser Text muss vier Zeilen haben.");
		
		about.addChild(aboutTextHeader);
		about.addChild(aboutText);
		
		HTMLElement buttonBox = HTMLElement.div().setID("button-box");
		
		buttonBox.css()
			.addProperty("position", "absolute")
			.addProperty("width", "100%")
			.addProperty("height", "5%")
			.addProperty("top", "55%")
			.addProperty("display", "inline-flex")
			.addProperty("justify-content", "center")
			.addProperty("align-items", "center");
		
		HTMLElement tryButton = HTMLElement.button("Try it out");
		
		tryButton.css()
			.addProperty("position", "absolute")
			.addProperty("width", "10%;")
			.addProperty("height", "100%")
			.addProperty("border", "2px solid white")
			.addProperty("border-radius", "3px")
			.addProperty("background-color", "transparent")
			.addProperty("color", "white")
			.addProperty("font-size", "18px")
			.addProperty("opacity", "0.6");
		
		tryButton.onHover().css()
			.addProperty("background-color", "hsl(0, 0%, 20%)")
			.addProperty("cursor", "pointer")
			.addProperty("opacity", "1");
		
		buttonBox.addChild(tryButton);
		
		pageContentDiv.addChild(about);
		pageContentDiv.addChild(buttonBox);
		div.addChild(addHeader(home));
		pageContentDiv.addChild(HTMLElement.img(Var.HEAD_IMG));
		div.addChild(pageContentDiv);
		
		home.addElement(addLoading(home));
		
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
		div.addChild(awaitingDiv);
		
		registerPage.addElement(addLoading(registerPage));
		
		registerPage.addElement(div);
		
		return registerPage;
	}
	
	private static HTMLDocument initLogin() {
		HTMLDocument loginPage = new HTMLDocument();
		CSSStylesheet style = loginPage.getStyle();
		
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
				
				@SuppressWarnings("deprecation")
				@Override
				public void invoke(JSFunctionInvokedEvent event, JSONObject params) {
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
					loggedInSessions.put(event.getConnection().getSessionID(), acc);
				}
				
				@Override
				public String getParamsMethod() {
					return 
							"let pw = $(\"#password-field\").val();" +
							"return {name: $(\"#name-field\").val(), password: pw};";
				}
			});

		div.addChild(addHeader(loginPage));
		div.addChild(HTMLElement.img(Var.HEAD_IMG));
		div.addChild(name);
		div.addChild(pass);
		div.addChild(confirm);
		
		loginPage.addElement(addLoading(loginPage));
		
		loginPage.addElement(div);
		
		return loginPage;
	}
	
	private static HTMLDocument initLogout() {
		HTMLDocument logoutPage = new HTMLDocument();
		
		logoutPage.addBuildAction(event -> {
			loggedInSessions.remove(event.getConnection().getSessionID());
			event.getConnection().addPoll(HttpClientPoll.redirect("/"));
		});
		
		return logoutPage;
	}
	
}
