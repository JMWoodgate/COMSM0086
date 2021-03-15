package com.company;

import com.company.DBExceptions.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FileIO {

    private final String folderName;

    public FileIO(String folderName){
        this.folderName = folderName;
    }

    public void writeFile(String folderName, String fileName, Table table) throws IOException {
        File folderToOpen = new File(folderName);
        if(!folderToOpen.exists()){
            final boolean mkdir = folderToOpen.mkdir();
            if(!mkdir){
                throw new IOException();
            }
        }
        File fileToOpen = new File(folderToOpen, fileName + ".tab");
        FileWriter fileWriter = new FileWriter(fileToOpen);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        if(!fileToOpen.exists()){
            final boolean newFile = fileToOpen.createNewFile();
            if(!newFile){
                throw new IOException();
            }
        }
        writeToOpenFile(bufferedWriter, table);
        bufferedWriter.close();
    }

    private void writeToOpenFile(BufferedWriter bufferedWriter, Table table)
            throws IOException {
        ArrayList<String> columns = table.getColumns();
        ArrayList<ArrayList<String>> rows = table.getRows();
        bufferedWriter.write(formatString(columns));
        bufferedWriter.write("\n");
        for (ArrayList<String> row : rows) {
            bufferedWriter.write(formatString(row));
            bufferedWriter.write("\n");
        }
        bufferedWriter.flush();
    }

    public String formatString(ArrayList<String> stringToFormat){
        String formatString;
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : stringToFormat) {
            stringBuilder.append(s);
            stringBuilder.append("\t");
        }
        formatString = stringBuilder.toString();
        return formatString;
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
                        fileName = removeExtension(fileName);
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

    private String removeExtension(String fileNameWithExtension)
        throws EmptyName {
        if(fileNameWithExtension==null){
            throw new EmptyName(StorageType.FILE, folderName);
        }
        else{
            String[] listOfWords = fileNameWithExtension.split(".tab");
            return listOfWords[0];
        }

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
