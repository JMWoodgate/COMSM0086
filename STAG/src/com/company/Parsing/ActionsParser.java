package com.company.Parsing;
import com.company.Action;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;

public class ActionsParser {
    private String actionFilename;
    private ArrayList<Action> actions;

    public ActionsParser(String actionFilename){
        try{
            this.actionFilename = actionFilename;
            actions = new ArrayList<>();
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(actionFilename);
            JSONObject parsed = (JSONObject)parser.parse(reader);
            JSONArray actionList = (JSONArray)parsed.get("actions");
            actionList.forEach(action -> parseAction((JSONObject) action));
            //printActions();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //parses each action object and stores info
    private void parseAction(JSONObject action){
        Action newAction = new Action();
        JSONArray triggers = (JSONArray) action.get("triggers");
        triggers.forEach(trigger -> newAction.setTriggers((String)trigger));
        JSONArray subjects = (JSONArray) action.get("subjects");
        subjects.forEach(subject -> newAction.setSubjects((String)subject));
        JSONArray consumed = (JSONArray) action.get("consumed");
        consumed.forEach(c -> newAction.setConsumed((String)c));
        JSONArray produced = (JSONArray) action.get("produced");
        produced.forEach(p -> newAction.setProduced((String)p));
        String narration = (String) action.get("narration");
        newAction.setNarration(narration);
        actions.add(newAction);
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void printActions(){
        for(Action a : actions){
            System.out.println("Triggers: "+a.getTriggers());
            System.out.println("Subjects: "+a.getSubjects());
            System.out.println("Consumed: "+a.getConsumed());
            System.out.println("Produced: "+a.getProduced());
            System.out.println("Narration: "+a.getNarration());
        }
    }
}
