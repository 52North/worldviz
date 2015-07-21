package org.n52.v3d.worldviz.featurenet.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("ColorPalette")
public class ColorPalette{
    @XStreamImplicit
    protected List<ColorEntry> colorEntry;
    
    public List getColorEntry(){
        return colorEntry;
    }
    
}