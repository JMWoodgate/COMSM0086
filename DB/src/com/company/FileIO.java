package com.company;

import com.company.DBExceptions.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class FileIO {

    private final String folderName;

    public FileIO(String folderName){
        this.folderName = folderName;
    }

    public void deleteFolder() throws DBException{
        File folder = new File(folderName);
        File[] listOfFiles;
        if(folder.exists()) {
            listOfFiles = folder.listFiles();
            assert listOfFiles != null;
            deleteListOfFiles(listOfFiles);
            if(!folder.delete()){
                throw new FileException("couldn't delete folder "+folderName);
            }
        }
    }

    private void deleteListOfFiles(File[] listOfFiles) throws DBException{
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                //save name for error handling
                String fileName = listOfFile.getName();
                if (!listOfFile.delete()) {
                    throw new FileException("couldn't delete file " + fileName);
                }
            }
        }
    }

    public Database makeFolder(String parentFolder, String folderName)
            throws DBException {
        if(folderName==null||parentFolder==null){
            throw new EmptyData("database name in makeFolder");
        }
        File folderToMake = new File(parentFolder, folderName);
        if(!folderToMake.exists()){
            final boolean mkdir = folderToMake.mkdir();
            if(!mkdir){
                throw new FileException("couldn't make folder");
            }
            //create new database and return
            return new Database(folderName);
        }else{
            throw new FileExists(folderName);
        }
    }

    public void makeFile(String parentFolder, String tableName)
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
    }

    public void writeFile(String folderName, String fileName, Table table)
            throws IOException, DBException {
        File fileToOpen = new File(folderName, fileName+".tab");
        if(!fileToOpen.exists()){
            final boolean newFile = fileToOpen.createNewFile();
            if(!newFile){
                throw new FileException("couldn't create file");
            }
        }
        FileWriter fileWriter = new FileWriter(fileToOpen);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        //getting tables and rows from memory
        ArrayList<String> columns = table.getColumns();
        ArrayList<ArrayList<String>> rows = table.getRows();
        //formatting columns and rows, writing to file
        bufferedWriter.write(formatString(columns));
        bufferedWriter.write("\n");
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
        //turning list to String and putting a tab between each token
        for (String s : stringToFormat) {
            stringBuilder.append(s);
            stringBuilder.append("\t");
        }
        formatString = stringBuilder.toString();
        return formatString;
    }

    public HashMap<String, Database> readAllFolders(String homeDirectory)
            throws DBException, IOException {
        File homeFolder = new File(homeDirectory);
        File[] listOfFiles;
        HashMap<String, Database> databaseMap = new HashMap<>();
        if(homeFolder.exists()){
            listOfFiles = homeFolder.listFiles();
        }else{
            final boolean mkdir = homeFolder.mkdir();
            if (!mkdir) {
                throw new FileException("could not create home directory");
            }
            return databaseMap;
        }
        if(listOfFiles == null){
            return databaseMap;
        }else{
            databaseMap = readAllFolders(listOfFiles, databaseMap);
        }
        return databaseMap;
    }

    //loop through folders and read each one, store in hashmap
    private HashMap<String, Database> readAllFolders(File[] listOfFiles, HashMap<String, Database> databaseMap)
            throws DBException, IOException {
        for(File currentFile : listOfFiles){
            if(currentFile.isDirectory()){
                String filePath = currentFile.getPath();
                Database database = readFolder(filePath);
                databaseMap.put(database.getDatabaseName(), database);
            }
        }
        return databaseMap;
    }

    public Database readFolder(String folderName) throws DBException, IOException {
        File folder = new File(folderName);
        File[] listOfFiles;
        Database newDatabase;
        if(folder.exists()) {
            listOfFiles = folder.listFiles();
            newDatabase = new Database(folder.getName());
        }else{
            throw new EmptyData(folderName+" in readFolder");
        }
        if(listOfFiles == null){
            throw new EmptyData(folderName+" in readFolder");
        }
        for (File fileToOpen : listOfFiles) {
            if (fileToOpen.isFile()) {
                String fileName = fileToOpen.getName();
                ArrayList<String> dataFromFile = readFile(fileToOpen);
                //sending the data we read to our new table
                fileName = removeExtension(fileName);
                Table newTable = new Table(folder.getName(), fileName);
                newTable.fillTableFromFile(dataFromFile);
                //adding the new table to the list of tables in our database
                newDatabase.addTable(newTable);
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
        if (fileToOpen.exists()) {
            FileReader reader = new FileReader(fileToOpen);
            BufferedReader buffReader = new BufferedReader(reader);
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
