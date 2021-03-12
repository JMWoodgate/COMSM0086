package com.company;
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

        Table newTable = new Table(name, fileToOpen);
        System.out.println(newTable.getRows().get(0).getElements());
        System.out.println(newTable.getColumns().get(0).getColumnName());

        Test testing = new Test();
    }
}
