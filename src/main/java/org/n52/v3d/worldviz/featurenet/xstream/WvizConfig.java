package org.n52.v3d.worldviz.featurenet.xstream;

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

import org.n52.v3d.worldviz.helper.RelativePaths;

@XStreamAlias("WvizConfig")
public class WvizConfig {

    private WvizConfig wvizConfig;

    @XStreamImplicit
    protected List<Background> background;
    
    @XStreamImplicit
    protected List<Viewpoint> viewpoint;
    
    @XStreamImplicit
    protected List<ConnectionNet> connectionNet;

    public List getBackground() {
        return background;
    }
    
    public List getViewpoint() {
        return viewpoint;
    }
    
    public List getConnectionNet() {
        return connectionNet;
    }

    public WvizConfig(String xml) {
        File file = new File(xml);
        XStream xStream = new XStream();
        xStream.processAnnotations(WvizConfig.class);
        wvizConfig = (WvizConfig) xStream.fromXML(file);
    }

    public WvizConfig getConfiguration() {
        return wvizConfig;
    }

    //Adhitya: Christian could check the last line of the output log? 
    public static void main(String args[]) {
        XStream xStream = new XStream();
        xStream.processAnnotations(WvizConfig.class);
        System.out.println(xStream.toXML((new WvizConfig(RelativePaths.STYLE_CONFIGURATION_XML)).getConfiguration()));
    }

}
