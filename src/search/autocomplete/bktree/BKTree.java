package search.autocomplete.bktree;

import search.autocomplete.DataIndexNode;

import java.util.*;

/**
 * @author Pavel Lymar
 */
public class BKTree {
    private BKTreeNode root;

    public BKTree() {
        this.root = null;
    }

    public void add(DataIndexNode node) {
        if (root == null) {
            root = new BKTreeNode(node);
        } else {
            BKTreeNode current = root;
            int distance = calculateDistance(current.getName(), node.getName());
            while (current.getChild(distance) != null) {
                current = current.getChild(distance);
                distance = calculateDistance(current.getName(), node.getName());
                if (distance == 0) {
                    return;
                }
            }
            current.addChild(distance, new BKTreeNode(node));
        }
    }

    public void addAll(List<DataIndexNode> nodes) {
        nodes.forEach(this::add);
    }

    public Map<Integer, TreeSet<BKTreeNode>> search(String word, int distanceThreshold) {
        Map<Integer, TreeSet<BKTreeNode>> results = new HashMap<>();
        search(root, word, distanceThreshold, results);
        return results;
    }

    private void search(BKTreeNode node, String word, int distanceThreshold, Map<Integer, TreeSet<BKTreeNode>> results) {
        if (node == null) {
            return;
        }
        int distance = calculateDistance(node.getName(), word);
        if (distance <= distanceThreshold) {
            if (!results.containsKey(distance)) {
                results.put(distance, new TreeSet<>());
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
