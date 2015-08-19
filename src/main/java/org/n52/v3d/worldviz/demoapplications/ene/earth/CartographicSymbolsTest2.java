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
 * icense version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.v3d.worldviz.demoapplications.ene.earth;

import java.util.ArrayList;
import java.util.List;

import org.n52.v3d.worldviz.dataaccess.load.DatasetLoader;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.helper.RelativePaths;
import org.n52.v3d.worldviz.extensions.mappers.MpAttrFeature2AttrSymbol;
import org.n52.v3d.worldviz.extensions.mappers.T3dAttrSymbolInstance;
import org.n52.v3d.worldviz.projections.AxisSwitchTransform;
import org.n52.v3d.worldviz.worldscene.VsCartographicSymbolsOnASphereScene;
import org.n52.v3d.worldviz.worldscene.VsCartographicSymbolsScene;
import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgAttrFeature;

public class CartographicSymbolsTest2 {

	
//	private static String attributeName = "INES scale level";
	private static String dataXML = RelativePaths.COUNTRIES_POINT_XML;
	private static String outputFile = "test/CountriesPointSymbols.x3d";
	
	private static String latAttr= "Latitude";
	private static String lonAttr= "Longitude";
	
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		XmlDataset countriesPoint = null;

		DatasetLoader countriesPointLoader = new DatasetLoader(dataXML);

		try {
			countriesPoint = countriesPointLoader.loadDataset();
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<VgAttrFeature> features = countriesPoint.getFeatures();

		MpAttrFeature2AttrSymbol symbolMapper = new MpAttrFeature2AttrSymbol();
		List<T3dAttrSymbolInstance> attrSymbols = new ArrayList<T3dAttrSymbolInstance>(
				features.size());
		
		AxisSwitchTransform axisSwitch = new AxisSwitchTransform();

		for (VgAttrFeature vgAttrFeature : features) {
			// the geometry in THIS case is a point, thus we may cast it
			// normally an instanceOf-check must be done
	
			
			double latitude = Double.parseDouble((String)vgAttrFeature.getAttributeValue(latAttr));
			double longitude = Double.parseDouble((String)vgAttrFeature.getAttributeValue(lonAttr));
			GmPoint gmPoint = new GmPoint(longitude, latitude, 0);
			
			//point in virtual world (axes need to be switched.)
			T3dVector virtualPoint = axisSwitch.transform(gmPoint);
			
			attrSymbols.add(symbolMapper.createConeSymbol(vgAttrFeature,
					new GmPoint(virtualPoint.getX(), virtualPoint.getY(), virtualPoint.getZ())));
		}

//		MpValue2ScaledSymbol symbolScaleMapper = new MpValue2ScaledSymbol();
//
//		// set palette
//		double[] inputValues = new double[] { minValue, maxValue };
//		double[] outputFactors = new double[] { 0.5, 5 };
//		symbolScaleMapper.setPalette(inputValues, outputFactors);
//
//		MpValue2ColoredSymbol symbolColorMapper = new MpValue2ColoredSymbol();
//		double[] inputValuesForColor = new double[] { minValue, maxValue };
//		T3dColor[] outputColors = new T3dColor[] { new T3dColor(1, 1, 0),
//				new T3dColor(1, 0, 0) };
//		symbolColorMapper.setPalette(inputValuesForColor, outputColors);

		for (int i = 0; i < features.size(); i++) {
			VgAttrFeature vgAttrFeature = features.get(i);

			T3dAttrSymbolInstance t3dAttrSymbolInstance = attrSymbols.get(i);

//			Object attributeValue = vgAttrFeature
//					.getAttributeValue(attributeName);
//
//			AttributeValuePair attrValuePair = new AttributeValuePair(
//					attributeName, attributeValue);
//
//			// TODO scale parameter anpassen und experimentieren
//			T3dAttrSymbolInstance scaledSymbol = symbolScaleMapper.scaleY(
//					t3dAttrSymbolInstance, attrValuePair);

			// grundrissebene kleiner machen!
			t3dAttrSymbolInstance.setxScale(0.1);
			t3dAttrSymbolInstance.setzScale(0.1);
			t3dAttrSymbolInstance.setyScale(3);

//			T3dAttrSymbolInstance scaledColoredSymbol = symbolColorMapper
//					.transform(scaledSymbol, attrValuePair);

			attrSymbols.set(i, t3dAttrSymbolInstance);
		}

		VsCartographicSymbolsOnASphereScene scene = new VsCartographicSymbolsOnASphereScene(
				outputFile);
		
//		VsCartographicSymbolsScene scene = new VsCartographicSymbolsScene(
//				outputFile);

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

}
