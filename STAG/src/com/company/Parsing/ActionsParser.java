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
            JSONArray actionList = (JSONArray)parsed;
            actionList.forEach(action -> parseAction((JSONObject) action));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void parseAction(JSONObject action){
        String triggers = (String) action.get("triggers");
        System.out.println(triggers);
        String subjects = (String) action.get("subjects");
        System.out.println(subjects);
        String consumed = (String) action.get("consumed");
        System.out.println(consumed);
        String produced = (String) action.get("produced");
        System.out.println(produced);
        String narration = (String) action.get("narration");
        System.out.println(narration);
    }
}
