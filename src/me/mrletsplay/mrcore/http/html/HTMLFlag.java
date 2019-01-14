package me.mrletsplay.mrcore.http.html;

import me.mrletsplay.mrcore.misc.FlagCompound.EnumFlag;

public enum HTMLFlag implements EnumFlag<HTMLFlag> {

	ELEMENT_REMOVE_IF_EMPTY;

	@Override
	public HTMLFlag get() {
		return this;
	}
	
}
