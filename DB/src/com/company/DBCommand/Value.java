package com.company.DBCommand;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;

import java.util.ArrayList;

public class Value {

    private float floatLiteral;
    private final ArrayList<String> command;
    private int index;
    private LiteralType type;
    private String value;

    public Value(ArrayList<String> command, int index) throws DBException {
        this.command = command;
        this.index = index;
        if (command != null) {
            value = command.get(index);
            //checks the token and sets the type of value
            setLiteralType();
        } else {
            throw new EmptyData("Command");
        }
    }

    public int getIndex(){
        return index;
    }

    public String getValue() throws EmptyData {
        if(value!=null) {
            return value;
        }
        throw new EmptyData("value");
    }

    public LiteralType getLiteralType() {
        return type;
    }

    public float getFloatLiteral(){
        return floatLiteral;
    }

    private void setLiteralType() throws DBException{
        if(integerLiteral(command.get(index))){
            type = LiteralType.INTEGER;
        }
        else if(floatLiteral(command.get(index))){
            type = LiteralType.FLOAT;
        }
        else if(boolLiteral(command.get(index))){
            type = LiteralType.BOOLEAN;
        }
        else if(stringLiteral(command, index)){
            type = LiteralType.STRING;
        }
        else{
            throw new CommandException(command.get(index), index, "value");
        }
    }

    private boolean stringLiteral(ArrayList<String> tokenizedCommand, int index) {
        String token = tokenizedCommand.get(index);
        if(token.charAt(0)!='\''){
            return false;
        }
        int lastIndex = token.length()-1;
        String stringLiteral;
        if(token.charAt(lastIndex)!= '\''){
            //if the end of that token isn't a single quote, concat tokens
            stringLiteral = concatStringLiteral(tokenizedCommand, index);
            return true;
        }
        stringLiteral = token;
        return true;
    }

    private String concatStringLiteral(
            ArrayList<String> tokenizedCommand, int index){
        String currentToken = tokenizedCommand.get(index);
        String stringLiteral = currentToken;
        //loop through until we find the end of the stringLiteral
        while(currentToken.charAt(currentToken.length()-1)!='\''
                &&index<tokenizedCommand.size()-1){
            index++;
            currentToken = tokenizedCommand.get(index);
            //concat next token to the end of our stringLiteral
            stringLiteral = stringLiteral.concat(currentToken);
        }
        //update the index to be pointing to end of stringLiteral
        this.index = index;
        value = stringLiteral;
        return stringLiteral;
    }

    private boolean floatLiteral(String token) throws DBException {
        if (token != null) {
            try {
                floatLiteral = Float.parseFloat(token);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        else {
            throw new EmptyData("Command in floatLiteral");
        }
    }

    private boolean integerLiteral(String token) throws DBException {
        if(token!=null){
            try{
                floatLiteral = Integer.parseInt(token);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        else {
            throw new EmptyData("Command in integerLiteral");
        }
    }

    private boolean boolLiteral(String token) throws DBException{
        if(token!=null){
            boolean booleanLiteral;
            if(token.equals("true")){
                booleanLiteral = true;
                return true;
            }
            else if(token.equals("false")){
                booleanLiteral = false;
                //returning true because the token is a boolean value
                return true;
            }
            else{
                //otherwise return false because it is not a boolean value
                return false;
            }
        }
        else {
            throw new EmptyData("Command in boolLiteral");
        }
    }
}
