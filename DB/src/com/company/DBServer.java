package com.company;
import java.io.*;
import java.net.*;
import java.util.*;

class DBServer
{

    public DBServer(int portNumber)
    {
    }

    public static LinkedList<String> readFile(File fileToOpen) throws IOException
    {
        LinkedList<String> fileStorage = null;
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
            String currentLine = buffReader.readLine();

            fileStorage = new LinkedList<>();

            while (currentLine != null) {
                fileStorage.add(currentLine);
                fileStorage.add("\n");
                currentLine = buffReader.readLine();
            }
            buffReader.close();
        }
        return fileStorage;
    }

    public static void main(String[] args)
    {
        //DBServer server = new DBServer(8888);
        String name = ".." + File.separator + "Testfiles" + File.separator + "contact-details.tab";
        File fileToOpen = new File(name);
        LinkedList<String> fileStorage = null;

        try{
            fileStorage = readFile(fileToOpen);
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println(fileStorage);
    }
}
