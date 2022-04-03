package search.content;

/**
 * @author Pavel Lymar
 */
public abstract class ContentWrapper {
    private final String name;

    protected ContentWrapper(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract double getDefaultRelevance();
}
