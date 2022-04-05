package search;

import java.util.ArrayDeque;
import java.util.List;

/**
 * Utility class to count distance between words.
 *
 * @author Pavel Lymar
 */
public class StringUtils {
    public static int calculateDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i < s1.length() + 1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j < s2.length() + 1; j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i < s1.length() + 1; i++) {
            for (int j = 1; j < s2.length() + 1; j++) {
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + (s1.charAt(i - 1) != s2.charAt(j - 1) ? 1 : 0)
                );
            }
        }
        return dp[s1.length()][s2.length()];
    }

    public static int getCommonPrefix(String s1, String s2) {
        int i = 0, len = Math.min(s1.length(), s2.length());
        while (i < len && s1.charAt(i) == s2.charAt(i)) {
            i++;
        }
        return i;
    }

    public static ArrayDeque<String> splitIntoWords(String query) {
        return new ArrayDeque<>(List.of(query.split("[\\p{Punct}\\s&&[^'-]]+")));
    }

    public static double getMaxThreshold(String s) {
        return Math.log(Math.max(s.length() - 1, 1));
    }
}
