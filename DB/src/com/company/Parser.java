package com.company;

public class Parser {

    private String command;

    public Parser(String command){
        this.command = command;
        Tokenizer tokenizer = new Tokenizer(command);
    }


}
