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
import org.n52.v3d.worldviz.worldscene.VsWorldCountriesOnASphereScene;
import org.n52.v3d.worldviz.worldscene.helper.CountryBordersLODEnum;
import org.n52.v3d.worldviz.worldscene.helper.FindExtrudeAndColorMissingCountriesHelper;
import org.n52.v3d.triturus.t3dutil.T3dColor;
import org.n52.v3d.triturus.vgis.VgAttrFeature;

public class CarbonEmissionsPerCapita_average {

	private static String attributeName = "2010";
	private static String dataXML = RelativePaths.CARBON_EMISSIONS_PER_CAPITA_XML;
	private static String outputFile = "test/CarbonEmissionsPerCapita2010_new.x3d";
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
						new T3dColor(1f, 1f, 0f), new T3dColor(1f, 0f, 0f) },
				true);

		List<VgAttrFeature> coloredTransfFeatures = colorMapper.transform(
				attributeName, features);

		// extrusion mapper
		MpValue2ExtrudedAttrFeature extrusionMapper = new MpValue2ExtrudedAttrFeature();

		extrusionMapper.setNeutralExtrusionHeight(0.5);

		extrusionMapper.setPalette(
				new double[] { minValue, average, maxValue }, new double[] {
						0.5, 1.7, 3 }, true);

		List<VgAttrFeature> extrudedAndColoredCountries = extrusionMapper
				.transform(attributeName, coloredTransfFeatures);

		FindExtrudeAndColorMissingCountriesHelper remainingCountriesColorer = new FindExtrudeAndColorMissingCountriesHelper(
				worldBordersLOD);
		List<VgAttrFeature> allWorldCountries = remainingCountriesColorer
				.findExtrudeAndColorMissingCountries(
						extrudedAndColoredCountries, null, 0.5);

		VsWorldCountriesOnASphereScene scene = new VsWorldCountriesOnASphereScene(
				outputFile);

		for (VgAttrFeature coloredFeature : allWorldCountries) {

			scene.addWorldCountry(coloredFeature);
		}

		// this is for quick test purposes
		// scene.setGenerateAdditionalInnerPolygonPoints(false);

		scene.setExtrudeCountries(true);

		scene.generateScene();

		System.out.println("Success!");
		long endTime = System.currentTimeMillis();

		System.out.println("required time: " + (endTime - startTime) / 1000
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
