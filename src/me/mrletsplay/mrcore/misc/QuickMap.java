package me.mrletsplay.mrcore.misc;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class QuickMap<K, V> {

	private List<Map.Entry<K, V>> entries;
	
	public QuickMap() {
		this.entries = new ArrayList<>();
	}
	
	public QuickMap<K, V> put(K key, V value) {
		this.entries.add(new AbstractMap.SimpleEntry<>(key, value));
		return this;
	}
	
	public <T extends Map<K, V>> T make(Supplier<T> supplier) {
		T t = supplier.get();
		this.entries.forEach(en -> t.put(en.getKey(), en.getValue()));
		return t;
	}
	
	public HashMap<K, V> makeHashMap() {
		return make(HashMap::new);
	}

}
