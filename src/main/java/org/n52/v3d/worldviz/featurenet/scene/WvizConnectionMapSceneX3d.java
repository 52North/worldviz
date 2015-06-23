package org.n52.v3d.worldviz.featurenet.scene;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.vgis.VgFeature;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.worldviz.featurenet.VgRelation;
import org.n52.v3d.worldviz.projections.Wgs84ToX3DTransform;

/**
 * Concrete X3D scene description of a 3-d connection-map.
 *
 * @author Adhitya Kamakshidasan
 */
public class WvizConnectionMapSceneX3d {

    private boolean x3domMode = false;

    /**
     * exports the X3D description to a file.
     *
     * @param fileName File name (and path)
     */
    private WvizVirtualConnectionMapScene scene;

    //List of Style Parameters that were defined in the XML sheet - More will be added!
    public String symbolType, symbolColor, symbolSize;

    public static final String defaultSymbolSize = "0.15";
    
    public String propertyName;

    public Map<VgPoint, VgPoint> pointMap = new HashMap<VgPoint, VgPoint>(); //This is used to map the geo-coordinates to the scene coordinates
    
    public Map<VgFeature, String> labels = new HashMap<VgFeature, String>();

    public Map<String, String> svgMap = new HashMap<String, String>(); // Cannot be viable once there are more properties

    public double displacementX, displacementY;

    //Currently, this file supports only Cylinders, when more parameters are used,
    //this paramenter should be used and more if statements should be added.
    public String geometryType;

    private BufferedWriter document;

    
    public ArrayList <VgPoint> geoCoordinates;
    
    public boolean isX3domMode() {
        return x3domMode;
    }

    public void setX3domMode(boolean x3dom) {
        this.x3domMode = x3dom;
    }
    
    
    public ArrayList <VgPoint> getGeoCoordinates(){
        return this.geoCoordinates;
    }
    

    public WvizConnectionMapSceneX3d(WvizVirtualConnectionMapScene scene) {
        this.scene = scene;
        
        Wgs84ToX3DTransform x3dTransform = new Wgs84ToX3DTransform();
        
        ArrayList<VgPoint> sceneCoordinates = x3dTransform.transform(
                x3dTransform.transformVertices(scene.getVertices())
        );
        
        geoCoordinates = x3dTransform.transformVertices(scene.getVertices());
        
        for(int i=0; i<geoCoordinates.size(); i++){
            pointMap.put(geoCoordinates.get(i), sceneCoordinates.get(i));
        }
        
        //Check in XML file, if there is a default configuration or not
        getDefaultConfiguration(scene);
    }

    private void getDefaultConfiguration(WvizVirtualConnectionMapScene scene) {
        //We will use 0, for default configuration

        //For specific configuration, we should change our current XML schema
        WvizConfig wvizConfig = scene.getStyle();
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
        propertyName = label.getPropertyName();

        Font font = (Font) textVisualizer.getFont().get(0);
        List svgParameter = font.getSvgParameter();

        for (Object object : svgParameter) {
            String name = ((SvgParameter) object).getName();
            String value = ((SvgParameter) object).getValue();
            svgMap.put(name, value);
        }

        LabelPlacement labelPlacement = (LabelPlacement) textVisualizer.getLabelPlacement().get(0);
        PointPlacement pointPlacement = (PointPlacement) labelPlacement.getPointPlacement().get(0);
        Displacement displacement = (Displacement) pointPlacement.getDisplacement().get(0);

        displacementX = displacement.getDisplacementX();
        displacementY = displacement.getDisplacementY();

        Fill fill = (Fill) textVisualizer.getFill().get(0);
        svgParameter = fill.getSvgParameter();

        for (Object object : svgParameter) {
            String name = ((SvgParameter) object).getName();
            String value = ((SvgParameter) object).getValue();
            svgMap.put(name, value);
        }

        Relations relations = (Relations) mapper.getRelations().get(0);
        LineVisualizer lineVisualizer = (LineVisualizer) relations.getLineVisualizer().get(0);
        Geometry geometry = (Geometry) lineVisualizer.getGeometry().get(0);
        geometryType = geometry.getType();

        Stroke stroke = (Stroke) lineVisualizer.getStroke().get(0);

        svgParameter = stroke.getSvgParameter();

        for (Object object : svgParameter) {
            String name = ((SvgParameter) object).getName();
            String value = ((SvgParameter) object).getValue();
            svgMap.put(name, value);
        }

        for (VgFeature feature : scene.getVertices()) {
            labels.put(feature, (String) ((GmAttrFeature) feature).getAttributeValue(propertyName));
        }

    }

    public Map<VgFeature, String> getLabels() {
        return labels;
    }

    private void writeLine(String pLine) {
        try {
            document.write(pLine);
            document.newLine();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void writeLine() {
        try {
            document.newLine();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void writeToFile(String fileName) {

        try {
            document = new BufferedWriter(new FileWriter(fileName));

            if (x3domMode) {
                writeLine("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
                writeLine("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
                writeLine("  <head>");
                writeLine("    <link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.x3dom.org/x3dom/release/x3dom.css\" />");
                writeLine("    <script type=\"text/javascript\" src=\"http://www.x3dom.org/x3dom/release/x3dom.js\"></script>");
                writeLine("  </head>");
                writeLine("  <body>");
                writeLine();
                writeLine();
            }

            writeLine("<X3D xmlns=\"http://www.web3d.org/specifications/x3d-namespace\" showStat=\"true\" showriteLineog=\"true\"");
            writeLine("  x=\"0px\" y=\"0px\" width=\"400px\" height=\"400px\">");
            writeLine();
            writeLine("  <Scene>");
            writeLine();
            
            //@ToDo: Instead of Hardcoding the skyColor, we should include it in the XML file
            writeLine("    <Background skyColor='1 1 1' />");
            writeLine();

            for (VgRelation edge : scene.getEdges()) {
                VgPoint firstVertex = (VgPoint) (edge.getFrom()).getGeometry();
                VgPoint secondVertex = (VgPoint) (edge.getTo()).getGeometry();
                
                firstVertex = pointMap.get(firstVertex);
                secondVertex = pointMap.get(secondVertex);
                
                //calculate angles/rotation
                SceneSymbolTransformer sceneSymbolTransformer = new SceneSymbolTransformer(firstVertex, secondVertex);
                double angleX = sceneSymbolTransformer.getAngleX();
                double angleY = sceneSymbolTransformer.getAngleY();
                double angleZ = sceneSymbolTransformer.getAngleZ();
                
                //calculate translation (mid-point between both end-points of the edge)
                VgPoint midPoint = sceneSymbolTransformer.getMidPoint();
                
                //cylinder height
                double cylinderHeight = sceneSymbolTransformer.getLengthFromTo();
                
                writeLine("    <Transform translation=\"" + midPoint.getX() + " " + midPoint.getY() + " " + midPoint.getZ() + "\">");
                writeLine("      <Transform rotation=\"1 0 0 " + angleX + "\">");
                writeLine("        <Transform rotation=\"0 1 0 " + angleY + "\">");
                writeLine("          <Transform rotation=\"0 0 1 " + angleZ + "\">");
                
                
                
                writeLine("            <Shape>");
                writeLine("              <Appearance>");
                writeLine("                <Material emissiveColor=\"" + svgMap.get("stroke") + "\"/>");
                writeLine("              </Appearance>");
                
                writeLine("              <Cylinder height=\"" + cylinderHeight + "\" radius=\""+ svgMap.get("stroke-width")+ "\"/>");
                
                writeLine("            </Shape>");
                writeLine("          </Transform>");
                writeLine("        </Transform>");
                writeLine("      </Transform>");
                writeLine("    </Transform>");
                writeLine();
            }

            for (VgRelation arc : scene.getArcs()) {
                VgPoint firstVertex = (VgPoint) (arc.getFrom()).getGeometry();
                VgPoint secondVertex = (VgPoint) (arc.getTo()).getGeometry();
                
                firstVertex = pointMap.get(firstVertex);
                secondVertex = pointMap.get(secondVertex);
                
                //calculate angles/rotation
                SceneSymbolTransformer angleCalc = new SceneSymbolTransformer(firstVertex, secondVertex);
                double angleX = angleCalc.getAngleX();
                double angleY = angleCalc.getAngleY();
                double angleZ = angleCalc.getAngleZ();
                
                //calculate translation (mid-point between both end-points of the edge)
                VgPoint midPoint = angleCalc.getMidPoint();
                
                //cylinder height
                double cylinderHeight = angleCalc.getLengthFromTo();
                
                writeLine("    <Transform translation=\"" + midPoint.getX() + " " + midPoint.getY() + " " + midPoint.getZ() + "\">");
                writeLine("      <Transform rotation=\"1 0 0 " + angleX + "\">");
                writeLine("        <Transform rotation=\"0 1 0 " + angleY + "\">");
                writeLine("          <Transform rotation=\"0 0 1 " + angleZ + "\">");
                
                writeLine("            <Shape>");
                writeLine("              <Appearance>");
                writeLine("                <Material emissiveColor=\"" + svgMap.get("stroke") + "\"/>");
                writeLine("              </Appearance>");
                
                writeLine("              <Cylinder height=\"" + cylinderHeight + "\" radius=\""+ svgMap.get("stroke-width")+ "\"/>");
                
                writeLine("            </Shape>");
                writeLine("          </Transform>");
                writeLine("        </Transform>");
                writeLine("      </Transform>");
                writeLine("    </Transform>");
                
                writeLine();
            }

            for (VgFeature vertex : scene.getVertices()) {
                VgPoint point = (VgPoint) (vertex.getGeometry());
                point = pointMap.get(point);
                writeLine("    <Transform translation='" + point.getX() + " " + point.getY() + " " + point.getZ() + "'>");
                writeLine("      <Shape>");
                writeLine("        <Appearance>");
                writeLine("          <Material diffuseColor=\"" + symbolColor + "\"/>");
                writeLine("        </Appearance>");
                
                if ("Sphere".equals(symbolType)) {
                    writeLine("        <Sphere radius='" + symbolSize + "'/>");
                }
                else if ("Box".equals(symbolType)) {
                    writeLine("        <Box size='" + symbolSize + "'/>");
                }
                else {
                    writeLine("        <Sphere radius='" + defaultSymbolSize + "'/>"); // If an incorrect symbol type has been specified
                }
                
                writeLine("      </Shape>");
                writeLine("    </Transform>");
                writeLine();

                writeLine("    <Transform translation='" + (point.getX() + displacementX) + " " + (point.getY() + displacementY) + " " + point.getZ()+svgMap.get("stroke-width")+5 + "'>");
                //@ToDo: Instead of Hardcoding the axisOfRotation, we should include it in the XML file
                writeLine("        <Billboard axisOfRotation='0 0 0'>");
                writeLine("          <Shape>");
                writeLine("            <Appearance>");
                writeLine("              <Material diffuseColor=\"" + svgMap.get("fill") + "\"/>");
                writeLine("            </Appearance>");
                writeLine("            <Text string=\"" + getLabels().get(vertex) + "\">");
                writeLine("                <FontStyle family='"+svgMap.get("font-family")+"' size='"+svgMap.get("font-size")+"'/>");
                writeLine("            </Text>");
                writeLine("          </Shape>");
                writeLine("        </Billboard>");
                writeLine("    </Transform>");
                writeLine();

            }

            writeLine("  </Scene>");
            writeLine();
            writeLine("</X3D>");

            if (x3domMode) {
                writeLine();
                writeLine();
                writeLine("  </body>");
                writeLine("</html>");
            }

            document.close();
        }
        catch (FileNotFoundException fileNotFoundException) {
            System.err.println("Could not access file \"" + fileName + "\".");
        }
        catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
        catch (Exception exception) {
            System.err.println(exception.getMessage());
        }

    }

}
