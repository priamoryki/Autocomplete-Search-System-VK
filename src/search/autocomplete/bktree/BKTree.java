package search.autocomplete.bktree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Pavel Lymar
 */
public class BKTree {
    private BKTreeNode root;

    public BKTree() {
        root = null;
    }

    public void add(String word) {
        if (root == null) {
            root = new BKTreeNode(word);
        } else {
            BKTreeNode current = root;
            int distance = calculateDistance(current.getWord(), word);
            while (current.getChild(distance) != null) {
                current = current.getChild(distance);
                if (calculateDistance(current.getWord(), word) == 0) {
                    return;
                }
            }
            current.addChild(distance, new BKTreeNode(word));
        }
    }

    public void addAll(List<String> words) {
        words.forEach(this::add);
    }

    public Map<Integer, List<BKTreeNode>> search(String word, int distanceThreshold) {
        Map<Integer, List<BKTreeNode>> results = new HashMap<>();
        search(root, word, distanceThreshold, results);
        return results;
    }

    private void search(BKTreeNode node, String word, int distanceThreshold, Map<Integer, List<BKTreeNode>> results) {
        int distance = calculateDistance(node.getWord(), word);
        if (distance <= distanceThreshold) {
            if (!results.containsKey(distance)) {
                results.put(distance, new ArrayList<>());
            }
            results.get(distance).add(node);
        }
        List<Integer> childKeysWithinDistanceThreshold = node.getChildKeysWithinDistance(
                distance - distanceThreshold,
                distance + distanceThreshold
        );
        for (Integer childKey : childKeysWithinDistanceThreshold) {
            search(node.getChild(childKey), word, distanceThreshold, results);
        }
    }

    private int calculateDistance(String s1, String s2) {
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
}
