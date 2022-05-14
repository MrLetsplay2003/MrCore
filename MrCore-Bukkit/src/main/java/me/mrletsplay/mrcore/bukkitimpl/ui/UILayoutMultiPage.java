package me.mrletsplay.mrcore.bukkitimpl.ui;

/**
 * This class is used by UIMultiPages to define a basic, flexible layout which will then be parsed into the final chat message<br>
 * This class is only available to the UIBuilderMultiPage/UIMultiPage
 * @author MrLetsplay2003
 */
public class UILayoutMultiPage extends UILayout {
	
	private int itemAmount = 0;
	
	@Override
	public UILayoutMultiPage addText(String text) {
		super.addText(text);
		return this;
	}
	
	@Override
	public UILayoutMultiPage addElement(String element) {
		super.addElement(element);
		return this;
	}
	
	@Override
	public UILayoutMultiPage newLine() {
		super.newLine();
		return this;
	}
	
	/**
	 * Adds multi page elements<br>
	 * These will later be replaced by the multi page element's layouts
	 * @param count The amount of elements to add
	 * @param includeNewLines Whether a {@link UILayoutMultiPage#newLine()} should be added before each element
	 * @return This UILayoutMultiPage instance
	 */
	public UILayoutMultiPage addPageElements(int count, boolean includeNewLines) {
		itemAmount += count;
		for(int i = 0; i < count; i++) {
			getElements().add(new UILayoutElement(UILayoutElementType.MULTIPAGE_PAGE_ELEMENT).setProperty("newline", includeNewLines));
		}
		return this;
	}
	
	public int getItemAmount() {
		return itemAmount;
	}
	
}
