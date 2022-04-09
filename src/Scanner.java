import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Pavel Lymar
 */
public class Scanner implements AutoCloseable {
    private final BufferedReader reader;
    private final char[] buffer = new char[1024];
    private int id = 0;
    private int bufferSize = 0;

    public Scanner(InputStream stream) {
        this.reader = new BufferedReader(new InputStreamReader(stream));
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateBuffer() {
        try {
            bufferSize = reader.read(buffer);
            id = 0;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean hasNext() {
        if (id >= bufferSize) {
            updateBuffer();
        }
        return bufferSize != -1;
    }

    public Character nextChar() {
        if (hasNext()) {
            return buffer[id++];
        }
        return null;
    }

    public String next() {
        StringBuilder result = new StringBuilder();
        while (hasNext()) {
            Character c = nextChar();
            if (!Character.isWhitespace(c)) {
                result.append(c);
            } else if (result.length() != 0) {
                break;
            }
        }
        return result.toString();
    }

    public String nextLine() {
        StringBuilder result = new StringBuilder();
        while (hasNext()) {
            Character c = nextChar();
            if (c.equals('\n')) {
                break;
            }
            result.append(c);
        }
        return result.toString();
    }

    public Integer nextInt() {
        try {
            return Integer.parseInt(next());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
