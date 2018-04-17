package me.mrletsplay.mrcore.misc;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Probability {
	
	private static final Random DEFAULT_RANDOM = new Random();
	
	public static <T> T choose(List<ProbabilityElement<T>> elements) {
		return choose(elements, DEFAULT_RANDOM);
	}

	public static <T> T choose(List<ProbabilityElement<T>> elements, Random r){
		double sum = 0;
		HashMap<DoubleRange, ProbabilityElement<T>> els = new HashMap<>();
		for(ProbabilityElement<T> el : elements) {
			els.put(new DoubleRange(sum, sum += el.probability), el);
		}
		double val = r.nextDouble() * sum;
		ProbabilityElement<T> el = els.entrySet().stream().filter(e -> e.getKey().contains(val)).map(e -> e.getValue()).findFirst().orElse(null);
		return el!=null?el.element:null;
	}
	
	private static class DoubleRange {
		
		public final double start, end;
		
		public DoubleRange(double start, double end) {
			this.start = start;
			this.end = end;
		}
		
		public boolean contains(double a) {
			return a >= start && a <= end;
		}
		
	}
	
	public static class ProbabilityElement<T> {
		
		public final T element;
		public final double probability;
		
		public ProbabilityElement(T  element, double probability) {
			this.element = element;
			this.probability = probability;
		}
		
	}
	
}
