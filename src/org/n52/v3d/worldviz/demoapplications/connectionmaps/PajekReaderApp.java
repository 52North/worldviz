package org.n52.v3d.worldviz.demoapplications.connectionmaps;
import java.util.ArrayList;

import org.n52.v3d.worldviz.featurenet.impl.Edge;
import org.n52.v3d.worldviz.featurenet.impl.FileIO;
import org.n52.v3d.worldviz.featurenet.impl.Vector;
import org.n52.v3d.worldviz.featurenet.impl.X3DScene;

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
            System.out.println("done");
        }
    }

    public PajekReaderApp() {
        super();
    }

}
