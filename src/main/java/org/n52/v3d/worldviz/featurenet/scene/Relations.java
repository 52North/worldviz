package org.n52.v3d.worldviz.featurenet.scene;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("Relations")
class Relations{
    @XStreamImplicit
    protected List<LineVisualizer> lineVisualizer;
    
    public List getLineVisualizer(){
        return lineVisualizer;
    }
    
}