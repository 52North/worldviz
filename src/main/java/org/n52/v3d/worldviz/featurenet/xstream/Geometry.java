package org.n52.v3d.worldviz.featurenet.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/*It seems to be a bad idea to have empty elements in the XML file
  with respect to XStream, therefore I've (Adhitya) altered this part of the schema */ 


@XStreamAlias("Geometry")
public class Geometry {
    @XStreamAlias("type")
    String type;
    
    @XStreamAlias("CreaseAngle")
    double CreaseAngle = 0.785;
    
    @XStreamAlias("HelixTurns")
    int HelixTurns = 24;
    
    @XStreamAlias("EllipseTurns")
    int EllipseTurns = 3;

    @XStreamAlias("CircleTurns")
    int CircleTurns = 24;
    
    @XStreamAlias("Ratio")
    double Ratio = 1.5;
    
    @XStreamAlias("ConeHeight")
    double ConeHeight = 0.1;
    
    @XStreamAlias("RibbonStep")
    double RibbonStep = 0.1;
    
    public String getType(){
        return type;
    }
    
    public double getCreaseAngle(){
        return CreaseAngle;
    }
    
    public int getCircleTurns(){
        return CircleTurns;
    }
    
    public int getHelixTurns(){
        return HelixTurns;
    }
    
    public int getEllipseTurns(){
        return EllipseTurns;
    }
    
    public double getRatio(){
        return Ratio;
    }
    
    public double getConeHeight(){
        return ConeHeight;
    }
    
    public double getRibbonStep(){
        return RibbonStep;
    }
}
