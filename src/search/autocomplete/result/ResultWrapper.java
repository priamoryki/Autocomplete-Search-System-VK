package search.autocomplete.result;

import search.content.Content;

/**
 * Wrapper to sort {@link search.autocomplete.index.DataIndex#search(String)} results.
 * Have related {@link Content} and relevance value inside.
 *
 * @author Pavel Lymar
 */
public class ResultWrapper implements Comparable<ResultWrapper> {
    private final Content content;
    private final double relevance;

    public ResultWrapper(Content content, double relevance) {
        this.content = content;
        this.relevance = relevance * content.getRelevance();
    }

    public Content getContent() {
        return content;
    }

    @Override
    public int compareTo(ResultWrapper o) {
        int result = Double.compare(o.relevance, relevance);
        if (result == 0) {
            return content.compareTo(o.content);
        }
        return result;
    }
}
