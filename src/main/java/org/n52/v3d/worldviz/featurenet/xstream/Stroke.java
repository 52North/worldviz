package org.n52.v3d.worldviz.featurenet.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("Stroke")
public class Stroke{
    @XStreamImplicit
    private List<SvgParameter> svgParameter;
    
    public List getSvgParameter(){
        return svgParameter;
    }
    
}