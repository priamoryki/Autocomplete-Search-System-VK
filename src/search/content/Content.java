package search.content;

/**
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

    public int compareTo(Content o) {
        int result = Double.compare(relevance, o.relevance);
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
