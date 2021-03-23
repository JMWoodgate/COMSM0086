package com.company;

import com.company.DBExceptions.*;

import java.io.*;
import java.util.ArrayList;

public class FileIO {

    private final String folderName;

    public FileIO(String folderName){
        this.folderName = folderName;
    }

    public void deleteFolder() throws DBException{
        File folder = new File(folderName);
        File[] listOfFiles;
        if(folder.exists()) {
            //creating a list of files that are within the folder
            listOfFiles = folder.listFiles();
            assert listOfFiles != null;
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    //saving the name in a string for error handling
                    String fileName = listOfFile.getName();
                    if (!listOfFile.delete())
                        throw new FileException("couldn't delete file "+fileName);
                    }
                }
            if(!folder.delete()){
                throw new FileException("couldn't delete folder "+folderName);
            }
        }
    }

    public Database makeFolder(String parentFolder, String folderName) throws DBException {
        if(folderName==null||parentFolder==null){
            throw new EmptyData("database name in makeFolder");
        }
        File folderToMake = new File(parentFolder, folderName);
        //Check that folder doesn't already exist
        if(!folderToMake.exists()){
            final boolean mkdir = folderToMake.mkdir();
            //create new folder (for new database)
            if(!mkdir){
                throw new FileException("couldn't make folder");
            }
            //create new database and return
            return new Database(folderName);
        }else{
            throw new FileExists(folderName);
        }
    }

    public File makeFile(String parentFolder, String tableName)
            throws DBException, IOException {
        if(tableName==null||parentFolder==null){
            throw new EmptyData("table name/parent folder in makeFile");
        }
        File folderToOpen = new File(parentFolder);
        //checking that the folder we want to write the file into exists
        if(!folderToOpen.exists()) {
            final boolean mkdir = folderToOpen.mkdir();
            if (!mkdir) {
                throw new FileException("couldn't create directory");
            }
        }
        File fileToMake = new File(parentFolder, tableName+".tab");
        if(!fileToMake.exists()){
            final boolean newFile = fileToMake.createNewFile();
            if(!newFile){
                throw new FileException("couldn't create file");
            }
        }else{
            throw new FileExists(tableName);
        }
        return fileToMake;
    }

    public void writeFile(String folderName, String fileName, Table table) throws IOException, DBException {
        //opening file, creating if it doesn't exist
        File fileToOpen = new File(folderName, fileName+".tab");
        if(!fileToOpen.exists()){
            final boolean newFile = fileToOpen.createNewFile();
            if(!newFile){
                throw new FileException("couldn't create file");
            }
        }
        //creating a new file writer
        FileWriter fileWriter = new FileWriter(fileToOpen);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        //write the table to the file
        //getting tables and rows from memory
        ArrayList<String> columns = table.getColumns();
        System.out.println("columns in writeFile");
        ArrayList<ArrayList<String>> rows = table.getRows();
        //formatting columns and writing to file
        bufferedWriter.write(formatString(columns));
        bufferedWriter.write("\n");
        //formatting rows and adding one at a time to file
        for (ArrayList<String> row : rows) {
            bufferedWriter.write(formatString(row));
            bufferedWriter.write("\n");
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    public String formatString(ArrayList<String> stringToFormat){
        String formatString;
        StringBuilder stringBuilder = new StringBuilder();
        //turning ArrayList into String and putting a tab between each token
        for (String s : stringToFormat) {
            stringBuilder.append(s);
            stringBuilder.append("\t");
        }
        formatString = stringBuilder.toString();
        return formatString;
    }

    public Database readFolder(String folderName) throws FileException {
        File folder = new File(folderName);
        File[] listOfFiles;
        Database newDatabase;
        if(folder.exists()) {
            //creating a list of files that are within the folder
            listOfFiles = folder.listFiles();
            newDatabase = new Database(folderName);
        }else{
            throw new EmptyData(folderName);
        }
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
                        newTable = new Table(folderName, fileName);
                        newTable.fillTableFromFile(dataFromFile);
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
