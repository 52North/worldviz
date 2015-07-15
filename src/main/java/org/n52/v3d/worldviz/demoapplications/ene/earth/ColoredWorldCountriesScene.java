package org.n52.v3d.worldviz.demoapplications.ene.earth;

import java.util.List;

import org.n52.v3d.worldviz.dataaccess.load.DatasetLoader;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.helper.RelativePaths;
import org.n52.v3d.worldviz.extensions.mappers.MpValue2ColoredAttrFeature;
import org.n52.v3d.worldviz.worldscene.VsWorldCountriesOnASphereScene;

import org.n52.v3d.triturus.vgis.VgAttrFeature;

public class ColoredWorldCountriesScene {

	private static String attributeName = "Land";
	private static String dataXML = RelativePaths.COUNTRY_CODES_XML;
	private static String outputFile = "test/Countries.x3d";
	private static double minValue;
	private static double maxValue;

	public static void main(String[] args) {

		long startTime = System.currentTimeMillis();

		XmlDataset carbonEmissionsPerCapita = null;

		DatasetLoader countryCodeTest = new DatasetLoader(dataXML);

		try {
			carbonEmissionsPerCapita = countryCodeTest.loadDataset();
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<VgAttrFeature> features = carbonEmissionsPerCapita.getFeatures();

		// setMinMaxForAttribute(features);

		MpValue2ColoredAttrFeature colorMapper = new MpValue2ColoredAttrFeature();

		// colorMapper.setPalette(new double[] { minValue,
		// (maxValue + minValue) / 2, maxValue }, new T3dColor[] {
		// new T3dColor(1f, 0f, 0f), new T3dColor(1f, 1f, 0f),
		// new T3dColor(0f, 1f, 0f) });

		List<VgAttrFeature> coloredTransfFeatures = colorMapper.transform(
				attributeName, features);

		VsWorldCountriesOnASphereScene scene = new VsWorldCountriesOnASphereScene(
				outputFile);

		for (VgAttrFeature coloredFeature : coloredTransfFeatures) {

			scene.addWorldCountry(coloredFeature);
		}

		scene.setxRasterWidth(1.5f);
		scene.setyRasterWidth(1.5f);

		scene.generateScene();

		System.out.println("Success!");
		long endTime = System.currentTimeMillis();

		System.out.println("required time: " + (endTime - startTime) / 1000
				+ "s");
	}

	private static void setMinMaxForAttribute(List<VgAttrFeature> features) {
		double locMin = -1;
		double locMax = -1;

		for (VgAttrFeature vgAttrFeature : features) {

			String attributeValueAsString = (String) vgAttrFeature
					.getAttributeValue(attributeName);
			double value = Double.valueOf(attributeValueAsString);

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

	}

}
