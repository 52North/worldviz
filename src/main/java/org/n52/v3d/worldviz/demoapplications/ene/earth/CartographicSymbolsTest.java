package org.n52.v3d.worldviz.demoapplications.ene.earth;

import java.util.ArrayList;
import java.util.List;

import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.t3dutil.T3dColor;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.worldviz.dataaccess.load.DatasetLoader;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.extensions.mappers.AttributeValuePair;
import org.n52.v3d.worldviz.extensions.mappers.MpAttrFeature2AttrSymbol;
import org.n52.v3d.worldviz.extensions.mappers.MpValue2ColoredSymbol;
import org.n52.v3d.worldviz.extensions.mappers.MpValue2ScaledSymbol;
import org.n52.v3d.worldviz.extensions.mappers.T3dAttrSymbolInstance;
import org.n52.v3d.worldviz.helper.RelativePaths;
import org.n52.v3d.worldviz.projections.AxisSwitchTransform;
import org.n52.v3d.worldviz.worldscene.VsCartographicSymbolsOnASphereScene;

public class CartographicSymbolsTest {

	private static String attributeName = "INES scale level";

	private static String dataXML = RelativePaths.NUCLEAR_ACCIDENTS_XML;
	private static String outputFile = "test/NuclearAccidents.x3d";
	private static double minValue;
	private static double maxValue;

	public static void main(String[] args) {

		long startTime = System.currentTimeMillis();

		XmlDataset nuclearAccidents = null;

		DatasetLoader nuclearAccidentsLoader = new DatasetLoader(dataXML);

		try {
			nuclearAccidents = nuclearAccidentsLoader.loadDataset();
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<VgAttrFeature> features = nuclearAccidents.getFeatures();

		MpAttrFeature2AttrSymbol symbolMapper = new MpAttrFeature2AttrSymbol();
		List<T3dAttrSymbolInstance> attrSymbols = new ArrayList<T3dAttrSymbolInstance>(
				features.size());

		setMinMaxForAttribute(features);

		MpValue2ScaledSymbol symbolScaleMapper = new MpValue2ScaledSymbol();

		// set palette
		double[] inputValues = new double[] { minValue, maxValue };
		double[] outputFactors = new double[] { 0.1, 1 };
		symbolScaleMapper.setPalette(inputValues, outputFactors);

		MpValue2ColoredSymbol symbolColorMapper = new MpValue2ColoredSymbol();
		double[] inputValuesForColor = new double[] { minValue, maxValue };
		T3dColor[] outputColors = new T3dColor[] { new T3dColor(1, 1, 0),
				new T3dColor(1, 0, 0) };
		symbolColorMapper.setPalette(inputValuesForColor, outputColors);

		AxisSwitchTransform axisSwitch = new AxisSwitchTransform();

		for (VgAttrFeature vgAttrFeature : features) {
			// the geometry in THIS case is a point, thus we may cast it
			// normally an instanceOf-check must be done
			VgPoint geometry = (VgPoint) vgAttrFeature.getGeometry();

			// point in virtual world (axes need to be switched.)
			T3dVector virtualPoint = axisSwitch.transform(geometry);

			T3dAttrSymbolInstance sphereSymbol = symbolMapper
					.createSphereSymbol(
							vgAttrFeature,
							new GmPoint(virtualPoint.getX(), virtualPoint
									.getY(), virtualPoint.getZ()));

			Object attributeValue = vgAttrFeature
					.getAttributeValue(attributeName);

			AttributeValuePair attrValuePair = new AttributeValuePair(
					attributeName, attributeValue);

			// TODO scale parameter anpassen und experimentieren
			T3dAttrSymbolInstance scaledSymbol = symbolScaleMapper.scaleTotal(
					sphereSymbol, attrValuePair);

			// grundrissebene kleiner machen!
			// scaledSymbol.setxScale(0.1);
			// scaledSymbol.setzScale(0.1);

			T3dAttrSymbolInstance scaledColoredSymbol = symbolColorMapper
					.transform(scaledSymbol, attrValuePair);

			attrSymbols.add(scaledColoredSymbol);
		}

		VsCartographicSymbolsOnASphereScene scene = new VsCartographicSymbolsOnASphereScene(
				outputFile);

		for (T3dAttrSymbolInstance attrSymbol : attrSymbols) {

			scene.addCartographicSymbol(attrSymbol);
		}

		scene.setRadius(10);

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
