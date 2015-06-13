package org.n52.v3d.worldviz.test;

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.util.HashMap;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import org.junit.Before;

import org.junit.Test;
import org.n52.v3d.worldviz.featurenet.scene.ConnectionNet;
import org.n52.v3d.worldviz.featurenet.scene.Displacement;
import org.n52.v3d.worldviz.featurenet.scene.Features;
import org.n52.v3d.worldviz.featurenet.scene.Fill;
import org.n52.v3d.worldviz.featurenet.scene.Font;
import org.n52.v3d.worldviz.featurenet.scene.Geometry;
import org.n52.v3d.worldviz.featurenet.scene.Label;
import org.n52.v3d.worldviz.featurenet.scene.LabelPlacement;
import org.n52.v3d.worldviz.featurenet.scene.LineVisualizer;
import org.n52.v3d.worldviz.featurenet.scene.Mapper;
import org.n52.v3d.worldviz.featurenet.scene.PointPlacement;
import org.n52.v3d.worldviz.featurenet.scene.PointVisualizer;
import org.n52.v3d.worldviz.featurenet.scene.Relations;
import org.n52.v3d.worldviz.featurenet.scene.Stroke;
import org.n52.v3d.worldviz.featurenet.scene.SvgParameter;
import org.n52.v3d.worldviz.featurenet.scene.T3dSymbol;
import org.n52.v3d.worldviz.featurenet.scene.TextVisualizer;
import org.n52.v3d.worldviz.featurenet.scene.WvizConfig;

public class WvizConfig_Test {

    //Adhitya: This should be later moved to the RelativePaths Class
    public static final String filePath = "data\\WvizConfig.xml";
    
    private WvizConfig wvizConfig;

    @Before
    public void before() {
        try {
            File file = new File(filePath);
            XStream xStream = new XStream();
            xStream.processAnnotations(WvizConfig.class);
            wvizConfig = (WvizConfig) xStream.fromXML(file);
        }
        catch (Exception exception) {
            System.err.println(exception.getMessage());
        }
    }

    @Test
    public void testBitmap() {
        try {
            assertTrue(wvizConfig != null);

            String symbolType, symbolSize, symbolColor, propertyName, geometryType;

            Map<Object, Object> svgMap = new HashMap<Object, Object>(); // Cannot be viable once there are more properties

            double displacementX, displacementY;

            //We will use 0, for default configuration
            //For specific configuration, we should change our current XML schema
            ConnectionNet connectionNet = (ConnectionNet) wvizConfig.getConnectionNet().get(0);
            
            assertTrue(connectionNet != null);
            
            Mapper mapper = (Mapper) connectionNet.getMapper().get(0);
            
            assertTrue(mapper != null);
            
            Features features = (Features) mapper.getFeatures().get(0);
            
            assertTrue(features != null);
            
            PointVisualizer pointVisualizer = (PointVisualizer) features.getPointVisualizer().get(0);
            
            assertTrue(pointVisualizer != null);
            
            T3dSymbol t3dSymbol = (T3dSymbol) pointVisualizer.getT3dSymbol().get(0);
            
            assertTrue(t3dSymbol != null);
            
            symbolType = t3dSymbol.getType();
            
            assertTrue(symbolType != null);
            
            symbolSize = t3dSymbol.getSize();
            
            assertTrue(symbolSize != null);
            
            symbolColor = t3dSymbol.getColor();
            
            assertTrue(symbolColor != null);
            
            TextVisualizer textVisualizer = (TextVisualizer) features.getTextVisualizer().get(0);
            
            assertTrue(textVisualizer != null);
            
            Label label = (Label) textVisualizer.getLabel().get(0);
            
            assertTrue(label != null);
            
            propertyName = label.getPropertyName();

            assertTrue(propertyName != null);
            
            Font font = (Font) textVisualizer.getFont().get(0);
            
            assertTrue(font != null);
            
            List svgParameter = font.getSvgParameter();

            assertTrue(svgParameter != null);
            
            for (Object sp : svgParameter) {
                Object name = ((SvgParameter) sp).getName();
                Object value = ((SvgParameter) sp).getValue();
                svgMap.put(name, value);
            }

            LabelPlacement labelPlacement = (LabelPlacement) textVisualizer.getLabelPlacement().get(0);
            
            assertTrue(labelPlacement != null);
            
            PointPlacement pointPlacement = (PointPlacement) labelPlacement.getPointPlacement().get(0);
            
            assertTrue(pointPlacement != null);
            
            Displacement displacement = (Displacement) pointPlacement.getDisplacement().get(0);

            assertTrue(displacement != null);
            
            //It's of no use, but still let it be here. :P
            displacementX = displacement.getDisplacementX();
            displacementY = displacement.getDisplacementY();

            Fill fill = (Fill) textVisualizer.getFill().get(0);
            
            assertTrue(fill != null);
            
            svgParameter = fill.getSvgParameter();

            assertTrue(svgParameter != null);
            
            for (Object sp : svgParameter) {
                Object name = ((SvgParameter) sp).getName();
                Object value = ((SvgParameter) sp).getValue();
                svgMap.put(name, value);
            }

            Relations relations = (Relations) mapper.getRelations().get(0);
            
            assertTrue(relations != null);
            
            LineVisualizer lineVisualizer = (LineVisualizer) relations.getLineVisualizer().get(0);
            
            assertTrue(lineVisualizer != null);
            
            Geometry geometry = (Geometry) lineVisualizer.getGeometry().get(0);
            
            assertTrue(geometry != null);
            
            geometryType = geometry.getType();

            assertTrue(geometryType != null);
            
            Stroke stroke = (Stroke) lineVisualizer.getStroke().get(0);

            assertTrue(stroke != null);
            
            svgParameter = stroke.getSvgParameter();
            
            assertTrue(svgParameter != null);

            for (Object sp : svgParameter) {
                Object name = ((SvgParameter) sp).getName();
                Object value = ((SvgParameter) sp).getValue();
                svgMap.put(name, value);
            }

            for (Map.Entry<Object, Object> entry : svgMap.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                assertTrue(key != null);
                assertTrue(value != null);
            }

        }
        catch (Exception exception) {
            exception.printStackTrace();
        }

    }

}
