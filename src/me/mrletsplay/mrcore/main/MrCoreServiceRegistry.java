package me.mrletsplay.mrcore.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MrCoreServiceRegistry {
	
	private static Map<String, Object> registeredServices = new HashMap<>();
	private static Map<String, List<CompletableFuture<Object>>> waitingRegistrations;
	
	public static void registerService(String name, Object service) {
		registeredServices.put(name, service);
		waitingRegistrations.getOrDefault(name, new ArrayList<>()).forEach(f -> f.complete(service));
	}
	
	public static <T> CompletableFuture<T> awaitServiceRegistration(String name, Class<T> type) {
		CompletableFuture<T> f = new CompletableFuture<>();
		CompletableFuture<Object> f2 = new CompletableFuture<>();
		f2.thenAccept(obj -> f.complete(type.cast(obj)));
		List<CompletableFuture<Object>> l = waitingRegistrations.getOrDefault(name, new ArrayList<>());
		l.add(f2);
		waitingRegistrations.put(name, l);
		return f;
	}
	
	public static Map<String, Object> getRegisteredServices() {
		return registeredServices;
	}

}
