package search.autocomplete.bktree;

import search.autocomplete.DataIndexNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Pavel Lymar
 */
public class BKTreeNode implements Comparable<BKTreeNode> {
    private final DataIndexNode content;
    private final Map<Integer, BKTreeNode> children;

    public BKTreeNode(DataIndexNode content) {
        this.content = content;
        this.children = new HashMap<>();
    }

    public void addChild(int key, BKTreeNode node) {
        children.put(key, node);
    }

    public BKTreeNode getChild(int key) {
        return children.get(key);
    }

    public List<Integer> getChildKeysWithinDistance(int minDistance, int maxDistance) {
        return children.keySet().stream()
                .filter(n -> minDistance <= n && n <= maxDistance).collect(Collectors.toList());
    }

    public String getName() {
        return content.getName();
    }

    public DataIndexNode getContent() {
        return content;
    }

    @Override
    public int compareTo(BKTreeNode o) {
        return content.compareTo(o.content);
    }
}
