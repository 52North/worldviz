package org.n52.v3d.worldviz.featurenet.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


@XStreamAlias("T3dSymbol")
public class T3dSymbol{
    @XStreamAlias("type")
    @XStreamAsAttribute
    String type;
    
    @XStreamAlias("size")
    @XStreamAsAttribute
    double size;
        
    @XStreamAlias("color")
    @XStreamAsAttribute
    String color;
    
    public String getType(){
        return type;
    }
    
    public double getSize(){
        return size;
    }
    
    public String getColor(){
        return color;
    }
    
    
}