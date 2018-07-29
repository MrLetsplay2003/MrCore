package me.mrletsplay.mrcore.http.webinterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import me.mrletsplay.mrcore.http.server.HttpClientPoll;
import me.mrletsplay.mrcore.http.server.HttpConnection;
import me.mrletsplay.mrcore.http.server.HttpServer;
import me.mrletsplay.mrcore.http.webinterface.doc.PageDocs;
import me.mrletsplay.mrcore.http.webinterface.plugins.PagePluginsBase;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;

public class Webinterface {

	private static HttpServer server;
	protected static Map<String, WebinterfaceAccount> loggedInSessions;
	protected static Map<String, JSONObject> awaitingConfirmation;
	private static List<PluginWebinterfaceImpl> pluginPages;
	
	public static void start() {
		loggedInSessions = new HashMap<>();
		awaitingConfirmation = new HashMap<>();
		pluginPages = new ArrayList<>();
		
		server = new HttpServer(WebinterfaceDataManager.getWebinterfacePort());
		
		server.addPage("/", PageHome.getPage());
		server.addPage("/register", PageRegister.getPage());
		server.addPage("/login", PageLogin.getPage());
		server.addPage("/logout", PageLogout.getPage());
		server.addPage("/plugins/home", PagePluginsBase.getPage());
		server.addPage("/docs", PageDocs.getPage());
		
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
	
	public static void registerPluginPage(PluginWebinterfaceImpl page) {
		pluginPages.add(page);
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
	
}
