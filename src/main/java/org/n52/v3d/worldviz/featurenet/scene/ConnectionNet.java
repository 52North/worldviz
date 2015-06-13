package org.n52.v3d.worldviz.featurenet.scene;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("ConnectionNet")
public class ConnectionNet{
    
    @XStreamImplicit
    protected List<Mapper> mapper;
    
    public List getMapper(){
        return mapper;
    }
    
}