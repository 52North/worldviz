package org.n52.v3d.worldviz.featurenet.scene;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/*It seems to be a bad idea to have empty elements in the XML file
  with respect to XStream, therefore I've altered this part of the schema */ 


@XStreamAlias("Geometry")
public class Geometry {
    @XStreamAlias("type")
    @XStreamAsAttribute
    String type;

    public String getType(){
        return type;
    }
    
}
