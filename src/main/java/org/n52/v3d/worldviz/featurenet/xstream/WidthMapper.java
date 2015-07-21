package org.n52.v3d.worldviz.featurenet.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("WidthMapper")
public class WidthMapper{
    
    @XStreamImplicit
    private List <InterpolationMode> interpolationMode;
    
    @XStreamImplicit
    private List<WidthPalette> widthPalette;
    
    
    public List getInterpolationMode(){
        return interpolationMode;
    }
    
    public List getWidthPalette(){
        return widthPalette;
    }
    
}