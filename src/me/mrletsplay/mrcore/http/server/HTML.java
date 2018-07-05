package me.mrletsplay.mrcore.http.server;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.http.server.js.JSFunction;
import me.mrletsplay.mrcore.http.server.js.JSParsedFunction;
import me.mrletsplay.mrcore.http.server.js.JSScript;

public class HTML {

	public static class HTMLDocument {
		
		private List<HTMLElement> elements;
		
		public HTMLDocument() {
			this.elements = new ArrayList<>();
		}
		
		public void addElement(HTMLElement element) {
			elements.add(element);
		}
		
		public List<HTMLElement> getElements() {
			return elements;
		}
		
		public HTMLBuiltDocument build() {
			JSScript script = new JSScript();
			StringBuilder builder = new StringBuilder();
			builder.append("<head>");
			builder.append("</head>");
			
			builder.append("<body>");
			for(HTMLElement el : elements ) {
				appendElement(builder, script, el);
			}
			
			builder.append("<script src=\"https://code.jquery.com/jquery-3.3.1.min.js\"></script>");
			builder.append("<script src=\"https://graphite-official.com/api/mrcore/files/http_client_impl.js\"></script>");
			builder.append("<script>");
			builder.append(script.asString());
			builder.append("</script>");
			builder.append("</body>");
			return new HTMLBuiltDocument(this, script, builder.toString());
		}
		
		private void appendElement(StringBuilder builder, JSScript script, HTMLElement el) {
			if(el.type != null) {
				builder.append("<").append(el.type);
				if(el.onClicked != null) {
					JSParsedFunction parsed = script.appendFunction(el.onClicked);
					builder.append(" onclick=").append(parsed.getName()).append("()");
				}
				builder.append(">");
			}
			builder.append(el.getContent());
			el.children.forEach(c -> appendElement(builder, script, c));
			if(el.type != null) {
				builder.append("</").append(el.type).append(">");
			}
		}
		
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
	
	public static class HTMLHead {
		
		
		
	}
	
	public static class HTMLBody {
		
		
		
	}
	
	public static class HTMLElement {
		
		private String type, content;
		private List<HTMLElement> children;
		private HTMLElement parent;
		
		private JSFunction onClicked;
		
		public HTMLElement(String type, String content) {
			this.type = type;
			this.content = content;
			this.children = new ArrayList<>();
		}
		
		public String getType() {
			return type;
		}
		
		public String getContent() {
			return content;
		}
		
		public void addChild(HTMLElement child) {
			child.parent = this;
			children.add(child);
		}
		
		public void removeChild(HTMLElement child) {
			if(!children.contains(child)) return;
			child.parent = null;
			children.remove(child);
		}
		
		public void setOnClicked(JSFunction onClicked) {
			this.onClicked = onClicked;
		}
		
		public HTMLElement getParent() {
			return parent;
		}
		
		public List<HTMLElement> getChildren() {
			return children;
		}
		
//		public String asString() {
//			if(onClicked != null) context.getScript().appendFunction(onClicked);
//			return 
//					ifNotNull(type, "<" + type + ifNotNull(onClicked, "onclicked="+onClicked.build(context).asString()) + ">") +
//					content +
//					children.stream().map(c -> c.toString()).collect(Collectors.joining()) +
//					ifNotNull(type, "</" + type + ">");
//		}
		
		public static HTMLElement p(String p) {
			return new HTMLElement("p", p);
		}
		
		public static HTMLElement raw(String raw) {
			return new HTMLElement(null, raw);
		}
		
//		private static String ifNotNull(Object a, String b) {
//			return a != null ? b : "";
//		}
		
	}
	
}
