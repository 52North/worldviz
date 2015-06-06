package org.n52.v3d.worldviz.featurenet.scene;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("TextVisualizer")
class TextVisualizer{
    @XStreamImplicit
    protected List<Label> label;
    @XStreamImplicit
    protected List<Font> font;
    @XStreamImplicit
    protected List<LabelPlacement> labelPlacement;
    @XStreamImplicit
    protected List<Fill> fill;
    
    public List getLabel(){
        return label;
    }
    
    public List getFont(){
        return font;
    }
    
    public List getLabelPlacement(){
        return labelPlacement;
    }
    
    public List getFill(){
        return fill;
    }
    
}