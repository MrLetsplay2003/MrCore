package me.mrletsplay.mrcore.bukkitimpl.ui;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import me.mrletsplay.mrcore.bukkitimpl.ui.event.UIBuildEvent;
import me.mrletsplay.mrcore.bukkitimpl.ui.event.UIBuildPageItemEvent;

public class UIMultiPage<T> extends UI {

	private UIBuilderMultiPage<T> builder;
	
	public UIMultiPage(UIBuilderMultiPage<T> builder) {
		super(builder);
		this.builder = builder;
	}
	
	/**
	 * Returns the first page (page 0) of this UI using {@link #getForPlayer(Player, int)}
	 */
	@Override
	public UIMessage getForPlayer(Player p) {
		return getForPlayer(p, 0);
	}
	
	@SuppressWarnings("unchecked")
	public UIMessage getForPlayer(Player p, int page) {
		long lElID = 0;
		UIMessage uiMessage = super.getForPlayer(p);
		String instanceID = uiMessage.getInstance().getID();
		List<T> items = builder.getSupplier().getItems();
		UILayoutMultiPage lmp = (UILayoutMultiPage) builder.getLayout();
		int nPg = items.size()/lmp.getItemAmount();
		if(page >= nPg || page < 0) return null;
		Map<String, Object> props = uiMessage.getInstance().getProperties();
		props.put("page", page);
		Map<String, UIElement> elements = (Map<String, UIElement>) props.get("elements");
		int index = page * lmp.getItemAmount();
		UIBuildEvent e = new UIBuildEvent(uiMessage.getInstance(), p);
		for(int i = 0; i < uiMessage.getMessageElements().size();  i++) {
			Object msgEl = uiMessage.getMessageElements().get(i);
			if(msgEl instanceof UILayoutElement) {
				UILayoutElement lEl = (UILayoutElement) msgEl;
				switch(lEl.getType()) {
					case MULTIPAGE_PAGE_ELEMENT:
						if(index >= items.size()) {
							uiMessage.getMessageElements().remove(i);
							continue;
						}
						UIBuildPageItemEvent<T> pEv = new UIBuildPageItemEvent<>(uiMessage.getInstance(), p, page, items, i, i - index);
						UIElement el = builder.getSupplier().toUIElement(pEv, items.get(index++));
						String elID = "multi_"+(lElID++);
						elements.put(elID, el);
						uiMessage.getMessageElements().set(i, UILayoutParser.parseElement(e, el, instanceID, elID));
						boolean nL = (boolean) lEl.getProperty("newline");
						if(nL) uiMessage.getMessageElements().add(i, new UILayoutElement(UILayoutElementType.NEWLINE));
					default:
						break;
				}
			}
		}
		uiMessage.getInstance().getProperties().put("elements", elements);
		return uiMessage;
	}
	
	@Override
	public UIBuilderMultiPage<T> getBuilder() {
		return builder;
	}
	
}
