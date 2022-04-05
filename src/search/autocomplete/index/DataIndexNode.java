package search.autocomplete.index;

import search.autocomplete.automata.Automata;
import search.autocomplete.result.ResultWrapper;
import search.content.Content;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Node in {@link DataIndex} that stores corresponding {@link Content} inside.
 *
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

    /**
     * @see Automata#getRelevance
     * @param automata automata to base search sort on.
     * @return content in all subtree that is stored in nodes.
     */
    public TreeSet<ResultWrapper> getAllContentInSubTree(Automata automata) {
        TreeSet<ResultWrapper> result = new TreeSet<>();
        for (Content content : getAllContent()) {
            result.add(new ResultWrapper(content, automata.getRelevance()));
        }
        for (Map.Entry<String, DataIndexNode> entry : getChildren().entrySet()) {
            result.addAll(entry.getValue().getAllContentInSubTree(automata.step(entry.getKey())));
        }
        return result;
    }
}
