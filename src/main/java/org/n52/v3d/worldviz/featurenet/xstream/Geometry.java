package org.n52.v3d.worldviz.featurenet.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

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
    
    @XStreamAlias("HeightRatio")
    double HeightRatio = 15.0;
    
    @XStreamAlias("RibbonStep")
    double RibbonStep = 0.1;
    
    @XStreamAlias("RadiusRatio")
    double RadiusRatio = 2.5;

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
    
    public double getHeightRatio(){
        return HeightRatio;
    }
    
    public double getRibbonStep(){
        return RibbonStep;
    }
    
    public double getRadiusRatio(){
        return RadiusRatio;
    }
    
}
