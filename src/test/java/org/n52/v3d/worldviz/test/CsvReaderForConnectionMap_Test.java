package org.n52.v3d.worldviz.test;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.n52.v3d.worldviz.featurenet.impl.CsvReaderForConnectionMap;
import org.n52.v3d.worldviz.featurenet.impl.WvizUniversalFeatureNet;
import org.n52.v3d.worldviz.helper.RelativePaths;

public class CsvReaderForConnectionMap_Test {

	private static CsvReaderForConnectionMap csvReaderForConnectionMap;
	
	private static WvizUniversalFeatureNet featureNet;
	
	@Before
	public void before() {
		csvReaderForConnectionMap = new CsvReaderForConnectionMap();
		
		featureNet = csvReaderForConnectionMap.readFromFile(RelativePaths.IMPORTS_PARTNER_CSV, ';');
	}

	@Test
	public void test() {
		assertNotNull(featureNet);
		
		assertTrue(featureNet.getRelations().size() > 0);
		assertTrue(featureNet.getFeatures().size() > 0);
	}

}
