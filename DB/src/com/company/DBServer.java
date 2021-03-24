package com.company;
import com.company.DBCommand.Interpreter;
import com.company.DBCommand.Parser;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;
import com.company.DBExceptions.FileException;

import java.io.*;
import java.net.*;
import java.util.*;

class DBServer
{
    private String folderName;
    private Interpreter interpreter;
    public DBServer(int portNumber)
    {
        try {
            //open connection
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            //create databases directory to store info
            folderName = "."+ File.separator+"databases";
            interpreter = new Interpreter(folderName);
            while(true) processNextConnection(serverSocket);
        } catch(IOException | FileException ioe) {
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
        } catch(IOException | DBException ioe) {
            System.err.println(ioe);
        } catch(NullPointerException npe) {
            System.out.println("Connection Lost");
        }
    }

    private void processNextCommand(BufferedReader socketReader, BufferedWriter socketWriter)
            throws IOException, NullPointerException, DBException {
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

        while(parsedOK) {
            String incomingCommand = socketReader.readLine();
            parser = new Parser(incomingCommand, folderName);
            parsedOK = parser.getParsedOK();
            if (!parsedOK) {
                parser = exceptionLoop(socketWriter, socketReader, parser, incomingCommand);
            }
            folderName = parser.getCurrentFolder();
            ArrayList<String> currentCommand = parser.getTokenizedCommand();
            interpreter.interpretCommand(currentCommand.get(0), parser);
            while(!interpreter.getInterpretedOK()){
                parser.setException(interpreter.getException());
                exceptionLoop(socketWriter, socketReader, parser, incomingCommand);
            }
            socketWriter.write("[OK] Thanks for your message: " + incomingCommand);
            socketWriter.write("\n" + ((char) 4) + "\n");
            socketWriter.flush();
        }
    }

    private Parser exceptionLoop(BufferedWriter socketWriter, BufferedReader socketReader,
                                  Parser parser, String incomingCommand) throws IOException {
        boolean parsedOK = false;
        while (!parsedOK) {
            socketWriter.flush();
            socketWriter.write("[ERROR] from: " + incomingCommand);
            socketWriter.write("\n" + parser.getException());
            socketWriter.write("\n" + ((char) 4) + "\n");
            socketWriter.flush();
            incomingCommand = socketReader.readLine();
            parser = new Parser(incomingCommand, folderName);
            parsedOK = parser.getParsedOK();
        }
        return parser;
    }

    public static void main(String[] args) {
        try {
            String folderName = "."+ File.separator+"databases";
            File parentFolder = new File(folderName);
            if(!parentFolder.exists()) {
                final boolean mkdir = parentFolder.mkdir();
                //create new folder (for new database)
                if (!mkdir) {
                    throw new IOException();
                }
            }
            Test testing = new Test();
        } catch (DBException | IOException e) {
            e.printStackTrace();
        }
        DBServer server = new DBServer(8888);
    }
}
