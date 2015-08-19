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
 * icense version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.v3d.worldviz.featurenet.impl;

import java.util.ArrayList;

import org.n52.v3d.worldviz.featurenet.VgRelation;

import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.vgis.VgFeature;

public class PajekReader extends FileIO {

    /**
     * reads a feature net given in Pajek NET format from a file.
     *
     * @param fileName File path and name
     * @return Feature-net
     */
    public WvizUniversalFeatureNet readFromFile(String fileName) throws PajekException {
        
        PajekReader pajekReader = new PajekReader();
        pajekReader.init(fileName);
        
        if (pajekReader.readFile(pajekReader.getReader())) {
            ArrayList<Edge> edges = pajekReader.getEdges();
            ArrayList<Vertex> vertices = pajekReader.getVertices();
            ArrayList<Arc> arcs = pajekReader.getArcs();

            ArrayList<VgFeature> features = new ArrayList<VgFeature>();
            ArrayList<VgRelation> relations = new ArrayList<VgRelation>();
            
            for (Vertex vertex : vertices) {
                VgFeature node = new GmAttrFeature();
                ((GmAttrFeature) node).setGeometry(new GmPoint(vertex.getX(), vertex.getY(), vertex.getZ()));
                ((GmAttrFeature) node).addAttribute("name", "String", vertex.getLabel());
                features.add(node);
            }
            
            logger.info("Added Vertices into FeatureNet");

            for (Edge e : edges) {
                int first = e.getFirstVertex();
                int second = e.getSecondVertex();

                /*This is under the assumption that all nodes that are present in vertexArrayList are in proper order (1,2,...n) 
                 If this is not the case, then a sorting operation can be performed on the list based on vertexNumber
                 We do vertexNumber-1 in nodes.get() because Pajek is 1-indexed while Java is 0-indexed
                 */
                VgRelation edge = new WvizConnection(features.get(first - 1), features.get(second - 1), e.getWeight());
                relations.add(edge);
            }
            
            logger.info("Added Edges into FeatureNet");
            
            for (Arc a : arcs) {
                int first = a.getFirstVertex();
                int second = a.getSecondVertex();

                /*This is under the assumption that all nodes that are present in vertexArrayList are in proper order (1,2,...n) 
                 In case if this is not the case, then a sorting operation can be performed on the list based on vertexNumber
                 We do vertexNumber-1 in nodes.get() because Pajek is 1-indexed while Java is 0-indexed
                 */
                VgRelation arc = new WvizFlow(features.get(first - 1), features.get(second - 1), a.getWeight());
                relations.add(arc);
            }
            
            logger.info("Added Arcs into FeatureNet");

            WvizUniversalFeatureNet net = new WvizUniversalFeatureNet(features, relations);
            return net;

        }
        else {
            return null;
        }
    }
    
}
