package org.n52.v3d.worldviz.featurenet.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("Label")
public class Label{
    @XStreamAlias("PropertyName")
    private String PropertyName;
    
    public String getPropertyName(){
        return PropertyName;
    }
    
}