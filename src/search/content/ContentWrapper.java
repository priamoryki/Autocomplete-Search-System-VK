package search.content;

/**
 * @author Pavel Lymar
 */
public abstract class ContentWrapper implements Comparable<ContentWrapper> {
    private final String name;

    protected ContentWrapper(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract double getDefaultRelevance();

    public int compareTo(ContentWrapper o) {
        return name.compareTo(o.name);
    }
}
