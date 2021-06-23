package com.company.Element;

import java.util.ArrayList;

public class SubjectUtility {

    public SubjectUtility(){}

    public boolean moveSubject(String subject,
                               ArrayList<Subject> subjectListToCheck,
                               ArrayList<Subject> currentLocationSubjectList,
                               Location currentLocation){
        //get subject object from location
        Subject subjectObject = getSubject(subject, subjectListToCheck);
        //check if the subject to produce exists in this location
        if(subjectObject!=null){
            //create new subject in the current location
            setSubject(subjectObject.getName(),
                    subjectObject.getDescription(),
                    subjectObject.getType(),
                    currentLocationSubjectList,
                    currentLocation);
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

    public void removeSubjectFromLocation(Subject subject){
        if(subject==null){
            return;
        }
        Location location = subject.getLocation();
        switch (subject.getType()) {
            case "artefact":
                removeSubject(subject, location.getArtefacts());
                return;
            case "furniture":
                removeSubject(subject, location.getFurniture());
                return;
            case "character":
                removeSubject(subject, location.getCharacters());
                return;
            default:
                //subject is in inventory
        }
    }

    public Subject getSubjectFromLocation(String subjectName, Location location) {
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
        //if subject does not exist in this location, it will be null
        return subject;
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
