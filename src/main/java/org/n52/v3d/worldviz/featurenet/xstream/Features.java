package org.n52.v3d.worldviz.featurenet.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("Features")
public class Features{
    @XStreamImplicit
    protected List <PointVisualizer> pointVisualizer;
    @XStreamImplicit
    protected List <TextVisualizer> textVisualizer;

    public List getPointVisualizer(){
        return pointVisualizer;
    }
    
    public List getTextVisualizer(){
        return textVisualizer;
    }
    
}