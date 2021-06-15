package com.company;
import com.company.Parsing.ActionsParser;
import com.company.Parsing.EntitiesParser;
import com.company.StagExceptions.StagException;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

class StagServer
{
    private String entityFilename;
    private String actionFilename;

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
            StagEngine engine = new StagEngine(entityFilename, actionFilename);
            while(true) acceptNextConnection(ss);
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void acceptNextConnection(ServerSocket ss)
    {
        try {
            // Next line will block until a connection is received
            Socket socket = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Connection Established");
            processNextCommand(in, out);
            out.close();
            in.close();
            socket.close();
        } catch(IOException | StagException ioe) {
            System.err.println(ioe);
        } catch(NullPointerException npe) {
            System.out.println("Connection Lost");
        }
    }

    private void processNextCommand(BufferedReader in, BufferedWriter out) throws IOException, StagException
    {
        String line = in.readLine();
        out.write("You said... " + line + "\n");
        String[] splitString = line.split(":");
        System.out.println(splitString[0]);

    }
}
