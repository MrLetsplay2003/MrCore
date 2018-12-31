package me.mrletsplay.mrcore.misc;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SingleLookupList<I, E> extends AbstractList<E> implements LookupList<I, E> {

	private List<E> elements;
	private Map<I, E> lookupMap;
	private Function<E, I> mappingFunction;
	
	public SingleLookupList(Function<E, I> mappingFunction) {
		this(new ArrayList<>(), mappingFunction);
	}
	
	public SingleLookupList(Collection<? extends E> collection, Function<E, I> mappingFunction) {
		this.elements = new ArrayList<>(collection);
		this.lookupMap = new HashMap<>();
		this.mappingFunction = mappingFunction;
		createMappings();
	}
	
	private void createMappings() {
		for(E e : elements) {
			lookupMap.put(mappingFunction.apply(e), e);
		}
	}
	
	private void removeMapping(E e) {
		lookupMap.remove(mappingFunction.apply(e), e);
	}
	
	private void addMapping(E e) {
		lookupMap.put(mappingFunction.apply(e), e);
	}

	@Override
	public E lookup(I item) {
		return lookupMap.get(item);
	}
	
	@Override
	public E set(int index, E element) {
		E old = elements.set(index, element);
		if(old != null) removeMapping(old);
		if(element != null) addMapping(element);
		return old;
	}
	
	@Override
	public void add(int index, E element) {
		elements.add(index, element);
		if(element != null) addMapping(element);
	}
	
	@Override
	public E remove(int index) {
		E old = elements.remove(index);
		if(old != null) removeMapping(old);
		return old;
	}

	@Override
	public E get(int index) {
		return elements.get(index);
	}

	@Override
	public int size() {
		return elements.size();
	}
	
}
