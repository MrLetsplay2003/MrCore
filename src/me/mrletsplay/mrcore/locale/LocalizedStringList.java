package me.mrletsplay.mrcore.locale;

import java.util.List;
import java.util.stream.Collectors;

public interface LocalizedStringList extends LocalizedObject<List<String>> {
	
	@Override
	default List<String> cast(Object obj) {
		return ((List<?>) obj).stream().map(o -> (String) o).collect(Collectors.toList());
	}
	
}
