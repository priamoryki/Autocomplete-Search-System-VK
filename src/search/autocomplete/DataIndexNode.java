package search.autocomplete;

import search.autocomplete.bktree.BKTree;
import search.autocomplete.bktree.BKTreeNode;
import search.content.ContentWrapper;
import search.content.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * Node in {@link DataIndex}.
 * Stores its children and content({@link ContentWrapper}) inside.
 *
 * @author Pavel Lymar
 */
public class DataIndexNode implements Comparable<DataIndexNode> {
    private final ContentWrapper content;
    private double relevance;
    private final TreeSet<DataIndexNode> children;
    private final BKTree bkTree;

    public DataIndexNode(String name) {
        this.children = new TreeSet<>();
        this.content = new Query(name.toLowerCase());
        this.bkTree = new BKTree();
    }

    public DataIndexNode(ContentWrapper content) {
        this.children = new TreeSet<>();
        this.content = content;
        this.bkTree = new BKTree();
    }

    public String getName() {
        return content.getName();
    }

    public DataIndexNode getChildByName(String name, int distanceThreshold) {
        Map<Integer, TreeSet<BKTreeNode>> searchResult = bkTree.search(name, distanceThreshold);
        Integer distance = searchResult.keySet().stream().min(Integer::compareTo).orElse(null);
        if (distance == null) {
            return null;
        }
        return searchResult.get(distance).last().getContent();
    }

    public List<DataIndexNode> getSortedChildren() {
        return new ArrayList<>(children);
    }

    public DataIndexNode addChild(DataIndexNode node) {
        children.add(node);
        bkTree.add(node);
        return node;
    }

    public double getDefaultRelevance() {
        return content.getDefaultRelevance();
    }

    public Double getRelevance() {
        return relevance;
    }

    public void addRelevance(Double relevance) {
        this.relevance += relevance;
    }

    @Override
    public int compareTo(DataIndexNode o) {
        int result = Double.compare(o.relevance, relevance);
        if (result == 0) {
            return content.compareTo(o.content);
        }
        return result;
    }
}
