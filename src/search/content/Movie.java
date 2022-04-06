package search.content;

/**
 * @author Pavel Lymar
 */
public final class Movie extends Content {
    private final double rating;

    public Movie(String name, double rating) {
        super(name);
        this.rating = rating;
    }

    @Override
    public double getDefaultRelevance() {
        return 1d + rating / 10;
    }
}
