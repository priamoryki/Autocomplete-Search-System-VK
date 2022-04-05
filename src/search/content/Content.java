package search.content;

/**
 * Abstract class that stores information based on witch we can sort search results.
 * @see Query
 * @see Movie
 * @see Product
 *
 * @author Pavel Lymar
 */
public abstract class Content implements Comparable<Content> {
    private final String name;
    private double relevance;

    protected Content(String name) {
        this.name = name;
        this.relevance = getDefaultRelevance();
    }

    public String getName() {
        return name;
    }

    public double getRelevance() {
        return relevance;
    }

    public void addRelevance(double relevance) {
        this.relevance += relevance;
    }

    public abstract double getDefaultRelevance();

    @Override
    public int compareTo(Content o) {
        int result = Double.compare(o.relevance, relevance);
        if (result == 0) {
            return name.compareTo(o.name);
        }
        return result;
    }

    @Override
    public String toString() {
        return name;
    }
}
