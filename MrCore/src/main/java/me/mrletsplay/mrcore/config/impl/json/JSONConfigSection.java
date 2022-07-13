package me.mrletsplay.mrcore.config.impl.json;

import me.mrletsplay.mrcore.config.CustomConfig;
import me.mrletsplay.mrcore.config.StringifiableConfigSection;
import me.mrletsplay.mrcore.config.impl.AbstractConfigSection;

public class JSONConfigSection extends AbstractConfigSection implements StringifiableConfigSection {

	public JSONConfigSection(CustomConfig config) {
		super(config);
	}

	@Override
	public String saveToString() {
		return toJSON().toFancyString();
	}

}
