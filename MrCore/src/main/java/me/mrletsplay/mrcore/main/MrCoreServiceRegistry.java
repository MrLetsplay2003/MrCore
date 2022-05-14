package me.mrletsplay.mrcore.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MrCoreServiceRegistry {
	
	private static Map<String, Object> registeredServices = new HashMap<>();
	private static Map<String, List<CompletableFuture<Object>>> waitingRegistrations = new HashMap<>();
	
	public static void registerService(String name, Object service) {
		registeredServices.put(name.toLowerCase(), service);
		waitingRegistrations.getOrDefault(name.toLowerCase(), new ArrayList<>()).forEach(f -> f.complete(service));
	}
	
	public static boolean isServiceRegistered(String name) {
		return registeredServices.containsKey(name.toLowerCase());
	}
	
	public static Object getRegisteredService(String name) {
		return registeredServices.get(name.toLowerCase());
	}
	
	public static <T> T getRegisteredService(String name, Class<T> type) {
		return type.cast(getRegisteredService(name));
	}
	
	public static CompletableFuture<Object> awaitServiceRegistration(String name) {
		if(isServiceRegistered(name)) {
			CompletableFuture<Object> f = new CompletableFuture<>();
			f.complete(getRegisteredService(name));
			return f;
		}
		CompletableFuture<Object> f = new CompletableFuture<>();
		List<CompletableFuture<Object>> l = waitingRegistrations.getOrDefault(name.toLowerCase(), new ArrayList<>());
		l.add(f);
		waitingRegistrations.put(name.toLowerCase(), l);
		return f;
	}
	
	public static <T> CompletableFuture<T> awaitServiceRegistration(String name, Class<T> type) {
		if(isServiceRegistered(name)) {
			CompletableFuture<T> f = new CompletableFuture<>();
			f.complete(getRegisteredService(name, type));
			return f;
		}
		CompletableFuture<T> f = new CompletableFuture<>();
		CompletableFuture<Object> f2 = awaitServiceRegistration(name);
		f2.thenAccept(obj -> f.complete(type.cast(obj)));
		return f;
	}
	
	public static Map<String, Object> getRegisteredServices() {
		return registeredServices;
	}

}
