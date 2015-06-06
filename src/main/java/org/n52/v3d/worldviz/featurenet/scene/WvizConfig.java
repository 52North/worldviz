package org.n52.v3d.worldviz.featurenet.scene;

/**
 * A <tt>WvizConfig</tt> object holds the complete description of presentation 
 * parameters needed to set-up WorldViz scenes.
 * 
 * @author Adhitya Kamakshidasan, Benno Schmidt
 * 
 * @see VgFeatureNet
 * @see WvizVirtualConnectionMapScene 
 */


/*Almost all elements of the XML file have been represented as List objects in Java
  In case, we would like to extend the same XML file in future, for more than one Feature or Relation,
  most of the implementation would still remain the same, and very little would have to be changed.
*/


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.io.File;
import java.util.List;


@XStreamAlias("WvizConfig")
public class WvizConfig {
    
    @XStreamImplicit
    protected List<ConnectionNet> connectionNet;

    public List getConnectionNet(){
        return connectionNet;
    }
    
    
    public WvizConfig getWvizConfig(String xml){
        File file = new File(xml);
        XStream xStream = new XStream();
        xStream.processAnnotations(WvizConfig.class);
        WvizConfig wVizConfig = (WvizConfig) xStream.fromXML(file);       
        return wVizConfig;
    }


    public static void main(String args[]){
        
        //Let's test it out!
        
        WvizConfig wvizConfig = new WvizConfig();
        wvizConfig = wvizConfig.getWvizConfig(args[0]);
        
        //System.out.print("\n\n" + xStream.toXML(wVizConfig));
        
        //We will use 0, for default configuration
        ConnectionNet connectionNet = (ConnectionNet) wvizConfig.getConnectionNet().get(0);
        Mapper mapper = (Mapper) connectionNet.getMapper().get(0);
        Features features = (Features) mapper.getFeatures().get(0);
        PointVisualizer pointVisualizer = (PointVisualizer) features.getPointVisualizer().get(0);
        T3dSymbol t3dSymbol = (T3dSymbol) pointVisualizer.getT3dSymbol().get(0);
        String symbolType = t3dSymbol.getType();
        
        System.out.println(symbolType);
        
    }
}
