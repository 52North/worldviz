package org.n52.v3d.worldviz.featurenet.scene;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("Displacement")
class Displacement{
    @XStreamAlias("DisplacementX")
    protected double DisplacementX;
    @XStreamAlias("DisplacementY")
    protected double DisplacementY;
    
    public double getDisplacementX(){
        return DisplacementX;
    }
    
    public double getDisplacementY(){
        return DisplacementY;
    }
    
}