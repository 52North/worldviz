package org.n52.v3d.worldviz.featurenet.impl;
import java.util.ArrayList;
import java.util.Arrays;

public class Vector extends Parse {
    private double dX,dY,dZ;
    private double angleX,angleY,angleZ;

    public void getDirection(Edge edge, ArrayList<Vertex> vertexArrayList){
        Vertex firstVertex = vertexArrayList.get(edge.firstVertex-1); //ArrayList starts from 1!
        Vertex secondVertex = vertexArrayList.get(edge.secondVertex-1);
        vectorDifference(firstVertex,secondVertex);
        normalizeVector(firstVertex,secondVertex);
        angleX = Math.acos(dX);
        angleY = Math.acos(dY);
        angleZ = Math.acos(dZ);
        System.out.println(edge.firstVertex+"-"+edge.secondVertex+": ");
        System.out.println("firstVertex: "+ Arrays.toString(firstVertex.getCoordinates()));
        System.out.println("secondVertex: "+Arrays.toString(secondVertex.getCoordinates()));
        System.out.println("Angle of edge: "+angleX+", "+angleY+", "+angleZ);
        System.out.println("Length of edge: "+length(firstVertex,secondVertex));
        System.out.println("");
    }

    public void vectorDifference(Vertex v1, Vertex v2) {
        dX = v1.getX() - v2.getX();
        dY = v1.getY() - v2.getY();
        dZ = v1.getZ() - v2.getZ();
    }

    public double length(Vertex firstVertex , Vertex secondVertex) {
        vectorDifference(firstVertex,secondVertex);
        return Math.sqrt(dX*dX + dY*dY + dZ*dZ);
    }

    public boolean normalizeVector(Vertex firstVector, Vertex secondVector) {
        double length = this.length(firstVector,secondVector);
        if (length == 0.0){
            return false;
        }
        else{
            dX /= length;
            dY /= length;
            dZ /= length;
            return true;
        }
    }

}
