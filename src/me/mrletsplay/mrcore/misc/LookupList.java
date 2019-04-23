package me.mrletsplay.mrcore.misc;

import java.util.List;

public interface LookupList<I, E> extends List<E> {

	public E lookup(I item);
	
}
