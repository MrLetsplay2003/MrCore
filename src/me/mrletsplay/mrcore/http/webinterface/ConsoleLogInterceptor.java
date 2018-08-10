package me.mrletsplay.mrcore.http.webinterface;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConsoleLogInterceptor {

	public static final Interceptor INTERCEPTOR;
	
	private static List<Consumer<InterceptorLogEvent>> listeners;
	
	static {
		listeners = new ArrayList<>();
		INTERCEPTOR = new Interceptor(System.out);
		System.setOut(INTERCEPTOR);
	}
	
	public static void a() {}
	
	public static void addListener(Consumer<InterceptorLogEvent> listener) {
		listeners.add(listener);
	}
	
	public static List<Consumer<InterceptorLogEvent>> getListeners() {
		return listeners;
	}
	
	public static class Interceptor extends PrintStream {

		public Interceptor(OutputStream out) {
			super(out);
		}
		
		@Override
		public void print(String s) {
			super.print(s);
			InterceptorLogEvent e = new InterceptorLogEvent(s);
			listeners.forEach(l -> l.accept(e));
		}
		
	}
	
	public static class InterceptorLogEvent {
		
		private String logLine;
		
		protected InterceptorLogEvent(String logLine) {
			this.logLine = logLine;
		}
		
		public String getLogLine() {
			return logLine;
		}
		
	}

}
