package search.autocomplete;

import search.autocomplete.bktree.BKTree;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class in simply Suffix Tree with checking the most similar word in children using {@link BKTree}
 * while going down.
 *
 * @author Pavel Lymar
 */
public class DataIndex {
    private final DataIndexNode root;

    public DataIndex() {
        root = new DataIndexNode("");
    }

    public void addQuery(Deque<String> query) {
        addQuery(root, query);
    }

    private void addQuery(DataIndexNode node, Deque<String> query) {
        if (query.isEmpty()) {
            node.addRelevance(node.getDefaultRelevance());
            return;
        }
        String nextWord = query.pop().toLowerCase();
        DataIndexNode nextNode = node.getNameToNode().get(nextWord);
        if (nextNode == null) {
            nextNode = node.addChild(new DataIndexNode(nextWord));
        }
        addQuery(nextNode, query);
        node.addRelevance(nextNode.getRelevance());
    }

    public void addQueries(List<String> queries) {
        for (String query : queries) {
            addQuery(splitIntoWords(query));
        }
    }

    public List<String> getSuggestions(String query) {
        DataIndexNode node = root;
        for (String word : splitIntoWords(query)) {
            node = node.getNameToNode().get(word.toLowerCase());
            if (node == null) {
                return new ArrayList<>();
            }
        }
        return node.getChildren().stream().map(DataIndexNode::getName).collect(Collectors.toList());
    }

    private ArrayDeque<String> splitIntoWords(String query) {
        return new ArrayDeque<>(List.of(query.split("[\\p{Punct}\\s]+")));
    }
}
