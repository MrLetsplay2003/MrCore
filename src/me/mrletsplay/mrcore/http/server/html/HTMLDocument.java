package me.mrletsplay.mrcore.http.server.html;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringEscapeUtils;

import me.mrletsplay.mrcore.http.server.HttpConnection;
import me.mrletsplay.mrcore.http.server.HttpConnectionInstance;
import me.mrletsplay.mrcore.http.server.HttpConstants;
import me.mrletsplay.mrcore.http.server.HttpServer.ClientHeader;
import me.mrletsplay.mrcore.http.server.HttpStatusCode;
import me.mrletsplay.mrcore.http.server.ParsedURL;
import me.mrletsplay.mrcore.http.server.css.CSSStyleElement;
import me.mrletsplay.mrcore.http.server.css.CSSStylesheet;
import me.mrletsplay.mrcore.http.server.html.HTMLElementTextInput.TextInputChangedEvent;
import me.mrletsplay.mrcore.http.server.js.JSBuiltFunction;
import me.mrletsplay.mrcore.http.server.js.JSBuiltScript;
import me.mrletsplay.mrcore.http.server.js.JSFunctionConsumingCallable;
import me.mrletsplay.mrcore.http.server.js.JSFunctionRaw;
import me.mrletsplay.mrcore.http.server.js.JSScript;

public class HTMLDocument {

	private HttpStatusCode statusCode;
	private List<HTMLElement> elements;
	private JSScript baseScript;
	private CSSStylesheet style;
	private List<Consumer<HttpSiteAccessedEvent>> accessActions;
	private List<Consumer<HttpSiteBuiltEvent>> buildActions;
	private Map<String, String> headerProperties;
	private String redirect;
	private String name;
	private String icon;
	
	public HTMLDocument() {
		this(HttpStatusCode.OKAY_200);
	}
	
	public HTMLDocument(HttpStatusCode statusCode) {
		this.statusCode = statusCode;
		this.elements = new ArrayList<>();
		this.baseScript = new JSScript();
		this.style = new CSSStylesheet();
		this.accessActions = new ArrayList<>();
		this.buildActions = new ArrayList<>();
		this.headerProperties = new HashMap<>();
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
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public String getName() {
		return name;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public CSSStylesheet getStyle() {
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
	
	public HTMLBuiltDocument build(HttpConnectionInstance forInstance, ClientHeader clientHeader, ParsedURL requestedURL, String... params) {
		JSBuiltScript script = baseScript.build();
		CSSStylesheet style = this.style.clone();
		StringBuilder builder = new StringBuilder();
		AtomicInteger uID = new AtomicInteger(0);
		
		StringBuilder body = new StringBuilder();
		HttpSiteAccessedEvent event = new HttpSiteAccessedEvent(forInstance, clientHeader, requestedURL);
		for(HTMLElement el : elements ) {
			appendElement(body, script, style, el, uID, event, params);
		}
		
		builder.append("<head>");
		if(icon != null) builder.append("<link rel=\"icon\" href=\"" + icon + "\">");
		builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"https://fonts.googleapis.com/css?family=Ek+Mukta\">");
		builder.append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css\">");
		if(name != null) builder.append("<title>"+StringEscapeUtils.escapeHtml(name)+"</title>");
		builder.append("<style>");
		builder.append(style.asString(event));
		builder.append("</style>");
		builder.append("</head>");
		
		builder.append("<body>");
		builder.append(body);
		builder.append("<script src=\"https://code.jquery.com/jquery-3.3.1.min.js\"></script>");
		builder.append("<script src=\"https://graphite-official.com/api/mrcore/files/http_client_impl.js\"></script>");
		builder.append("<script>");
		builder.append(script.asString());
		builder.append("</script>");
		builder.append("</body>");
		accessActions.forEach(a -> a.accept(event));
		switch(event.getResult().getType()) {
			case ALLOW:
				HTMLBuiltDocument doc = new HTMLBuiltDocument(this, script, builder.toString());
				HttpSiteBuiltEvent event2 = new HttpSiteBuiltEvent(forInstance, clientHeader, requestedURL, doc);
				buildActions.forEach(a -> a.accept(event2));
				return doc;
			case DENY:
				return forInstance.getConnection().getServer().get403Page().build(forInstance, clientHeader, requestedURL, HttpConstants.HTML_403_REQUESTED_URL, requestedURL.getPath());
			case REDIRECT:
				HTMLBuiltDocument b = forInstance.getConnection().getServer().get403Page().build(forInstance, clientHeader, requestedURL, HttpConstants.HTML_403_REQUESTED_URL, requestedURL.getPath());
				b.setRedirect(event.getResult().getData());
				return b;
		}
		return HttpConstants.HTML_INTERNALS_ERROR_PAGE.build(forInstance, clientHeader, requestedURL);
	}
	
	private StringBuilder appendElement(StringBuilder builder, JSBuiltScript script, CSSStylesheet style, HTMLElement el, AtomicInteger uID, HttpSiteAccessedEvent event, String... params) {
		if(event.getConnectionInstance() != null && el.getCondition() != null && !el.getCondition().apply(event)) return builder;
		HTMLElement.OnHover onHover = el.onHover();
		HTMLElement.OnClicked onClicked = el.onClicked();
		String elID = el.getID() == null ? "el_" + uID.get() : el.getID();
//		if(el.getID() == null) el.setID("el_"+uID.get());
		uID.set(uID.get() + 1);
		if(!el.css().isEmpty()) {
			style.addElement("#" + elID, el.css().clone());
		}
		if(!onHover.css().isEmpty()) {
			CSSStyleElement stl = onHover.css().clone();
			style.addElement("#" + elID + ":hover", stl);
		}
		if(!onClicked.css().isEmpty()) {
			CSSStyleElement stl = onClicked.css().clone();
			style.addElement("#" + elID + ":active", stl);
		}
		if(el.getType() != null) {
			builder.append("<").append(el.getType()).append(el.getClasses().isEmpty()? "" : " class=\"" + el.getClasses().stream().collect(Collectors.joining(" ")) + "\"");
			builder.append(" id=\"" + elID + "\"");
			if(onClicked.getFunction() != null) {
				if(onClicked.getFunction() instanceof JSFunctionRaw) {
					builder.append(" onclick=").append(StringEscapeUtils.escapeHtml(((JSFunctionRaw) onClicked.getFunction()).asString(null)));
				}else {
					JSBuiltFunction f = script.appendFunction(onClicked.getFunction());
					builder.append(" onclick=").append(f.getName()).append("(this)");
				}
			}
			if(onHover.getFunction() != null) {
				if(onHover.getFunction() instanceof JSFunctionRaw) {
					builder.append(" onmouseover=").append(StringEscapeUtils.escapeHtml(((JSFunctionRaw) onHover.getFunction()).asString(null)));
				}else {
					JSBuiltFunction f = script.appendFunction(onHover.getFunction());
					builder.append(" onmouseover=").append(f.getName()).append("(this)");
				}
			}
			if(el instanceof HTMLElementTextInput) {
				HTMLElementTextInput input = (HTMLElementTextInput) el;
				StringBuilder chBuilder = new StringBuilder();
				if(input.onChanged().getFunction() != null) {
					if(input.onChanged().getFunction() instanceof JSFunctionRaw) {
						chBuilder.append(StringEscapeUtils.escapeHtml(((JSFunctionRaw) input.onChanged().getFunction()).asString(null))).append(";");
					}else {
						JSBuiltFunction f = script.appendFunction(input.onChanged().getFunction());
						chBuilder.append(f.getName()).append("(this);");
					}
				}
				if(input.onChanged().getEventHandler() != null) {
					JSFunctionConsumingCallable consC = new JSFunctionConsumingCallable() {
						
						@Override
						public void invoke(JSFunctionConsumingInvokedEvent event) {
							if(!requireParams(event.getParameters(), "input")) return;
							if(!(event.getParameters().get("input") instanceof String)) return;
							String inp = event.getParameters().getString("input");
							input.onChanged().getEventHandler().accept(TextInputChangedEvent.of(event, elID, inp));
						}
						
						@Override
						public String getParamsMethod() {
							return "return {input: self.value}";
						}
					};
					JSBuiltFunction f = script.appendFunction(consC);
					chBuilder.append(f.getName()).append("(this);");
				}
				if(chBuilder.length() != 0) builder.append(" onchange=").append(chBuilder);
				if(input.getPlaceholder() != null) builder.append(" placeholder=\"" + input.getPlaceholder() + "\"");
			}
			builder.append(">");
		}
		String content = el.getContent(event);
		if(content == null) content = "";
		for(int i = 0; i < params.length; i+=2) {
			content = content.replace(params[i], params[i+1]);
		}
		List<Map.Entry<Integer, CharSequence>> strs = new ArrayList<>();
		strs.add(new AbstractMap.SimpleEntry<>(el.getContentSortingIndex(), content));
		el.getAllChildren(event).forEach(c -> strs.add(new AbstractMap.SimpleEntry<>(c.getSortingIndex(), appendElement(new StringBuilder(), script, style, c, uID, event, params))));
		strs.stream().sorted((o1, o2) -> o1.getKey() - o2.getKey()).map(en -> en.getValue()).collect(Collectors.toList()).forEach(builder::append);
		if(el.getType() != null) {
			builder.append("</").append(el.getType()).append(">");
		}
		return builder;
	}
	
	public HTMLDocument clone() {
		return new HTMLDocument(this);
	}
	
	public static class HTMLBuiltDocument {
		
		private HTMLDocument base;
		private JSBuiltScript script;
		private String htmlCode, redirect;
		
		public HTMLBuiltDocument(HTMLDocument base, JSBuiltScript script, String htmlCode) {
			this.base = base;
			this.script = script;
			this.htmlCode = htmlCode;
			this.redirect = base.getRedirect();
		}
		
		public HttpStatusCode getStatusCode() {
			return base.getStatusCode();
		}
		
		public HTMLDocument getBase() {
			return base;
		}
		
		public JSBuiltScript getScript() {
			return script;
		}
		
		public void setRedirect(String redirect) {
			this.redirect = redirect;
		}
		
		public String getRedirect() {
			return redirect;
		}
		
		public String getHTMLCode() {
			return htmlCode;
		}
		
	}
	
	public static class HttpSiteAccessedEvent {
		
		private HttpConnectionInstance connectionInstance;
//		private boolean allowAccess = true;
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
		
//		public void setAllowAccess(boolean allowAccess) {
//			this.allowAccess = allowAccess;
//		}
//		
//		public boolean shouldAllowAccess() {
//			return allowAccess;
//		}
		
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
			
			private static enum Type {
				
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
