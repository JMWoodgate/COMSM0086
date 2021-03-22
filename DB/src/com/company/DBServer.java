package com.company;
import com.company.DBCommand.Parser;
import com.company.DBExceptions.DBException;

import java.io.*;
import java.net.*;
import java.util.*;

class DBServer
{
    public DBServer(int portNumber)
    {
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            while(true) processNextConnection(serverSocket);
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void processNextConnection(ServerSocket serverSocket)
    {
        try {
            Socket socket = serverSocket.accept();
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Connection Established");
            while(true) processNextCommand(socketReader, socketWriter);
        } catch(IOException ioe) {
            System.err.println(ioe);
        } catch(NullPointerException npe) {
            System.out.println("Connection Lost");
        }
    }

    private void processNextCommand(BufferedReader socketReader, BufferedWriter socketWriter) throws IOException, NullPointerException
    {
        String folderName = File.separator + "Users" + File.separator + "jessw" + File.separator
                + "Documents" + File.separator + "Java" + File.separator + "COMSM0086" +
                File.separator + "Testfiles" + File.separator;
        //String newFolderName = File.separator + "Users" + File.separator + "jessw" + File.separator
        //+ "Documents" + File.separator + "Java" + File.separator + "COMSM0086" +
        //File.separator + "DB" + File.separator + "Testfiles" + File.separator;

        boolean parsedOK = true;
        Parser parser = null;

        /*try {
            FileIO fileIO = new FileIO(folderName);
            Database database = fileIO.readFolder(folderName);
            socketWriter.write(database.getDatabase());
            socketWriter.flush();
            //ArrayList<String> listOfTableNames = database.getTableNames();
            //ArrayList<Table> tables = database.getTables();
            //for(int i = 0; i < database.getNumberOfTables(); i++){
            //    fileIO.writeFile(newFolderName, listOfTableNames.get(i), tables.get(i));
            //}
        } catch(DBException | IOException e){
            e.printStackTrace();
        }*/

        String incomingCommand = socketReader.readLine();
        parser = new Parser(incomingCommand);
        parsedOK = parser.getParsedOK();
        if(parsedOK){
            socketWriter.write("[OK2] Thanks for your message: " + incomingCommand);
            socketWriter.write("\n" + ((char)4) + "\n");
            socketWriter.flush();
        }
        else{
            socketWriter.write("[ERROR] Thanks for your message: " + incomingCommand);
            socketWriter.write("\n"+parser.getException());
            socketWriter.write("\n" + ((char)4) + "\n");
            socketWriter.flush();
        }
        System.out.println("Received message: " + incomingCommand);
        socketWriter.write("[OK1] Thanks for your message: " + incomingCommand);
        socketWriter.write("\n" + ((char)4) + "\n");
        socketWriter.flush();


    }

    public static void main(String[] args) {
        try {
            Test testing = new Test();
        } catch (DBException e) {
            e.printStackTrace();
        }
        DBServer server = new DBServer(8888);
    }
}
