/*
 * KMP is a pattern matching algorithm that searches for occurrences of a word within a main text string.
 * Functionality:
 * 1. It preprocesses the pattern to create a longest prefix-suffix (LPS) array.
 * 2. It uses the LPS array to skip unnecessary comparisons in the main text.
 * * Time Complexity: O(n + m), where n is the length of the main text and m is the length of the pattern.
 * * Example:
 *   - Main Text: "ababcabcabababd"
 *  - Pattern: "ababd"
 * *  - Output: The pattern is found at index 10 in the main text.
 * * 
 * 
 */
public class PatternMatcher {
    
    public static boolean kmpSearch(String text, String pattern) {
        int[] lps = computeLPSArray(pattern);
        int i = 0; // index for text
        int j = 0; // index for pattern
        
        while (i < text.length()) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }
            if (j == pattern.length()) {
                System.out.println("Pattern found at index " + (i - j));
                return true; // Pattern found
            } else if (i < text.length() && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return false; // Pattern not found
    }


    private static int[] computeLPSArray(String pat) {
        // This method computes the Longest Prefix Suffix (LPS) array for the given pattern.
        int[] lps = new int[pat.length()];
        int length = 0; // length of the previous longest prefix suffix
        int i = 1;

        // The LPS array is used to skip unnecessary comparisons in the KMP algorithm.
        while (i < pat.length()) {
            if (pat.charAt(i) == pat.charAt(length)) {
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }
}
