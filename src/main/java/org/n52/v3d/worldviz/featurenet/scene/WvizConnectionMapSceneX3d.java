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
import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.t3dutil.MpSimpleHypsometricColor;
import org.n52.v3d.triturus.t3dutil.T3dColor;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgFeature;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.worldviz.extensions.mappers.MpValue2NumericExtent;
import org.n52.v3d.worldviz.featurenet.VgRelation;
import org.n52.v3d.worldviz.projections.Wgs84ToX3DTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.n52.v3d.worldviz.featurenet.shapes.*;

/**
 * Concrete X3D scene description of a 3-d relation-map.
 *
 * @author Adhitya Kamakshidasan
 */
public class WvizConnectionMapSceneX3d extends WvizConcreteConnectionMapScene{

    final Logger logger = LoggerFactory.getLogger(WvizConnectionMapSceneX3d.class);
	
    private boolean x3domMode = true;

    //This is used to map the geo-coordinates to the scene coordinates
    private Map<VgPoint, VgPoint> pointMap = new HashMap<VgPoint, VgPoint>();
    private Map<VgPoint, Integer> indexPointMap = new HashMap<VgPoint, Integer>();
    
    private ArrayList <VgPoint> geoCoordinates;
    
    private boolean ribbonMode = true;
    
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
        
        /*
         * this additional list provides additional coordinates for the bounding box of the later scene
         * coordinates.
         * It is needed if the worldMap shall be drawn underneath the vertices for better georeference. 
         * Then The whole scene must be computed according to the BBOX of the world(map).
         */
        ArrayList<VgPoint> additionalBboxCoordinates = new ArrayList<VgPoint>();
        if(isUseWorldMap){
        	VgPoint lowerLeftCorner = new GmPoint(-180, -90, 0);
        	VgPoint upperRightCorner = new GmPoint(180, 90, 0);
        	additionalBboxCoordinates.add(lowerLeftCorner);
        	additionalBboxCoordinates.add(upperRightCorner);
        }	
        
        geoCoordinates = x3dTransform.transformVertices(scene.getVertices());
        
        ArrayList<VgPoint> sceneCoordinates = x3dTransform.transform(x3dTransform.transformVertices(scene.getVertices()), additionalBboxCoordinates);
        
        for(int i=0; i<geoCoordinates.size(); i++){
            pointMap.put(geoCoordinates.get(i), sceneCoordinates.get(i));
            indexPointMap.put(sceneCoordinates.get(i),i+1);
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
                writeLine("    <script type=\"text/javascript\" src=\"http://code.jquery.com/jquery-2.1.0.min.js\" ></script>");
                writeLine("    <script type=\"text/javascript\" src=\"http://rawgit.com/cDanowski/worldviz/master/src/main/resources/js/mapper.js\"></script>");
                writeLine("    <script type=\"text/javascript\" src=\"http://rawgit.com/cDanowski/worldviz/master/src/main/resources/js/script.js\"></script>");
                writeLine("    <script type=\"text/javascript\" src=\"http://rawgit.com/cDanowski/worldviz/master/src/main/resources/js/shapes.js\"></script>");
                
                writeLine("    <script type=\"text/javascript\" src=\"http://rawgit.com/cDanowski/worldviz/master/src/main/resources/js/jscolor/jscolor.js\"></script>");
                
		writeLine("    <link rel=\"stylesheet\" type=\"text/css\" href=\"http://rawgit.com/cDanowski/worldviz/master/src/main/resources/css/style.css\">");
				
		writeLine("    <!-- Change the path, when the repository is changed! -->");
                writeLine("    <script type=\"text/javascript\">");
                
                writeLine("    function getIndexSize(){");
		writeLine("        return "+scene.getVertices().size()+";" );
                writeLine("    }");
                
                writeLine("    function getNormalColor(){");
		writeLine("        return '"+normalColor+"';" );
                writeLine("    }");
                
                writeLine("    function getNormalGlow(){");
		writeLine("        return '"+normalGlow+"';" );
                writeLine("    }");
                
                writeLine("    function getHighlightColor(){");
		writeLine("        return '"+highlightColor+"';" );
                writeLine("    }");
                
                writeLine("    function getHighlightGlow(){");
		writeLine("        return '"+highlightGlow+"';" );
                writeLine("    }");
                
                writeLine("    function getCurrentColor(){");
		writeLine("        return '"+currentColor+"';" );
                writeLine("    }");
                
                writeLine("    function getCurrentGlow(){");
		writeLine("        return '"+currentGlow+"';" );
                writeLine("    }");
                
                writeLine("    function getRibbonCircleTurns(){");
		writeLine("        return parseInt("+ribbonCircleTurns+");" );
                writeLine("    }");
                
                writeLine("    function getRibbonHelixTurns(){");
		writeLine("        return parseInt("+ribbonHelixTurns+");" );
                writeLine("    }");
                
                writeLine("    function getRibbonStep(){");
		writeLine("        return parseFloat("+ribbonStep+");" );
                writeLine("    }");
                
                writeLine("    function getCurveCircleTurns(){");
		writeLine("        return parseInt("+curveCircleTurns+");" );
                writeLine("    }");

                writeLine("    function getCurveEllipseTurns(){");
		writeLine("        return parseInt("+curveEllipseTurns+");" );
                writeLine("    }");
 
                writeLine("    function getArrowRatio(){");
		writeLine("        return "+radiusRatio+";" );
                writeLine("    }"); 
                
                writeLine("    function getLinearColorInterpolation(){");
		writeLine("        return "+linearColorInterpolation+";" );
                writeLine("    }");                 

                writeLine("    function getLinearWidthInterpolation(){");
		writeLine("        return "+linearWidthInterpolation+";" );
                writeLine("    }");                    
                writeLine();
                
                writeLine("    function getViewpointPosition(){");
		writeLine("        return '"+position+"';" );
                writeLine("    }");                    
                writeLine();

                writeLine("    function getViewpointOrientation(){");
		writeLine("        return '"+orientation+"';" );
                writeLine("    }");                    
                writeLine();
                
                writeLine("  </script>");
                writeLine("  </head>");
                writeLine("  <body>");
                writeLine();
                writeLine();
            }

            writeLine("<X3D xmlns=\"http://www.web3d.org/specifications/x3d-namespace\" showStat=\"false\" showriteLineog=\"false\" showLog=\"false\"");
            writeLine("  x=\"0px\" y=\"0px\" width=\"800px\" height=\"800px\">");
            writeLine();
            writeLine("  <Scene>");
            writeLine();
            
            
            if(!x3domMode){
                writeLine("    <Background skyColor='"+skyColor+"' />");
            }
            
            writeLine("    <Viewpoint id=\"viewpoint\" position='"+position+"' orientation='"+orientation+"' > </Viewpoint>");
            
            writeLine("");
            
            if (isUseWorldMap){
            	logger.info("Generating background world map with texture path pointing to {}", texturePath);
            
            	/*
            	 * The parameter symbolSize places the worldMap to the corresponding spot 
            	 * (directly underneath the vertices).
            	 */
            	writeLine("    <Transform translation=\"0 " + (-symbolSize) + " 0\">");
            	writeLine("      <Shape>");
            	writeLine("        <Appearance>");
            	writeLine("          <Material diffuseColor=\"1 1 1\">");
            	writeLine("          </Material>");
            	writeLine("          <ImageTexture url=\"" + texturePath + "\"/>");
            	writeLine("        </Appearance>");
            	writeLine("        <Box size=\"2 0.001 1\"/>");
            	writeLine("      </Shape>");
            	writeLine("    </Transform>");
            	
            	writeLine();
            }

            logger.info("Parsing Edges");
            MpSimpleHypsometricColor simpleColorMapper = new MpSimpleHypsometricColor();
            MpValue2NumericExtent widthMapper = new MpValue2NumericExtent();
                
            simpleColorMapper.setPalette(inputColorValues, outputColorValues, linearColorInterpolation);
            widthMapper.setPalette(inputWidthValues, outputWidthValues, linearWidthInterpolation);
                
            for (VgRelation edge : scene.getEdges()) {

                logger.info("Parsing Edge: "+edge.getFrom().toString()+" <--> "+edge.getTo().toString());

                VgPoint firstVertex = (VgPoint) (edge.getFrom()).getGeometry();
                VgPoint secondVertex = (VgPoint) (edge.getTo()).getGeometry();

                firstVertex = pointMap.get(firstVertex);
                secondVertex = pointMap.get(secondVertex);

                int firstId = indexPointMap.get(firstVertex);
                int secondId = indexPointMap.get(secondVertex);

                //calculate angles/rotation
                SceneSymbolTransformer_StraightConnections sceneSymbolTransformer = new SceneSymbolTransformer_StraightConnections(firstVertex, secondVertex);
                double angleX = sceneSymbolTransformer.getAngleX();
                double angleY = sceneSymbolTransformer.getAngleY();
                double angleZ = sceneSymbolTransformer.getAngleZ();

                //calculate translation (mid-point between both end-points of the edge)
                VgPoint midPoint = sceneSymbolTransformer.getMidPoint();

                //cylinder height
                double distance = sceneSymbolTransformer.getLengthFromTo();

                if(distance != 0){

                    double weight = (Double) edge.getValue();

                    writeLine("    <Transform translation=\"" + midPoint.getX() + " " + midPoint.getY() + " " + midPoint.getZ() + "\">");
                    writeLine("      <Transform rotation=\"1 0 0 " + angleX + "\">");
                    writeLine("        <Transform rotation=\"0 1 0 " + angleY + "\">");
                    writeLine("          <Transform rotation=\"0 0 1 " + angleZ + "\">");

                    writeLine("            <Shape render=\"false\" DEF=\"ribbonShape\" " +"data-class=\"relation\" "+ "data-firstId=\""+firstId+"\""+" data-secondId=\""+secondId+"\">");
                    writeLine("              <Appearance>");

                    T3dColor color = simpleColorMapper.transform(weight);
                    float red = color.getRed();
                    float green = color.getGreen();
                    float blue = color.getBlue();

                    writeLine("                <Material data-weight=\""+weight+"\" diffuseColor='"+red+" "+green+" "+blue+"'/>");
                    writeLine("              </Appearance>");

                    writeLine("              <Extrusion data-shape=\"ribbonShape\" data-weight=\""+weight+"\" data-distance=\""+distance+"\" creaseAngle='"+ribbonCreaseAngle+"'");
                    writeLine("              crossSection='");
                    Circle circle = new Circle();

                    double radius = widthMapper.transform(weight);

                    ArrayList<T3dVector> circlePoints = circle.generateCircle(radius, ribbonCircleTurns);
                    for(T3dVector vector: circlePoints){
                        double x = floor(100000*vector.getX() +0.5)/100000;
                        double y = floor(100000*vector.getY() +0.5)/100000;
                        writeLine("              "+x+" "+y);
                    }
                    writeLine("              '");
                    writeLine("              spine='");
                    Ribbon ribbon = new Ribbon();
                    ArrayList<T3dVector> ribbonPoints = ribbon.generateRibbon(radius,distance,ribbonHelixTurns,ribbonStep);
                    for(T3dVector vector: ribbonPoints){
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

            
            	
            T3dVector curveDirection = new T3dVector(0, 1, 0);

            for (VgRelation edge : scene.getEdges()) {

                logger.info("Parsing Edge: "+edge.getFrom().toString()+" <--> "+edge.getTo().toString());

                VgPoint firstVertex = (VgPoint) (edge.getFrom()).getGeometry();
                VgPoint secondVertex = (VgPoint) (edge.getTo()).getGeometry();

                firstVertex = pointMap.get(firstVertex);
                secondVertex = pointMap.get(secondVertex);

                int firstId = indexPointMap.get(firstVertex);
                int secondId = indexPointMap.get(secondVertex);

                //calculate angles/rotation
                SceneSymbolTransformer_CurvedConnections sceneSymbolTransformer = new SceneSymbolTransformer_CurvedConnections(firstVertex, secondVertex, curveDirection);
                double angleX = sceneSymbolTransformer.getAngleX();
                double angleY = sceneSymbolTransformer.getAngleY();
                double angleZ = sceneSymbolTransformer.getAngleZ();

                //calculate translation (mid-point between both end-points of the edge)
                VgPoint midPoint = sceneSymbolTransformer.getMidPoint();

                //cylinder height
                double distance = sceneSymbolTransformer.getLengthFromTo();

                double weight = (Double) edge.getValue();

                if(distance != 0){

                    writeLine("    <Transform translation=\"" + midPoint.getX() + " " + midPoint.getY() + " " + midPoint.getZ() + "\">");
                    writeLine("      <Transform rotation=\"1 0 0 " + angleX + "\">");
                    writeLine("        <Transform rotation=\"0 1 0 " + angleY + "\">");

                    writeLine("            <Shape render=\"true\" DEF=\"ellipseShape\" " +"data-class=\"relation\" " + "data-firstId=\""+firstId+"\""+" data-secondId=\""+secondId+"\">");
                    writeLine("              <Appearance>");
                    T3dColor color = simpleColorMapper.transform(weight);
                    float red = color.getRed();
                    float green = color.getGreen();
                    float blue = color.getBlue();

                    writeLine("                <Material data-weight=\""+weight+"\" diffuseColor='"+red+" "+green+" "+blue+"'/>");
                    writeLine("              </Appearance>");
                    
                    double ellipse_x = distance/2;
                    double ellipse_y = curveRatio * ellipse_x;

                    writeLine("              <Extrusion data-shape=\"ellipseShape\" data-weight=\""+weight+"\" data-ellipse_x=\""+ellipse_x+"\" data-ellipse_y=\""+ellipse_y+"\" beginCap='true' convex='false' creaseAngle='"+curveCreaseAngle+"'");
                    writeLine("              crossSection='");
                    Circle circle = new Circle();

                    double radius = widthMapper.transform(weight);

                    ArrayList<T3dVector> circlePoints = circle.generateCircle(radius, curveCircleTurns);
                    for(T3dVector vector: circlePoints){
                        double x = floor(100000*vector.getX() +0.5)/100000;
                        double y = floor(100000*vector.getY() +0.5)/100000;
                        writeLine("              "+x+" "+y);
                    }
                    writeLine("              '");
                    writeLine("              spine='");
                    
                    Ellipse ellipse = new Ellipse();
                    ArrayList<T3dVector> curvePoints = ellipse.generateEllipse(ellipse_x ,ellipse_y, curveEllipseTurns);

                    for(T3dVector vector: curvePoints){
                        double x = floor(100000*vector.getX() +0.5)/100000;
                        double y = floor(100000*vector.getY() +0.5)/100000;
                        double z = floor(100000*vector.getZ() +0.5)/100000;
                        writeLine("              "+x+" "+y+" "+z);
                    }
                    writeLine("              '/>");
                    writeLine("            </Shape>");
                    writeLine("        </Transform>");
                    writeLine("      </Transform>");
                    writeLine("    </Transform>");
                    writeLine();

                }

            }
            
            
            for (VgRelation edge : scene.getEdges()) {

                logger.info("Parsing Edge: "+edge.getFrom().toString()+" <--> "+edge.getTo().toString());

                VgPoint firstVertex = (VgPoint) (edge.getFrom()).getGeometry();
                VgPoint secondVertex = (VgPoint) (edge.getTo()).getGeometry();

                firstVertex = pointMap.get(firstVertex);
                secondVertex = pointMap.get(secondVertex);

                int firstId = indexPointMap.get(firstVertex);
                int secondId = indexPointMap.get(secondVertex);

                //calculate angles/rotation
                SceneSymbolTransformer_StraightConnections sceneSymbolTransformer = new SceneSymbolTransformer_StraightConnections(firstVertex, secondVertex);
                double angleX = sceneSymbolTransformer.getAngleX();
                double angleY = sceneSymbolTransformer.getAngleY();
                double angleZ = sceneSymbolTransformer.getAngleZ();

                //calculate translation (mid-point between both end-points of the edge)
                VgPoint midPoint = sceneSymbolTransformer.getMidPoint();

                //cylinder height
                double cylinderHeight = sceneSymbolTransformer.getLengthFromTo();
                if(cylinderHeight != 0){

                    writeLine("    <Transform translation=\"" + midPoint.getX() + " " + midPoint.getY() + " " + midPoint.getZ() + "\">");
                    writeLine("      <Transform rotation=\"1 0 0 " + angleX + "\">");
                    writeLine("        <Transform rotation=\"0 1 0 " + angleY + "\">");
                    writeLine("          <Transform rotation=\"0 0 1 " + angleZ + "\">");


                    double weight = (Double)edge.getValue();
                    double radius = widthMapper.transform(weight);

                    T3dColor color = simpleColorMapper.transform(weight);

                    float red = color.getRed();
                    float green = color.getGreen();
                    float blue = color.getBlue();

                    writeLine("            <Shape render=\"false\" DEF=\"cylinderShape\" " +"data-class=\"relation\" " + "data-firstId=\""+firstId+"\""+" data-secondId=\""+secondId+"\">");
                    writeLine("              <Appearance>");
                    writeLine("                <Material data-weight=\""+weight+"\" diffuseColor='"+red+" "+green+" "+blue+"'/>");
                    writeLine("              </Appearance>");

                    writeLine("              <Cylinder data-weight=\""+weight+"\" height=\"" + cylinderHeight + "\" radius=\""+radius+ "\"/>");

                    writeLine("            </Shape>");
                    writeLine("          </Transform>");
                    writeLine("        </Transform>");
                    writeLine("      </Transform>");
                    writeLine("    </Transform>");
                    writeLine();
                }
            }

            logger.info("Parsing Arcs");
            
            for (VgRelation arc : scene.getArcs()) {
                
                logger.info("Parsing Arc: "+arc.getFrom().toString()+" --> "+arc.getTo().toString());
                
                VgPoint firstVertex = (VgPoint) (arc.getFrom()).getGeometry();
                VgPoint secondVertex = (VgPoint) (arc.getTo()).getGeometry();
                
                firstVertex = pointMap.get(firstVertex);
                secondVertex = pointMap.get(secondVertex);
                
                int firstId = indexPointMap.get(firstVertex);
                int secondId = indexPointMap.get(secondVertex);
                    
                
                //calculate angles/rotation
                SceneSymbolTransformer_StraightConnections angleCalc = new SceneSymbolTransformer_StraightConnections(firstVertex, secondVertex);
                double angleX = angleCalc.getAngleX();
                double angleY = angleCalc.getAngleY();
                double angleZ = angleCalc.getAngleZ();
                
                //calculate translation (mid-point between both end-points of the edge)
                VgPoint midPoint = angleCalc.getMidPoint();
                
                double distance = angleCalc.getLengthFromTo();
                
                double cylinderHeight = distance;
                double coneHeight = distance/heightRatio;
               
                //First translate by y/2 units
                //We subtract 1 symbolSize unit, because the edge starts from that that point and our symbol overlaps it
                //We subtract 1 more symbolSize unit, to make sure the cone starts from the bounding edge of the symbol
                
                //There is some overlapping issue, that is why I'm keeping to 1.5 now. If this looks good, we could keep that!
                
                double coneTranslation = (distance/2) - (symbolSize*1);
                //double coneTranslation = (distance/2) - (symbolSize*2.0); 
                
                double weight = (Double)arc.getValue();
                
                double cylinderRadius = widthMapper.transform(weight);
                double coneRadius = cylinderRadius * radiusRatio;
                
                T3dColor color = simpleColorMapper.transform(weight);
                float red = color.getRed();
                float green = color.getGreen();
                float blue = color.getBlue();
                
                if(distance != 0){
                    writeLine("    <Transform translation=\"" + midPoint.getX() + " " + midPoint.getY() + " " + midPoint.getZ() + "\">");
                    writeLine("      <Transform rotation=\"1 0 0 " + angleX + "\">");
                    writeLine("        <Transform rotation=\"0 1 0 " + angleY + "\">");
                    writeLine("          <Transform rotation=\"0 0 1 " + angleZ + "\">");

                    writeLine("            <Group>");
                    writeLine("              <Shape render=\"true\" DEF=\"arrowCylinderShape\" " +"data-class=\"relation\" " + "data-firstId=\""+firstId+"\""+" data-secondId=\""+secondId+"\">");
                    writeLine("                <Appearance>");
                    writeLine("                  <Material data-weight=\""+weight+"\" diffuseColor='"+red+" "+green+" "+blue+"'/>");
                    writeLine("                </Appearance>");
                    writeLine("                <Cylinder data-weight=\""+weight+"\" radius='"+cylinderRadius+"' height='"+cylinderHeight+"' top='false'/>");
                    writeLine("              </Shape>");                


                    writeLine("              <Transform translation='0 "+coneTranslation+" 0'>");
                    writeLine("                <Shape render=\"true\" DEF=\"arrowConeShape\" " +"data-class=\"relation\" " + "data-firstId=\""+firstId+"\""+" data-secondId=\""+secondId+"\">");
                    writeLine("                  <Appearance>");
                    writeLine("                    <Material data-weight=\""+weight+"\" diffuseColor='"+red+" "+green+" "+blue+"'/>");
                    writeLine("                  </Appearance>"); 
                    writeLine("                  <Cone data-weight=\""+weight+"\" bottomRadius='"+coneRadius+"' height='"+coneHeight+"' top='true'/>");
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
                int indexPoint = indexPointMap.get(point);
                if ("sphere".equalsIgnoreCase(symbolType)) {
                    writeLine("    <Transform translation='" + point.getX() + " " + point.getY() + " " + point.getZ() + "'>");
                    writeLine("      <Shape render=\"true\" DEF=\"sphereShape\" " +"data-class=\"feature\" " + "data-index=\""+indexPoint+"\""+">");
                    writeLine("        <Appearance>");
                    writeLine("          <Material id = \"feature"+indexPoint+"\" diffuseColor=\"" + normalColor + "\"/>");
                    writeLine("        </Appearance>");
                    writeLine("        <Sphere radius='" + symbolSize + "'/>");
                }
                else if ("box".equalsIgnoreCase(symbolType)) {
                    writeLine("    <Transform translation='" + point.getX() + " " + point.getY() + " " + point.getZ() + "'>");
                    writeLine("      <Shape render=\"true\" DEF=\"boxShape\" " +"data-class=\"feature\" " + "data-index=\""+indexPoint+"\""+">");
                    writeLine("        <Appearance>");
                    writeLine("          <Material id = \"feature"+indexPoint+"\" diffuseColor=\"" + normalColor + "\"/>");
                    writeLine("        </Appearance>");
                    writeLine("        <Box size='" + symbolSize + "'/>");
                }
                else {
                    writeLine("    <Transform translation='" + point.getX() + " " + point.getY() + " " + point.getZ() + "'>");
                    writeLine("      <Shape render=\"true\" DEF=\"sphereShape\" " +"data-class=\"feature\" " + "data-index=\""+indexPoint+"\""+">");
                    writeLine("        <Appearance>");
                    writeLine("          <Material id = \"feature"+indexPoint+"\" diffuseColor=\"" + normalColor + "\"/>");
                    writeLine("        </Appearance>");
                    writeLine("        <Sphere radius='" + defaultSymbolSize + "'/>"); // If an incorrect symbol type has been specified
                }
                
                writeLine("      </Shape>");
                writeLine("    </Transform>");
                writeLine();

                writeLine("    <Transform translation='" + (point.getX() + displacementX) + " " + (point.getY() + displacementY) + " " + (point.getZ() + displacementZ)+ "'" 
                			+ " scale='" + svgMap.get("font-size") + " " + svgMap.get("font-size") + " " + svgMap.get("font-size") + "'>");
                writeLine("        <Billboard axisOfRotation='"+billboardAxis+"'>");
                writeLine("          <Shape id = \"featureLabel"+indexPoint+"\" render=\"true\" DEF=\"labelText\" data-class=\"information\">");
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
                                
                writeLine("<button onclick=\"showAllRelations()\">Show all</button>");
                writeLine("<input id =\"cylinderRadioButton\" type=\"radio\" name=\"shapeRadioButton\" value=\"cylinderShape\">Cylinder");
                writeLine("<input id =\"ribbonRadioButton\" type=\"radio\" name=\"shapeRadioButton\" value=\"ribbonShape\">Ribbon");
                writeLine("<input id =\"ellipseRadioButton\" type=\"radio\" name=\"shapeRadioButton\" value=\"ellipseShape\" checked>Ellipse");
                
                writeLine("<br>");
                writeLine("<br>");
                
                writeLine("<button onclick=\"restoreViewpoint()\">Restore Viewpoint</button>");
                writeLine("<br>");
                writeLine("<br>");
                writeLine("<br>");
                
                writeLine("<input type=\"text\" id=\"NewInputColor\" placeholder=\"Input Weight\">");
                writeLine("<input type=\"text\" id=\"NewOutputColor\" class=\"color\" placeholder=\"Output Color\">");
                writeLine("<button id=\"ColorAddButton\" onclick=\"addNewColor()\">Add Color</button>");
                
                writeLine("<br>");
                writeLine("<br>");
                writeLine("<input type=\"text\" id=\"DeleteColor\" placeholder=\"Delete Weight\">");
                writeLine("<button id=\"ColorDeleteButton\" onclick=\"deleteNewColor()\">Delete Color</button>");
                
                writeLine("<br>");
                writeLine("<br>");

                writeLine("<input type=\"text\" id=\"NewInputWidth\" placeholder=\"Input Weight\">");
                writeLine("<input type=\"text\" id=\"NewOutputWidth\" placeholder=\"Output Width\">");
                writeLine("<button id=\"WidthAddButton\" onclick=\"addNewWidth()\">Add Width</button>");
                writeLine("<br>");
                writeLine("<br>");
                writeLine("<input type=\"text\" id=\"DeleteWidth\" placeholder=\"Delete Weight\">");
                writeLine("<button id=\"WidthDeleteButton\" onclick=\"deleteNewWidth()\">Delete Width</button>");
                
                writeLine("<br>");
                writeLine("<br>");
                writeLine("<br>");
                
                writeLine("<button id=\"ColorChangeButton\" onclick=\"changeColors()\">Change Colors</button>");
                writeLine("<button id=\"WidthChangeButton\" onclick=\"changeWidths()\">Change Widths</button>");
                writeLine("<button id=\"SourceCodeButton\" onclick=\"saveTextAsFile()\">Export File</button>");
                writeLine("<br>");
                writeLine("<br>");
                
                writeLine("<span id=\"lastClickedObject\">-</span>");
                writeLine("<br>");
                writeLine("<br>");
                
                writeLine("<table id = \"colorTable\">");
                writeLine("<thead>");
                writeLine("<tr><th>Input Weight</th><th>Output Color</th></tr>");
                writeLine("</thead>");
                writeLine("<tbody>");
                for (int i=0;i<inputColorValues.length;i++){ 
                  writeLine("<tr><td>");
                  writeLine(String.valueOf(inputColorValues[i]));
                  writeLine("</td><td>");
                  writeLine(outputColorValues[i].getRed() + " "+ outputColorValues[i].getGreen()+ " "+outputColorValues[i].getBlue());
                  writeLine("</td></tr>");
                }
                writeLine("</tbody>");
                writeLine("</table>");
                
                
                writeLine("<table id = \"widthTable\">");
                writeLine("<thead>");
                writeLine("<tr><th>Input Weight</th><th>Output Width</th></tr>");
                writeLine("</thead>");
                writeLine("<tbody>");
                for (int i=0;i<inputWidthValues.length;i++){ 
                  writeLine("<tr><td>");
                  writeLine(String.valueOf(inputWidthValues[i]));
                  writeLine("</td><td>");
                  writeLine(String.valueOf(outputWidthValues[i]));
                  writeLine("</td></tr>");
                }
                writeLine("</tbody>");
                writeLine("</table>");
                

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
