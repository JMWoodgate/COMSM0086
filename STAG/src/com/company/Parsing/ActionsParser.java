package com.company.Parsing;
import com.company.Action;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;

public class ActionsParser {
    private ArrayList<Action> actions;

    public ActionsParser(String actionFilename){
        try{
            actions = new ArrayList<>();
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(actionFilename);
            JSONObject parsed = (JSONObject)parser.parse(reader);
            JSONArray actionList = (JSONArray)parsed.get("actions");
            for(Object action : actionList){
                parseAction((JSONObject) action);
            }
            //printActions();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //parses each action object and stores info
    private void parseAction(JSONObject action){
        Action newAction = new Action();
        JSONArray triggers = (JSONArray) action.get("triggers");
        for(Object trigger : triggers){
            newAction.setTriggers((String)trigger);
        }
        JSONArray subjects = (JSONArray) action.get("subjects");
        for(Object subject : subjects){
            newAction.setSubjects((String)subject);
        }
        JSONArray consumed = (JSONArray) action.get("consumed");
        for(Object consume : consumed){
            newAction.setConsumed((String)consume);
        }
        JSONArray produced = (JSONArray) action.get("produced");
        for(Object produce : produced){
            newAction.setProduced((String)produce);
        }
        String narration = (String) action.get("narration");
        newAction.setNarration(narration);
        actions.add(newAction);
    }

    public ArrayList<Action> getActions() {
        return actions;
    }
}
