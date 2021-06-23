package com.company.Element;

import com.company.StagExceptions.SubjectDoesNotExist;

import java.util.ArrayList;

public class Subject implements Element {

    private String name;
    private String description;
    private String type;
    private Location location;

    public Subject(){}

    public Subject(String name, String description,
                   String type, Location location){
        this.name = name;
        this.description = description;
        this.type = type;
        //if location is null, it is in the player's inventory
        this.location = location;
    }

    public Location getLocation(){ return location; }

    public String getType(){ return type; }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public boolean moveSubject(String subject,
                                ArrayList<Subject> subjectListToCheck,
                                ArrayList<Subject> currentLocationSubjectList,
                               Location location){
        //get subject object from location
        Subject subjectObject = getSubject(subject, subjectListToCheck);
        //check if the subject to produce exists in this location
        if(subjectObject!=null){
            //create new subject in the current location
            setSubject(subjectObject.getName(),
                    subjectObject.getDescription(),
                    subjectObject.getType(),
                    currentLocationSubjectList,
                    location);
            //remove subject from its old location
            removeSubject(subjectObject.getName(), subjectListToCheck);
            return true;
        }
        return false;
    }


    public void setSubject(String name, String description,
                           String type, ArrayList<Subject> subjectList,
                           Location location){
        Subject subject = new Subject(name, description, type, location);
        subjectList.add(subject);
    }

    public void removeSubject(String subject,
                              ArrayList<Subject> subjectList){
        subjectList.removeIf(
                s -> s.getName().equals(subject)
                        || s.getDescription().equals(subject));
    }

    public void removeSubject(Subject subject,
                              ArrayList<Subject> subjectList){
        subjectList.remove(subject);
    }

    public boolean removeSubjectFromLocation(Subject subject){
        if(subject==null){
            return false;
        }
        Location location = subject.getLocation();
        if(subject.getType().equals("artefact")){
            removeSubject(subject, location.getArtefacts());
            return true;
        }else if(subject.getType().equals("furniture")){
            removeSubject(subject, location.getFurniture());
            return true;
        }else if(subject.getType().equals("character")){
            removeSubject(subject, location.getCharacters());
            return true;
        }
        //subject is in inventory
        return false;
    }

    public Subject getSubjectFromLocation(String subjectName, Location location) throws SubjectDoesNotExist {
        ArrayList<Subject> artefacts = location.getArtefacts();
        ArrayList<Subject> furniture = location.getFurniture();
        ArrayList<Subject> characters = location.getCharacters();
        //look for it in location artefacts
        Subject subject = getSubject(subjectName, artefacts);
        if(subject!=null){
            return subject;
        }
        //look for it in location furniture
        subject = getSubject(subjectName, furniture);
        if(subject!=null){
            return subject;
        }
        //look for it in location characters
        subject = getSubject(subjectName, characters);
        if(subject!=null){
            return subject;
        }
        //subject does not exist here
        return null;
    }

    public Subject getSubject(String subjectName,
                              ArrayList<Subject> subjectList){
        if(subjectList == null){
            return null;
        }
        for(Subject s : subjectList){
            if(s!=null && s.getName().equals(subjectName)){
                return s;
            }
        }
        return null;
    }

    public Element getElement(String elementName,
                              ArrayList<Element> elementList){
        if(elementList == null){
            return null;
        }
        for(Element e : elementList){
            if(e!=null && e.getName().equals(elementName)){
                return e;
            }
        }
        return null;
    }
}
