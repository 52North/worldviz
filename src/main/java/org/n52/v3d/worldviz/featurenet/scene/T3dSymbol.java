package org.n52.v3d.worldviz.featurenet.scene;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


@XStreamAlias("T3dSymbol")
class T3dSymbol{
    @XStreamAlias("type")
    @XStreamAsAttribute
    String type;
    
    @XStreamAlias("size")
    @XStreamAsAttribute
    String size;
        
    @XStreamAlias("color")
    @XStreamAsAttribute
    String color;
    
    public String getType(){
        return type;
    }
    
    public String getSize(){
        return size;
    }
    
    public String getColor(){
        return color;
    }
    
    
}