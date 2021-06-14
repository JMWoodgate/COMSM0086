package com.company.Parsing;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

public class ActionsParser {
    private String actionFilename;

    public ActionsParser(String actionFilename){
        try{
            this.actionFilename = actionFilename;
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(actionFilename);
            JSONObject parsed = (JSONObject)parser.parse(reader);
            JSONArray actionList = (JSONArray)parsed.get("actions");
            actionList.forEach(action -> parseAction((JSONObject) action));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void parseAction(JSONObject action){
        System.out.print("\n"+"triggers: ");
        JSONArray triggers = (JSONArray) action.get("triggers");
        triggers.forEach(trigger -> System.out.print(trigger+" "));
        System.out.print("\n"+"subjects: ");
        JSONArray subjects = (JSONArray) action.get("subjects");
        subjects.forEach(subject -> System.out.print(subject+" "));
        System.out.print("\n"+"consumed: ");
        JSONArray consumed = (JSONArray) action.get("consumed");
        consumed.forEach(c -> System.out.print(c+" "));
        System.out.print("\n"+"produced: ");
        JSONArray produced = (JSONArray) action.get("produced");
        produced.forEach(p -> System.out.print(p+" "));
        System.out.print("\n"+"narration: ");
        String narration = (String) action.get("narration");
        System.out.println(narration);
    }
}
