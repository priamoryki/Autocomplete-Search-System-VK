package search.content;

/**
 * @author Pavel Lymar
 */
public class Query extends Content {
    public Query(String name) {
        super(name);
    }

    @Override
    public double getDefaultRelevance() {
        return 1d;
    }
}
