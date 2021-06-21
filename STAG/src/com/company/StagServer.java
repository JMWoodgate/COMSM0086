package com.company;
import com.company.Parsing.ActionsParser;
import com.company.Parsing.EntitiesParser;
import com.company.StagExceptions.InvalidCommand;
import com.company.StagExceptions.StagException;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

class StagServer
{
    private String entityFilename;
    private String actionFilename;
    private StagEngine engine;

    public static void main(String args[])
    {
        if(args.length != 2) System.out.println("Usage: java StagServer <entity-file> <action-file>");
        else new StagServer(args[0], args[1], 8888);
    }

    public StagServer(String entityFilename, String actionFilename, int portNumber)
    {
        try {
            //open connection
            ServerSocket ss = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            this.entityFilename = entityFilename;
            this.actionFilename = actionFilename;
            engine = new StagEngine(entityFilename, actionFilename);
            while(true) acceptNextConnection(ss);
        } catch(IOException | StagException ioe) {
            System.err.println(ioe);
        } catch(NullPointerException npe) {
            System.out.println("Connection Lost");
        }
    }

    private void acceptNextConnection(ServerSocket ss) throws StagException
    {
        try {
            // Next line will block until a connection is received
            Socket socket = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            processNextCommand(in, out);
            out.close();
            in.close();
            socket.close();
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void processNextCommand(BufferedReader in, BufferedWriter out) throws IOException, StagException
    {
        String line = in.readLine();
        String[] splitString = line.split(":", 2);
        //check if the player already exists
        if(!engine.playerExists(splitString[0])){
            engine.addPlayer(splitString[0]);
        } //check if the player is the current player or different
        else if(!engine.getCurrentPlayer().getName().equals(splitString[0])){
            engine.changePlayer(splitString[0]);
        }
        try {
            String response = engine.interpretCommand(splitString[1]);
            out.write(response+"\n");
        }catch(StagException e){
            e.printStackTrace();
           out.write(e.toString());
        }

    }
}
