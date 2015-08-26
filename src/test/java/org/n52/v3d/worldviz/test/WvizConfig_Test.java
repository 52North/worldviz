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
package org.n52.v3d.worldviz.test;

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.util.HashMap;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import org.junit.Before;

import org.junit.Test;
import org.n52.v3d.worldviz.featurenet.xstream.*;

public class WvizConfig_Test {

    //Adhitya: This should be later moved to the RelativePaths Class
    public static final String filePath = "data\\WvizConfig.xml";
    
    private WvizConfig wvizConfig;

    @Before
    public void parseWvizConfigFile() {
            File file = new File(filePath);
            XStream xStream = new XStream();
            xStream.processAnnotations(WvizConfig.class);
            wvizConfig = (WvizConfig) xStream.fromXML(file);
    }

    @Test
    public void testContentsOfParsedWvizConfigNotNull() {
            assertTrue(wvizConfig != null);

            String symbolType, normalColor, propertyName, geometryType;
            
            double symbolSize;

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
            
            normalColor = t3dSymbol.getNormalColor();
            
            assertTrue(normalColor != null);
            
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
            symbolSize = t3dSymbol.getSize();
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
}
