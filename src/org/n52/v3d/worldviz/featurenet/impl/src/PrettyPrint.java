import java.util.ArrayList;

public class PrettyPrint{
    public void printVertices(ArrayList<Vertex> vertexArrayList){
        for(Vertex vertex:vertexArrayList){
            System.out.println("VertexNumber: " + vertex.vertexNumber + " Label: " + vertex.label + " Coordinates: (" + vertex.x + ", " + vertex.y + ", " + vertex.z + ")");
        }
    }

    public void printEdges(ArrayList<Edge> edgeArrayList){
        for(Edge edge:edgeArrayList){
            System.out.println("firstVertex: " + edge.firstVertex + " secondVertex: " + edge.secondVertex + " Weight: " + edge.weight);
        }
    }

    public void printGraph(ArrayList<Vertex> vertexArrayList,ArrayList<Edge> edgeArrayList){
        printVertices(vertexArrayList);
        printEdges(edgeArrayList);
    }
}
