package me.mrletsplay.mrcore.http.webinterface.plugins;

import org.bukkit.Bukkit;

import me.mrletsplay.mrcore.http.server.html.ConsolePollType;
import me.mrletsplay.mrcore.http.server.html.CustomPollHandler;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent.AccessResult;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;
import me.mrletsplay.mrcore.http.server.html.HTMLElementTextInput;
import me.mrletsplay.mrcore.http.server.js.JSFunction;
import me.mrletsplay.mrcore.http.webinterface.Webinterface;
import me.mrletsplay.mrcore.http.webinterface.WebinterfaceAccount;

public class PagePluginsConsole {

	private static HTMLDocument console;
	
	static {
		console = PagePluginsBase.getBase();
		console.setName("Plugins | Console");
		
		HTMLElement div = console.getElementByID("tab-content");
		
		div.addChild(HTMLElement.h1("Console"));
		
		HTMLElement cDiv = HTMLElement.textareaReadOnly(20, 150).setID("console-textarea");
		
		cDiv.css()
			.position("relative", "10px", "10px", "calc(100% - 20px)", "auto")
			.addProperty("outline", "none")
			.addProperty("font-family", "'Ek Mukta'");
		
		div.addChild(cDiv);
		
		HTMLElementTextInput cInp = HTMLElement.inputText("Console");

		cInp.css()
			.position("relative", "20px", "10px", "calc(100% - 20px)", "auto")
			.addProperty("font-size", "20px")
			.addProperty("padding-top", "5px")
			.addProperty("padding-left", "5px")
			.addProperty("padding-bottom", "5px")
			.addProperty("outline", "none")
			.addProperty("font-family", "'Ek Mukta'");
		
		cInp.onChanged()
			.event(event -> {
				System.out.println("REMOTE @ " + event.getConnection().getHostAddress() + " > " + event.getInputText());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), event.getInputText());
				event.setInputText("");
			});
		
		div.addChild(cInp);
		
		console.addCustomPollHandler(CustomPollHandler.create(ConsolePollType.CONSOLE_LINE, JSFunction.of(
				"let textArea = document.getElementById(\"console-textarea\");" +
				"textArea.value = textArea.value + poll.data.line + '\\n';" +
				"textArea.scrollTop = textArea.scrollHeight;"
			)));
		
		console.addAccessAction(event -> {
			if(!Webinterface.isLoggedIn(event.getConnection())) return;
			WebinterfaceAccount acc = Webinterface.getLoggedInAccount(event.getConnection());
			if(!acc.hasPermission("webinterface.console")) {
				event.setResult(AccessResult.redirect("/"));
			}
		});
	}
	
	public static HTMLDocument getPage() {
		return console;
	}
	
}
