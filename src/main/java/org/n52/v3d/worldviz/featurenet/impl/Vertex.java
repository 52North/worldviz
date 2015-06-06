package org.n52.v3d.worldviz.featurenet.impl;
public class Vertex {

    protected int vertexNumber;
    protected String label;
    protected Double x,y,z;

    Vertex(int vertexNumber, String label, Double x, Double y,Double z){
        this.vertexNumber = vertexNumber;
        this.label = label;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getVertexNumber(){
        return vertexNumber;
    }

    public String getLabel(){
        return label;
    }

    public Double[] getCoordinates(){
        Double[] coordinates = {x,y,z};
        return coordinates;
    }

    public Double getX(){
        return x;
    }

    public Double getY(){
        return y;
    }

    public Double getZ(){
        return z;
    }

}
