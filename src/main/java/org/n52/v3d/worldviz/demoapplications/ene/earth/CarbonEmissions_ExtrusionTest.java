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
package org.n52.v3d.worldviz.demoapplications.ene.earth;

import java.util.List;

import org.n52.v3d.worldviz.dataaccess.load.DatasetLoader;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.helper.RelativePaths;
import org.n52.v3d.worldviz.extensions.mappers.MpValue2ColoredAttrFeature;
import org.n52.v3d.worldviz.extensions.mappers.MpValue2ExtrudedAttrFeature;
import org.n52.v3d.worldviz.projections.AxisSwitchTransform;
import org.n52.v3d.worldviz.worldscene.VsWorldCountriesOnASphereScene;
import org.n52.v3d.worldviz.worldscene.VsWorldCountriesScene;
import org.n52.v3d.worldviz.worldscene.helper.CountryBordersLODEnum;
import org.n52.v3d.worldviz.worldscene.helper.FindExtrudeAndColorMissingCountriesHelper;
import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.t3dutil.T3dColor;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.n52.v3d.triturus.vgis.VgGeomObject;

public class CarbonEmissions_ExtrusionTest {

	private static String attributeName = "2010";
	private static String dataXML = RelativePaths.CARBON_EMISSIONS_XML;
	private static String outputFile = "test/CarbonEmissions2010_extruded_sphere_test.x3d";
	private static String NODATA_VALUE = "NODATA";
	private static double value_0;
	private static double value_100;
	private static double value_25;
	private static double value_50;
	private static double value_75;
	private static double average;

	public static void main(String[] args) {

		long startTime = System.currentTimeMillis();

		XmlDataset carbonEmissions = null;

		CountryBordersLODEnum worldBordersLOD = CountryBordersLODEnum.SIMPLIFIED_110m;

		DatasetLoader countryCodeTest = new DatasetLoader(dataXML,
				worldBordersLOD);

		countryCodeTest.setCountryBordersLOD(worldBordersLOD);

		try {
			carbonEmissions = countryCodeTest.loadDataset();
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<VgAttrFeature> features = carbonEmissions.getFeatures();

		setValuesForAttribute(features);

		MpValue2ColoredAttrFeature colorMapper = new MpValue2ColoredAttrFeature();

		colorMapper.setPalette(new double[] { value_0, value_25, value_50,
				value_75, value_100 }, new T3dColor[] {
				new T3dColor(72 / 255f, 239 / 255f, 0f),
				new T3dColor(175 / 255f, 239 / 255f, 0f),
				new T3dColor(239 / 255f, 239 / 255f, 0f),
				new T3dColor(239 / 255f, 127 / 255f, 0f),
				new T3dColor(239 / 255f, 0f, 0f) }, true);

		List<VgAttrFeature> coloredTransfFeatures = colorMapper.transform(
				attributeName, features);

		// extrusion mapper
		MpValue2ExtrudedAttrFeature extrusionMapper = new MpValue2ExtrudedAttrFeature();

		extrusionMapper.setNeutralExtrusionHeight(0.5);

		extrusionMapper
				.setPalette(new double[] { value_0, value_25, value_50,
						value_75, value_100 },
						new double[] { 0.5, 1, 1.5, 2, 3 }, true);

		List<VgAttrFeature> extrudedAndColoredCountries = extrusionMapper
				.transform(attributeName, coloredTransfFeatures);

		// find and color/extrude missing countries
		FindExtrudeAndColorMissingCountriesHelper remainingCountriesColorerAndExtruder = new FindExtrudeAndColorMissingCountriesHelper(
				worldBordersLOD);
		List<VgAttrFeature> allColoredWorldCountries = remainingCountriesColorerAndExtruder
				.findExtrudeAndColorMissingCountries(
						extrudedAndColoredCountries, null, 0.5);

		// VsColoredWorldCountriesScene scene = new
		// VsColoredWorldCountriesScene(
		// outputFile);
		
		AxisSwitchTransform axisSwitch = new AxisSwitchTransform();

		VsWorldCountriesScene scene = new VsWorldCountriesOnASphereScene(
				outputFile);

		// this is for quick test purposes
		((VsWorldCountriesOnASphereScene) scene)
				.setGenerateAdditionalInnerPolygonPoints(false);
		// scene.setxRasterWidth(2.5f);
		// scene.setyRasterWidth(2.5f);

//		 VsWorldCountriesScene scene = new VsWorldCountriesScene(
//		 outputFile);

		for (VgAttrFeature coloredExtrudedFeature : allColoredWorldCountries) {
			
//			VgGeomObject geometry = coloredExtrudedFeature.getGeometry();
//			VgGeomObject geom_transformed = axisSwitch.transform(geometry);
//			
//			((GmAttrFeature) coloredExtrudedFeature).setGeometry(geom_transformed);
			
			scene.addWorldCountry(coloredExtrudedFeature);
		}

		scene.setExtrudeCountries(true);

		scene.setDrawBorders(true);

		scene.generateScene();

		System.out.println("Success!");
		long endTime = System.currentTimeMillis();

		System.out.println("required time: " + (endTime - startTime) / 1000
				+ "s");

		System.out.println("Average: " + average);
		System.out.println("0%_Value: " + value_0);
		System.out.println("25%_Value: " + value_25);
		System.out.println("50%_Value: " + value_50);
		System.out.println("75%_Value: " + value_75);
		System.out.println("100%_Value: " + value_100);
	}

	private static void setValuesForAttribute(List<VgAttrFeature> features) {
		double locMin = -1;
		double locMax = -1;
		double sum = 0;
		int feature_size = 0;

		for (VgAttrFeature vgAttrFeature : features) {

			String attributeValue_string = (String) vgAttrFeature
					.getAttributeValue(attributeName);

			if (attributeValue_string.equals(NODATA_VALUE))
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

		// average solution

		value_100 = locMax;
		value_0 = locMin;

		average = sum / feature_size;

		value_25 = locMax * 0.25;
		value_50 = locMax * 0.50;
		value_75 = locMax * 0.75;

	}

}
