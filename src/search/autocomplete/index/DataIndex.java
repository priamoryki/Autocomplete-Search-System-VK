package search.autocomplete.index;

import search.StringUtils;
import search.autocomplete.automata.Automata;
import search.autocomplete.automata.LevenshteinAutomata;
import search.autocomplete.result.ResultWrapper;
import search.content.Content;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author Pavel Lymar
 */
public class DataIndex {
    private final DataIndexNode root;

    public DataIndex() {
        this.root = new DataIndexNode();
    }

    public void add(Content content) {
        for (String word : StringUtils.splitIntoWords(content.getName().toLowerCase())) {
            add(root, word, content);
        }
    }

    public void addAll(List<Content> contentList) {
        contentList.forEach(this::add);
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

    public List<Content> search(String phrase, int limit) {
        return search(phrase).stream().limit(limit).collect(Collectors.toList());
    }

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

    private TreeSet<ResultWrapper> getAllContentInSubTree(DataIndexNode node, Automata automata) {
        TreeSet<ResultWrapper> result = new TreeSet<>();
        for (Content content : node.getAllContent()) {
            result.add(new ResultWrapper(content, automata.getRelevance()));
        }
        for (Map.Entry<String, DataIndexNode> entry : node.getChildren().entrySet()) {
            result.addAll(getAllContentInSubTree(entry.getValue(), automata.step(entry.getKey())));
        }
        return result;
    }

    private TreeSet<ResultWrapper> search(DataIndexNode node, String prefix, Automata automata) {
        if (automata.isCorrectWord()) {
            return getAllContentInSubTree(node, automata);
        }
        if (automata.isIncorrectWord()) {
            return new TreeSet<>();
        }
        TreeSet<ResultWrapper> result = new TreeSet<>();
        for (Map.Entry<String, DataIndexNode> entry : node.getChildren().entrySet()) {
            String transition = entry.getKey();
            DataIndexNode nextNode = entry.getValue();
            result.addAll(search(nextNode, prefix + transition, automata.stepWhileCorrect(transition)));
        }
        return result;
    }
}
