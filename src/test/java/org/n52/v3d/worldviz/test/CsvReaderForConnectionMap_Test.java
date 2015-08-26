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

import org.hamcrest.core.IsNull;
import org.hamcrest.number.OrderingComparison;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.n52.v3d.worldviz.featurenet.impl.CsvReaderForConnectionMap;
import org.n52.v3d.worldviz.featurenet.impl.WvizUniversalFeatureNet;
import org.n52.v3d.worldviz.helper.RelativePaths;

public class CsvReaderForConnectionMap_Test {

	private static CsvReaderForConnectionMap csvReaderForConnectionMap;

	private static WvizUniversalFeatureNet featureNet;

	@Before
	public void parseFeatureNetFromCsv() {
		csvReaderForConnectionMap = new CsvReaderForConnectionMap();

		featureNet = csvReaderForConnectionMap.readFromFile(
				RelativePaths.IMPORTS_PARTNER_CSV, ';');
	}

	@Test
	public void testFeatureNetIsNotNull() {
		Assert.assertThat("FeatureNet is not null", featureNet,
				IsNull.notNullValue());
	}
	
	@Test
	public void testFeatureNetIsNotEmpty() {
		Assert.assertThat("Relations-count is greater 0", featureNet
				.getRelations().size(), OrderingComparison.greaterThan(0));

		Assert.assertThat("Feature-count is greater 0", featureNet
				.getFeatures().size(), OrderingComparison.greaterThan(0));	
	}

}
