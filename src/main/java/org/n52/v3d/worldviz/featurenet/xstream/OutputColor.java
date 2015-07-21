package org.n52.v3d.worldviz.featurenet.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

@XStreamAlias("OutputColor")
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"value"})
public class OutputColor{
    @XStreamAlias("type")
    @XStreamAsAttribute
    protected String type;
    protected String value;
    
    public String getType(){
        return type;
    }
    
    public String getValue(){
        return value;
    }
    
}