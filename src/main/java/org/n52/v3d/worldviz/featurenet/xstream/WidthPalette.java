package org.n52.v3d.worldviz.featurenet.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("WidthPalette")
public class WidthPalette{
    @XStreamImplicit
    protected List<WidthEntry> widthEntry;
    
    public List getWidthEntry(){
        return widthEntry;
    }
    
}