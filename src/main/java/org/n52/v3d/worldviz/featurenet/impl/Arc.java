package org.n52.v3d.worldviz.featurenet.impl;
public class Arc{

    protected int firstVertex,secondVertex,weight;

    Arc(int firstVertex, int secondVertex, int weight){
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
