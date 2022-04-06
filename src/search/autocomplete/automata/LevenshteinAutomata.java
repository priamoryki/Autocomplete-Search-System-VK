package search.autocomplete.automata;

import java.util.Arrays;

/**
 * Default {@code LevenshteinAutomata} to make steps based on Edit Distance.
 *
 * @author Pavel Lymar
 * @see Automata
 */
public final class LevenshteinAutomata extends Automata {
    private final int size;
    private final double threshold;
    private final int[] distances;

    public LevenshteinAutomata(String pattern, double threshold) {
        super(pattern, "");
        this.size = pattern.length() + 1;
        this.threshold = threshold;
        this.distances = new int[size];
        for (int i = 0; i < size; i++) {
            this.distances[i] = i;
        }
    }

    private LevenshteinAutomata(String pattern, String word, double threshold, int[] vector) {
        super(pattern, word);
        this.size = pattern.length() + 1;
        this.threshold = threshold;
        this.distances = vector;
    }

    private int getDistance() {
        return distances[size - 1];
    }

    @Override
    public double getRelevance() {
        int length = Math.max(pattern.length(), word.length());
        if (length == 0) {
            return 1;
        }
        return 1 - getDistance() / (double) length;
    }

    @Override
    public boolean isCorrectWord() {
        return getDistance() <= threshold;
    }

    @Override
    public boolean isIncorrectWord() {
        return Arrays.stream(distances).min().orElse(Integer.MAX_VALUE) > threshold;
    }

    @Override
    public LevenshteinAutomata step(char symbol) {
        int[] newDistances = new int[size];
        newDistances[0] = distances[0] + 1;
        for (int i = 1; i < size; i++) {
            newDistances[i] = pattern.charAt(i - 1) == symbol ?
                    distances[i - 1] : Math.min(newDistances[i - 1], Math.min(distances[i], distances[i - 1])) + 1;
        }
        return new LevenshteinAutomata(pattern, word + symbol, threshold, newDistances);
    }
}

