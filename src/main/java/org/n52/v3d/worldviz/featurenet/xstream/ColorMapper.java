package org.n52.v3d.worldviz.featurenet.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("ColorMapper")
public class ColorMapper{
    
    @XStreamImplicit
    private List <InterpolationMode> interpolationMode;
    
    @XStreamImplicit
    private List<ColorPalette> colorPalette;
    
    
    public List getInterpolationMode(){
        return interpolationMode;
    }
    
    public List getColorPalette(){
        return colorPalette;
    }
    
}