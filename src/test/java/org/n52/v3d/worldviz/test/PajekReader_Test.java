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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.vgis.VgFeature;
import org.n52.v3d.triturus.vgis.VgGeomObject;
import org.n52.v3d.worldviz.featurenet.VgRelation;
import org.n52.v3d.worldviz.featurenet.impl.PajekReader;
import org.n52.v3d.worldviz.featurenet.impl.WvizConnection;
import org.n52.v3d.worldviz.featurenet.impl.WvizFlow;
import org.n52.v3d.worldviz.featurenet.impl.WvizUniversalFeatureNet;

public class PajekReader_Test {

    public PajekReader reader;
    public WvizUniversalFeatureNet wvizuniversalfeaturenet;

    //Adhitya: This should be later moved to the RelativePaths Class
    public static final String filePath = "data\\Flows_of_trade.net";

    @Before
    public void before() {
        reader = new PajekReader();
        System.out.println("Using file: " + filePath);
        try {
            wvizuniversalfeaturenet = reader.readFromFile(filePath);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        try {
            assertTrue(wvizuniversalfeaturenet != null);

            List<VgFeature> features = (List<VgFeature>) (wvizuniversalfeaturenet.getFeatures());
            assertTrue(features != null);

            VgFeature firstFeature = features.get(0);
            assertTrue(firstFeature instanceof GmAttrFeature);
            VgGeomObject geometry = firstFeature.getGeometry();
            assertTrue(geometry != null);

            List<VgRelation> relations = (List<VgRelation>) (wvizuniversalfeaturenet.getRelations());
            assertTrue(relations != null);

            VgRelation firstRelation = relations.get(0);
            //This could be possibly changed in the future
            assertTrue((firstRelation instanceof WvizConnection) || (firstRelation instanceof WvizFlow));

            VgFeature firstVertex = firstRelation.getFrom();
            assertTrue(firstVertex instanceof GmAttrFeature);
            geometry = firstVertex.getGeometry();
            assertTrue(geometry != null);

            VgFeature secondVertex = firstRelation.getTo();
            assertTrue(secondVertex instanceof GmAttrFeature);
            geometry = secondVertex.getGeometry();
            assertTrue(geometry != null);

            //EdgeWeight can be null in nature - We are not testing it!
        }
        catch (Exception exception) {
            System.err.println(exception.getMessage());
        }

    }

}
