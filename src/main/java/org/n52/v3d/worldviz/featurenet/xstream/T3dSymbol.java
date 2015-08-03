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

    @XStreamAlias("normalGlow")
    @XStreamAsAttribute
    String normalGlow;

    @XStreamAlias("currentGlow")
    @XStreamAsAttribute
    String currentGlow;

    @XStreamAlias("highlightGlow")
    @XStreamAsAttribute
    String highlightGlow;

    
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
    
    public String getNormalGlow(){
        return normalGlow;
    }

    public String getCurrentGlow(){
        return currentGlow;
    }
    
    public String getHighlightGlow(){
        return highlightGlow;
    }
}