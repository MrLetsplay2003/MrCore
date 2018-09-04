package me.mrletsplay.mrcore.misc;

import java.util.ArrayList;
import java.util.List;

//TODO
public class StringUtils {

	/**
	 * 
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
	
	public static List<String> wrapString(String s) {
		List<String> lines = new ArrayList<>();
		
		return lines;
	}

}
