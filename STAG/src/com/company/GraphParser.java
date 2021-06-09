package com.company;
import com.alexmerz.graphviz.*;
import com.alexmerz.graphviz.objects.*;
import com.company.Element.Location;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class GraphParser {

    private String entityFilename;
    private ArrayList<Location> locations;

    public GraphParser(String entityFilename){
        locations = new ArrayList<>();
        try {
            this.entityFilename = entityFilename;
            Parser parser = new Parser();
            FileReader reader = new FileReader(entityFilename);
            parser.parse(reader);
            ArrayList<Graph> graphs = parser.getGraphs();
            ArrayList<Graph> subGraphs = graphs.get(0).getSubgraphs();

            for(Graph g : subGraphs){
                System.out.printf("id1 = %s\n",g.getId().getId());
                String elementId = g.getId().getId();
                //if first element id is locations, need to create a new instance of locations and store values
                //if it is paths, need to do something different
                if(elementId.equals("locations")) {
                    //create new location and store in array
                    Location currentLocation = new Location();
                    locations.add(currentLocation);
                    ArrayList<Graph> subGraphs1 = g.getSubgraphs();
                    for (Graph g1 : subGraphs1) {
                        ArrayList<Node> nodesLoc = g1.getNodes(false);
                        Node nLoc = nodesLoc.get(0);
                        System.out.printf("\tid2 = %s, name = %s\n", g1.getId().getId(), nLoc.getId().getId());
                        //get name and cluster id and store in object
                        String clusterId = g1.getId().getId();
                        String clusterName = nLoc.getId().getId();
                        currentLocation.setName(clusterName);
                        ArrayList<Graph> subGraphs2 = g1.getSubgraphs();
                        for (Graph g2 : subGraphs2) {
                            System.out.printf("\t\tid3 = %s\n", g2.getId().getId());
                            String innerElementId = g2.getId().getId();
                            ArrayList<Node> nodesEnt = g2.getNodes(false);
                            switch (innerElementId) {
                                case "artefacts":
                                    for (Node nEnt : nodesEnt) {
                                        System.out.printf("\t\t\tid4 = %s, description = %s\n", nEnt.getId().getId(), nEnt.getAttribute("description"));
                                        String elementTypeId = nEnt.getId().getId();
                                        String elementTypeDescription = nEnt.getAttribute("description");
                                        currentLocation.setArtefact(elementTypeDescription, elementTypeId);
                                    }
                                    break;
                                case "characters":
                                    for (Node nEnt : nodesEnt) {
                                        System.out.printf("\t\t\tid4 = %s, description = %s\n", nEnt.getId().getId(), nEnt.getAttribute("description"));
                                        String elementTypeId = nEnt.getId().getId();
                                        String elementTypeDescription = nEnt.getAttribute("description");
                                        currentLocation.setCharacter(elementTypeDescription, elementTypeId);
                                    }
                                    break;
                                case "furniture":
                                    for (Node nEnt : nodesEnt) {
                                        System.out.printf("\t\t\tid4 = %s, description = %s\n", nEnt.getId().getId(), nEnt.getAttribute("description"));
                                        String elementTypeId = nEnt.getId().getId();
                                        String elementTypeDescription = nEnt.getAttribute("description");
                                        currentLocation.setFurniture(elementTypeDescription, elementTypeId);
                                    }
                                    break;
                            }
                        }
                    }
                }
                ArrayList<Edge> edges = g.getEdges();
                for (Edge e : edges){
                    System.out.printf("Path from %s to %s\n", e.getSource().getNode().getId().getId(), e.getTarget().getNode().getId().getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
