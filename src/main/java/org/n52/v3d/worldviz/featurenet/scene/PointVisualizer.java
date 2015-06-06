package org.n52.v3d.worldviz.featurenet.scene;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("PointVisualizer")
class PointVisualizer{
    @XStreamImplicit
    protected List<T3dSymbol> t3dsymbol;
    
    public List getT3dSymbol(){
        return t3dsymbol;
    }
    
}