package org.n52.v3d.worldviz.featurenet.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("ConnectionNet")
public class ConnectionNet{
    
    @XStreamImplicit
    protected List<Mapper> mapper;
    
    @XStreamImplicit
    protected List<BackgroundWorldMap> backgroundWorldMap;
    
    public List getMapper(){
        return mapper;
    }
    
    public List getBackgroundWorldMap(){
        return backgroundWorldMap;
    }
    
}