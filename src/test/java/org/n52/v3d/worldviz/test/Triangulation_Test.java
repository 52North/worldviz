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
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.n52.v3d.triturus.vgis.VgGeomObject;
import org.n52.v3d.triturus.vgis.VgIndexedTIN;
import org.n52.v3d.worldviz.dataaccess.load.DatasetLoader;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.helper.RelativePaths;
import org.n52.v3d.worldviz.triangulation.PolygonTriangulator;
import org.n52.v3d.worldviz.extensions.VgMultiPolygon;

public class Triangulation_Test {

	XmlDataset carbonEmissionsPerCapita;

	@Before
	public void before() {
		DatasetLoader countryCodeTest = new DatasetLoader(RelativePaths.CARBON_EMISSIONS_PER_CAPITA_XML);

		try {
			carbonEmissionsPerCapita = countryCodeTest.loadDataset();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test() {

		assertTrue(carbonEmissionsPerCapita != null);

		List<VgAttrFeature> features = carbonEmissionsPerCapita.getFeatures();

		VgAttrFeature firstFeature = features.get(0);

		VgGeomObject geometry = firstFeature.getGeometry();

		assertTrue(geometry instanceof VgMultiPolygon);

		VgMultiPolygon multiPolygon = (VgMultiPolygon) geometry;

		List<VgIndexedTIN> vgTINs = PolygonTriangulator.triangulateMultiPolygon(multiPolygon);

		assertTrue(vgTINs.size() > 0);

	}

}
