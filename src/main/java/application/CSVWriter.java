package application;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("CallToPrintStackTrace")
public class CSVWriter {

    public void createFile(String fileName) {
        File myObj = new File(fileName);
        if (myObj.exists()) {
            System.out.println("File " + myObj.getName() + " already exists. Overwriting...");
        } else {
            System.out.println("File " + myObj.getName() + " created.");
        }

        writeToFile(fileName, "PuzzleNum,Zeroes,TTC", false);

    }

    public void writeToFile(String fileName, String input, boolean append) {
        try {
            FileWriter fw = new FileWriter(fileName, append);
            fw.write(input + System.lineSeparator());
            fw.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public String getAbsolutePath(String fileName) {
        File myObj = new File(fileName);
        return "file://" + myObj.getAbsolutePath();
    }

}