package com.company;
import com.company.DBExceptions.DBException;

import java.io.*;
import java.net.*;
import java.util.*;

class DBServer
{
    private int portNumber;

    public DBServer(int portNumber)
    {
        this.portNumber = portNumber;
    }

    public static void main(String[] args)
    {
        //DBServer server = new DBServer(8888);
        String folderName = File.separator + "Users" + File.separator + "jessw" + File.separator
        + "Documents" + File.separator + "Java" + File.separator + "COMSM0086" +
                File.separator + "Testfiles" + File.separator;
        String newFolderName = File.separator + "Users" + File.separator + "jessw" + File.separator
                + "Documents" + File.separator + "Java" + File.separator + "COMSM0086" +
                File.separator + "DB" + File.separator + "Testfiles" + File.separator;

        try {
            FileIO fileIO = new FileIO(folderName);
            Database database = fileIO.readFolder(folderName);
            database.printDatabase();
            ArrayList<String> listOfTableNames = database.getTableNames();
            ArrayList<Table> tables = database.getTables();
            for(int i = 0; i < database.getNumberOfTables(); i++){
                fileIO.writeFile(newFolderName, listOfTableNames.get(i), tables.get(i));
            }
        } catch(DBException | IOException e){
            e.printStackTrace();
        }

        try {
            Test testing = new Test();
        } catch (DBException e) {
            e.printStackTrace();
        }
    }
}
