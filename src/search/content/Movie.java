package search.content;

/**
 * @author Pavel Lymar
 */
public class Movie extends Content {
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
