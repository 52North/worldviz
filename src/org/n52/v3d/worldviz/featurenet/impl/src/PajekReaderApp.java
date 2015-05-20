import java.util.ArrayList;

public class PajekReaderApp extends FileIO{

    public static void main(String args[]){
        PajekReaderApp pajekReaderApp = new PajekReaderApp();
        pajekReaderApp.init(args);
        if(pajekReaderApp.readFile(pajekReaderApp.getReader())){
            ArrayList<Edge> edgeArrayList = pajekReaderApp.getEdges();
            for(Edge edge:edgeArrayList){
                new Vector().getDirection(edge,pajekReaderApp.getVertices());
            }
            X3DScene x3DScene= new X3DScene(pajekReaderApp.getVertices(),pajekReaderApp.getEdges());
            x3DScene.writeToX3dFile("graph.html");
        }
    }

    public PajekReaderApp() {
        super();
    }

}
