package org.n52.v3d.worldviz.featurenet.scene;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("Font")
public class Font{
    @XStreamImplicit
    protected List<SvgParameter> svgParameter;
    
    public List getSvgParameter(){
        return svgParameter;
    }
    
}