package org.n52.v3d.worldviz.featurenet.scene;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("PointPlacement")
class PointPlacement{
    @XStreamImplicit
    protected List<Displacement> displacement;
    
    public List getDisplacement(){
        return displacement;
    }
    
}