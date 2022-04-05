package search.autocomplete.automata;

/**
 * Abstract Automata for searching in {@link search.autocomplete.index.DataIndex}.
 *
 * @author Pavel Lymar
 */
public abstract class Automata {
    protected final String pattern, word;

    protected Automata(String pattern, String word) {
        this.pattern = pattern;
        this.word = word;
    }

    public String getPattern() {
        return pattern;
    }

    /**
     * Used to create {@link search.autocomplete.result.ResultWrapper}.
     *
     * @return relevance of the word with this pattern.
     */
    public abstract double getRelevance();

    public String getWord() {
        return word;
    }

    public abstract boolean isCorrectWord();

    public abstract boolean isIncorrectWord();

    public abstract Automata step(char symbol);

    /**
     * Making the step in {@code Automata} based on {@link String}.
     * Called when there is need to change {@code Automata} state.
     * @see #step(char)
     *
     * @param symbols corresponds to the {@link String} that we want to make step to.
     * @return new state {@code Automata}.
     */
    public Automata step(String symbols) {
        if (symbols.length() == 0) {
            return this;
        }
        return step(symbols.charAt(0)).step(symbols.substring(1));
    }

    /**
     * Making the step in {@code Automata} based on {@link String} while state isn't incorrect.
     * Called if we need to go down in {@link search.autocomplete.index.DataIndex}
     *
     * @param symbols corresponds to the {@link String} that we want to make step to.
     * @return new state {@code Automata}.
     */
    public Automata stepWhileCorrect(String symbols) {
        if (symbols.length() == 0 || isCorrectWord()) {
            return this;
        }
        return step(symbols.charAt(0)).stepWhileCorrect(symbols.substring(1));
    }
}
