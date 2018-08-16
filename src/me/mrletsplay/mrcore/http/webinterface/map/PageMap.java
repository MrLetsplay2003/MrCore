package me.mrletsplay.mrcore.http.webinterface.map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import me.mrletsplay.mrcore.http.server.InternalPage;
import me.mrletsplay.mrcore.http.server.InternalPage.InternalResult;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;
import me.mrletsplay.mrcore.http.server.js.JSScript;
import me.mrletsplay.mrcore.http.webinterface.plugins.PagePluginsBase;

public class PageMap {
	
	private static HTMLDocument page;
	private static final int
			TILE_SIZE = 256;
	
	static {
		page = PagePluginsBase.getBase();
		JSScript script = page.getBaseScript();
		
		script.appendBaseCode(
				"let map = new Map(document.getElementById(\"mc-map\"));" +
				"map.update();"
			);
		
		HTMLElement div = page.getElementByID("tab-content");
		
		HTMLElement mcMap = HTMLElement.div().setID("mc-map");
		
		mcMap.css()
			.position("absolute", "0", "0", "100%", "100%")
			.addProperty("overflow", "hidden");
		
		div.addChild(mcMap);
		
		page.setName("Plugins | Map");
		
		page.addElement(HTMLElement.scriptSrc("https://graphite-official.com/api/mrcore/files/map.js"));
		page.addElement(HTMLElement.scriptSrc("https://graphite-official.com/api/mrcore/files/jquery.mousewheel.min.js"));
		page.addAccessAction(event -> {
			
		});
	}
	
	public static HTMLDocument getPage() {
		return page;
	}
	
	public static InternalPage getInternalPage() {
		return event -> {
			int cX = Integer.valueOf(event.getRequestedURL().getGetParameters().get("x"));
			int cZ = Integer.valueOf(event.getRequestedURL().getGetParameters().get("z"));
			int tileSize = Integer.valueOf(event.getRequestedURL().getGetParameters().get("tileSize"));
			BufferedImage img = new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D g2d = img.createGraphics();
			for(int x = 0; x < tileSize; x++) {
				for(int y = 0; y < tileSize; y++) {
					BufferedImage img2 = MapRenderer.getChunkAt(cX + x, cZ + y);
					if(img != null) {
						g2d.drawImage(img2, x * TILE_SIZE / tileSize, y * TILE_SIZE / tileSize, TILE_SIZE / tileSize + 2, TILE_SIZE / tileSize + 2, null);
					}
				}
			}
			return InternalResult.ofImage(img);
		};
	}
	
}
