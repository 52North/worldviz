package org.n52.v3d.worldviz.demoapplications.ene.earth;

import java.util.List;

import org.n52.v3d.triturus.t3dutil.T3dColor;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.n52.v3d.worldviz.dataaccess.load.DatasetLoader;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.helper.RelativePaths;
import org.n52.v3d.worldviz.triturusextensions.mappers.MpValue2ColoredAttrFeature;
import org.n52.v3d.worldviz.worldscene.VsWorldCountriesOnASphereScene;
import org.n52.v3d.worldviz.worldscene.helper.CountryBordersLODEnum;
import org.n52.v3d.worldviz.worldscene.helper.FindExtrudeAndColorMissingCountriesHelper;

public class CarbonEmissionsPerCapita_average {

	private static String attributeName = "1990";
	private static String dataXML = RelativePaths.CARBON_EMISSIONS_PER_CAPITA_XML;
	private static String outputFile = "test/CarbonEmissionsPerCapita1990_new.x3d";
	private static double minValue;
	private static double maxValue;
	private static double average;

	public static void main(String[] args) {

		long startTime = System.currentTimeMillis();

		XmlDataset carbonEmissions = null;

		CountryBordersLODEnum worldBordersLOD = CountryBordersLODEnum.SIMPLIFIED_50m;

		DatasetLoader countryCodeTest = new DatasetLoader(dataXML,
				worldBordersLOD);

		try {
			carbonEmissions = countryCodeTest.loadDataset();
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<VgAttrFeature> features = carbonEmissions.getFeatures();

		setMinMaxForAttribute(features);

		MpValue2ColoredAttrFeature colorMapper = new MpValue2ColoredAttrFeature();

		colorMapper.setPalette(new double[] { minValue, average, maxValue },
				new T3dColor[] { new T3dColor(0f, 1f, 0f),
						new T3dColor(1f, 1f, 0f), new T3dColor(1f, 0f, 0f) }, true);

		List<VgAttrFeature> coloredTransfFeatures = colorMapper.transform(
				attributeName, features);

		FindExtrudeAndColorMissingCountriesHelper remainingCountriesColorer = new FindExtrudeAndColorMissingCountriesHelper(
				worldBordersLOD);
		List<VgAttrFeature> allWorldCountries = remainingCountriesColorer
				.findExtrudeAndColorMissingCountries(coloredTransfFeatures);

		VsWorldCountriesOnASphereScene scene = new VsWorldCountriesOnASphereScene(
				outputFile);

		for (VgAttrFeature coloredFeature : allWorldCountries) {

			scene.addWorldCountry(coloredFeature);
		}

		// this is for quick test purposes
		// scene.setGenerateAdditionalInnerPolygonPoints(false);

		scene.generateScene();

		System.out.println("Success!");
		long endTime = System.currentTimeMillis();

		System.out.println("benötigte Zeit: " + (endTime - startTime) / 1000
				+ "s");

		System.out.println("Average: " + average);
		System.out.println("MaxValue: " + maxValue);
		System.out.println("MinValue: " + minValue);
	}

	private static void setMinMaxForAttribute(List<VgAttrFeature> features) {
		double locMin = -1;
		double locMax = -1;
		double sum = 0;
		int feature_size = 0;

		for (VgAttrFeature vgAttrFeature : features) {

			String attributeValue_string = (String) vgAttrFeature
					.getAttributeValue(attributeName);

			if (attributeValue_string.equalsIgnoreCase("NODATA"))
				continue;

			double value = Double.valueOf(attributeValue_string);

			sum += value;
			feature_size++;

			if (locMin == -1 && locMax == -1) {
				// erster Schritt
				locMin = value;
				locMax = value;
			} else {
				// jeder weitere Schritt
				if (value < locMin)
					locMin = value;
				else if (value > locMax)
					locMax = value;
			}

		}

		maxValue = locMax;
		minValue = locMin;
		average = sum / feature_size;

	}

}
