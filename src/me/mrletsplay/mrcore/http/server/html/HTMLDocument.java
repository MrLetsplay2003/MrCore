package me.mrletsplay.mrcore.http.server.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.server.HttpConnection;
import me.mrletsplay.mrcore.http.server.HttpConnectionInstance;
import me.mrletsplay.mrcore.http.server.HttpConstants;
import me.mrletsplay.mrcore.http.server.HttpServer.ClientHeader;
import me.mrletsplay.mrcore.http.server.HttpStatusCode;
import me.mrletsplay.mrcore.http.server.ParsedURL;
import me.mrletsplay.mrcore.http.server.css.CSSStyleSheet;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltDocument;
import me.mrletsplay.mrcore.http.server.js.JSScript;

public class HTMLDocument {

	private HttpStatusCode statusCode;
	private List<HTMLElement> elements;
	private JSScript baseScript;
	private CSSStyleSheet style;
	private List<Consumer<HttpSiteAccessedEvent>> accessActions;
	private List<Consumer<HttpSiteBuiltEvent>> buildActions;
	private Map<String, String> headerProperties;
	private String redirect;
	private String name;
	private String icon;
	private List<CustomPollHandler> customPollHandlers;
	
	public HTMLDocument() {
		this(HttpStatusCode.OKAY_200);
	}
	
	public HTMLDocument(HttpStatusCode statusCode) {
		this.statusCode = statusCode;
		this.elements = new ArrayList<>();
		this.baseScript = new JSScript();
		this.style = new CSSStyleSheet();
		this.accessActions = new ArrayList<>();
		this.buildActions = new ArrayList<>();
		this.headerProperties = new HashMap<>();
		this.customPollHandlers = new ArrayList<>();
	}
	
	public HTMLDocument(HTMLDocument doc) {
		this.statusCode = doc.statusCode;
		this.elements = new ArrayList<>(doc.elements).stream().map(HTMLElement::clone).collect(Collectors.toList());
		this.baseScript = doc.baseScript.clone();
		this.style = doc.style.clone();
		this.accessActions = new ArrayList<>(doc.accessActions);
		this.buildActions = new ArrayList<>(doc.buildActions);
		this.headerProperties = new HashMap<>(doc.headerProperties);
		this.redirect = doc.redirect;
		this.name = doc.name;
		this.icon = doc.icon;
		this.customPollHandlers = new ArrayList<>(doc.customPollHandlers);
	}
	
	public void addElement(HTMLElement element) {
		elements.add(element);
	}
	
	public void addAccessAction(Consumer<HttpSiteAccessedEvent> function) {
		accessActions.add(function);
	}
	
	public void addBuildAction(Consumer<HttpSiteBuiltEvent> function) {
		buildActions.add(function);
	}
	
	public void addCustomPollHandler(CustomPollHandler handler) {
		this.customPollHandlers.add(handler);
	}
	
	public List<CustomPollHandler> getCustomPollHandlers() {
		return customPollHandlers;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public String getName() {
		return name;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public CSSStyleSheet getStyle() {
		return style;
	}
	
	public JSScript getBaseScript() {
		return baseScript;
	}
	
	public HTMLElement getElementByID(String id) {
		return elements.stream().map(e -> e.getElementByID(id)).filter(e -> e != null).findFirst().orElse(null);
	}
	
	public List<HTMLElement> getElements() {
		return elements;
	}
	
	public List<Consumer<HttpSiteAccessedEvent>> getAccessActions() {
		return accessActions;
	}
	
	public List<Consumer<HttpSiteBuiltEvent>> getBuildActions() {
		return buildActions;
	}
	
	public HttpStatusCode getStatusCode() {
		return statusCode;
	}
	
	public Map<String, String> getHeaderProperties() {
		return headerProperties;
	}
	
	public String getRedirect() {
		return redirect;
	}
	
	public HTMLDocument addHeaderProperty(String key, String value) {
		headerProperties.put(key, value);
		return this;
	}
	
	public HTMLDocument setRedirect(String url) {
		redirect = url;
		return this;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public HTMLDocument appendDocument(HTMLDocument doc) {
		elements.addAll(doc.elements);
		style.appendStylesheet(doc.style);
		baseScript.appendScript(doc.baseScript);
		return this;
	}
	
	public HTMLBuiltDocument build(HttpConnectionInstance connectionInstance, ClientHeader clientHeader, ParsedURL requestedURL, String... params) {
		HttpSiteAccessedEvent event = new HttpSiteAccessedEvent(connectionInstance, clientHeader, requestedURL);
		accessActions.forEach(a -> a.accept(event));
		switch(event.getResult().getType()) {
			case ALLOW:
				HTMLBuiltDocument bD = new HTMLBuiltDocument(this, event, params);
				elements.stream().filter(el -> el.getCondition() == null || el.getCondition().apply(event)).forEach(bD::appendElement);
				bD.onBuildFinished();
				HttpSiteBuiltEvent event2 = new HttpSiteBuiltEvent(connectionInstance, clientHeader, requestedURL, bD);
				buildActions.forEach(a -> a.accept(event2));
				return bD;
			case DENY:
				return connectionInstance.getConnection().getServer().get403Page().build(connectionInstance, clientHeader, requestedURL, HttpConstants.HTML_403_REQUESTED_URL, requestedURL.getPath());
			case REDIRECT:
				HTMLBuiltDocument b = connectionInstance.getConnection().getServer().get403Page().build(connectionInstance, clientHeader, requestedURL, HttpConstants.HTML_403_REQUESTED_URL, requestedURL.getPath());
				b.setRedirect(event.getResult().getData());
				return b;
		}
		return HttpConstants.HTML_INTERNALS_ERROR_PAGE.build(connectionInstance, clientHeader, requestedURL);
		
	}
	
	public HTMLDocument clone() {
		return new HTMLDocument(this);
	}
	
	public static class HttpSiteAccessedEvent {
		
		private HttpConnectionInstance connectionInstance;
		private ClientHeader clientHeader;
		private ParsedURL requestedURL;
		private AccessResult result;
		
		public HttpSiteAccessedEvent(HttpConnectionInstance connectionInstance, ClientHeader clientHeader, ParsedURL requestedURL) {
			this.connectionInstance = connectionInstance;
			this.clientHeader = clientHeader;
			this.requestedURL = requestedURL;
			this.result = AccessResult.allow();
		}
		
		public HttpConnectionInstance getConnectionInstance() {
			return connectionInstance;
		}
		
		public HttpConnection getConnection() {
			return connectionInstance.getConnection();
		}
		
		public ClientHeader getClientHeader() {
			return clientHeader;
		}
		
		public ParsedURL getRequestedURL() {
			return requestedURL;
		}
		
		public void setResult(AccessResult result) {
			this.result = result;
		}
		
		public AccessResult getResult() {
			return result;
		}
		
		public static class AccessResult {
			
			private Type type;
			private String data;
			
			private AccessResult(Type type, String data) {
				this.type = type;
				this.data = data;
			}
			
			public Type getType() {
				return type;
			}
			
			public String getData() {
				return data;
			}
			
			public static enum Type {
				
				ALLOW,
				DENY,
				REDIRECT;
				
			}
			
			public static AccessResult allow() {
				return new AccessResult(Type.ALLOW, null);
			}
			
			public static AccessResult deny() {
				return new AccessResult(Type.DENY, null);
			}
			
			public static AccessResult redirect(String url) {
				return new AccessResult(Type.REDIRECT, url);
			}
			
		}
		
	}
	
	public static class HttpSiteBuiltEvent {

		private HTMLBuiltDocument builtDocument;
		private HttpConnectionInstance connectionInstance;
		private ClientHeader clientHeader;
		private ParsedURL requestedURL;
		
		public HttpSiteBuiltEvent(HttpConnectionInstance connectionInstance, ClientHeader clientHeader, ParsedURL requestedURL, HTMLBuiltDocument builtDocument) {
			this.connectionInstance = connectionInstance;
			this.clientHeader = clientHeader;
			this.requestedURL = requestedURL;
			this.builtDocument = builtDocument;
		}
		
		public HTMLBuiltDocument getBuiltDocument() {
			return builtDocument;
		}
		
		public HTMLDocument getDocument() {
			return builtDocument.getBase();
		}
		
		public HttpConnectionInstance getConnectionInstance() {
			return connectionInstance;
		}
		
		public HttpConnection getConnection() {
			return connectionInstance.getConnection();
		}
		
		public ClientHeader getClientHeader() {
			return clientHeader;
		}
		
		public ParsedURL getRequestedURL() {
			return requestedURL;
		}
		
	}
	
}
