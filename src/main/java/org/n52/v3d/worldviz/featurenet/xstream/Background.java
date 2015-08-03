package org.n52.v3d.worldviz.featurenet.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


@XStreamAlias("Background")
public class Background{
    @XStreamAlias("skyColor")
    @XStreamAsAttribute
    String skyColor;

    
    public String getSkyColor(){
        return skyColor;
    }
}