package org.n52.v3d.worldviz.featurenet.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("PointVisualizer")
public class PointVisualizer{
    @XStreamImplicit
    protected List<T3dSymbol> t3dsymbol;
    
    public List getT3dSymbol(){
        return t3dsymbol;
    }
    
}