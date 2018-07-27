package me.mrletsplay.mrcore.http.server.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.server.HttpConnection;
import me.mrletsplay.mrcore.http.server.HttpConnectionInstance;
import me.mrletsplay.mrcore.http.server.HttpStatusCode;
import me.mrletsplay.mrcore.http.server.css.CSSStyleElement;
import me.mrletsplay.mrcore.http.server.css.CSSStylesheet;
import me.mrletsplay.mrcore.http.server.js.JSScript;

public class HTMLDocument {

	private HttpStatusCode statusCode;
	private List<HTMLElement> elements;
	private JSScript baseScript;
	private CSSStylesheet style;
	private List<Consumer<HttpSiteAccessedEvent>> buildActions;
	private Map<String, String> headerProperties;
	private String redirect;
	
	public HTMLDocument() {
		this(HttpStatusCode.OKAY_200);
	}
	
	public HTMLDocument(HttpStatusCode statusCode) {
		this.statusCode = statusCode;
		this.elements = new ArrayList<>();
		this.baseScript = new JSScript();
		this.style = new CSSStylesheet();
		this.buildActions = new ArrayList<>();
		this.headerProperties = new HashMap<>();
	}
	
	public void addElement(HTMLElement element) {
		elements.add(element);
	}
	
	public void addBuildAction(Consumer<HttpSiteAccessedEvent> function) {
		buildActions.add(function);
	}
	
	public CSSStylesheet getStyle() {
		return style;
	}
	
	public JSScript getBaseScript() {
		return baseScript;
	}
	
	public List<HTMLElement> getElements() {
		return elements;
	}
	
	public List<Consumer<HttpSiteAccessedEvent>> getBuildActions() {
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
	
	public HTMLDocument appendDocument(HTMLDocument doc) {
		elements.addAll(doc.elements);
		style.appendStylesheet(doc.style);
		baseScript.appendScript(doc.baseScript);
		return this;
	}
	
	private void appendElement(StringBuilder builder, JSScript script, CSSStylesheet style, HTMLElement el, AtomicInteger uID, HttpSiteAccessedEvent event, String... params) {
//		System.out.println(el.getCondition() + "/" + el.getCondition() != null ? el.getCondition().apply(event) : "");
		if(event.getConnectionInstance() != null && el.getCondition() != null && !el.getCondition().apply(event)) return;
		HTMLElement.OnHover onHover = el.onHover();
		HTMLElement.OnClicked onClicked = el.onClicked();
		if(el.getID() == null) el.setID("el_"+uID.get());
		uID.set(uID.get() + 1);
		if(!el.css().isEmpty()) {
			style.addElement("#" + el.getID(), el.css().clone());
		}
		if(!onHover.css().isEmpty()) {
			CSSStyleElement stl = onHover.css().clone();
			style.addElement("#" + el.getID() + ":hover", stl);
		}
		if(!onClicked.css().isEmpty()) {
			CSSStyleElement stl = onClicked.css().clone();
			style.addElement("#" + el.getID() + ":active", stl);
		}
		if(el.getType() != null) {
			builder.append("<").append(el.getType()).append(el.getClasses().isEmpty()? "" : " class=\"" + el.getClasses().stream().collect(Collectors.joining(" ")) + "\"");
			if(el.getID() != null) {
				builder.append(" id=\""+el.getID()+"\"");
			}
			if(onClicked.getFunction() != null) {
				script.appendFunction(onClicked.getFunction());
				builder.append(" onclick=").append(onClicked.getFunction().getName()).append("()");
			}
			if(onHover.getFunction() != null) {
				script.appendFunction(onHover.getFunction());
				builder.append(" onmouseover=").append(onHover.getFunction()).append("()");
			}
			builder.append(">");
		}
		String content = el.getContent(event);
		for(int i = 0; i < params.length; i+=2) {
			content = content.replace(params[i], params[i+1]);
		}
		builder.append(content);
		el.getChildren().forEach(c -> appendElement(builder, script, style, c, uID, event, params));
		if(el.getType() != null) {
			builder.append("</").append(el.getType()).append(">");
		}
	}
	
	public HTMLBuiltDocument build(HttpConnectionInstance forInstance, String... params) {
		JSScript script = baseScript.clone();
		CSSStylesheet style = this.style.clone();
		StringBuilder builder = new StringBuilder();
		AtomicInteger uID = new AtomicInteger(0);
		
		StringBuilder body = new StringBuilder();
		HttpSiteAccessedEvent event = new HttpSiteAccessedEvent(forInstance);
		for(HTMLElement el : elements ) {
			appendElement(body, script, style, el, uID, event, params);
		}
		
		builder.append("<head>");
		builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"https://fonts.googleapis.com/css?family=Ek+Mukta\">");
		builder.append("<style>");
		builder.append(style.asString());
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
		
		buildActions.forEach(a -> a.accept(event));
		return new HTMLBuiltDocument(this, script, builder.toString());
	}
	
	public static class HTMLBuiltDocument {
		
		private HTMLDocument base;
		private JSScript script;
		private String htmlCode;
		
		public HTMLBuiltDocument(HTMLDocument base, JSScript script, String htmlCode) {
			this.base = base;
			this.script = script;
			this.htmlCode = htmlCode;
		}
		
		public HttpStatusCode getStatusCode() {
			return base.getStatusCode();
		}
		
		public HTMLDocument getBase() {
			return base;
		}
		
		public JSScript getScript() {
			return script;
		}
		
		public String getHTMLCode() {
			return htmlCode;
		}
		
	}
	
	public static class HttpSiteAccessedEvent {
		
		private HttpConnectionInstance connectionInstance;
		
		public HttpSiteAccessedEvent(HttpConnectionInstance connectionInstance) {
			this.connectionInstance = connectionInstance;
		}
		
		public HttpConnectionInstance getConnectionInstance() {
			return connectionInstance;
		}
		
		public HttpConnection getConnection() {
			return connectionInstance.getConnection();
		}
		
	}
	
}
