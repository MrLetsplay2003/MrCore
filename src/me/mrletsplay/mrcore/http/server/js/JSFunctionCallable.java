package me.mrletsplay.mrcore.http.server.js;

import me.mrletsplay.mrcore.http.server.HttpServer;

public interface JSFunctionCallable extends JSFunction {

	public void invoke(JSFunctionCalledEvent event);

	@Override
	public default String innerJS(String name) {
		return
				"functionCallback(\""+name+"\")";
	}
	
	public static class JSFunctionCalledEvent {
		
		private HttpServer server;
		
		public JSFunctionCalledEvent(HttpServer server) {
			this.server = server;
		}
		
		public HttpServer getServer() {
			return server;
		}
		
	}
	
}
