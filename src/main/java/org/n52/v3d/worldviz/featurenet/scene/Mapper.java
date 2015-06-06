package org.n52.v3d.worldviz.featurenet.scene;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("Mapper")
class Mapper {
    @XStreamImplicit
    protected List <Features> features;
    @XStreamImplicit
    protected List <Relations> relations;
    
    public List getFeatures(){
        return features;
    }
    
    public List getRelations(){
        return relations;
    }
    
}
