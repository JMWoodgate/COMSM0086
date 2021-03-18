package com.company;

import com.company.DBExceptions.EmptyData;

import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Tokenizer {

    private final ArrayList<String> tokenizedCommand;

    public Tokenizer(String command) throws EmptyData {
        tokenizedCommand = tokenizeCommand(command);
    }

    public String nextToken(int nextIndex){
        return tokenizedCommand.get(nextIndex);
    }

    public ArrayList<String> getTokenizedCommand() {
        return tokenizedCommand;
    }

    private ArrayList<String> tokenizeCommand(String command) throws EmptyData {
        if(command!=null) {
            command = command.toLowerCase();
            //turning command into an array list
            ArrayList<String> tokenizedCommand = new ArrayList<>();
            StringTokenizer tokenizer = new StringTokenizer(command, " \t\n\r\f();=<>!*", true);
            while (tokenizer.hasMoreElements()) {
                tokenizedCommand.add(tokenizer.nextToken());
            }
            tokenizedCommand = cleanCommandList(tokenizedCommand);
            return tokenizedCommand;
        }
        throw new EmptyData("Command");
    }

    //getting rid of empty strings in tokenized command list
    private ArrayList<String> cleanCommandList(ArrayList<String> tokenizedCommand)
            throws EmptyData {
        if(tokenizedCommand != null) {
            for (int i = 0; i < tokenizedCommand.size(); i++) {
                String currentToken = tokenizedCommand.get(i);
                switch (currentToken) {
                    case (""):
                    case (" "):
                    case ("\t"):
                    case ("\n"):
                    case ("\r"):
                    case ("\f"):
                        tokenizedCommand.remove(currentToken);
                        i--;
                        break;
                    default:
                        break;
                }
            }
            return tokenizedCommand;
        }
        throw new EmptyData("Command");
    }
}
