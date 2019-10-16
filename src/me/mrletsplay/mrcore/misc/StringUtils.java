package me.mrletsplay.mrcore.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {

	/**
	 * Calculates the similarity between two strings
	 * @param string A string
	 * @param other Another string
	 * @param ignoreCase Whether case sensitivity should be ignored while comparing the strings
	 * @return A value representing the similarity of the two strings, ranging from 0 to 1 (1 meaning the two strings are equal)
	 */
	public static double similarity(String string, String other, boolean ignoreCase) {
		if(ignoreCase ? string.equalsIgnoreCase(other) : string.equals(other)) return 1; // Included for performance
		String longer = string.length() > other.length() ? string : other;
		String shorter = string.length() > other.length() ? other: string;
		if(ignoreCase) {
			longer = longer.toLowerCase();
			shorter = shorter.toLowerCase();
		}
		char[] lChars = longer.toCharArray();
		char[] sChars = shorter.toCharArray();
		double highestSimilarity = 0;
		for(int i = 0; i <= longer.length() - shorter.length(); i++) {
			int matchingChars = 0;
			for(int j = 0; j < sChars.length; j++) {
				if(sChars[j] == lChars[i + j]) {
					matchingChars++;
				}
			}
			double similarity = matchingChars / (double) sChars.length;
			if(similarity > highestSimilarity) highestSimilarity = similarity;
		}
		return highestSimilarity * (shorter.length() / (double) longer.length());
	}
	
	/**
	 * Splits a string into multiple lines by the following rules:
	 * - A string will never be longer than *length*
	 * - The string will be cut on the end of a word
	 * - If that's not possible, the string will be cut exactly at *length*
	 * @param string The string to cut into lines
	 * @param length The maximum length of a line
	 * @return An array of lines cut into properly-sized pieces by above rules
	 */
	public static List<String> wrapString(String string, int length) {
		List<String> words = new ArrayList<>();
		Collections.addAll(words, string.split(" ", -1));
		List<String> lineBuf = new ArrayList<>();
		List<String> buf = new ArrayList<>();
		boolean a = true;
		while(!words.isEmpty() || !buf.isEmpty()) {
			if(a && !words.isEmpty()) buf.add(words.remove(0));
			a = true;
			if(toString(buf).length()>length) {
				String o = buf.remove(buf.size()-1);
				if(buf.isEmpty()) {
					List<String> buf2 = new ArrayList<>();
					while(o.length()>length) {
						String sub = o.substring(0, length);
						o = o.substring(length);
						buf2.add(sub);
					}
					buf.add(o);
					lineBuf.addAll(buf2);
				}else {
					lineBuf.add(toString(buf));
					buf.clear();
					buf.add(o);
					a = false;
				}
			}else if(words.isEmpty()) {
				lineBuf.add(toString(buf));
				buf.clear();
			}
		}
		if(!buf.isEmpty()) lineBuf.add(toString(buf));
		return lineBuf;
	}
	
	private static String toString(List<String> list) {
		return list.stream().collect(Collectors.joining(" "));
	}

}
