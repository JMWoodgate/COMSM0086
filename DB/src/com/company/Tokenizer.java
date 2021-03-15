package com.company;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Tokenizer {

    String command;

    public Tokenizer(String command){
        this.command = command;
    }

    public ArrayList<String> tokenizeCommand(String command){
        ArrayList<String> tokenizedCommand = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(command);
        while(tokenizer.hasMoreElements()){
            tokenizedCommand.add(tokenizer.nextToken());
        }

        return tokenizedCommand;
    }
}
