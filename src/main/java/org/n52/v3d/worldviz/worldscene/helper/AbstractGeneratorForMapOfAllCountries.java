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
package org.n52.v3d.worldviz.worldscene.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.n52.v3d.worldviz.dataaccess.load.DatasetLoader;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.helper.RelativePaths;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Use this class to generate a map of all world countries. Key =
 * iso3166-alpha2-code; Value = country as VgAttrFeature
 * 
 * @author Christian Danowski
 * 
 */
public abstract class AbstractGeneratorForMapOfAllCountries implements
		GeneratorForMapOfAllCountries {

	private Logger logger = LoggerFactory.getLogger(getClass());

	protected String pathToEneDatasetWithAllWorldCountries = RelativePaths.COUNTRY_CODES_XML;
	private String attributeName_iso3166_alpha2_code = "ISO 3166-2 code";

	public AbstractGeneratorForMapOfAllCountries() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Use this method to generate a map of all world countries. Key =
	 * iso3166-alpha2-code; Value = country as VgAttrFeature
	 * 
	 * @param worldBordersLOD
	 * 
	 * @return a map of all world's countries
	 */
	public Map<String, VgAttrFeature> generateAllCountriesMap() {

		XmlDataset countryCodes = null;

		DatasetLoader countryCodesLoader = setupDatasetLoader();

		if (logger.isDebugEnabled())
			logger.debug(
					"Generating a map of all world's countries from dataset {} with level of detail {}",
					this.pathToEneDatasetWithAllWorldCountries,
					countryCodesLoader.getCountryBordersLOD());

		try {
			countryCodes = countryCodesLoader.loadDataset();
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<VgAttrFeature> features = countryCodes.getFeatures();

		Map<String, VgAttrFeature> countriesMap = new HashMap<String, VgAttrFeature>(
				features.size());

		for (VgAttrFeature vgAttrFeature : features) {
			countriesMap.put((String) vgAttrFeature
					.getAttributeValue(attributeName_iso3166_alpha2_code),
					vgAttrFeature);
		}

		return countriesMap;

	}

	protected abstract DatasetLoader setupDatasetLoader();

}
