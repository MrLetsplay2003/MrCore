package me.mrletsplay.mrcore.http.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.http.css.CSSElement;
import me.mrletsplay.mrcore.http.css.built.CSSBuiltElement;
import me.mrletsplay.mrcore.http.css.built.CSSBuiltProperty;
import me.mrletsplay.mrcore.http.html.built.HTMLBuiltElement;
import me.mrletsplay.mrcore.http.server.HttpDynamicValue;
import me.mrletsplay.mrcore.misc.EnumFlagCompound;

public class HTMLElement {
	
	private HttpDynamicValue<HTMLDocumentBuildEvent, String> type, content;
	private Map<String, HttpDynamicValue<HTMLDocumentBuildEvent, String>> attributes;
	private List<HttpDynamicValue<HTMLDocumentBuildEvent, ? extends HTMLElement>> children;
	private CSSElement style;
	private EnumFlagCompound<HTMLFlag> flags;

	public HTMLElement(HttpDynamicValue<HTMLDocumentBuildEvent, String> type) {
		this.type = type;
		this.content = HttpDynamicValue.empty();
		this.style = new CSSElement(null, null);
		this.attributes = new HashMap<>();
		this.children = new ArrayList<>();
		this.flags = EnumFlagCompound.noneOf(HTMLFlag.class);
	}
	
	public HTMLElement(String type) {
		this(HttpDynamicValue.of(type));
	}
	
	public HttpDynamicValue<HTMLDocumentBuildEvent, String> getType() {
		return type;
	}
	
	public void setContent(HttpDynamicValue<HTMLDocumentBuildEvent, String> content) {
		this.content = content;
	}
	
	public void setContent(String content) {
		setContent(HttpDynamicValue.of(content));
	}
	
	public void setAttribute(String key, HttpDynamicValue<HTMLDocumentBuildEvent, String> value) {
		attributes.put(key, value);
	}
	
	public void setAttribute(String key, String value) {
		setAttribute(key, HttpDynamicValue.of(value));
	}
	
	public Map<String, HttpDynamicValue<HTMLDocumentBuildEvent, String>> getAttributes() {
		return attributes;
	}
	
	public void addChild(HttpDynamicValue<HTMLDocumentBuildEvent, HTMLElement> child) {
		children.add(child);
	}
	
	public void addChild(HTMLElement child) {
		addChild(HttpDynamicValue.of(child));
	}
	
	public List<HttpDynamicValue<HTMLDocumentBuildEvent, ? extends HTMLElement>> getChildren() {
		return children;
	}
	
	public CSSElement getStyle() {
		return style;
	}
	
	public EnumFlagCompound<HTMLFlag> getFlags() {
		return flags;
	}
	
	public HTMLBuiltElement build(HTMLDocumentBuildEvent event) {
		HTMLBuiltElement el = new HTMLBuiltElement(this);
		Optional<String> tp = type.get(event);
		if(!tp.isPresent()) throw new IllegalStateException("Can't not have a type");
		el.setType(tp.get());
		CSSBuiltElement stl = style.build(event);
		if(!stl.getProperties().isEmpty()) el.addAttribute("style", stl.getProperties().stream().map(CSSBuiltProperty::asString).collect(Collectors.joining()));
		for(Map.Entry<String, HttpDynamicValue<HTMLDocumentBuildEvent, String>> en : getAttributes().entrySet()) {
			Optional<String> opt = en.getValue().get(event);
			if(!opt.isPresent() || opt.get() == null) continue;
			el.addAttribute(en.getKey(), opt.get());
		}
		for(HttpDynamicValue<HTMLDocumentBuildEvent, ? extends HTMLElement> en : getChildren()) {
			Optional<? extends HTMLElement> opt = en.get(event);
			if(!opt.isPresent() || opt.get() == null) continue;
			HTMLElement elm = opt.get();
			HTMLBuiltElement bel = elm.build(event);
			if(elm.getFlags().hasFlag(HTMLFlag.ELEMENT_REMOVE_IF_EMPTY) && bel.isEmpty()) continue;
			el.addChild(bel);
		}
		Optional<String> cont = content.get(event);
		if(cont.isPresent()) el.setContent(cont.get());
		return el;
	}
	
}
