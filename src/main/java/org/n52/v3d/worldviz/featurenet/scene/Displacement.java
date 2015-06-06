package org.n52.v3d.worldviz.featurenet.scene;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("Displacement")
class Displacement{
    @XStreamAlias("DisplacementX")
    protected int DisplacementX;
    @XStreamAlias("DisplacementY")
    protected int DisplacementY;
    
    public int getDisplacementX(){
        return DisplacementX;
    }
    
    public int getDisplacementY(){
        return DisplacementY;
    }
    
}