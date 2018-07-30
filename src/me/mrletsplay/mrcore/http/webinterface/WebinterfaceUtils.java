package me.mrletsplay.mrcore.http.webinterface;

import me.mrletsplay.mrcore.http.server.css.CSSStyleElement;
import me.mrletsplay.mrcore.http.server.css.CSSStylesheet;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;

public class WebinterfaceUtils {
	
	public static final String NAME = "Aperonex",
							   HEAD_IMG = "/_internals/img/flow.jpg",
							   ICON = "/_internals/img/icon.png",
							   DEMO_ICON = "/_internals/img/demo.png",
							   HOME_ICON = "/_internals/img/home.png",
							   DOCUMENTATION_ICON = "/_internals/img/doc.png",
							   USERS_ICON = "/_internals/img/users.png";

	public static HTMLElement addLoading(HTMLDocument doc) {
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
	
	public static HTMLElement addHeader(HTMLDocument doc) {
		CSSStylesheet style = doc.getStyle();
		HTMLElement headerDiv = HTMLElement.div().setID("page-header");
		
		style.getElement("page-content")
			.addProperty("position", "absolute")
			.addProperty("top", "65px")
			.addProperty("left", "0")
			.addProperty("width", "100%")
			.addProperty("height", "calc(100% - 65px)")
			.addProperty("overflow", "hidden");
		
		headerDiv.css()
			.addProperty("position", "absolute")
			.addProperty("top", "0")
			.addProperty("left", "0")
			.addProperty("width", "100%")
			.addProperty("height", "65px")
			.addProperty("opacity", "1")
			.addProperty("background-color", "hsl(0, 0%, 20%)")
			.addProperty("z-index", "10");
		
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
		
		HTMLElement img = HTMLElement.img(WebinterfaceUtils.ICON).setID("header-img");
		
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
		
		HTMLElement login = HTMLElement.a("Login", "/login")
				.setID("login")
				.condition(event -> !Webinterface.isLoggedIn(event.getConnection()));
		
		login.css()
			.addProperty("height", "100%")
			.addProperty("display", "inline-flex")
			.addProperty("justify-content", "center")
			.addProperty("align-items", "center")
			.addProperty("font-family", "'Ek Mukta'")
			.addProperty("color", "white")
			.addProperty("font-size", "20px")
			.addProperty("margin-right", "5%");
		
		HTMLElement register = HTMLElement.a("Register", "/register")
				.setID("register")
				.condition(event -> !Webinterface.isLoggedIn(event.getConnection()));
		
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
			})
				.setID("name")
				.condition(event -> Webinterface.isLoggedIn(event.getConnection()));
		
		name.css()
			.addProperty("height", "100%")
			.addProperty("display", "inline-flex")
			.addProperty("justify-content", "center")
			.addProperty("align-items", "center")
			.addProperty("font-family", "'Ek Mukta'")
			.addProperty("color", "white")
			.addProperty("font-size", "20px")
			.addProperty("margin-left", "5%");
		
		HTMLElement logout = HTMLElement.a("Logout", "/logout")
				.setID("logout")
				.condition(event ->  Webinterface.isLoggedIn(event.getConnection()));
		
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
		
		middleContent.addChild(HTMLElement.a("", "/").addChild(HTMLElement.img(WebinterfaceUtils.HOME_ICON).css(e).onHover(hover).onClicked(click)));
		middleContent.addChild(HTMLElement.a("", "/docs").addChild(HTMLElement.img(WebinterfaceUtils.DOCUMENTATION_ICON).css(e).onHover(hover).onClicked(click)));
		middleContent.addChild(HTMLElement.a("", "/plugins/home").addChild(HTMLElement.img(WebinterfaceUtils.USERS_ICON).css(e).onHover(hover).onClicked(click)));
		
		style.get("#middle-content > a > img")
			.addProperty("height", "90%")
			.addProperty("margin-left", "10px");
		
		headerDiv.addChild(leftContent);
		headerDiv.addChild(middleContent);
		headerDiv.addChild(rightContent);
		return headerDiv;
	}
	
}
