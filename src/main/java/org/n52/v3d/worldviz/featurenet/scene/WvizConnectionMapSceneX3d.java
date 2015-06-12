package org.n52.v3d.worldviz.featurenet.scene;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgFeature;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.worldviz.featurenet.VgRelation;

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

    public Map<VgFeature, String> labels = new HashMap<VgFeature, String>();

    public Map<String, String> svgMap = new HashMap<String, String>(); // Cannot be viable once there are more properties

    public double displacementX, displacementY;

    //Currently, geometryType,stroke-width is not being used.
    //Ask Benno, if he can generate a X3D file, using a cylinder between 2 points
    public String geometryType;

    private BufferedWriter document;

    public boolean isX3domMode() {
        return x3domMode;
    }

    public void setX3domMode(boolean x3dom) {
        this.x3domMode = x3dom;
    }

    public WvizConnectionMapSceneX3d(WvizVirtualConnectionMapScene scene) {
        this.scene = scene;
        
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

        for (VgFeature f : scene.getVertices()) {
            labels.put(f, (String) ((GmAttrFeature) f).getAttributeValue(propertyName));
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

    public void writeToFile(String pFilename) {

        try {
            document = new BufferedWriter(new FileWriter(pFilename));

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
            
            /*
             * Christian: I added a Background node to have a white Background for now
             * (it is hardcoded for now)
             * --> maybe we should add such a parameter in the XML-file?
             */
            writeLine("  	   <Background skyColor='1 1 1' />");
            writeLine();

            for (VgRelation edge : scene.getEdges()) {
                VgPoint firstVertex = (VgPoint) (edge.getFrom()).getGeometry();
                VgPoint secondVertex = (VgPoint) (edge.getTo()).getGeometry();
                
                //calculate angles/rotation
                SceneSymbolTransformer angleCalc = new SceneSymbolTransformer(firstVertex, secondVertex);
                double angleX = angleCalc.getAngleX();
                double angleY = angleCalc.getAngleY();
                double angleZ = angleCalc.getAngleZ();
                
                //calculate translation (mid-point between both end-points of the edge)
                VgPoint midPoint = angleCalc.getMidPoint();
                
                //cylinder heigt
                double cylinderHeight = angleCalc.getLengthFromTo();
                
                writeLine("    <Transform translation=\"" + midPoint.getX() + " " 
                		+ midPoint.getY() + " " + midPoint.getZ() + "\">");
                writeLine("      <Transform rotation=\"1 0 0 " + angleX + "\">");
                writeLine("        <Transform rotation=\"0 1 0 " + angleY + "\">");
                writeLine("          <Transform rotation=\"0 0 1 " + angleZ + "\">");
                
                
                
                writeLine("            <Shape>");
                writeLine("              <Appearance>");
                writeLine("                <Material emissiveColor=\"" + svgMap.get("stroke") + "\"/>");
                writeLine("              </Appearance>");
//                writeLine("        <LineSet vertexCount='2'" + ">");
//                writeLine("          <Coordinate point='");
//                writeLine("                             " + firstVertex.getX() + " " + firstVertex.getY() + " " + firstVertex.getZ());
//                writeLine("                             " + secondVertex.getX() + " " + secondVertex.getY() + " " + secondVertex.getZ());
//                writeLine("            '></Coordinate>");
//                writeLine("        </LineSet>");
                
                writeLine("              <Cylinder height=\"" + cylinderHeight + "\" radius=\"0.10\"/>");
                
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
                
                
              //calculate angles/rotation
                SceneSymbolTransformer angleCalc = new SceneSymbolTransformer(firstVertex, secondVertex);
                double angleX = angleCalc.getAngleX();
                double angleY = angleCalc.getAngleY();
                double angleZ = angleCalc.getAngleZ();
                
                //calculate translation (mid-point between both end-points of the edge)
                VgPoint midPoint = angleCalc.getMidPoint();
                
                //cylinder heigt
                double cylinderHeight = angleCalc.getLengthFromTo();
                
                writeLine("    <Transform translation=\"" + midPoint.getX() + " " 
                		+ midPoint.getY() + " " + midPoint.getZ() + "\">");
                writeLine("      <Transform rotation=\"1 0 0 " + angleX + "\">");
                writeLine("        <Transform rotation=\"0 1 0 " + angleY + "\">");
                writeLine("          <Transform rotation=\"0 0 1 " + angleZ + "\">");
                
                writeLine("            <Shape>");
                writeLine("              <Appearance>");
                writeLine("                <Material emissiveColor=\"" + svgMap.get("stroke") + "\"/>");
                writeLine("              </Appearance>");
//                writeLine("              <LineSet vertexCount='2'" + ">");
//                writeLine("                <Coordinate point='");
//                writeLine("                             " + firstVertex.getX() + " " + firstVertex.getY() + " " + firstVertex.getZ());
//                writeLine("                             " + secondVertex.getX() + " " + secondVertex.getY() + " " + secondVertex.getZ());
//                writeLine("            '></Coordinate>");
//                writeLine("        </LineSet>");
                
                writeLine("              <Cylinder height=\"" + cylinderHeight + "\" radius=\"0.10\"/>");
                
                writeLine("            </Shape>");
                writeLine("          </Transform>");
                writeLine("        </Transform>");
                writeLine("      </Transform>");
                writeLine("    </Transform>");
                
                writeLine();
            }

            for (VgFeature vertex : scene.getVertices()) {
                VgPoint point = (VgPoint) (vertex.getGeometry());
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

                writeLine("    <Transform translation='" + (point.getX() + displacementX) + " " + (point.getY() + displacementY) + " " + point.getZ() + "'>");
                writeLine("      <Shape>");
                writeLine("        <Appearance>");
                writeLine("          <Material diffuseColor=\"" + svgMap.get("fill") + "\"/>");
                writeLine("        </Appearance>");
                writeLine("        <Text string='" + getLabels().get(vertex) + "'>");
                writeLine("            <FontStyle family='"+svgMap.get("font-family")+"' size='"+svgMap.get("font-size")+"'/>");
                writeLine("        </Text>");
                writeLine("      </Shape>");
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
        catch (FileNotFoundException e) {
            System.err.println("Could not access file \"" + pFilename + "\".");
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

}
