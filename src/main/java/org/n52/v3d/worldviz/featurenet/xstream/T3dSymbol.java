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
        
    @XStreamAlias("normalColor")
    @XStreamAsAttribute
    String normalColor;
    
    @XStreamAlias("currentColor")
    @XStreamAsAttribute
    String currentColor;

    @XStreamAlias("highlightColor")
    @XStreamAsAttribute
    String highlightColor;
    
    public String getType(){
        return type;
    }
    
    public double getSize(){
        return size;
    }

    public String getNormalColor(){
        return normalColor;
    }
    
    public String getCurrentColor(){
        return currentColor;
    }
    
    public String getHighlightColor(){
        return highlightColor;
    }

}