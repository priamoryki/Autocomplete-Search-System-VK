package search.autocomplete.automata;

import java.util.Arrays;

/**
 * @author Pavel Lymar
 */
public final class LevenshteinAutomata extends Automata {
    private final int size;
    private final double threshold;
    private final int[] vector;

    public LevenshteinAutomata(String pattern, double threshold) {
        super(pattern, "");
        this.size = pattern.length() + 1;
        this.threshold = threshold;
        this.vector = new int[size];
        for (int i = 0; i < size; i++) {
            this.vector[i] = i;
        }
    }

    private LevenshteinAutomata(String pattern, String word, double threshold, int[] vector) {
        super(pattern, word);
        this.size = pattern.length() + 1;
        this.threshold = threshold;
        this.vector = vector;
    }

    private int getDistance() {
        return vector[size - 1];
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
        return Arrays.stream(vector).min().orElse(Integer.MAX_VALUE) > threshold;
    }

    @Override
    public LevenshteinAutomata step(char symbol) {
        int[] newVector = new int[size];
        newVector[0] = vector[0] + 1;
        for (int i = 1; i < size; i++) {
            if (pattern.charAt(i - 1) == symbol) {
                newVector[i] = vector[i - 1];
            } else {
                newVector[i] = Math.min(newVector[i - 1], Math.min(vector[i], vector[i - 1])) + 1;
            }
        }
        return new LevenshteinAutomata(pattern, word + symbol, threshold, newVector);
    }
}

