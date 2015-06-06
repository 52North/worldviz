package org.n52.v3d.worldviz.featurenet.scene;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

@XStreamAlias("SvgParameter")
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"value"})
class SvgParameter{
    @XStreamAlias("name")
    @XStreamAsAttribute
    protected String name;
    protected String value;
    
    public String getName(){
        return name;
    }
    
    public String getValue(){
        return value;
    }
    
}