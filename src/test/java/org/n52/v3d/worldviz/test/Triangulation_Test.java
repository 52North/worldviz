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
import org.n52.v3d.worldviz.triturusextensions.VgMultiPolygon;

public class Triangulation_Test {

	XmlDataset carbonEmissionsPerCapita;

	@Before
	public void before() {
		DatasetLoader countryCodeTest = new DatasetLoader(
				RelativePaths.CARBON_EMISSIONS_PER_CAPITA_XML);

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

		List<VgIndexedTIN> vgTINs = PolygonTriangulator
				.triangulateMultiPolygon(multiPolygon);

		assertTrue(vgTINs.size() > 0);

	}

}
