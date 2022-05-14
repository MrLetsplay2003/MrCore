package me.mrletsplay.mrcore.bukkitimpl.ui;

import java.util.HashMap;

public class UIBuilderMultiPage<T> extends UIBuilder {
	
	private UIItemSupplier<T> supplier;
	
	/**
	 * Constructs a UIBuilderMultiPage instance
	 * @see <a href="https://github.com/MrLetsplay2003/MrCore/wiki/ChatUI">ChatUI wiki</a>
	 */
	public UIBuilderMultiPage() {
		super();
	}
	
	@Override
	public UIBuilderMultiPage<T> addElement(String key, UIElement element) {
		super.addElement(key, element);
		return this;
	}
	
	/**
	 * @deprecated Use {@link #addNextPageElement(String, UIElement)} instead
	 */
	@Deprecated
	public UIBuilderMultiPage<T> addNextPageElement(String key, String layout){
		return addNextPageElement(key, new StaticUIElement(layout));
	}
	
	/**
	 * Adds a "next page" element to the UI<br>
	 * This will have the default action of switching to the page after the current one<br>
	 * If there is no page before the current one, the click will be ignored
	 * @param key The element key
	 * @param layout The layout string to use
	 * @return This UIBuilderMultiPage instance
	 * @see UIBuilder#addElement(String, UIElement)
	 */
	public UIBuilderMultiPage<T> addNextPageElement(String key, UIElement layout){
		return addElement(key, layout.setAction(event -> {
			int page = (int) event.getUIInstance().getProperties().get("page");
			UIMessage msg = ((UIMultiPage<?>) event.getUIInstance().getUI()).getForPlayer(event.getPlayer(), page+1);
			if(msg != null) msg.sendToPlayer(event.getPlayer());
		}));
	}

	/**
	 * @deprecated Use {@link #addPreviousPageElement(String, UIElement)} instead
	 */
	@Deprecated
	public UIBuilderMultiPage<T> addPreviousPageElement(String key, String layout){
		return addPreviousPageElement(key, new StaticUIElement(layout));
	}

	/**
	 * Adds a "next page" element to the UI<br>
	 * This will have the default action of switching to the page before the current one<br>
	 * If there is no page before the current one, the click will be ignored
	 * @param key The element key
	 * @param layout The layout to use
	 * @return This UIBuilderMultiPage instance
	 * @see UIBuilder#addElement(String, UIElement)
	 */
	public UIBuilderMultiPage<T> addPreviousPageElement(String key, UIElement layout){
		return addElement(key, layout.setAction(event -> {
			int page = (int) event.getUIInstance().getProperties().get("page");
			UIMessage msg = ((UIMultiPage<?>) event.getUIInstance().getUI()).getForPlayer(event.getPlayer(), page-1);
			if(msg != null) msg.sendToPlayer(event.getPlayer());
		}));
	}
	
	/**
	 * This method will not work with UIBuilderMultiPage instances and will throw an exception<br>
	 * Use {@link UIBuilderMultiPage#setLayout(UILayoutMultiPage)} instead
	 */
	@Deprecated
	@Override
	public UIBuilder setLayout(UILayout layout) {
		throw new UnsupportedOperationException("Use setLayout(UILayoutMultiPage) instead");
	}

	/**
	 * Sets the UILayoutMultiPage for the UI
	 * @param layout The {@link UILayoutMultiPage} to use
	 * @return This UIBuilderMultiPage instance
	 */
	public UIBuilderMultiPage<T> setLayout(UILayoutMultiPage layout) {
		super.setLayout(layout);
		return this;
	}
	
	/**
	 * Sets the supplier to use for gathering multipage items
	 * @param supplier The supplier to use
	 * @return This UIBuilderMultiPage instance
	 */
	public UIBuilderMultiPage<T> setSupplier(UIItemSupplier<T> supplier) {
		this.supplier = supplier;
		return this;
	}
	
	public UIItemSupplier<T> getSupplier() {
		return supplier;
	}

	@Override
	public UIBuilderMultiPage<T> setProperties(HashMap<String, Object> properties) {
		super.setProperties(properties);
		return this;
	}
	
	@Override
	public UIMultiPage<T> build() {
		return new UIMultiPage<T>(this);
	}
	
}
