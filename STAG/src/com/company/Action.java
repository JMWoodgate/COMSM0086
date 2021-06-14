package com.company;

import java.util.ArrayList;

public class Action {
    private final ArrayList<String> triggers;
    private final ArrayList<String> subjects;
    private final ArrayList<String> consumed;
    private final ArrayList<String> produced;
    private String narration;

    public Action(){
        triggers = new ArrayList<>();
        subjects = new ArrayList<>();
        consumed = new ArrayList<>();
        produced = new ArrayList<>();
    }

    public void setTriggers(String trigger){
        triggers.add(trigger);
    }

    public ArrayList<String> getTriggers(){
        return triggers;
    }

    public void setSubjects(String subject){
        subjects.add(subject);
    }

    public ArrayList<String> getSubjects(){
        return subjects;
    }

    public void setConsumed(String item){
        consumed.add(item);
    }

    public ArrayList<String> getConsumed(){
        return consumed;
    }

    public void setProduced(String item){
        produced.add(item);
    }

    public ArrayList<String> getProduced(){
        return produced;
    }

    public void setNarration(String narration){
        this.narration = narration;
    }

    public String getNarration(){
        return narration;
    }
}
