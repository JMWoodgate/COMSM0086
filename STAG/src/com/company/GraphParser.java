package com.company;
import com.alexmerz.graphviz.*;
import com.alexmerz.graphviz.objects.*;
import com.company.Element.Location;
import com.company.StagExceptions.DataTypeException;

import javax.xml.crypto.Data;
import java.io.FileReader;
import java.util.ArrayList;

public class GraphParser {

    private String entityFilename;
    private final ArrayList<Location> locations;

    public GraphParser(String entityFilename){
        locations = new ArrayList<>();
        try {
            this.entityFilename = entityFilename;
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

    private void parseGraphs(ArrayList<Graph> subGraphs) throws DataTypeException {
        for(Graph g : subGraphs){
            System.out.printf("id1 = %s\n",g.getId().getId());
            String elementId = g.getId().getId();
            //if first element id is locations, need to create a new
            // instance of locations and store values
            //if it is paths, need to do something different
            if(elementId.equals("locations")) {
                parseLocation(g);
            }
            else if(!elementId.equals("paths")){
                throw new DataTypeException(elementId);
            }
            ArrayList<Edge> edges = g.getEdges();
            parseEdges(edges);
        }
    }

    private void parseEdges(ArrayList<Edge> edges){
        for (Edge e : edges){
            System.out.printf("Path from %s to %s\n",
                    e.getSource().getNode().getId().getId(),
                    e.getTarget().getNode().getId().getId());
            //need to iterate through the stored locations
            //if the location we are on is equal to the source path,
            // need to store the target location in the object
            String source = e.getSource().getNode().getId().getId();
            String target = e.getTarget().getNode().getId().getId();
            storePath(source, target);
        }
    }

    //loops through our list of locations and stores the path details
    private void storePath(String source, String target){
        for(Location l : locations){
            if(l.getName().equals(source)){
                l.setPath(target);
            }
        }
    }

    //loops through graphs and parses each location
    private void parseLocation(Graph g) throws DataTypeException{
        ArrayList<Graph> subGraphs1 = g.getSubgraphs();
        for (Graph g1 : subGraphs1) {
            //create new location and store in array -- this is overwriting each time so need to change
            Location currentLocation = new Location();
            locations.add(currentLocation);
            ArrayList<Node> nodesLoc = g1.getNodes(false);
            Node nLoc = nodesLoc.get(0);
            System.out.printf("\tid2 = %s, name = %s, description = %s\n", g1.getId().getId(), nLoc.getId().getId(), nLoc.getAttribute("description"));
            //store name and description of location
            storeNameDescription(currentLocation, nLoc);
            ArrayList<Graph> subGraphs2 = g1.getSubgraphs();
            //parse inner graphs
            parseInnerGraphs(subGraphs2, currentLocation);
        }
    }

    //stores name and description of current location
    private void storeNameDescription(Location currentLocation, Node nLoc){
        String locationDescription = nLoc.getAttribute("description");
        String clusterName = nLoc.getId().getId();
        currentLocation.setName(clusterName);
        currentLocation.setDescription(locationDescription);
    }

    //loops through inner graphs to parse
    private void parseInnerGraphs(ArrayList<Graph> subGraphs, Location currentLocation) throws DataTypeException{
        for (Graph g2 : subGraphs) {
            System.out.printf("\t\tid3 = %s\n", g2.getId().getId());
            String innerElementId = g2.getId().getId();
            ArrayList<Node> nodesEnt = g2.getNodes(false);
            parseNodes(nodesEnt, currentLocation, innerElementId);
        }
    }

    //loop through nodes and parse
    private void parseNodes(ArrayList<Node> nodesEnt, Location currentLocation, String id) throws DataTypeException {
        for (Node nEnt : nodesEnt) {
            System.out.printf("\t\t\tid4 = %s, description = %s\n", nEnt.getId().getId(), nEnt.getAttribute("description"));
            storeDetails(currentLocation, id, nEnt.getAttribute("description"), nEnt.getId().getId());
        }
    }

    //stores description and id of artefact/furniture/characters in current location
    private void storeDetails(Location currentLocation, String dataType, String description, String id) throws DataTypeException {
        switch(dataType){
            case "artefacts":
                currentLocation.setArtefact(description, id);
                break;
            case "furniture":
                currentLocation.setFurniture(description, id);
                break;
            case "characters":
                currentLocation.setCharacter(description, id);
                break;
            default:
                throw new DataTypeException(dataType);
        }
    }
}
