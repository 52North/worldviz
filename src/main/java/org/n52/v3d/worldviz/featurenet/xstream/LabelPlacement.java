package org.n52.v3d.worldviz.featurenet.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("LabelPlacement")
public class LabelPlacement{
    @XStreamImplicit
    protected List<PointPlacement> pointPlacement;
    
    @XStreamAlias("BillboardAxis")
    String BillboardAxis;
    
    public List getPointPlacement(){
        return pointPlacement;
    }
    
    public String getBillboardAxis(){
        return BillboardAxis;
    }
    
}