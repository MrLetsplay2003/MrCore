package me.mrletsplay.mrcore.http.webinterface.doc;

import me.mrletsplay.mrcore.http.server.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;
import me.mrletsplay.mrcore.http.webinterface.WebinterfaceUtils;

public class PageDocs {

	private static HTMLDocument docs;
	
	static {
		docs = new HTMLDocument();
		
		HTMLElement div = HTMLElement.div().setID("page-content");
		
		div.addChild(HTMLElement.h1("Docs"));

		docs.addElement(WebinterfaceUtils.addLoading(docs));
		docs.addElement(WebinterfaceUtils.addHeader(docs));
		docs.addElement(div);
	}
	
	public static HTMLDocument getPage() {
		return docs;
	}
	
}
