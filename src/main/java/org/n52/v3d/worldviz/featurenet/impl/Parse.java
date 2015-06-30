package org.n52.v3d.worldviz.featurenet.impl;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parse extends PrettyPrint {
    
    public static final Logger logger = LoggerFactory.getLogger(Parse.class);

    public static final String VERTEX_KEYWORD = "*Vertices";
    public static final String EDGE_KEYWORD = "*Edges";
    public static final String ARC_KEYWORD = "*Arcs";
    public static final char COMMENT_KEYWORD = '%';

    public static final String NUMBER_ERROR = "There should be a number here!";

    private static int countVertices = 0;
    private static int totalVertices = 0;

    private static boolean isVerticesOver = false;
    private static boolean isVerticesGoingOn = false;
    private static boolean isEdgesGoingOn = false;
    private static boolean isArcsGoingOn = false;

    public ArrayList<Vertex> vertexArrayList = new ArrayList<Vertex>();
    public ArrayList<Edge> edgeArrayList = new ArrayList<Edge>();
    public ArrayList<Arc> arcArrayList = new ArrayList<Arc>();

    public ArrayList getVertices() {
        return vertexArrayList;
    }

    public ArrayList getEdges() {
        return edgeArrayList;
    }

    public ArrayList getArcs() {
        return arcArrayList;
    }

    public String format(String line) {
        line = line.trim();
        line = line.replaceAll("\\s+", " "); // Remove extra spaces
        return line;
    }

    public boolean parseVertex(String line) {
        line = format(line);
        if (isComment(line)) {
            return true;
        }
        else {
            List<String> parts = new ArrayList<String>();
            
            //This is used to remove quotes from the Line processed
            Matcher matcher = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(line);
            while (matcher.find()) {
                parts.add(matcher.group(1).replace("\"", ""));
            }
            if (parts.size() >= 4) {
                countVertices++; /*Increment count */

                boolean allCorrect = false;
                int vertexNumber = 0;
                String label = "";
                Double x, y, z;
                x = y = z = 0.0;
                try {
                    vertexNumber = Integer.parseInt(parts.get(0));
                    if (vertexNumber == countVertices) {
                        label = parts.get(1);
                        try {
                            x = Double.parseDouble(parts.get(2));
                            y = Double.parseDouble(parts.get(3));
                            if (parts.size() == 5) {
                                z = Double.parseDouble(parts.get(4));
                            }
                            allCorrect = true;
                        }
                        catch (NumberFormatException nfe) {
                            logger.error(NUMBER_ERROR);
                        }
                    }
                }
                catch (NumberFormatException nfe) {
                    logger.error(NUMBER_ERROR);
                    return false;
                }
                if (allCorrect) {
                    Vertex vertex = new Vertex(vertexNumber, label, x, y, z);
                    vertexArrayList.add(vertex);
                    logger.info("Vertex Parsed");
                    if (countVertices == totalVertices) {
                        /*All vertices mentioned have been parsed*/
                        isVerticesOver = true;
                        isVerticesGoingOn = false;
                    }
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                logger.error("Currently supports only Number + label + Coordinates format");
                return false;
            }
        }
    }

    public boolean parseEdge(String line) {
        line = format(line);
        if (isComment(line)) {
            return true;
        }
        else {
            String[] parts = line.split(" ");
            if (parts.length >= 2) {
                boolean allCorrect = false;
                int firstVertex, secondVertex, weight;
                firstVertex = secondVertex = weight = 0;
                try {
                    firstVertex = Integer.parseInt(parts[0]);
                    secondVertex = Integer.parseInt(parts[1]);
                    if(parts.length == 3){
                        weight = Integer.parseInt(parts[2]);
                    }
                    allCorrect = true;
                }
                catch (NumberFormatException nfe) {
                    logger.error(NUMBER_ERROR);
                    return false;
                }
                if (allCorrect) {
                    Edge edge = new Edge(firstVertex, secondVertex, weight);
                    edgeArrayList.add(edge);
                    logger.info("Edge Parsed");
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                logger.error("Currently supports only Node + Node +/- Weight");
                return false;
            }
        }
    }

    //ParseArc is exactly the same as parseEdge, with few minor changes
    public boolean parseArc(String line) {
        line = format(line);
        if (isComment(line)) {
            return true;
        }
        else {
            String[] parts = line.split(" ");
            if (parts.length >= 2) {
                boolean allCorrect = false;
                int firstVertex, secondVertex, weight;
                firstVertex = secondVertex = weight = 0;
                try {
                    firstVertex = Integer.parseInt(parts[0]);
                    secondVertex = Integer.parseInt(parts[1]);
                    if(parts.length == 3){
                        weight = Integer.parseInt(parts[2]);
                    }
                    allCorrect = true;
                }
                catch (NumberFormatException nfe) {
                    System.err.println(NUMBER_ERROR);
                    return false;
                }
                if (allCorrect) {
                    Arc arc = new Arc(firstVertex, secondVertex, weight);
                    arcArrayList.add(arc);
                    logger.info("Arc Parsed");
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                logger.error("Currently supports only Node + Node +/- Weight");
                return false;
            }
        }
    }

    public boolean isVertexDeclaration(String line) {
        return line.startsWith(VERTEX_KEYWORD);
    }

    public boolean isVertexDeclarationSyntax(String line) {
        if (line.contains(" ")) {
            line = format(line);
            String[] parts = line.split(" ");
            if (parts.length != 2) {
                //Should contain *Vertices followed by a single integer
                logger.error("Invalid syntax declaration for Vertices");
                return false;
            }
            else {
                try {
                    int vertices = Integer.parseInt(parts[1]);
                    totalVertices = vertices;
                    logger.info("Expected vertices: " + totalVertices);
                }
                catch (NumberFormatException nfe) {
                    System.err.println(NUMBER_ERROR);
                    return false;
                }
                return true;
            }
        }
        else {
            logger.error(line + " does not contain a space");
            return false;
        }
    }

    public boolean isEdgeDeclaration(String line) {
        line = line.trim();
        if (line.startsWith(EDGE_KEYWORD) && line.length() == 6) {
            //If there are more characters than count(*Edges), it is definitely wrong!
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isArcDeclaration(String line) {
        line = line.trim();
        return line.startsWith(ARC_KEYWORD) && line.length() == 5; //If there are more characters than count(*Arcs), it is definitely wrong!
    }

    public boolean isComment(String line) {
        return line.charAt(0) == COMMENT_KEYWORD;
    }

    public boolean readFile(BufferedReader bufferedReader) throws PajekException{
        boolean value = true;
        try {
            for (String line; ((line = bufferedReader.readLine()) != null) && value;) {

                if (line.trim().length() > 0) {

                    if (isVerticesGoingOn && !isVerticesOver) { /* Vertices is going on*/

                        if (!parseVertex(line)) {
                            value = false;
                        }
                    }
                    else {
                        //Expecting a Comment or Vertex declaration or Edge or Arc declaration
                        if (isComment(line)) {
                            logger.info("Comment encountered");
                            //Do absolutely nothing!
                        }
                        else if (isVertexDeclaration(line)) {
                            if (isVertexDeclarationSyntax(line)) { //See if it is of proper syntax!
                                logger.info("Vertex Syntax Declaration statement encountered ");
                                isVerticesGoingOn = true;
                                isVerticesOver = false;
                            }
                            else {
                                value = false;
                            }
                        }
                        else if (isEdgeDeclaration(line)) {
                            logger.info("Edge Syntax Declaration statement encountered ");
                            isEdgesGoingOn = true;
                            isArcsGoingOn = false;
                        }
                        else if (isArcDeclaration(line)) {
                            logger.info("Arcs Syntax Declaration statement encountered ");
                            isArcsGoingOn = true;
                            isEdgesGoingOn = false;
                        }
                        else if (isEdgesGoingOn) { /*Edges is going on*/
                            if (!parseEdge(line)) {
                                logger.error("Error!");
                                value = false;
                            }
                        }
                        else if (isArcsGoingOn) {
                            if (!parseArc(line)) {
                                logger.error("Error!");
                                value = false;
                            }
                        }
                        else {
                            logger.error("What you have written is pure gibberish! :D ");
                            value = false;
                        }
                    }

                }
                else {
                    logger.info("End of file");
                    value = false;
                }
            }
        }
        catch (Exception ex) {
            logger.info(ex.getMessage());
            value = false;
        }
        if(!value){
            throw new PajekException();
        }
        return value;
    }

    public static class PajekException extends Exception {
        public PajekException() {
            logger.error("Check your Pajek file");
        }
    }

}
