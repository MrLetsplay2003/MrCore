package me.mrletsplay.mrcore.http.server.css;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.server.css.CSSStyleElement.CSSTarget.CSSTargetType;
import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.http.server.html.HTMLElement;
import me.mrletsplay.mrcore.http.server.html.built.HTMLBuiltDocument;

public class CSSStyleElement extends CSSStyleable implements Cloneable {

	public List<CSSTarget> targets;
	
	public CSSStyleElement() {
		this.targets = new ArrayList<>();
	}
	
	public CSSStyleElement(CSSStyleElement from) {
		super(from);
		this.targets = new ArrayList<>(from.targets).stream().map(t -> t.clone()).collect(Collectors.toList());
	}
	
	@Override
	public CSSStyleElement addProperty(String key, Function<HttpSiteAccessedEvent, String> value) {
		super.addProperty(key, value);
		return this;
	}
	
	@Override
	public CSSStyleElement addProperty(String key, String value) {
		super.addProperty(key, value);
		return this;
	}

	@Override
	public CSSStyleElement position(String type, String top, String left, String width, String height) {
		super.position(type, top, left, width, height);
		return this;
	}

	@Override
	public CSSStyleElement dimensions(String width, String height) {
		super.dimensions(width, height);
		return this;
	}
	
	public CSSTarget targetElement(CSSTargetType type, String id) {
		CSSTarget target = new CSSTarget(type, "#" + id);
		targets.add(target);
		return target;
	}
	
	public CSSTarget targetElement(CSSTargetType type, HTMLElement element) {
		CSSTarget target = new CSSTarget(type, element);
		targets.add(target);
		return target;
	}
	
	public CSSTarget targetQuery(CSSTargetType type, String query) {
		CSSTarget target = new CSSTarget(type, query);
		targets.add(target);
		return target;
	}
	
	public String asString(String target, HttpSiteAccessedEvent event, HTMLBuiltDocument context) {
		StringBuilder builder = new StringBuilder();
		builder
			.append(target)
			.append("{")
			.append(getProperties().entrySet().stream().map(en -> en.getKey() + ": " + en.getValue().apply(event) + ";").collect(Collectors.joining()))
			.append("}");
		targets.stream().filter(t -> !t.isEmpty()).forEach(t -> builder.append(t.asString(target, event, context)));
		return builder.toString();
	}
	
	@Override
	public CSSStyleElement clone() {
		return new CSSStyleElement(this);
	}
	
	@Override
	public boolean isEmpty() {
		return super.isEmpty() && targets.stream().allMatch(CSSTarget::isEmpty);
	}
	
	public static class CSSTarget extends CSSStyleable {
		
		private CSSTargetType targetType;
		private String selector;
		private Function<HTMLBuiltDocument, String> getTargetMethod;
		
		private CSSTarget(CSSTargetType targetType, String query) {
			this.targetType = targetType;
			this.getTargetMethod = doc -> query;
		}
		
		private CSSTarget(CSSTargetType targetType, HTMLElement target) {
			this.targetType = targetType;
			this.getTargetMethod = doc -> "#" + doc.getElement(target).getID();
		}
		
		public CSSTarget(CSSTarget from) {
			super(from);
			this.targetType = from.targetType;
			this.selector = from.selector;
			this.getTargetMethod = from.getTargetMethod;
		}
		
		public CSSTarget selector(String selector) {
			this.selector = selector;
			return this;
		}
		
		@Override
		public CSSTarget addProperty(String key, Function<HttpSiteAccessedEvent, String> value) {
			super.addProperty(key, value);
			return this;
		}
		
		@Override
		public CSSTarget addProperty(String key, String value) {
			super.addProperty(key, value);
			return this;
		}

		@Override
		public CSSTarget position(String type, String top, String left, String width, String height) {
			super.position(type, top, left, width, height);
			return this;
		}

		@Override
		public CSSTarget dimensions(String width, String height) {
			super.dimensions(width, height);
			return this;
		}
		
		public CSSTargetType getTargetType() {
			return targetType;
		}
		
		public Function<HTMLBuiltDocument, String> getGetTargetMethod() {
			return getTargetMethod;
		}
		
		public String getTarget(HTMLBuiltDocument document) {
			return getTargetMethod.apply(document);
		}
		
		public String asString(String target, HttpSiteAccessedEvent event, HTMLBuiltDocument context) {
			StringBuilder builder = new StringBuilder();
			builder
				.append(target)
				.append(targetType.css)
				.append(getTarget(context));
			
			if(selector != null) builder.append(":").append(selector);
				
			builder
				.append("{")
				.append(getProperties().entrySet().stream().map(en -> en.getKey() + ": " + en.getValue().apply(event) + ";").collect(Collectors.joining()))
				.append("}");
			return builder.toString();
		}
		
		@Override
		public CSSTarget clone() {
			return new CSSTarget(this);
		}
		
		public static enum CSSTargetType {
			
			CHILD(" "),
			DIRECT_CHILD(" > "),
			ADJACENT_SIBLING(" + "),
			GENERAL_SIBLING(" ~ ");
			
			public final String css;
			
			private CSSTargetType(String css) {
				this.css = css;
			}
			
			public static CSSTargetType getBySelector(String selector) {
				return Arrays.stream(values()).filter(e -> e.css.trim().equals(selector.trim())).findFirst().orElse(null);
			}
			
		}
		
	}
	
}
