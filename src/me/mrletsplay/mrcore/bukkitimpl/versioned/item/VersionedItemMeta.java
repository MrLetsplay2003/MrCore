package me.mrletsplay.mrcore.bukkitimpl.versioned.item;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.bukkit.inventory.meta.ItemMeta;

import me.mrletsplay.mrcore.misc.FriendlyException;

public interface VersionedItemMeta {
	
	public default Method getMethod(String name, Class<?>... parameterTypes) {
		return getMethod(getBukkit(), name, parameterTypes);
	}
	
	public default Object invoke(Method method, Object... args) {
		try {
			method.setAccessible(true);
			return method.invoke(getBukkit(), args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new FriendlyException("Failed to invoke bukkit method: " + method);
		}
	}

	public ItemMeta getBukkit();
	
	public static boolean isInstance(ItemMeta meta, String interfaceName) {
		return Arrays.stream(meta.getClass().getInterfaces()).anyMatch(i -> {
			return i.getName().equals(interfaceName);
		});
	}
	
	public static Method getMethod(Object object, String name, Class<?>... parameterTypes) {
		try {
			return object.getClass().getMethod(name, parameterTypes);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new FriendlyException("Failed to get bukkit method: " + name + "(" + Arrays.stream(parameterTypes).map(Class::getName).collect(Collectors.joining(", ")) + ")", e);
		}
	}
	
	public static Object invoke(Object object, Method method, Object... args) {
		try {
			method.setAccessible(true);
			return method.invoke(object, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new FriendlyException("Failed to invoke bukkit method: " + method);
		}
	}
	
}
