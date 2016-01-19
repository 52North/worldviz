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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.n52.v3d.worldviz.dataaccess.load.DatasetLoader;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.helper.RelativePaths;
import org.n52.v3d.worldviz.worldscene.VsAbstractWorldScene;
import org.n52.v3d.worldviz.worldscene.helper.CountryBordersLODEnum;
import org.n52.v3d.worldviz.worldscene.mapper.MpXmlDatasetVisualizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbo.fbg.worldviz.WvizConfigDocument;
import de.hsbo.fbg.worldviz.WvizConfigDocument.WvizConfig.GlobeVisualization.WorldCountries.PolygonVisualizer.LevelOfDetail;

/**
 * Simple demonstrator illustrating how to construct a globe-visualization from
 * a configuration file.
 *
 * @author Christian Danowski
 */

public class GlobeVisTest_withConfigFile {

	public String xmlDatasetFile, configurationFilePath;
	public String outputFilePath = RelativePaths.TEST_FOLDER;
	public boolean X3DOMMode = false;
	private WvizConfigDocument wVizConfigFile;
	private String attributeNameForMapping;
	private CountryBordersLODEnum countryBorderLOD;
	private String fileName;

	final static Logger logger = LoggerFactory.getLogger(GlobeVisTest_withConfigFile.class);

	public void setConfig(String xmlDatasetFile, String configurationFile, String attributeName) {
		this.configurationFilePath = configurationFile;
		this.attributeNameForMapping = attributeName;
		this.xmlDatasetFile = xmlDatasetFile;
		Path path = Paths.get(xmlDatasetFile);
		String fileName_withEnding = path.getFileName().toString();
		this.fileName = fileName_withEnding.split("\\.")[0];
	}

	public void setOutputFile(String outputFile) {
		this.outputFilePath = outputFile;
	}

	public String getOutputFile() {
		return this.outputFilePath;
	}

	public void run() {
		try {
			parseConfigFile();

			XmlDataset countrydataset = this.parseXmlDataset();

			this.generateX3dScenes(countrydataset, attributeNameForMapping);

			logger.info("Result written to file! " + outputFilePath);
		} catch (Exception ex) {
			logger.error(xmlDatasetFile);
			ex.printStackTrace();
		}

	}

	private void parseConfigFile() throws XmlException, IOException {
		File configFilePath = new File(configurationFilePath);

		this.wVizConfigFile = WvizConfigDocument.Factory.parse(configFilePath);

	}

	private XmlDataset parseXmlDataset() {
		LevelOfDetail levelOfDetail = this.wVizConfigFile.getWvizConfig().getGlobeVisualization().getWorldCountries()
				.getPolygonVisualizer().getLevelOfDetail();

		String lod = levelOfDetail.getLod();

		this.countryBorderLOD = CountryBordersLODEnum.SIMPLIFIED_50m;

		if (lod.equalsIgnoreCase(CountryBordersLODEnum.DETAILED.toString()))
			countryBorderLOD = CountryBordersLODEnum.DETAILED;

		else if (lod.equalsIgnoreCase(CountryBordersLODEnum.SIMPLIFIED_50m.toString()))
			countryBorderLOD = CountryBordersLODEnum.SIMPLIFIED_50m;

		else
			countryBorderLOD = CountryBordersLODEnum.SIMPLIFIED_110m;

		DatasetLoader datasetLoader = new DatasetLoader(xmlDatasetFile, countryBorderLOD);

		XmlDataset globeVisDataset = datasetLoader.loadDataset();

		return globeVisDataset;
	}

	private void generateX3dScenes(XmlDataset countrydataset, String attributeNameForMapping) {

		MpXmlDatasetVisualizer datasetMapper = new MpXmlDatasetVisualizer(wVizConfigFile, attributeNameForMapping);

		datasetMapper.setStyle(wVizConfigFile);
		datasetMapper.setCountryBorderLOD(this.countryBorderLOD);

		/*
		 * The returned list of scenes has the following order of elements:
		 * 
		 * 1. (optional) deformed globe
		 * 
		 * 2. Basic (flat) globe
		 * 
		 * 3. either countries scene or point symbols scene (which acts as an
		 * overlay on top of the base globe)
		 * 
		 * 4. joined scene that combines both prior scenes to a single scene
		 * 
		 * 
		 * 
		 */
		List<VsAbstractWorldScene> worldScenes = datasetMapper.transformToMultipleScenes(countrydataset,
				this.outputFilePath, this.fileName);

		int size = worldScenes.size();
		if (size != 3 && size != 4) {
			logger.error(
					"The size of the list with world scenes for a thematic dataset should be equal to 3 or 4. It is: {}",
					size);
		}

		for (VsAbstractWorldScene worldScene : worldScenes) {
			worldScene.generateScene();
		}

	}
}
