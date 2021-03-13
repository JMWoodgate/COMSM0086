package com.company;

import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;
import com.company.DBExceptions.FileException;

import java.io.*;
import java.util.ArrayList;

public class FileIO {

    private final String folderName;

    public FileIO(String folderName){
        this.folderName = folderName;
    }

    public Database readFolder(String folderName) throws FileException {
        File folder = new File(folderName);
        //creating a list of files that are within the folder
        File[] listOfFiles = folder.listFiles();
        Database newDatabase = new Database(folderName);

        //checking the list isn't empty
        if(listOfFiles == null){
            throw new EmptyData(folderName);
        }
        if(listOfFiles.length > 0) {
            //iterating through the list of files
            for(int i = 0; i < listOfFiles.length; i++) {
                //checking if it is a file
                if(listOfFiles[i].isFile()) {
                    //selecting current file
                    File fileToOpen = listOfFiles[i];
                    //saving the name in a string
                    String fileName = fileToOpen.getName();
                    //creating a variable to store the data in
                    ArrayList<String> dataFromFile = new ArrayList<>();
                    try {
                        //reading the data from the file
                        dataFromFile = readFile(fileToOpen);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //creating a new table
                    Table newTable = null;
                    try {
                        //sending the data we read to our new table
                        newTable = new Table(folderName, fileName, dataFromFile);
                    } catch (DBException e) {
                        e.printStackTrace();
                    }
                    //adding the new table to the list of tables in our database
                    newDatabase.addTable(newTable);
                }
            }
        }
        return newDatabase;
    }

    public ArrayList<String> readFile(File fileToOpen) throws IOException
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
