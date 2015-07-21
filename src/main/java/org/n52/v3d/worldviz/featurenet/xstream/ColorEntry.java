package org.n52.v3d.worldviz.featurenet.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("ColorEntry")
public class ColorEntry{
    @XStreamAlias("InputValue")
    protected double InputValue = 0.;
    
    @XStreamImplicit
    protected List<OutputColor> outputColor;
    
    
    public double getInputValue(){
        return InputValue;
    }
    
    public List getOutputColor(){
        return outputColor;
    }
    
}