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
        this.root = new DataIndexNode("");
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
        DataIndexNode nextNode = node.getChildByName(nextWord, 0);
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
        StringBuilder path = new StringBuilder();
        for (String word : splitIntoWords(query)) {
            word = word.toLowerCase();
            node = node.getChildByName(word, distanceThreshold(word));
            if (node == null) {
                return new ArrayList<>();
            }
            path.append(node.getName()).append(" ");
        }
        System.out.println(path);
        return node.getSortedChildren().stream().map(DataIndexNode::getName).collect(Collectors.toList());
    }

    private int distanceThreshold(String name) {
        return (int) Math.round((1 - 0.65) * name.length());
    }

    private ArrayDeque<String> splitIntoWords(String query) {
        return new ArrayDeque<>(List.of(query.split("[.,\\s]+")));
    }
}
