package me.mrletsplay.mrcore.http.webinterface.plugins;

import me.mrletsplay.mrcore.http.server.HttpClientPoll;
import me.mrletsplay.mrcore.http.server.html.ConsolePollType;
import me.mrletsplay.mrcore.http.server.html.CustomPollHandler;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;
import me.mrletsplay.mrcore.http.server.js.JSFunction;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;

public class PageConsole {

	private static HTMLDocument console;
	
	static {
		console = PagePluginsBase.getBase();
		console.setName("Plugins | Console");
		
		HTMLElement div = console.getElementByID("tab-content");
		
		div.addChild(HTMLElement.h1("@Home"));
		
		HTMLElement cDiv = HTMLElement.textareaReadOnly(20, 150).setID("console-textarea");
		
		div.addChild(cDiv);
		
		console.addCustomPollHandler(CustomPollHandler.create(ConsolePollType.CONSOLE_LINE, JSFunction.of(
				"console.log(\"GOT A POLL!\", poll);" +
				"let textArea = document.getElementById(\"console-textarea\");" +
				"textArea.value = textArea.value + poll.data.line + '\\n';" +
				"textArea.scrollTop = textArea.scrollHeight;"
			)));
		
		console.addAccessAction(event -> {
			event.getConnection().addPoll(HttpClientPoll.custom(ConsolePollType.CONSOLE_LINE, new JSONObject()));
		});
	}
	
	public static HTMLDocument getPage() {
		return console;
	}
	
}
