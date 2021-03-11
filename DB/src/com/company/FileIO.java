package com.company;

import java.io.*;
import java.util.ArrayList;

public class FileIO {

    public FileIO(){

    }

    public static ArrayList<String> readFile(File fileToOpen) throws IOException
    {
        ArrayList<String> dataFromFile = null;
        String currentLine;

        //opening file and assigning a buffered reader
        if (fileToOpen.exists()) {
            FileReader reader = null;
            try {
                reader = new FileReader(fileToOpen);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader buffReader = null;
            if (reader != null) {
                buffReader = new BufferedReader(reader);
            }
            assert buffReader != null;
            dataFromFile = new ArrayList<>();

            //reading from the file line by line, and storing each line in an ArrayList
            do {
                currentLine = buffReader.readLine();
                dataFromFile.add(currentLine);
            } while (currentLine != null);

            buffReader.close();
        }
        return dataFromFile;
    }
}
