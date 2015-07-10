package org.n52.v3d.worldviz.featurenet.scene;

import static java.lang.Math.floor;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgFeature;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.worldviz.featurenet.VgRelation;
import org.n52.v3d.worldviz.projections.Wgs84ToX3DTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Concrete X3D scene description of a 3-d connection-map.
 *
 * @author Adhitya Kamakshidasan
 */
public class WvizConnectionMapSceneX3d extends WvizConcreteConnectionMapScene{

    final Logger logger = LoggerFactory.getLogger(WvizConnectionMapSceneX3d.class);
	
    private boolean x3domMode = true;

    //This is used to map the geo-coordinates to the scene coordinates
    private Map<VgPoint, VgPoint> pointMap = new HashMap<VgPoint, VgPoint>(); 
    
    private ArrayList <VgPoint> geoCoordinates;
    
    private boolean ribbonMode = false;
    
    private boolean curveMode = true;
    
    public boolean isX3domMode() {
        return x3domMode;
    }

    public void setX3domMode(boolean x3dom) {
        this.x3domMode = x3dom;
    }

    public WvizConnectionMapSceneX3d(WvizVirtualConnectionMapScene scene) {
        super(scene);

        Wgs84ToX3DTransform x3dTransform = new Wgs84ToX3DTransform();
        
        ArrayList<VgPoint> sceneCoordinates = x3dTransform.transform(x3dTransform.transformVertices(scene.getVertices()));
        
        geoCoordinates = x3dTransform.transformVertices(scene.getVertices());
        
        for(int i=0; i<geoCoordinates.size(); i++){
            pointMap.put(geoCoordinates.get(i), sceneCoordinates.get(i));
        }
    }

    /**
     * exports the X3D description to a file.
     *
     * @param fileName File name (and path)
     */
    @Override
    public void writeToFile(String fileName) {
    	
    	logger.info("Start to write output file!");
    	
        try {
            document = new BufferedWriter(new FileWriter(fileName));

            if (x3domMode) {
                writeLine("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
                writeLine("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
                writeLine("  <head>");
                writeLine("    <link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.x3dom.org/x3dom/release/x3dom.css\" />");
                writeLine("    <script type=\"text/javascript\" src=\"http://www.x3dom.org/x3dom/release/x3dom-full.js\"></script>");
                writeLine("  </head>");
                writeLine("  <body>");
                writeLine();
                writeLine();
            }

            writeLine("<X3D xmlns=\"http://www.web3d.org/specifications/x3d-namespace\" showStat=\"true\" showriteLineog=\"true\" showLog=\"true\"");
            writeLine("  x=\"0px\" y=\"0px\" width=\"800px\" height=\"800px\">");
            writeLine();
            writeLine("  <Scene>");
            writeLine();
            
            //@ToDo: Instead of Hardcoding the skyColor, we should include it in the XML file
            
            if(!x3domMode){
                writeLine("    <Background skyColor='1 1 1' />");
            }
            
            writeLine();

            logger.info("Parsing Edges");
            

            if(ribbonMode){
                for (VgRelation edge : scene.getEdges()) {
                
                    logger.info("Parsing Edge: "+edge.getFrom().toString()+" <--> "+edge.getTo().toString());

                    VgPoint firstVertex = (VgPoint) (edge.getFrom()).getGeometry();
                    VgPoint secondVertex = (VgPoint) (edge.getTo()).getGeometry();

                    firstVertex = pointMap.get(firstVertex);
                    secondVertex = pointMap.get(secondVertex);

                    //calculate angles/rotation
                    SceneSymbolTransformer_StraightConnections sceneSymbolTransformer = new SceneSymbolTransformer_StraightConnections(firstVertex, secondVertex);
                    double angleX = sceneSymbolTransformer.getAngleX();
                    double angleY = sceneSymbolTransformer.getAngleY();
                    double angleZ = sceneSymbolTransformer.getAngleZ();

                    //calculate translation (mid-point between both end-points of the edge)
                    VgPoint midPoint = sceneSymbolTransformer.getMidPoint();

                    //cylinder height
                    double distance = sceneSymbolTransformer.getLengthFromTo();
                    
                    double radius = Double.parseDouble(svgMap.get("stroke-width"));

                    if(distance !=0){

                        writeLine("    <Transform translation=\"" + midPoint.getX() + " " + midPoint.getY() + " " + midPoint.getZ() + "\">");
                        writeLine("      <Transform rotation=\"1 0 0 " + angleX + "\">");
                        writeLine("        <Transform rotation=\"0 1 0 " + angleY + "\">");
                        writeLine("          <Transform rotation=\"0 0 1 " + angleZ + "\">");



                        writeLine("            <Shape>");
                        writeLine("              <Appearance>");
                        writeLine("                <Material diffuseColor='1 0.5 0'/>");
                        writeLine("              </Appearance>");

                        writeLine("              <Extrusion creaseAngle='"+0.785+"'");
                        writeLine("              crossSection='");
                        Circle circle = new Circle();
                        ArrayList<T3dVector> circlePoints = circle.generateCircle(0.01, 24);
                        for(T3dVector vector: circlePoints){
                            //double x = vector.getX();
                            //double y = vector.getY();
                            double x = floor(100000*vector.getX() +0.5)/100000;
                            double y = floor(100000*vector.getY() +0.5)/100000;
                            writeLine("              "+x+" "+y);
                        }
                        writeLine("              '");
                        writeLine("              spine='");
                        Ribbon ribbon = new Ribbon();
                        ArrayList<T3dVector> ribbonPoints = ribbon.generateRibbon(0.01,distance, 3);
                        for(T3dVector vector: ribbonPoints){
                            //double x = vector.getX();
                            //double y = vector.getY();
                            //double z = vector.getZ();
                            double x = floor(100000*vector.getX() +0.5)/100000;
                            double y = floor(100000*vector.getY() +0.5)/100000;
                            double z = floor(100000*vector.getZ() +0.5)/100000;
                            writeLine("              "+x+" "+y+" "+z);
                        }
                        writeLine("              '/>");
                        writeLine("            </Shape>");
                        writeLine("          </Transform>");
                        writeLine("        </Transform>");
                        writeLine("      </Transform>");
                        writeLine("    </Transform>");
                        writeLine();

                    }
                    
                }
            }
            
            else if(curveMode){
            	
            	T3dVector curveDirection = new T3dVector(0, 1, 0);
            	
                for (VgRelation edge : scene.getEdges()) {
                
                    logger.info("Parsing Edge: "+edge.getFrom().toString()+" <--> "+edge.getTo().toString());

                    VgPoint firstVertex = (VgPoint) (edge.getFrom()).getGeometry();
                    VgPoint secondVertex = (VgPoint) (edge.getTo()).getGeometry();

                    firstVertex = pointMap.get(firstVertex);
                    secondVertex = pointMap.get(secondVertex);

                    //calculate angles/rotation
                    SceneSymbolTransformer_CurvedConnections sceneSymbolTransformer = new SceneSymbolTransformer_CurvedConnections(firstVertex, secondVertex, curveDirection);
                    double angleX = sceneSymbolTransformer.getAngleX();
                    double angleY = sceneSymbolTransformer.getAngleY();
                    double angleZ = sceneSymbolTransformer.getAngleZ();

                    //calculate translation (mid-point between both end-points of the edge)
                    VgPoint midPoint = sceneSymbolTransformer.getMidPoint();

                    //cylinder height
                    double distance = sceneSymbolTransformer.getLengthFromTo();
                    
                    double radius = Double.parseDouble(svgMap.get("stroke-width"));

                    if(distance !=0){

                        writeLine("    <Transform translation=\"" + midPoint.getX() + " " + midPoint.getY() + " " + midPoint.getZ() + "\">");
                        writeLine("      <Transform rotation=\"1 0 0 " + (angleX + 1.57) + "\">");
                        writeLine("        <Transform rotation=\"0 1 0 " + angleY + "\">");
                        writeLine("          <Transform rotation=\"0 0 1 " + angleZ + "\">");



                        writeLine("            <Shape>");
                        writeLine("              <Appearance>");
                        writeLine("                <Material diffuseColor='1 0 1'/>");
                        writeLine("              </Appearance>");

                        writeLine("              <Extrusion beginCap='true' convex='false' creaseAngle='"+1.57+"'");
                        writeLine("              crossSection='");
                        Circle circle = new Circle();
                        ArrayList<T3dVector> circlePoints = circle.generateCircle(0.025, 24);
                        for(T3dVector vector: circlePoints){
                            //double x = vector.getX();
                            //double y = vector.getY();
                            double x = floor(100000*vector.getX() +0.5)/100000;
                            double y = floor(100000*vector.getY() +0.5)/100000;
                            writeLine("              "+x+" "+y);
                        }
                        writeLine("              '");
                        writeLine("              spine='");
                        Curve curve = new Curve();
                        ArrayList<T3dVector> curvePoints = curve.generateCurve(distance/2, 24); // The radius will be half the distance between the two points
                        for(T3dVector vector: curvePoints){
                            //double x = vector.getX();
                            //double y = vector.getY();
                            //double z = vector.getZ();
                            double x = floor(100000*vector.getX() +0.5)/100000;
                            double y = floor(100000*vector.getY() +0.5)/100000;
                            double z = floor(100000*vector.getZ() +0.5)/100000;
                            writeLine("              "+x+" "+y+" "+z);
                        }
                        writeLine("              '/>");
                        writeLine("            </Shape>");
                        writeLine("          </Transform>");
                        writeLine("        </Transform>");
                        writeLine("      </Transform>");
                        writeLine("    </Transform>");
                        writeLine();

                    }
                    
                }
            }
            
            else if(!ribbonMode && !curveMode){
            
                for (VgRelation edge : scene.getEdges()) {

                    logger.info("Parsing Edge: "+edge.getFrom().toString()+" <--> "+edge.getTo().toString());

                    VgPoint firstVertex = (VgPoint) (edge.getFrom()).getGeometry();
                    VgPoint secondVertex = (VgPoint) (edge.getTo()).getGeometry();

                    firstVertex = pointMap.get(firstVertex);
                    secondVertex = pointMap.get(secondVertex);

                    //calculate angles/rotation
                    SceneSymbolTransformer_StraightConnections sceneSymbolTransformer = new SceneSymbolTransformer_StraightConnections(firstVertex, secondVertex);
                    double angleX = sceneSymbolTransformer.getAngleX();
                    double angleY = sceneSymbolTransformer.getAngleY();
                    double angleZ = sceneSymbolTransformer.getAngleZ();

                    //calculate translation (mid-point between both end-points of the edge)
                    VgPoint midPoint = sceneSymbolTransformer.getMidPoint();

                    //cylinder height
                    double cylinderHeight = sceneSymbolTransformer.getLengthFromTo();
                    if(cylinderHeight !=0){
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
                }
            }

            logger.info("Parsing Arcs");
            
            for (VgRelation arc : scene.getArcs()) {
                
                logger.info("Parsing Arc: "+arc.getFrom().toString()+" --> "+arc.getTo().toString());
                
                VgPoint firstVertex = (VgPoint) (arc.getFrom()).getGeometry();
                VgPoint secondVertex = (VgPoint) (arc.getTo()).getGeometry();
                
                firstVertex = pointMap.get(firstVertex);
                secondVertex = pointMap.get(secondVertex);
                
                //calculate angles/rotation
                SceneSymbolTransformer_StraightConnections angleCalc = new SceneSymbolTransformer_StraightConnections(firstVertex, secondVertex);
                double angleX = angleCalc.getAngleX();
                double angleY = angleCalc.getAngleY();
                double angleZ = angleCalc.getAngleZ();
                
                //calculate translation (mid-point between both end-points of the edge)
                VgPoint midPoint = angleCalc.getMidPoint();
                
                double distance = angleCalc.getLengthFromTo();
                
                double cylinderHeight = distance;
                double coneHeight = 0.1; //To be removed
               
                //First translate by y/2 units
                //We subtract 1 symbolSize unit, because the edge starts from that that point and our symbol overlaps it
                //We subtract 1 more symbolSize unit, to make sure the cone starts from the bounding edge of the symbol
                
                //There is some overlapping issue, that is why I'm keeping to 1.5 now. If this looks good, we could keep that!
                
                double coneTranslation = (distance/2) - (symbolSize*1.5);
                //double coneTranslation = (distance/2) - (symbolSize*2.0); 
                
                double cylinderRadius = Double.parseDouble(svgMap.get("stroke-width"));
                double coneRadius = cylinderRadius * 5; //To be removed
                
                if(distance != 0){
                    writeLine("    <Transform translation=\"" + midPoint.getX() + " " + midPoint.getY() + " " + midPoint.getZ() + "\">");
                    writeLine("      <Transform rotation=\"1 0 0 " + angleX + "\">");
                    writeLine("        <Transform rotation=\"0 1 0 " + angleY + "\">");
                    writeLine("          <Transform rotation=\"0 0 1 " + angleZ + "\">");

                    writeLine("            <Group>");
                    writeLine("              <Shape>");
                    writeLine("                <Cylinder radius='"+cylinderRadius+"' height='"+cylinderHeight+"' top='false'/>");
                    writeLine("                <Appearance>");
                    writeLine("                  <Material diffuseColor='.1 .6 .1'/>");
                    writeLine("                </Appearance>");   
                    writeLine("              </Shape>");                


                    writeLine("              <Transform translation='0 "+coneTranslation+" 0'>");
                    writeLine("                <Shape>");
                    writeLine("                  <Cone bottomRadius='"+coneRadius+"' height='"+coneHeight+"' top='true'/>");
                    writeLine("                  <Appearance>");
                    writeLine("                    <Material diffuseColor='0 0.7 1'/>");
                    writeLine("                  </Appearance>");   
                    writeLine("                </Shape>");
                    writeLine("              </Transform>");
                    writeLine("            </Group>");

                    writeLine("          </Transform>");
                    writeLine("        </Transform>");
                    writeLine("      </Transform>");
                    writeLine("    </Transform>");

                    writeLine();    
                }
                
            }
            
            logger.info("Parsing Vertices");
            for (VgFeature vertex : scene.getVertices()) {
                Object vertexName = ((GmAttrFeature) vertex).getAttributeValue("name");
                logger.info("Parsing Vertex : "+(String)vertexName);
                
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

                writeLine("    <Transform translation='" + (point.getX() + displacementX) + " " + (point.getY() + displacementY) + " " + (point.getZ() + displacementZ)+ "'" 
                			+ " scale='" + svgMap.get("font-size") + " " + svgMap.get("font-size") + " " + svgMap.get("font-size") + "'>");
                //@ToDo: Instead of Hardcoding the axisOfRotation, we should include it in the XML file
                writeLine("        <Billboard axisOfRotation='0 0 0'>");
                writeLine("          <Shape>");
                writeLine("            <Appearance>");
                writeLine("              <Material diffuseColor=\"" + svgMap.get("fill") + "\"/>");
                writeLine("            </Appearance>");
                writeLine("            <Text string='" + labels.get(vertex) + "'>");
                writeLine("                <FontStyle family='"+svgMap.get("font-family")+"' size='1'/>");
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
