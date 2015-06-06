package org.n52.v3d.worldviz.featurenet.scene;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("Stroke")
class Stroke{
    @XStreamImplicit
    private List<SvgParameter> svgParameter;
}