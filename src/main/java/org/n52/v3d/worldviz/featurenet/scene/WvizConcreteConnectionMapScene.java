package org.n52.v3d.worldviz.featurenet.scene;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.t3dutil.T3dColor;
import org.n52.v3d.triturus.vgis.VgFeature;
import org.n52.v3d.worldviz.featurenet.xstream.*;

/**
 * Abstract base class for 3D 'Connection Map' scene descriptions. The class
 * holds common attributes and methods.
 *
 * @author Christian Danowski, Adhitya Kamakshidasan
 *
 */
public abstract class WvizConcreteConnectionMapScene {

    protected WvizVirtualConnectionMapScene scene;

    protected String symbolType;

    protected String skyColor;
    
    protected String position, orientation;
    
    protected String normalColor, currentColor, highlightColor;
  
    protected String normalGlow, currentGlow, highlightGlow;
    
    protected String billboardAxis;

    protected double symbolSize;

    protected static final double defaultSymbolSize = 0.15;

    protected String propertyName;

    protected Map<VgFeature, String> labels = new HashMap<VgFeature, String>();

    // Cannot be viable once there are more properties
    protected Map<String, String> svgMap = new HashMap<String, String>();

    protected double displacementX, displacementY, displacementZ;

    // Currently, this file supports only Cylinders, when more parameters are
    // used, this parameter should be used and more if statements should be
    // added.
    public String geometryType;
    
    public boolean linearColorInterpolation = false;
    
    public boolean linearWidthInterpolation = false;

    public double[] inputColorValues, inputWidthValues, outputWidthValues;
    public T3dColor[] outputColorValues;
    
    protected BufferedWriter document;
    
    public double ribbonCreaseAngle, curveCreaseAngle , curveRatio, arrowConeHeight, arrowRatio, ribbonStep;

    public int ribbonCircleTurns, ribbonHelixTurns,curveCircleTurns, curveEllipseTurns;
    
    public WvizConcreteConnectionMapScene(WvizVirtualConnectionMapScene scene) {
        this.scene = scene;

        // Check in XML file, if there is a default configuration or not
        getDefaultConfiguration(scene);
    }

    private void getDefaultConfiguration(WvizVirtualConnectionMapScene scene) {
	// IMPORTANT: We will use 0, for default configuration

        // For specific configuration, we should change our current XML schema
        WvizConfig wvizConfig = scene.getStyle();
        
        Background background = (Background) wvizConfig.getBackground().get(0);
        skyColor = background.getSkyColor();
        Viewpoint viewpoint = (Viewpoint) wvizConfig.getViewpoint().get(0);
        position = viewpoint.getPosition();
        orientation = viewpoint.getOrientation();
        
        ConnectionNet connectionNet = (ConnectionNet) wvizConfig.getConnectionNet().get(0);
        Mapper mapper = (Mapper) connectionNet.getMapper().get(0);
        Features features = (Features) mapper.getFeatures().get(0);
        PointVisualizer pointVisualizer = (PointVisualizer) features.getPointVisualizer().get(0);
        T3dSymbol t3dSymbol = (T3dSymbol) pointVisualizer.getT3dSymbol().get(0);
        symbolType = t3dSymbol.getType();
        symbolSize = t3dSymbol.getSize();
        
        normalColor = t3dSymbol.getNormalColor();
        currentColor = t3dSymbol.getCurrentColor();
        highlightColor = t3dSymbol.getHighlightColor();
        
        normalGlow = t3dSymbol.getNormalGlow();
        currentGlow = t3dSymbol.getCurrentGlow();
        highlightGlow = t3dSymbol.getHighlightGlow();
        
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
        billboardAxis = labelPlacement.getBillboardAxis();
        PointPlacement pointPlacement = (PointPlacement) labelPlacement.getPointPlacement().get(0);
        Displacement displacement = (Displacement) pointPlacement.getDisplacement().get(0);

        displacementX = displacement.getDisplacementX();
        displacementY = displacement.getDisplacementY();
        displacementZ = displacement.getDisplacementZ();

        Fill fill = (Fill) textVisualizer.getFill().get(0);
        svgParameter = fill.getSvgParameter();

        for (Object object : svgParameter) {
            String name = ((SvgParameter) object).getName();
            String value = ((SvgParameter) object).getValue();
            svgMap.put(name, value);
        }

        Relations relations = (Relations) mapper.getRelations().get(0);
        LineVisualizer lineVisualizer = (LineVisualizer) relations.getLineVisualizer().get(0);
        List <Geometry> geometryList = (List <Geometry>) lineVisualizer.getGeometry();
        
        for(Geometry geometry:geometryList){
            geometryType = geometry.getType();
            if(geometryType.equalsIgnoreCase("ribbon")){
                ribbonCreaseAngle = geometry.getCreaseAngle();
                ribbonCircleTurns = geometry.getCircleTurns();
                ribbonHelixTurns = geometry.getHelixTurns();
                ribbonStep = geometry.getRibbonStep();
            }
            else if(geometryType.equalsIgnoreCase("curve")){
                curveCreaseAngle = geometry.getCreaseAngle();
                curveCircleTurns = geometry.getCircleTurns();
                curveEllipseTurns = geometry.getEllipseTurns();
                curveRatio = geometry.getRatio();
            }
            else if(geometryType.equalsIgnoreCase("arrow")){
                arrowConeHeight = geometry.getConeHeight();
                arrowRatio = geometry.getRatio();
            }
        }

        ColorMapper colorMapper = (ColorMapper) lineVisualizer.getColorMapper().get(0);
        InterpolationMode colorInterpolationMode = (InterpolationMode) colorMapper.getInterpolationMode().get(0); 
        
        String colorInterpolationType = colorInterpolationMode.getType();
        
        if ("linear".equals(colorInterpolationType.toLowerCase()) ){
            linearColorInterpolation = true;
        }
        
        ColorPalette colorPalette = (ColorPalette) colorMapper.getColorPalette().get(0);
        
        List colorEntries = colorPalette.getColorEntry();
        
        ArrayList<Double> inputColorValueList = new ArrayList<Double>();
        ArrayList<T3dColor> outputColorValueList = new ArrayList<T3dColor>();
        float red,blue,green;
        
        for (Object object : colorEntries) {
            ColorEntry colorEntry = (ColorEntry) object;
            double inputValue = colorEntry.getInputValue();
            OutputColor outputColor = (OutputColor)(colorEntry.getOutputColor().get(0));
            String colorType = outputColor.getType();
            String colorValue = outputColor.getValue();
            
            if(colorType.equalsIgnoreCase("rgb")){
                String[] parts = colorValue.split(" ");
                red = Float.parseFloat(parts[0]);
                green = Float.parseFloat(parts[1]);
                blue = Float.parseFloat(parts[2]);
                T3dColor color = new T3dColor(red, green, blue);
                outputColorValueList.add(color);
            }
            inputColorValueList.add(inputValue);
        }
        
        //Convert the ArrayList(s) into Arrays
        
        inputColorValues = new double[inputColorValueList.size()];
        outputColorValues = new T3dColor[outputColorValueList.size()];
        
        for (int i=0; i<inputColorValueList.size();i++){
            inputColorValues[i] = inputColorValueList.get(i);
        }
        
        for (int i=0; i<outputColorValueList.size();i++){
            outputColorValues[i] = outputColorValueList.get(i);
        }
        
        for (VgFeature feature : scene.getVertices()) {
            labels.put(feature, (String) ((GmAttrFeature) feature).getAttributeValue(propertyName));
        }

        
        WidthMapper widthMapper = (WidthMapper) lineVisualizer.getWidthMapper().get(0);
        InterpolationMode widthInterpolationMode = (InterpolationMode) widthMapper.getInterpolationMode().get(0); 
        
        String widthInterpolationType = widthInterpolationMode.getType();
        
        if ("linear".equalsIgnoreCase(widthInterpolationType.toLowerCase()) ){
            linearWidthInterpolation = true;
        }
        
        WidthPalette widthPalette = (WidthPalette) widthMapper.getWidthPalette().get(0);
        
        List widthEntries = widthPalette.getWidthEntry();
        
        ArrayList<Double> inputWidthValueList = new ArrayList<Double>();
        ArrayList<Double> outputWidthValueList = new ArrayList<Double>();
        
        
        for (Object object : widthEntries) {
            WidthEntry widthEntry = (WidthEntry) object;
            double inputValue = widthEntry.getInputValue();
            double outputWidth = widthEntry.getOutputWidth();
            inputWidthValueList.add(inputValue);
            outputWidthValueList.add(outputWidth);
        }
        
        //Convert the ArrayList(s) into Arrays
        
        inputWidthValues = new double[inputWidthValueList.size()];
        outputWidthValues = new double[outputWidthValueList.size()];
        
        for (int i=0; i<inputWidthValueList.size();i++){
            inputWidthValues[i] = inputWidthValueList.get(i);
        }
        
        for (int i=0; i<outputWidthValueList.size();i++){
            outputWidthValues[i] = outputWidthValueList.get(i);
        }
        
        for (VgFeature feature : scene.getVertices()) {
            labels.put(feature, (String) ((GmAttrFeature) feature).getAttributeValue(propertyName));
        }

    }

    /**
     *
     * exports the scene description to a file.
     *
     * @param fileName File name (and path)
     */
    public abstract void writeToFile(String fileName);

    protected void writeLine(String pLine) {
        try {
            document.write(pLine);
            document.newLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    protected void writeLine() {
        try {
            document.newLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
