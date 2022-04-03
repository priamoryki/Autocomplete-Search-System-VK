import search.autocomplete.DataIndex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavel Lymar
 */
public class Main {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        List<String> queries = new ArrayList<>();
        // Initializing queries with movie titles from data/movies.html
        try (Scanner fileScanner = new Scanner(Files.newInputStream(Path.of("data/movies.html")))) {
            String prefix = " T:";
            while (fileScanner.hasNext()) {
                int id = 0;
                while (fileScanner.hasNext() && id != prefix.length()) {
                    if (fileScanner.nextChar() != prefix.charAt(id++)) {
                        id = 0;
                    }
                }
                if (fileScanner.hasNext()) {
                    StringBuilder movieTitle = new StringBuilder();
                    do {
                        movieTitle.append(fileScanner.nextChar());
                    } while (movieTitle.charAt(movieTitle.length() - 1) != '<');
                    queries.add(movieTitle.substring(0, movieTitle.length() - 1));
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        DataIndex dataSource = new DataIndex();
        dataSource.addQueries(queries);

        while (true) {
            System.out.print("Enter your query: ");
            String query = scn.nextLine();
            System.out.println("Suggestions to your query: " +
                    String.join(", ", dataSource.getSuggestions(query)));
        }
    }
}
