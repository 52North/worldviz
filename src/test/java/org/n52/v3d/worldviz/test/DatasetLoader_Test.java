package org.n52.v3d.worldviz.test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.triturus.vgis.VgPolygon;
import org.n52.v3d.worldviz.dataaccess.load.DatasetLoader;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.dataaccess.load.dataset.helper.Unit;
import org.n52.v3d.worldviz.helper.RelativePaths;
import org.n52.v3d.worldviz.triturusextensions.VgMultiPolygon;

/**
 * JUnit-test class for testing the {@link DatasetLoader} component of the
 * ENE-processing-unit.
 * 
 * @author Christian Danowski
 * 
 */
public class DatasetLoader_Test {

	/**
	 * Tests to load bitmap-data (file EarthAtNight.xml)
	 */
	@Test
	public void testBitmap() {
		DatasetLoader bitmapTest = new DatasetLoader(
				RelativePaths.EARTH_AT_NIGHT_XML);

		try {
			XmlDataset bitmapDataset = bitmapTest.loadDataset();

			assertTrue(bitmapDataset != null);
			assertThat(bitmapDataset.getTitles()[0],
					org.hamcrest.CoreMatchers
							.is("Nocturnal lighting situation"));
			assertThat(bitmapDataset.getMetadataReference(),
					org.hamcrest.CoreMatchers.is("EarthAtNight_meta.xml"));

			Unit unit = bitmapDataset.getUnit();
			// has no unit and thus no code and titles
			assertTrue(unit.getCode() == null);
			assertTrue(unit.getTitles().length == 0);

			List<VgAttrFeature> bitmapFeatures = bitmapDataset.getFeatures();
			// only one feature, because it is a bitmap, that should only have
			// one feature (with bitmap-parameters)
			assertTrue(bitmapFeatures.size() == 1);

			VgAttrFeature bitmapFeature = bitmapFeatures.get(0);
			assertTrue(bitmapFeature.getGeometry() instanceof VgPolygon);

			String[] attributeNames = bitmapFeature.getAttributeNames();

			assertTrue(attributeNames[0].equals("Spatial extent"));
			assertTrue(attributeNames[1].equals("File name"));
			assertTrue(attributeNames[2].equals("Image size"));

			Object fileNameValue = bitmapFeature
					.getAttributeValue(attributeNames[1]);
			assertTrue(fileNameValue.toString().equals("EarthAtNight.jpg"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Tests to load point-data (file NuclearAccidents.xml)
	 */
	@Test
	public void testPoint() {
		DatasetLoader pointTest = new DatasetLoader(
				RelativePaths.NUCLEAR_ACCIDENTS_XML);

		try {
			XmlDataset pointDataset = pointTest.loadDataset();

			assertTrue(pointDataset != null);
			assertThat(pointDataset.getTitles()[0],
					org.hamcrest.CoreMatchers.is("Nuclear accidents"));
			assertThat(pointDataset.getMetadataReference(),
					org.hamcrest.CoreMatchers.is("NuclearAccidents_meta.xml"));

			Unit unit = pointDataset.getUnit();
			// has no unit and thus no code and titles
			assertTrue(unit.getCode() == null);
			assertTrue(unit.getTitles().length == 0);

			List<VgAttrFeature> pointFeatures = pointDataset.getFeatures();
			// multiple point features
			assertTrue(pointFeatures.size() > 1);

			VgAttrFeature pointFeature = pointFeatures.get(0);
			assertTrue(pointFeature.getGeometry() instanceof VgPoint);

			String[] attributeNames = pointFeature.getAttributeNames();

			assertTrue(attributeNames[0].equals("ID"));
			assertTrue(attributeNames[1].equals("Longitude,Latitude"));
			assertTrue(attributeNames[2].equals("Place"));
			assertTrue(attributeNames[3].equals("Year"));
			assertTrue(attributeNames[4].equals("INES scale level"));

			Object placeNameValue = pointFeature
					.getAttributeValue(attributeNames[2]);
			assertTrue(placeNameValue.toString().equals("New Mexico"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Tests to load countryCode-data (file CarbonEmissionsPerCapita.xml)
	 */
	@Test
	public void testCountryCode() {

		DatasetLoader countryCodeTest = new DatasetLoader(
				RelativePaths.CARBON_EMISSIONS_PER_CAPITA_XML);
		try {
			XmlDataset countryCodeDataset = countryCodeTest.loadDataset();

			assertTrue(countryCodeDataset != null);
			assertThat(countryCodeDataset.getTitles()[0],
					org.hamcrest.CoreMatchers.is("Carbon emissions per capita"));
			assertThat(countryCodeDataset.getMetadataReference(),
					org.hamcrest.CoreMatchers
							.is("CarbonEmissionsPerCapita_meta.xml"));

			Unit unit = countryCodeDataset.getUnit();
			// has no unit and thus no code and titles
			assertTrue(unit.getCode().equals("t/a"));
			assertTrue(unit.getTitles()[0].equals("Metric Tons per capita per year"));

			List<VgAttrFeature> countryCodeFeatures = countryCodeDataset
					.getFeatures();
			// multiple point features
			assertTrue(countryCodeFeatures.size() > 1);

			VgAttrFeature countryCodeFeature = countryCodeFeatures.get(0);
			assertTrue(countryCodeFeature.getGeometry() instanceof VgMultiPolygon);

			String[] attributeNames = countryCodeFeature.getAttributeNames();

			assertTrue(attributeNames[0].equals("Country code"));
			assertTrue(attributeNames[1].equals("1960"));
			assertTrue(attributeNames[2].equals("1961"));
			assertTrue(attributeNames[3].equals("1962"));

			Object year1960Value = countryCodeFeature
					.getAttributeValue(attributeNames[1]);
			assertTrue(year1960Value.toString().equals("NODATA"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
