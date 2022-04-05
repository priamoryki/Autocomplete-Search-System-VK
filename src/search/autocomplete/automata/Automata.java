package search.autocomplete.automata;

/**
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

    public abstract double getRelevance();

    public String getWord() {
        return word;
    }

    public abstract boolean isCorrectWord();

    public abstract boolean isIncorrectWord();

    public abstract Automata step(char symbol);

    public Automata step(String symbols) {
        if (symbols.length() == 0) {
            return this;
        }
        return step(symbols.charAt(0)).step(symbols.substring(1));
    }

    public Automata stepWhileCorrect(String symbols) {
        if (symbols.length() == 0 || isCorrectWord()) {
            return this;
        }
        return step(symbols.charAt(0)).stepWhileCorrect(symbols.substring(1));
    }
}
