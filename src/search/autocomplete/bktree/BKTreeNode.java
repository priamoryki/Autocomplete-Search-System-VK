package search.autocomplete.bktree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Pavel Lymar
 */
public class BKTreeNode {
    private final String word;
    private final Map<Integer, BKTreeNode> children;

    public BKTreeNode(String word) {
        this.word = word;
        this.children = new HashMap<>();
    }

    public void addChild(int key, BKTreeNode node) {
        children.put(key, node);
    }

    public BKTreeNode getChild(int key) {
        return children.get(key);
    }

    public List<Integer> getChildKeysWithinDistance(int minDistance, int maxDistance) {
        return children.keySet().stream().filter(n -> n >= minDistance && n <= maxDistance).collect(Collectors.toList());
    }

    public String getWord() {
        return word;
    }
}
