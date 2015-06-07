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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XStreamAlias("WvizConfig")
public class WvizConfig {

    private WvizConfig wvizConfig;

    @XStreamImplicit
    protected List<ConnectionNet> connectionNet;

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

    private static void testConfiguration(WvizConfig wvizConfig) {

        String symbolType, symbolSize, symbolColor, propertyName, geometryType;

        Map<String, String> svgMap = new HashMap<String, String>(); // Cannot be viable once there are more properties

        double displacementX, displacementY;

        //We will use 0, for default configuration
        //For specific configuration, we should change our current XML schema
        ConnectionNet connectionNet = (ConnectionNet) wvizConfig.getConnectionNet().get(0);
        Mapper mapper = (Mapper) connectionNet.getMapper().get(0);
        Features features = (Features) mapper.getFeatures().get(0);
        PointVisualizer pointVisualizer = (PointVisualizer) features.getPointVisualizer().get(0);
        T3dSymbol t3dSymbol = (T3dSymbol) pointVisualizer.getT3dSymbol().get(0);
        symbolType = t3dSymbol.getType();
        symbolSize = t3dSymbol.getSize();
        symbolColor = t3dSymbol.getColor();
        TextVisualizer textVisualizer = (TextVisualizer) features.getTextVisualizer().get(0);
        Label label = (Label) textVisualizer.getLabel().get(0);
        propertyName = label.PropertyName;

        Font font = (Font) textVisualizer.getFont().get(0);
        List svgParameter = font.getSvgParameter();

        for (Object sp : svgParameter) {
            String name = ((SvgParameter) sp).getName();
            String value = ((SvgParameter) sp).getValue();
            svgMap.put(name, value);
        }

        LabelPlacement labelPlacement = (LabelPlacement) textVisualizer.getLabelPlacement().get(0);
        PointPlacement pointPlacement = (PointPlacement) labelPlacement.getPointPlacement().get(0);
        Displacement displacement = (Displacement) pointPlacement.getDisplacement().get(0);

        displacementX = displacement.getDisplacementX();
        displacementY = displacement.getDisplacementY();

        Fill fill = (Fill) textVisualizer.getFill().get(0);
        svgParameter = fill.getSvgParameter();

        for (Object sp : svgParameter) {
            String name = ((SvgParameter) sp).getName();
            String value = ((SvgParameter) sp).getValue();
            svgMap.put(name, value);
        }

        Relations relations = (Relations) mapper.getRelations().get(0);
        LineVisualizer lineVisualizer = (LineVisualizer) relations.getLineVisualizer().get(0);
        Geometry geometry = (Geometry) lineVisualizer.getGeometry().get(0);
        geometryType = geometry.getType();

        Stroke stroke = (Stroke) lineVisualizer.getStroke().get(0);

        svgParameter = stroke.getSvgParameter();

        for (Object sp : svgParameter) {
            String name = ((SvgParameter) sp).getName();
            String value = ((SvgParameter) sp).getValue();
            svgMap.put(name, value);
        }
        
        System.out.println(symbolType + " " + symbolSize + " " + symbolColor + " " + propertyName + " " + geometryType);
        System.out.println(displacementX + " " + displacementY);
        
        for (Map.Entry<String, String> entry : svgMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + " "+value);
        }
        
    }

    public static void main(String args[]) {
        //Let's test it out!
        WvizConfig wvizConfig = new WvizConfig(args[0]).getConfiguration();
        //System.out.print("\n\n" + xStream.toXML(wVizConfig));
        testConfiguration(wvizConfig);
    }
}
