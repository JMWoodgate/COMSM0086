package com.company;
import com.company.DBCommand.Interpreter;
import com.company.DBCommand.Parser;
import com.company.DBExceptions.DBException;
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
        } catch (DBException e) {
            e.printStackTrace();
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

    private void processNextCommand(BufferedReader socketReader,
                                    BufferedWriter socketWriter)
            throws IOException, NullPointerException, DBException {
        String incomingCommand = socketReader.readLine();
        Parser parser = new Parser(incomingCommand, folderName);
        boolean parsedOK = parser.getParsedOK();
        while (!parsedOK) {
            socketWriter.write("[ERROR] from: " + incomingCommand);
            socketWriter.write("\n" + parser.getException());
            socketWriter.write("\n" + ((char) 4) + "\n");
            socketWriter.flush();
            incomingCommand = socketReader.readLine();
            parser = new Parser(incomingCommand, folderName);
            parsedOK = parser.getParsedOK();
        }
        folderName = parser.getCurrentFolder();
        ArrayList<String> currentCommand = parser.getTokenizedCommand();
        String results = interpreter.interpretCommand(currentCommand.get(0), parser);
        if(!interpreter.getInterpretedOK()){
            socketWriter.write("[ERROR] from: " + incomingCommand);
            socketWriter.write("\n" + interpreter.getException());
        }else {
            socketWriter.write("[OK]");
            if(results!=null){
                socketWriter.write("\n"+results);
            }
        }
        socketWriter.write("\n" + ((char) 4) + "\n");
        socketWriter.flush();
    }

    public static void main(String[] args) {
        DBServer server = new DBServer(8888);
    }
}
