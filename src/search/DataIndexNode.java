package search;

import search.content.Content;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author Pavel Lymar
 */
public class DataIndexNode {
    private final HashMap<String, DataIndexNode> children;
    private final TreeSet<Content> allContent;

    public DataIndexNode() {
        this.children = new HashMap<>();
        this.allContent = new TreeSet<>();
    }

    public DataIndexNode addChild(String transition, DataIndexNode node) {
        children.put(transition, node);
        return node;
    }

    public DataIndexNode deleteChild(String transition) {
        return children.remove(transition);
    }

    public void addContent(Content content) {
        allContent.add(content);
    }

    public TreeSet<Content> getAllContent() {
        return allContent;
    }

    public Map<String, DataIndexNode> getChildren() {
        return children;
    }
}
