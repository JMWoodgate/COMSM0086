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
        String name = ".." + File.separator + "Testfiles" + File.separator + "contact-details.tab";
        File fileToOpen = new File(name);
        ArrayList<String> fileStorage = null;

        FileIO fileIO = new FileIO();

        try{
            fileStorage = fileIO.readFile(fileToOpen);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(fileStorage);
    }
}
