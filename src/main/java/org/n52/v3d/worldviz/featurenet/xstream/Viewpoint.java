package org.n52.v3d.worldviz.featurenet.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


@XStreamAlias("Viewpoint")
public class Viewpoint{
    @XStreamAlias("position")
    @XStreamAsAttribute
    String position;

    @XStreamAlias("orientation")
    @XStreamAsAttribute
    String orientation;
    
    public String getPosition(){
        return position;
    }
    
    public String getOrientation(){
        return orientation;
    }
    
}