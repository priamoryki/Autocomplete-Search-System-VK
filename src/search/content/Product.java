package search.content;

/**
 * @author Pavel Lymar
 */
public final class Product extends Content {
    private final double price;

    public Product(String name, double price) {
        super(name);
        this.price = price;
    }

    @Override
    public double getDefaultRelevance() {
        return 0.5d + price > 1000 ? 0.25 : 0.4;
    }
}
