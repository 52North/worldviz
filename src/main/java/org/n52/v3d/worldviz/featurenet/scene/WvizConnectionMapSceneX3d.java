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
        
        ArrayList<VgPoint> sceneCoordinates = x3dTransform.transform(x3dTransform.transformVertices(scene.getVertices()));
        
        geoCoordinates = x3dTransform.transformVertices(scene.getVertices());
        
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
                
                writeLine("    <script>");
                
                //Handle click on a shape
                writeLine("    function handleSingleClick(shape){");
                writeLine("        var data_class = $(shape).attr(\"data-class\"); ");
                writeLine("            if(data_class != \"feature\"){");
                writeLine("                document.getElementById(\"lastClickedObject\").innerHTML = data_class + \" false\";");
                writeLine("            }");
                writeLine("            else{");
                writeLine("                var data_index = $(shape).attr(\"data-index\"); ");
                writeLine("                document.getElementById(\"lastClickedObject\").innerHTML = data_class + \" \" + data_index;");
                writeLine("                showRelationsForNode(data_index);");
                writeLine("            }");
                writeLine("    }");

                writeLine();
                
                writeLine("    function showRelationsForNode(nodeId) {");
                writeLine("        var x = document.getElementsByTagName(\"shape\");");
                writeLine("        var i;");
                writeLine("        for (i = 0; i < x.length; i++) {");
                writeLine("            var data_class = x[i].getAttribute(\"data-class\");");
                writeLine("            if(data_class == \"relation\"){");
                writeLine("                var firstId = x[i].getAttribute(\"data-firstId\");");
                writeLine("                var secondId = x[i].getAttribute(\"data-secondId\");");
                writeLine("                if(firstId == nodeId || secondId == nodeId){");
                writeLine("                    x[i].render = \"true\";");
                writeLine("                }");
                writeLine("                else{");
                writeLine("                    x[i].render = \"false\";");
                writeLine("                }");
                writeLine("            }");
                writeLine("        }");
                writeLine("    }");
             
                writeLine();
                
                
                writeLine("    function showAllRelations() {");
                writeLine("        var x = document.getElementsByTagName(\"shape\");");
                writeLine("        var i;");
                writeLine("        for (i = 0; i < x.length; i++) {");
                writeLine("            var data_class = x[i].getAttribute(\"data-class\");");
                writeLine("            if(data_class == \"relation\"){");
                writeLine("                x[i].render = \"true\";");
                writeLine("            }");
                writeLine("        }");
                writeLine("    }");
                
                writeLine();
                
                writeLine("	function addNewColor(){");
                writeLine("		var table = document.getElementById(\"colorTable\");");
                writeLine("		var done = false;");
                writeLine();
                writeLine("		var newInputColor = document.getElementById(\"NewInputColor\").value;");
                writeLine("		var newOutputColor = document.getElementById(\"NewOutputColor\").value;");
                writeLine();
                writeLine("		if((newInputColor.split(\" \")).length == 1){");
                writeLine("			newInputColor = parseFloat(newInputColor);");
                writeLine("		}");
                writeLine();
                writeLine("		newOutputColor = newOutputColor.split(\" \");");
                writeLine();
                writeLine("		if(newOutputColor.length == 3){");
                writeLine("			var red = parseFloat(newOutputColor[0]);");
                writeLine("			var green = parseFloat(newOutputColor[1]);");
                writeLine("			var blue = parseFloat(newOutputColor[2]);");
                writeLine();
                writeLine("			if( !isNaN(newInputColor) && red>=0 && red<=1 && green>=0 && green<=1 && blue>=0 && blue<=1 ){");
                writeLine("				var valuePresent = false;");
                writeLine("				var i = 1;");
                writeLine("				var table_length = table.rows.length;");
                writeLine();
                writeLine("				for(;i<table_length;i++){");
                writeLine("					if (parseFloat(table.rows[i].cells[0].innerHTML) == newInputColor){");
                writeLine("						valuePresent = true;");
                writeLine("						break;");
                writeLine("					}");
                writeLine("				}");
                writeLine();
                writeLine("				if(!valuePresent){");
                writeLine("					var rows = table.rows.length;");
                writeLine("					var newRow = table.insertRow(rows);");
                writeLine("					var inputColor = newRow.insertCell(0);");
                writeLine("					var outputColor = newRow.insertCell(1);");
                writeLine("					inputColor.innerHTML = newInputColor;");
                writeLine("					outputColor.innerHTML = red + \" \"+green +\" \"+blue;");
                writeLine("				}");
                writeLine("				else{");
                writeLine("					table.rows[i].cells[1].innerHTML = red + \" \"+green +\" \"+blue;");
                writeLine("				}");
                writeLine("				done = true;");
                writeLine("			}");
                writeLine("			");
                writeLine("		}");
                writeLine("		");
                writeLine("		if(done){");
                writeLine("			document.getElementById(\"NewInputColor\").value = \"\";");
                writeLine("			document.getElementById(\"NewOutputColor\").value = \"\";");
                writeLine("		}");
                writeLine("			");	
                writeLine("	}");
                writeLine("	");
                
                writeLine("	function addNewWidth(){");
                writeLine("		var done = false;");
                writeLine("		var table = document.getElementById(\"widthTable\");");
                writeLine("	");
                writeLine("		var newInputWidth = document.getElementById(\"NewInputWidth\").value;");
                writeLine("		var newOutputWidth = document.getElementById(\"NewOutputWidth\").value;");
                writeLine("		");
                writeLine("		if( (newInputWidth.split(\" \")).length == 1 && (newOutputWidth.split(\" \")).length == 1){");
                writeLine("			newInputWidth = parseFloat(newInputWidth);");
                writeLine("			newOutputWidth = parseFloat(newOutputWidth);");
                writeLine("		}");
                writeLine("		");
                writeLine("		if( !isNaN(newInputWidth) && !isNaN(newOutputWidth) ){");
                writeLine("		");
                writeLine("			var valuePresent = false;");
                writeLine("			var i = 1;");
                writeLine("			var table_length = table.rows.length;");
                writeLine("			");
                writeLine("			for(;i<table_length;i++){");
                writeLine("				if (parseFloat(table.rows[i].cells[0].innerHTML) == newInputWidth){");
                writeLine("					valuePresent = true;");
                writeLine("					break;");
                writeLine("				}");
                writeLine("			}");
                writeLine("			");
                writeLine("			if(!valuePresent){");
                writeLine("				var rows = table.rows.length;");
                writeLine("				var newRow = table.insertRow(rows);");
                writeLine("				var inputWidth = newRow.insertCell(0);");
                writeLine("				var outputWidth = newRow.insertCell(1);");
                writeLine("				inputWidth.innerHTML = newInputWidth;");
                writeLine("				outputWidth.innerHTML = newOutputWidth;");
                writeLine("			}");
                writeLine("			else{");
                writeLine("				table.rows[i].cells[1].innerHTML = newOutputWidth;");
                writeLine("			}");
                writeLine("			done = true;");
                writeLine("		}");
                writeLine("		if(done){");
                writeLine("			document.getElementById(\"NewInputWidth\").value = \"\";");
                writeLine("			document.getElementById(\"NewOutputWidth\").value = \"\";");
                writeLine("		}");
                writeLine("		");
                writeLine("	}");
                writeLine("	");
                
                writeLine("	function deleteNewColor(){");
                
                writeLine("		var table = document.getElementById(\"colorTable\");");
                writeLine("		var rows = document.getElementById(\"colorTable\").rows.length;");
                writeLine("		table.deleteRow(rows-1);");
                writeLine("	}");
                writeLine("	");
                
                writeLine("	function deleteNewWidth(){");
                writeLine("		var table = document.getElementById(\"widthTable\");");
                writeLine("		var rows = document.getElementById(\"widthTable\").rows.length;");
                writeLine("		table.deleteRow(rows-1);");
                writeLine("	}");
                
                
                writeLine("	function sortColorTable(f,n){ ");
                writeLine("	  var rows = $('#colorTable tbody  tr').get(); ");
                writeLine(" ");
                writeLine("	  rows.sort(function(a, b) { ");
                writeLine(" ");
                writeLine("	  var A = $(a).children('td').eq(n).text().toUpperCase(); ");
                writeLine("	  var B = $(b).children('td').eq(n).text().toUpperCase(); ");
                writeLine("	   ");
                writeLine("	  A = parseFloat(A); ");
                writeLine("	  B = parseFloat(B); ");
                writeLine(" ");
                writeLine("	  if(A < B) { ");
                writeLine("		return 1*f; ");
                writeLine("	  } ");
                writeLine("	  if(A > B) { ");
                writeLine("		return -1*f; ");
                writeLine("	  } ");
                writeLine("	  return 0; ");
                writeLine("	  }); ");
                writeLine(" ");
                writeLine("	  $.each(rows, function(index, row) { ");
                writeLine("		$('#colorTable').children('tbody').append(row); ");
                writeLine("	  }); ");
                writeLine("	} ");
                writeLine("	 ");
                writeLine("	 ");
                
                writeLine("	function sortColorFunction(){ ");
                writeLine("		var f_sl = 1; ");
                writeLine("		f_sl *= -1; ");
                writeLine("		var n = 0; ");
                writeLine("		sortColorTable(f_sl,n); ");
                writeLine("	}; ");
                writeLine("	 ");
                
                writeLine("	function sortWidthTable(f,n){ ");
                writeLine("	  var rows = $('#widthTable tbody  tr').get(); ");
                writeLine(" ");
                writeLine("	  rows.sort(function(a, b) { ");
                writeLine(" ");
                writeLine("	  var A = $(a).children('td').eq(n).text().toUpperCase(); ");
                writeLine("	  var B = $(b).children('td').eq(n).text().toUpperCase(); ");
                writeLine("	   ");
                writeLine("	  A = parseFloat(A); ");
                writeLine("	  B = parseFloat(B); ");
                writeLine(" ");
                writeLine("	  if(A < B) { ");
                writeLine("		return 1*f; ");
                writeLine("	  } ");
                writeLine("	  if(A > B) { ");
                writeLine("		return -1*f; ");
                writeLine("	  } ");
                writeLine("	  return 0; ");
                writeLine("	  }); ");
                writeLine(" ");
                writeLine("	  $.each(rows, function(index, row) { ");
                writeLine("		$('#widthTable').children('tbody').append(row); ");
                writeLine("	  }); ");
                writeLine("	} ");
                writeLine("	 ");
                writeLine("	 ");
                
                writeLine("	function sortWidthFunction(){ ");
                writeLine("		var f_sl = 1; ");
                writeLine("		f_sl *= -1; ");
                writeLine("		var n = 0; ");
                writeLine("		sortWidthTable(f_sl,n); ");
                writeLine("	}; ");
                
                                
                writeLine("    $(document).ready(function(){");
                //Add a onclick callback to every shape
                writeLine("        $(\"shape\").each(function() {");
                writeLine("            $(this).attr(\"onclick\", \"handleSingleClick(this)\");");
                writeLine("        });");
                writeLine("    });");
                
                writeLine("    </script>");
                
                
                
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
            
            writeLine("    <Viewpoint position='0 0.5 2.5'> </Viewpoint>");
            
            writeLine("");

            logger.info("Parsing Edges");
            MpSimpleHypsometricColor simpleColorMapper = new MpSimpleHypsometricColor();
            MpValue2NumericExtent widthMapper = new MpValue2NumericExtent();
                
            simpleColorMapper.setPalette(inputColorValues, outputColorValues, linearColorInterpolation);
            widthMapper.setPalette(inputWidthValues, outputWidthValues, linearWidthInterpolation);
                

            if(ribbonMode){
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

                        writeLine("    <Transform translation=\"" + midPoint.getX() + " " + midPoint.getY() + " " + midPoint.getZ() + "\">");
                        writeLine("      <Transform rotation=\"1 0 0 " + angleX + "\">");
                        writeLine("        <Transform rotation=\"0 1 0 " + angleY + "\">");
                        writeLine("          <Transform rotation=\"0 0 1 " + angleZ + "\">");
                        
                        writeLine("            <Shape render=\"true\" DEF=\"ribbonShape\" " +"data-class=\"relation\" "+ "data-firstId=\""+firstId+"\""+" data-secondId=\""+secondId+"\">");
                        writeLine("              <Appearance>");
                        
                        double weight = (Double) edge.getValue();
                        T3dColor color = simpleColorMapper.transform(weight);
                        
                        float red = color.getRed();
                        float green = color.getGreen();
                        float blue = color.getBlue();
                        
                        writeLine("                <Material diffuseColor='"+red+" "+green+" "+blue+"'/>");
                        writeLine("              </Appearance>");

                        writeLine("              <Extrusion creaseAngle='"+0.785+"'");
                        writeLine("              crossSection='");
                        Circle circle = new Circle();
                        
                        double radius = widthMapper.transform(weight);
                        
                        ArrayList<T3dVector> circlePoints = circle.generateCircle(radius, 24);
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
                        ArrayList<T3dVector> ribbonPoints = ribbon.generateRibbon(radius,distance, 3);
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
            
            if(curveMode){
            	
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
//                        writeLine("          <Transform rotation=\"0 0 1 " + angleZ + "\">");



                        writeLine("            <Shape render=\"true\" DEF=\"ellipseShape\" " +"data-class=\"relation\" " + "data-firstId=\""+firstId+"\""+" data-secondId=\""+secondId+"\">");
                        writeLine("              <Appearance>");
                        T3dColor color = simpleColorMapper.transform(weight);
                        float red = color.getRed();
                        float green = color.getGreen();
                        float blue = color.getBlue();
                        
                        writeLine("                <Material diffuseColor='"+red+" "+green+" "+blue+"'/>");
                        writeLine("              </Appearance>");

                        writeLine("              <Extrusion beginCap='true' convex='false' creaseAngle='"+1.57+"'");
                        writeLine("              crossSection='");
                        Circle circle = new Circle();
                        
                        double radius = widthMapper.transform(weight);
                        
                        ArrayList<T3dVector> circlePoints = circle.generateCircle(radius, 24);
                        for(T3dVector vector: circlePoints){
                            //double x = vector.getX();
                            //double y = vector.getY();
                            double x = floor(100000*vector.getX() +0.5)/100000;
                            double y = floor(100000*vector.getY() +0.5)/100000;
                            writeLine("              "+x+" "+y);
                        }
                        writeLine("              '");
                        writeLine("              spine='");
                        
                        //Curve curve = new Curve();
                        //ArrayList<T3dVector> curvePoints = curve.generateCurve(distance/2, 24); // The radius will be half the distance between the two points
 
                        double ellipse_x = distance/2;
                        double ellipse_y = 1.5 * ellipse_x;
                        Ellipse ellipse = new Ellipse();
                        ArrayList<T3dVector> curvePoints = ellipse.generateEllipse(ellipse_x ,ellipse_y, 24);
                        
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
//                      writeLine("          </Transform>");
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
                        
                        writeLine("            <Shape render=\"true\" DEF=\"cylinderShape\" " +"data-class=\"relation\" " + "data-firstId=\""+firstId+"\""+" data-secondId=\""+secondId+"\">");
                        writeLine("              <Appearance>");
                        writeLine("                <Material diffuseColor='"+red+" "+green+" "+blue+"'/>");
                        writeLine("              </Appearance>");

                        writeLine("              <Cylinder height=\"" + cylinderHeight + "\" radius=\""+radius+ "\"/>");

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
                double coneHeight = 0.1; //To be removed
               
                //First translate by y/2 units
                //We subtract 1 symbolSize unit, because the edge starts from that that point and our symbol overlaps it
                //We subtract 1 more symbolSize unit, to make sure the cone starts from the bounding edge of the symbol
                
                //There is some overlapping issue, that is why I'm keeping to 1.5 now. If this looks good, we could keep that!
                
                double coneTranslation = (distance/2) - (symbolSize*1.5);
                //double coneTranslation = (distance/2) - (symbolSize*2.0); 
                
                double weight = (Double)arc.getValue();
                
                double cylinderRadius = widthMapper.transform(weight);
                double coneRadius = cylinderRadius * 5; //To be removed
                
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
                    writeLine("                  <Material diffuseColor='"+red+" "+green+" "+blue+"'/>");
                    writeLine("                </Appearance>");
                    writeLine("                <Cylinder radius='"+cylinderRadius+"' height='"+cylinderHeight+"' top='false'/>");
                    writeLine("              </Shape>");                


                    writeLine("              <Transform translation='0 "+coneTranslation+" 0'>");
                    writeLine("                <Shape render=\"true\" DEF=\"arrowConeShape\" " +"data-class=\"relation\" " + "data-firstId=\""+firstId+"\""+" data-secondId=\""+secondId+"\">");
                    writeLine("                  <Appearance>");
                    writeLine("                    <Material diffuseColor='"+red+" "+green+" "+blue+"'/>");
                    writeLine("                  </Appearance>"); 
                    writeLine("                  <Cone bottomRadius='"+coneRadius+"' height='"+coneHeight+"' top='true'/>");
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
                if ("Sphere".equals(symbolType)) {
                    writeLine("    <Transform translation='" + point.getX() + " " + point.getY() + " " + point.getZ() + "'>");
                    writeLine("      <Shape render=\"true\" DEF=\"sphereShape\" " +"data-class=\"feature\" " + "data-index=\""+indexPoint+"\""+">");
                    writeLine("        <Appearance>");
                    writeLine("          <Material diffuseColor=\"" + symbolColor + "\"/>");
                    writeLine("        </Appearance>");
                    writeLine("        <Sphere radius='" + symbolSize + "'/>");
                }
                else if ("Box".equals(symbolType)) {
                    writeLine("    <Transform translation='" + point.getX() + " " + point.getY() + " " + point.getZ() + "'>");
                    writeLine("      <Shape render=\"true\" DEF=\"boxShape\" " +"data-class=\"feature\" " + "data-index=\""+indexPoint+"\""+">");
                    writeLine("        <Appearance>");
                    writeLine("          <Material diffuseColor=\"" + symbolColor + "\"/>");
                    writeLine("        </Appearance>");
                    writeLine("        <Box size='" + symbolSize + "'/>");
                }
                else {
                    writeLine("    <Transform translation='" + point.getX() + " " + point.getY() + " " + point.getZ() + "'>");
                    writeLine("      <Shape render=\"true\" DEF=\"sphereShape\" " +"data-class=\"feature\" " + "data-index=\""+indexPoint+"\""+">");
                    writeLine("        <Appearance>");
                    writeLine("          <Material diffuseColor=\"" + symbolColor + "\"/>");
                    writeLine("        </Appearance>");
                    writeLine("        <Sphere radius='" + defaultSymbolSize + "'/>"); // If an incorrect symbol type has been specified
                }
                
                writeLine("      </Shape>");
                writeLine("    </Transform>");
                writeLine();

                writeLine("    <Transform translation='" + (point.getX() + displacementX) + " " + (point.getY() + displacementY) + " " + (point.getZ() + displacementZ)+ "'" 
                			+ " scale='" + svgMap.get("font-size") + " " + svgMap.get("font-size") + " " + svgMap.get("font-size") + "'>");
                //@ToDo: Instead of Hardcoding the axisOfRotation, we should include it in the XML file
                writeLine("        <Billboard axisOfRotation='0 0 0'>");
                writeLine("          <Shape DEF=\"labelText\" data-class=\"information\">");
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
                
                writeLine("<br>");
                writeLine("<br>");
                
                writeLine("<table id = \"colorTable\">");
                writeLine("<thead>");
                writeLine("<tr><th onclick = \"sortColorFunction()\">Input Colour</th><th>Output Color</th></tr>");
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
                
                writeLine("<br>");
                writeLine("<br>");
                
                writeLine("<table id = \"widthTable\">");
                writeLine("<thead>");
                writeLine("<tr><th onclick = \"sortWidthFunction()\">Input Width</th><th>Output Width</th></tr>");
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
                
                writeLine("<br>");
                writeLine("<br>");
                
                writeLine("Input Color: <input type=\"text\" id=\"NewInputColor\">");
                writeLine("Output Color: <input type=\"text\" id=\"NewOutputColor\">");
                writeLine("<br>");
                writeLine("<br>");
                writeLine("<button id=\"ColorAddButton\" onclick=\"addNewColor()\">Add Color</button>");
                writeLine("<button id=\"ColorAddButton\" onclick=\"deleteNewColor()\">Delete Color</button>");
                
                writeLine("<br>");
                writeLine("<br>");

                writeLine("Input Width: <input type=\"text\" id=\"NewInputWidth\">");
                writeLine("Output Width: <input type=\"text\" id=\"NewOutputWidth\">");
                writeLine("<br>");
                writeLine("<br>");
                writeLine("<button id=\"WidthAddButton\" onclick=\"addNewWidth()\">Add Width</button>");
                writeLine("<button id=\"WidthDeleteButton\" onclick=\"deleteNewWidth()\">Delete Width</button>");
                
                writeLine("<br>");
                writeLine("<br>");
                
                writeLine("<h3>Last clicked object:</h3> ");
                writeLine("<span id=\"lastClickedObject\">-</span>");
                
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
