package org.n52.v3d.worldviz.demoapplications.ene.earth;

import java.util.List;

import org.n52.v3d.triturus.t3dutil.T3dColor;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.n52.v3d.worldviz.dataaccess.load.DatasetLoader;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.helper.RelativePaths;
import org.n52.v3d.worldviz.triturusextensions.mappers.MpValue2ColoredAttrFeature;
import org.n52.v3d.worldviz.worldscene.VsWorldCountriesScene;
import org.n52.v3d.worldviz.worldscene.helper.CountryBordersLODEnum;
import org.n52.v3d.worldviz.worldscene.helper.FindExtrudeAndColorMissingCountriesHelper;

public class AccessToElectricity_flat2D {

	private static String attributeName = "Access to Electricity 2000, percentage of people estimated";
	private static String dataXML = RelativePaths.ELECTRICITY_ACCESS_XML;
	private static String outputFile = "test/AccessToElectricity_flat.x3d";
	private static double minValue;
	private static double maxValue;
	private static double average;

	public static void main(String[] args) {

		long startTime = System.currentTimeMillis();

		XmlDataset electricityAccess = null;

		CountryBordersLODEnum countryBordersLOD = CountryBordersLODEnum.SIMPLIFIED_50m;

		DatasetLoader countryCodeTest = new DatasetLoader(dataXML,
				countryBordersLOD);

		try {
			electricityAccess = countryCodeTest.loadDataset();
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<VgAttrFeature> features = electricityAccess.getFeatures();

		setMinMaxForAttribute(features);

		MpValue2ColoredAttrFeature colorMapper = new MpValue2ColoredAttrFeature();

		colorMapper.setPalette(new double[] { 0, 50, maxValue },
				new T3dColor[] { new T3dColor(1f, 0f, 0f),
						new T3dColor(1f, 1f, 0f), new T3dColor(0f, 1f, 0f) }, true);

		List<VgAttrFeature> coloredTransfFeatures = colorMapper.transform(
				attributeName, features);

		FindExtrudeAndColorMissingCountriesHelper remainingCountriesColorer = new FindExtrudeAndColorMissingCountriesHelper(
				countryBordersLOD);
		List<VgAttrFeature> allWorldCountries = remainingCountriesColorer
				.findExtrudeAndColorMissingCountries(coloredTransfFeatures);

		VsWorldCountriesScene scene = new VsWorldCountriesScene(
				outputFile);

		for (VgAttrFeature coloredFeature : allWorldCountries) {

			scene.addWorldCountry(coloredFeature);
		}

		// this is for quick test purposes
		// scene.setxRasterWidth(10f);
		// scene.setyRasterWidth(10f);
		scene.setDrawBorders(false);

		System.out
				.println("Begin Scene generation. This might take some time (up to several minutes) because of the triangulation of all polygons!");
		scene.generateScene();

		System.out.println("Success!");
		long endTime = System.currentTimeMillis();

		System.out.println("required time: " + (endTime - startTime) / 1000
				+ "s");

		System.out.println("Average: " + average);
	}

	private static void setMinMaxForAttribute(List<VgAttrFeature> features) {
		double locMin = -1;
		double locMax = -1;
		double sum = 0;

		for (VgAttrFeature vgAttrFeature : features) {

			double value = Double.valueOf((String) vgAttrFeature
					.getAttributeValue(attributeName));

			sum += value;

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
		average = sum / features.size();

	}

}
