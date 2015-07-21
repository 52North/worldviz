package org.n52.v3d.worldviz.featurenet.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("LineVisualizer")
public class LineVisualizer{
    
    //Adhitya: How do we change Geometry in the XML file?
    @XStreamImplicit
    private List <Geometry> geometry;
    
    @XStreamImplicit
    private List <Stroke> stroke;
    
    @XStreamImplicit
    private List <ColorMapper> colorMapper;
    
    @XStreamImplicit
    private List <WidthMapper> widthMapper;
    
    public List getGeometry(){
        return geometry;
    }
    
    public List getStroke(){
        return stroke;
    }
    
    public List getColorMapper(){
        return colorMapper;
    }
    
    public List getWidthMapper(){
        return widthMapper;
    }
    
}