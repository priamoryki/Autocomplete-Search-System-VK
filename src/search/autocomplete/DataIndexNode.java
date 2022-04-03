package search.autocomplete;

import search.content.ContentWrapper;
import search.content.Query;

import java.util.HashMap;
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
    private final Map<String, DataIndexNode> nameToNode;

    public DataIndexNode(String name) {
        children = new TreeSet<>();
        nameToNode = new HashMap<>();
        this.content = new Query(name.toLowerCase());
    }

    public DataIndexNode(ContentWrapper content) {
        children = new TreeSet<>();
        nameToNode = new HashMap<>();
        this.content = content;
    }

    public String getName() {
        return content.getName();
    }

    public Map<String, DataIndexNode> getNameToNode() {
        return nameToNode;
    }

    public TreeSet<DataIndexNode> getChildren() {
        return children;
    }

    public DataIndexNode addChild(DataIndexNode node) {
        children.add(node);
        nameToNode.put(node.content.getName(), node);
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
        return Double.compare(o.relevance, relevance);
    }
}
