package com.company;
import java.io.*;
import java.net.*;
import java.util.*;

class DBServer
{
    public DBServer(int portNumber)
    {
    }

    public static LinkedList readFile(File fileToOpen) {
        LinkedList fileStorage = null;
        if (fileToOpen.exists()) {
            FileReader reader = null;
            try {
                reader = new FileReader(fileToOpen);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader buffReader = new BufferedReader(reader);
            String currentLine = null;
            try {
                currentLine = buffReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileStorage = new LinkedList();

            while (currentLine != null) {
                fileStorage.add(currentLine);
                fileStorage.add("\n");
                try {
                    //can put this in a while loop, and store a new row each time until reaching the end of the file
                    currentLine = buffReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                buffReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileStorage;
    }

    public static void main(String[] args)
    {
        DBServer server = new DBServer(8888);
        String name = ".." + File.separator + "Testfiles" + File.separator + "contact-details.tab";
        File fileToOpen = new File(name);

        LinkedList fileStorage = readFile(fileToOpen);

        System.out.println(fileStorage);
    }
}
