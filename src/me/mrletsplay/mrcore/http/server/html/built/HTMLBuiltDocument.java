package me.mrletsplay.mrcore.http.server.html.built;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringEscapeUtils;

import me.mrletsplay.mrcore.http.server.HttpConnection;
import me.mrletsplay.mrcore.http.server.HttpStatusCode;
import me.mrletsplay.mrcore.http.server.css.CSSStyleSheet;
import me.mrletsplay.mrcore.http.server.html.BuiltCustomPollHandler;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;
import me.mrletsplay.mrcore.http.server.js.JSFunction;
import me.mrletsplay.mrcore.http.server.js.built.JSBuiltFunction;
import me.mrletsplay.mrcore.http.server.js.built.JSBuiltScript;

public class HTMLBuiltDocument {

	private HTMLDocument base;
	private CSSStyleSheet css;
	private JSBuiltScript script;
	private String redirect;
	private List<HTMLBuiltElement> elements;
	private long lastElementID;
	private HttpSiteAccessedEvent event;
	private String[] params;
	private String pageUID;
	private List<BuiltCustomPollHandler> customPollHandlers;
	
	public HTMLBuiltDocument(HTMLDocument base, HttpSiteAccessedEvent event, String... params) {
		this.base = base;
		this.script = base.getBaseScript().build(this, event);
		this.css = base.getStyle().clone();
		this.redirect = base.getRedirect();
		this.elements = new ArrayList<>();
		this.event = event;
		this.params = params;
		this.pageUID = UUID.randomUUID().toString();
		this.customPollHandlers = base.getCustomPollHandlers().stream().map(p -> p.build(this, event)).collect(Collectors.toCollection(ArrayList::new));
		StringBuilder pollHandlerFct = new StringBuilder();
		for(BuiltCustomPollHandler pH : customPollHandlers) {
			pollHandlerFct
				.append("if(poll.type==\"")
				.append(pH.getBase().getHandlingType().getIdentifier())
				.append("\"){")
				.append(pH.getHandlingFunction().getName())
				.append("(poll);")
				.append("}");
		}
		JSBuiltFunction pollHandler = script.appendFunction(JSFunction.of(pollHandlerFct.toString()));
		
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
	
	public CSSStyleSheet getCSS() {
		return css;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public String getRedirect() {
		return redirect;
	}
	
	public HttpSiteAccessedEvent getEvent() {
		return event;
	}
	
	public HttpConnection getConnection() {
		return event.getConnection();
	}
	
	public String randomElementID() {
		return "el_" + (lastElementID++);
	}
	
	public String getPageUID() {
		return pageUID;
	}
	
	public List<BuiltCustomPollHandler> getCustomPollHandlers() {
		return customPollHandlers;
	}
	
	public List<HTMLBuiltElement> getElements() {
		return elements;
	}
	
	public String[] getParameters() {
		return params;
	}
	
	public HTMLBuiltElement appendElement(HTMLElement element) {
		String id = element.getID();
		if(id == null) id = randomElementID();
//		HTMLBuiltElement e = new HTMLBuiltElement(element, id);
		HTMLBuiltElement e = element.build(null, this, id, event, params);
		elements.add(e);
		return e;
	}
	
	public void onBuildFinished() {
		elements.forEach(HTMLBuiltElement::callOnBuildFinished);
	}
	
	public String asString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("<head>");
		if(base.getIcon() != null) builder.append("<link rel=\"icon\" href=\"" + base.getIcon() + "\">");
		builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"https://fonts.googleapis.com/css?family=Ek+Mukta\">");
		builder.append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css\">");
		if(base.getName() != null) builder.append("<title>"+StringEscapeUtils.escapeHtml(base.getName())+"</title>");
		builder.append("<style>");
		builder.append(css.asString(event, this));
		builder.append("</style>");
		builder.append("</head>");
		
		builder.append("<body>");
		for(HTMLBuiltElement el : elements) {
			builder.append(el.asString());
		}
		builder.append("<script src=\"https://code.jquery.com/jquery-3.3.1.min.js\"></script>");
		builder.append("<script src=\"https://graphite-official.com/api/mrcore/files/http_client_impl.js\"></script>");
		builder.append("<script>");
		builder.append(script.asString());
		builder.append("</script>");
		builder.append("</body>");
		return builder.toString();
	}

	public HTMLBuiltElement getElement(HTMLElement target) {
		return elements.stream().map(e -> e.findElement(target)).filter(e -> e != null).findFirst().orElse(null);
	}

	public HTMLBuiltElement getElementByID(String elementID) {
		return elements.stream().map(e -> e.findElementByID(elementID)).filter(e -> e != null).findFirst().orElse(null);
	}

}
