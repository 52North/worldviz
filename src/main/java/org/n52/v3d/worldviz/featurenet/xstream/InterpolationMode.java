package org.n52.v3d.worldviz.featurenet.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("InterpolationMode")
public class InterpolationMode {
    @XStreamAlias("type")
    @XStreamAsAttribute
    String type;

    public String getType(){
        return type;
    }
    
}
