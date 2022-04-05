package search.autocomplete.result;

import search.content.Content;

/**
 * @author Pavel Lymar
 */
public class ResultWrapper implements Comparable<ResultWrapper> {
    private Content content;
    private double relevance;

    public ResultWrapper(Content content, double relevance) {
        this.content = content;
        this.relevance = relevance;
    }

    @Override
    public int compareTo(ResultWrapper o) {
        int result = Double.compare(relevance, o.relevance);
        if (result == 0) {
            return content.compareTo(o.content);
        }
        return result;
    }
}
