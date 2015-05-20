import java.io.BufferedReader;
import java.util.ArrayList;

public class Parse extends PrettyPrint{

    public static final String VERTEX_KEYWORD = "*Vertices";
    public static final String EDGE_KEYWORD = "*Edges";
    public static final char COMMENT_KEYWORD = '%';

    public static final String NUMBER_ERROR = "There should be a number here!";

    private static int countVertices = 0;
    private static int totalVertices = 0;

    private static boolean isVerticesOver = false;
    private static boolean isVerticesGoingOn = false;
    private static boolean isEdgesOver = false;
    private static boolean isEdgesGoingOn = false;

    public ArrayList<Vertex> vertexArrayList = new ArrayList<Vertex>();
    public ArrayList<Edge> edgeArrayList = new ArrayList<Edge>();


    public ArrayList getVertices(){
        return vertexArrayList;
    }

    public ArrayList getEdges(){
        return edgeArrayList;
    }

    public String format(String line){
        line = line.trim();
        line = line.replaceAll("\\s+", " "); // Remove extra spaces
        return line;
    }


    public boolean parseVertex(String line){
        line = format(line);
        if(isComment(line)){
            return true;
        }
        else {
            String[] parts = line.split(" ");
            if (parts.length == 5) {
                countVertices++; /*Increment count */
                boolean allCorrect = false;
                int vertexNumber = 0;
                String label = "";
                Double x, y, z;
                x = y = z = 0.0;
                try {
                    vertexNumber = Integer.parseInt(parts[0]);
                    if (vertexNumber == countVertices) {
                        label = parts[1];
                        if (label.charAt(0) == '"' && label.charAt(label.length() - 1) == '"') {
                            label = label.substring(1, label.length() - 1);
                            try {
                                x = Double.parseDouble(parts[2]);
                                y = Double.parseDouble(parts[3]);
                                z = Double.parseDouble(parts[4]);
                                allCorrect = true;
                            }
                            catch (NumberFormatException nfe) {
                                System.err.println(NUMBER_ERROR);
                            }
                        }
                    }
                } catch (NumberFormatException nfe) {
                    System.err.println(NUMBER_ERROR);
                    return false;
                }
                if (allCorrect) {
                    Vertex vertex = new Vertex(vertexNumber,label,x,y,z);
                    vertexArrayList.add(vertex);
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
                System.out.println("Currently supports only Number + label + Coordinates format");
                return false;
            }
        }
    }

    public boolean parseEdge(String line){
        line = format(line);
        if(isComment(line)){
            return true;
        }
        else {
            String[] parts = line.split(" ");
            if (parts.length == 3) {
                boolean allCorrect = false;
                int firstVertex,secondVertex,weight;
                firstVertex = secondVertex = weight = 0;
                try {
                    firstVertex = Integer.parseInt(parts[0]);
                    secondVertex = Integer.parseInt(parts[1]);
                    weight = Integer.parseInt(parts[2]);
                    allCorrect = true;
                }
                catch (NumberFormatException nfe) {
                    System.err.println(NUMBER_ERROR);
                    return false;
                }
                if (allCorrect) {
                    Edge edge = new Edge(firstVertex,secondVertex,weight);
                    edgeArrayList.add(edge);
                    return true;
                }
                else {
                    return false;
                }
            } else {
                System.err.println("Currently supports only Number + label + Coordinates format");
                return false;
            }
        }
    }


    public boolean isVertexDeclaration(String line){
        if(line.startsWith(VERTEX_KEYWORD)){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isVertexDeclarationSyntax(String line){
        if (line.contains(" ")) {
            line = format(line);
            String[] parts = line.split(" ");
            if(parts.length != 2){
                //Should contain *Vertices followed by a single integer
                System.err.println("Invalid syntax declaration for Vertices");
                return false;
            }
            else{
                try{
                    int vertices = Integer.parseInt(parts[1]);
                    totalVertices = vertices;
                    System.out.println("Expected vertices: "+totalVertices);
                }
                catch(NumberFormatException nfe) {
                    System.err.println(NUMBER_ERROR);
                    return false;
                }
                return true;
            }
        }
        else {
            System.err.println(line + " does not contain a space");
            return false;
        }
    }


    public boolean isEdgeDeclaration(String line){
        line = line.trim();
        if(line.startsWith(EDGE_KEYWORD) && line.length()==6){
            //If there are more characters than count(*Edges), it is definitely wrong!
            return true;
        }
        else{
            return false;
        }
    }


    public boolean isComment(String line){
        if(line.charAt(0) == COMMENT_KEYWORD){
            return true;
        }
        else{
            return false;
        }
    }


    public boolean readFile(BufferedReader bufferedReader){
        boolean value = true;
        try{
            for(String line; ((line = bufferedReader.readLine()) != null) && value; ) {

                if(line.trim().length() > 0){

                    if(isVerticesGoingOn && !isVerticesOver){ /* Vertices is going on*/
                        if(!parseVertex(line)){
                            System.err.println("Error!");
                            value = false;
                        }
                    }
                    else if(isEdgesGoingOn && !isEdgesOver){ /*Edges is going on*/
                        /*Currently, *Edges is the last set which is processed.
                         Once we have something more than that, this if statement has to be removed.
                          It will be added to the else statement below like the other conditions.
                          */
                        if(!parseEdge(line)){
                            System.err.println("Error!");
                            value = false;
                        }
                    }
                    else{
                        //Expecting a Comment or Vertex declaration or Edge declaration
                        if(isComment(line)){
                            //Do absolutely nothing!
                        }
                        else if(isVertexDeclaration(line)){
                            if(isVertexDeclarationSyntax(line)){ //See if it is of proper syntax!
                                isVerticesGoingOn = true;
                                isVerticesOver = false;
                            }
                            else{
                                System.err.println("Error!");
                                value = false;
                            }
                        }
                        else if(isEdgeDeclaration(line)){
                            isEdgesGoingOn = true;
                            isEdgesOver = false;
                        }
                        else{
                            //It's pure gibberish!
                            value = false;
                        }
                    }

                }
                else{
                    System.out.println("End of file");
                    value = false;
                }
            }
        }
        catch(Exception ex){
            System.err.println(ex.getMessage());
            return false;
        }
        return value;
    }


}
