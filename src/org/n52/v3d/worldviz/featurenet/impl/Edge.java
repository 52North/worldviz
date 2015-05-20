package org.n52.v3d.worldviz.featurenet.impl;
public class Edge {

    protected int firstVertex,secondVertex,weight;

    Edge(int firstVertex, int secondVertex, int weight){
        this.firstVertex = firstVertex;
        this.secondVertex = secondVertex;
        this.weight = weight;
    }

    public int getFirstVertex(){
        return firstVertex;
    }

    public int getSecondVertex (){
        return secondVertex;
    }

    public int getWeight(){
        return weight;
    }

}
