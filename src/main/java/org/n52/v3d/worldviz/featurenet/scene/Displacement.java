package org.n52.v3d.worldviz.featurenet.scene;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("Displacement")
public class Displacement{
    @XStreamAlias("DisplacementX")
    protected double DisplacementX = 0.;
    @XStreamAlias("DisplacementY")
    protected double DisplacementY = 0.;
    
    public double getDisplacementX(){
        return DisplacementX;
    }
    
    public double getDisplacementY(){
        return DisplacementY;
    }
    
}