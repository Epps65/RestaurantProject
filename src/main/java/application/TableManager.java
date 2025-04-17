package application;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

class TableManager {
    private  Map<String, boolean[]> tables;
    String fileName;

    public TableManager(String fileName) {
        tables = new HashMap<>();
        this.fileName = fileName;
    }

    public void loadTables() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        reader.readLine(); // skip header

        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(",");
            String key = tokens[0] + tokens[1]; // e.g., "A1"
            boolean[] flags = new boolean[5];
            for (int i = 0; i < 5; i++) {
                flags[i] = Boolean.parseBoolean(tokens[i + 2]); // exists, hasN, hasE, hasS, hasW
            }
            tables.put(key, flags);
        }
        reader.close();
    }

    public void saveTables() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        writer.write("column,row,exists,hasN,hasE,hasS,hasW");
        writer.newLine();

        for (Map.Entry<String, boolean[]> entry : tables.entrySet()) {
            String key = entry.getKey();
            char col = key.charAt(0);
            String row = key.substring(1);
            boolean[] flags = entry.getValue();

            // Build CSV row
            StringBuilder sb = new StringBuilder();
            sb.append(col).append(',').append(row);
            for (boolean flag : flags) {
                sb.append(',').append(flag);
            }

            writer.write(sb.toString());
            writer.newLine();
        }

        writer.close();
    }

    public boolean[] getStatus(String key) {
        return tables.get(key);
    }

    public void toggleStatus(String key, int x) throws IOException {
        //0=table, 1=N, 2=E, 3=S, 4=W
        boolean[] flags = tables.get(key);
        if (flags == null) throw new IllegalArgumentException("Invalid key: " + key);
        if (x == 0 && flags[x]) { //if a table is getting removed, remove all its chairs
            for (boolean flag : flags) {
                flags[x] = false;
            }
        } else {
            flags[x] = !flags[x];
        }

        saveTables();
    }

}

