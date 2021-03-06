package search.autocomplete.index;

import search.StringUtils;
import search.autocomplete.automata.Automata;
import search.autocomplete.automata.LevenshteinAutomata;
import search.autocomplete.result.ResultWrapper;
import search.content.Content;

import java.util.*;
import java.util.stream.Collectors;


/**
 * This class is simply prefix tree with memory optimization and search based on {@link LevenshteinAutomata}.
 * So depth of recursion would be {@code O(n)} ({@code n} is length of word).
 * <p>
 * Note: This can be easily made as abstraction, but I don't want this for the sake of execution time optimization.
 * </p>
 *
 * @author Pavel Lymar
 * @see DataIndexNode
 * @see LevenshteinAutomata
 */
public class DataIndex {
    private final DataIndexNode root;

    public DataIndex() {
        this.root = new DataIndexNode();
    }

    public void addAll(List<Content> contentList) {
        contentList.forEach(this::add);
    }

    public void add(Content content) {
        // root contains all queries, so if you need to update the relevance of Content you can just change content in
        // the root and remove all such content in node#allContent and then add new content
        if (root.getAllContent().contains(content)) {
            content.addRelevance(root.getAllContent().tailSet(content, true).first().getRelevance());
            root.getAllContent().remove(content);
        }
        root.getAllContent().add(content);

        for (String word : StringUtils.splitIntoWords(content.getName().toLowerCase())) {
            add(root, word, content);
        }
    }

    private void add(DataIndexNode node, String name, Content content) {
        if (name.length() == 0) {
            node.addContent(content);
            return;
        }
        int maxCommonPrefixLength = 0;
        String transition = "";
        for (Map.Entry<String, DataIndexNode> entry : node.getChildren().entrySet()) {
            int prefixLength = StringUtils.getCommonPrefix(entry.getKey(), name);
            if (prefixLength > maxCommonPrefixLength) {
                maxCommonPrefixLength = prefixLength;
                transition = entry.getKey();
            }
        }
        DataIndexNode nextNode = node.getChildren().get(transition);
        if (0 < maxCommonPrefixLength && maxCommonPrefixLength < transition.length()) {
            // Splitting the DataIndexNode
            nextNode = node.addChild(transition.substring(0, maxCommonPrefixLength), new DataIndexNode());
            nextNode.addChild(transition.substring(maxCommonPrefixLength), node.deleteChild(transition));
        }
        if (nextNode == null) {
            nextNode = node.addChild(name, new DataIndexNode());
            maxCommonPrefixLength = name.length();
        }
        add(nextNode, name.substring(maxCommonPrefixLength), content);
    }

    /**
     * @param phrase query to base on.
     * @param limit  limit of {@link Content} that will be returned.
     * @return sorted result of search.
     * @see #search(String)
     */
    public List<Content> search(String phrase, int limit) {
        return search(phrase).stream().limit(limit).collect(Collectors.toList());
    }

    /**
     * Returns sorted {@link List} of {@link Content}.
     * If input has list of words searches for each word and makes the intersection of results.
     *
     * @param phrase query to base on.
     * @return sorted result of search.
     * @see #search(DataIndexNode, String, Automata)
     */
    public List<Content> search(String phrase) {
        ArrayDeque<String> words = StringUtils.splitIntoWords(phrase.toLowerCase());
        TreeSet<ResultWrapper> result = new TreeSet<>();
        if (!words.isEmpty()) {
            String word = words.pop();
            result.addAll(search(root, "", new LevenshteinAutomata(word, StringUtils.getMaxThreshold(word))));
        }
        while (!words.isEmpty()) {
            String word = words.pop();
            result.retainAll(search(root, "", new LevenshteinAutomata(word, StringUtils.getMaxThreshold(word))));
        }
        return result.stream().map(ResultWrapper::getContent).collect(Collectors.toList());
    }

    /**
     * @param node     current node in the tree.
     * @param prefix   prefix name of this node.
     * @param automata automata to base search on.
     * @return allContent of {@code DataIndexNode}'s corresponding to the {@code automata}.
     */
    private TreeSet<ResultWrapper> search(DataIndexNode node, String prefix, Automata automata) {
        if (automata.isCorrectWord()) {
            return node.getAllContentInSubTree(automata);
        }
        if (automata.isIncorrectWord()) {
            return new TreeSet<>();
        }
        TreeSet<ResultWrapper> result = new TreeSet<>();
        for (Map.Entry<String, DataIndexNode> entry : node.getChildren().entrySet()) {
            String transition = entry.getKey();
            DataIndexNode nextNode = entry.getValue();
            result.addAll(search(nextNode, prefix + transition, automata.step(transition)));
        }
        return result;
    }
}
