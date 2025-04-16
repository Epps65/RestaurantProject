package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {
    private final BufferedReader reader;
    private boolean isDone = false;

    public CSVReader(String filePath) throws IOException {
        reader = new BufferedReader(new FileReader(filePath));
        // Optionally skip the header:
        reader.readLine();
    }

    public String[] getNextLine() throws IOException {
        if (isDone) return null;

        String line = reader.readLine();
        if (line == null) {
            isDone = true;
            reader.close();
            return null;
        }
        return line.split(",");
    }
}