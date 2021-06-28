package com.company.Parsing;
import com.alexmerz.graphviz.*;
import com.alexmerz.graphviz.objects.*;
import com.company.Element.Location;
import com.company.Element.Subject;
import com.company.Element.SubjectUtility;
import com.company.StagExceptions.LocationDoesNotExist;
import com.company.StagExceptions.StagException;
import com.company.StagExceptions.UnknownDataType;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Locale;

public class EntitiesParser {

    private final ArrayList<Location> locations;
    private final SubjectUtility subjectUtility;

    public EntitiesParser(String entityFilename){
        subjectUtility = new SubjectUtility();
        locations = new ArrayList<>();
        try {
            Parser parser = new Parser();
            FileReader reader = new FileReader(entityFilename);
            parser.parse(reader);
            ArrayList<Graph> graphs = parser.getGraphs();
            ArrayList<Graph> subGraphs = graphs.get(0).getSubgraphs();
            parseGraphs(subGraphs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseGraphs(ArrayList<Graph> subGraphs)
            throws StagException {
        for(Graph g : subGraphs){
            String elementId = g.getId().getId();
            //if first element id is locations, need to create a new
            // instance of locations and store values
            //if it is paths, need to do something different
            if(elementId.equals("locations")) {
                parseLocation(g);
            }
            else if(!elementId.equals("paths")){
                throw new UnknownDataType(elementId);
            }
            ArrayList<Edge> edges = g.getEdges();
            parseEdges(edges);
        }
    }

    public ArrayList<Location> getLocations(){
        return locations;
    }

    //parses and stores the edges/paths
    private void parseEdges(ArrayList<Edge> edges)
            throws LocationDoesNotExist {
        for (Edge e : edges){
            //need to iterate through the stored locations
            //if the location we are on is equal to the source path,
            // need to store the target location in the object
            String source = e.getSource().getNode().getId().getId();
            String target = e.getTarget().getNode().getId().getId();
            source = source.toLowerCase(Locale.ROOT);
            target = target.toLowerCase(Locale.ROOT);
            storePath(source, target);
        }
    }

    //finds our target location (if it exists) and stores the path details
    private void storePath(String source, String target)
            throws LocationDoesNotExist {
        for(Location l : locations) {
            if (l.getName().equals(source)) {
                l.setPath(target);
                return;
            }
        }
        throw new LocationDoesNotExist(source);
    }

    //loops through graphs and parses each location
    private void parseLocation(Graph g) throws UnknownDataType {
        ArrayList<Graph> subGraphs1 = g.getSubgraphs();
        for (Graph g1 : subGraphs1) {
            //create new location and store in array
            Location currentLocation = new Location();
            ArrayList<Node> nodesLoc = g1.getNodes(false);
            Node nLoc = nodesLoc.get(0);
            //store name and description of location
            storeNameDescription(currentLocation, nLoc);
            ArrayList<Graph> subGraphs2 = g1.getSubgraphs();
            //parse inner graphs
            parseInnerGraphs(subGraphs2, currentLocation);
        }
    }

    //stores name and description of current location and stores current location in hashmap
    private void storeNameDescription(Location currentLocation, Node nLoc){
        String locationDescription = nLoc.getAttribute("description");
        String locationName = nLoc.getId().getId();
        currentLocation.setName(locationName.toLowerCase(Locale.ROOT));
        currentLocation.setDescription(
                locationDescription.toLowerCase(Locale.ROOT));
        locations.add(currentLocation);
    }

    //loops through inner graphs to parse
    private void parseInnerGraphs(ArrayList<Graph> subGraphs,
                                  Location currentLocation)
            throws UnknownDataType {
        for (Graph g2 : subGraphs) {
            String innerElementId = g2.getId().getId();
            ArrayList<Node> nodesEnt = g2.getNodes(false);
            parseNodes(nodesEnt, currentLocation, innerElementId);
        }
    }

    //loop through nodes and parse
    private void parseNodes(ArrayList<Node> nodesEnt,
                            Location currentLocation, String id)
            throws UnknownDataType {
        for (Node nEnt : nodesEnt) {
            String description = nEnt.getAttribute("description");
            String newId = nEnt.getId().getId();
            storeDetails(currentLocation,
                    id.toLowerCase(Locale.ROOT),
                    description.toLowerCase(Locale.ROOT),
                    newId.toLowerCase(Locale.ROOT));
        }
    }

    //stores description and id of artefact/furniture/characters in current location
    private void storeDetails(
            Location currentLocation, String dataType,
            String description, String id) throws UnknownDataType {
        if(dataType.equals("artefacts") ||
                dataType.equals("furniture") ||
                dataType.equals("characters")){
            Subject subject = new Subject(id, description, dataType);
            subjectUtility.setSubject(subject, currentLocation.getArtefacts());
        }else{
            throw new UnknownDataType(dataType);
        }
    }
}
