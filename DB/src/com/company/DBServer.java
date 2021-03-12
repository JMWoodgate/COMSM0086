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
        String name = File.separator + "Users" + File.separator + "jessw" + File.separator
        + "Documents" + File.separator + "Java" + File.separator + "COMSM0086" +
                File.separator + "Testfiles" + File.separator + "contact-details.tab";
        File fileToOpen = new File(name);

        FileIO fileIO = new FileIO();
        ArrayList<String> dataFromFile = new ArrayList<>();
        try{
            dataFromFile = fileIO.readFile(fileToOpen);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Table newTable = null;
        try {
            newTable = new Table("newTable", name, dataFromFile);
        } catch (DBException e) {
            e.printStackTrace();
        }
        System.out.println(newTable.getRows().get(0).getElements());
        System.out.println(newTable.getColumns().get(0).getColumnName());
        System.out.println("rownum " + newTable.getNumberOfRows());
        System.out.println("colnum " + newTable.getNumberOfColumns());
        try {
            Test testing = new Test();
        } catch (DBException e) {
            e.printStackTrace();
        }
    }
}
