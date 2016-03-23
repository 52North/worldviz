/**
 * Copyright (C) 2015-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *  - Apache License, version 2.0
 *  - Apache Software License, version 1.0
 *  - GNU Lesser General Public License, version 3
 *  - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *  - Common Development and Distribution License (CDDL), version 1.0.
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.v3d.worldviz.featurenet.scene;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.t3dutil.T3dColor;
import org.n52.v3d.triturus.vgis.VgFeature;

import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.BackgroundWorldMap;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Features;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Features.PointVisualizer;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Features.PointVisualizer.T3DSymbol;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Features.TextVisualizer;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Features.TextVisualizer.Fill;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Features.TextVisualizer.Font;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Features.TextVisualizer.Font.SvgParameter;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Features.TextVisualizer.Label;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Features.TextVisualizer.LabelPlacement;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Features.TextVisualizer.LabelPlacement.PointPlacement;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Features.TextVisualizer.LabelPlacement.PointPlacement.Displacement;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Relations;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Relations.LineVisualizer;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Relations.LineVisualizer.ColorMapper;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Relations.LineVisualizer.ColorMapper.ColorPalette;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Relations.LineVisualizer.ColorMapper.ColorPalette.ColorEntry;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Relations.LineVisualizer.ColorMapper.ColorPalette.ColorEntry.OutputColor;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Relations.LineVisualizer.ColorMapper.InterpolationMode;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Relations.LineVisualizer.Geometry;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Relations.LineVisualizer.WidthMapper;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Relations.LineVisualizer.WidthMapper.WidthPalette;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Relations.LineVisualizer.WidthMapper.WidthPalette.WidthEntry;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.Viewpoint;

/**
 * Abstract base class for 3D 'Connection Map' scene descriptions. The class
 * holds common attributes and methods.
 *
 * @author Christian Danowski, Adhitya Kamakshidasan
 *
 */
public abstract class WvizConcreteConnectionMapScene {

    private static final String LINEAR_INTERPOLATION_CONFIG_STRING = "linear";

	protected WvizVirtualConnectionMapScene scene;

    protected String symbolType;

    protected String skyColor;
    
    protected boolean isUseWorldMap;
    
    protected String texturePath;
    
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
    
    public double ribbonCreaseAngle, curveCreaseAngle , curveRatio, heightRatio, radiusRatio, ribbonStep;

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
        
        // Background
        skyColor = wvizConfig.getBackground().getSkyColor();
        
        //Viewpoint
        Viewpoint viewpoint = wvizConfig.getViewpoint();
        position = viewpoint.getPosition();
        orientation = viewpoint.getOrientation();
        
        //Connection Net
        ConnectionNet connectionNet = wvizConfig.getConnectionNet();
        BackgroundWorldMap backgroundWorldMap = connectionNet.getBackgroundWorldMap();
        isUseWorldMap = backgroundWorldMap.isSetUseWorldMap();
        texturePath = backgroundWorldMap.getTexturePath();
        Mapper mapper = connectionNet.getMapper();
        Features features = mapper.getFeatures();
        PointVisualizer pointVisualizer = features.getPointVisualizer();
        T3DSymbol t3dSymbol = pointVisualizer.getT3DSymbol();
        symbolType = t3dSymbol.getType();
        symbolSize = t3dSymbol.getSize();
        
        normalColor = t3dSymbol.getNormalColor();
        currentColor = t3dSymbol.getCurrentColor();
        highlightColor = t3dSymbol.getHighlightColor();
        
        normalGlow = t3dSymbol.getNormalGlow();
        currentGlow = t3dSymbol.getCurrentGlow();
        highlightGlow = t3dSymbol.getHighlightGlow();
        
        TextVisualizer textVisualizer = features.getTextVisualizer();
        Label label = (Label) textVisualizer.getLabel();
        propertyName = label.getPropertyName();

        Font font = textVisualizer.getFont();
        SvgParameter[] svgParameter = font.getSvgParameterArray();

        for (Object object : svgParameter) {
            String name = ((SvgParameter) object).getName();
            String value = ((SvgParameter) object).getStringValue();
            svgMap.put(name, value);
        }

        LabelPlacement labelPlacement = textVisualizer.getLabelPlacement();
        billboardAxis = labelPlacement.getBillboardAxis();
        PointPlacement pointPlacement = labelPlacement.getPointPlacement();
        Displacement displacement = pointPlacement.getDisplacement();

        displacementX = displacement.getDisplacementX();
        displacementY = displacement.getDisplacementY();
        displacementZ = displacement.getDisplacementZ();

        Fill fill = textVisualizer.getFill();
        de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Features.TextVisualizer.Fill.SvgParameter svgParameter_fill;
        svgParameter_fill = fill.getSvgParameter();

        String name = svgParameter_fill.getName();
        String value = svgParameter_fill.getStringValue();
        svgMap.put(name, value);

        //Relations
        Relations relations = mapper.getRelations();
        LineVisualizer lineVisualizer = relations.getLineVisualizer();
        Geometry[] geometries = lineVisualizer.getGeometryArray();
        
        for(Geometry geometry:geometries){
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
                heightRatio = geometry.getHeightRatio();
                radiusRatio = geometry.getRadiusRatio();
            }
        }

        ColorMapper colorMapper = lineVisualizer.getColorMapper();
        InterpolationMode colorInterpolationMode = colorMapper.getInterpolationMode(); 
        
        String colorInterpolationType = colorInterpolationMode.getType();
        
        if (LINEAR_INTERPOLATION_CONFIG_STRING.equals(colorInterpolationType.toLowerCase()) ){
            linearColorInterpolation = true;
        }
        
        ColorPalette colorPalette = colorMapper.getColorPalette();
        
        ColorEntry[] colorEntries = colorPalette.getColorEntryArray();
        
        ArrayList<Double> inputColorValueList = new ArrayList<Double>();
        ArrayList<T3dColor> outputColorValueList = new ArrayList<T3dColor>();
        float red,blue,green;
        
        for (ColorEntry colorEntry : colorEntries) {
            double inputValue = colorEntry.getInputValue();
            OutputColor outputColor = colorEntry.getOutputColor();
            String colorType = outputColor.getType();
            String colorValue = outputColor.getStringValue();
            
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

        
        WidthMapper widthMapper = lineVisualizer.getWidthMapper();
        de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.ConnectionNet.Mapper.Relations.LineVisualizer.WidthMapper.InterpolationMode widthInterpolationMode = widthMapper.getInterpolationMode(); 
        
        String widthInterpolationType = widthInterpolationMode.getType();
        
        if (LINEAR_INTERPOLATION_CONFIG_STRING.equalsIgnoreCase(widthInterpolationType.toLowerCase()) ){
            linearWidthInterpolation = true;
        }
        
        WidthPalette widthPalette = widthMapper.getWidthPalette();
        
        WidthEntry[] widthEntries = widthPalette.getWidthEntryArray();
        
        ArrayList<Double> inputWidthValueList = new ArrayList<Double>();
        ArrayList<Double> outputWidthValueList = new ArrayList<Double>();
        
        
        for (WidthEntry widthEntry : widthEntries) {
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
