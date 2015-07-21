package org.n52.v3d.worldviz.featurenet.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("WidthEntry")
public class WidthEntry{
    @XStreamAlias("InputValue")
    protected double InputValue = 0.;
    
    @XStreamAlias("OutputWidth")
    protected double OutputWidth = 0.;
    
    
    public double getInputValue(){
        return InputValue;
    }
    
    public double getOutputWidth(){
        return OutputWidth;
    }
    
}