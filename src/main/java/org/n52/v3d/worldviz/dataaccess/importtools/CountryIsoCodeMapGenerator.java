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
package org.n52.v3d.worldviz.dataaccess.importtools;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is meant to produce a java map that maps a {@link Country} to it's
 * ISO 3166 country code.<br/>
 * Each map entry consists of:<br/>
 * 
 * <table border=1>
 * <tr>
 * <td>key:</td>
 * <td>ISO 3166 country code</td>
 * </tr>
 * <tr>
 * <td>value:</td>
 * <td>the corresponding country as {@link Country}-instance</td>
 * </table>
 * 
 * 
 * @author Christian Danowski
 */
public class CountryIsoCodeMapGenerator {

	private static Logger logger = LoggerFactory
			.getLogger(CountryIsoCodeMapGenerator.class);

	/**
	 * Produces a java map that maps a {@link Country} to it's ISO 3166 country
	 * code.<br/>
	 * Each map entry consists of:<br/>
	 * 
	 * <table border=1>
	 * <tr>
	 * <td>key:</td>
	 * <td>ISO 3166 country code</td>
	 * </tr>
	 * <tr>
	 * <td>value:</td>
	 * <td>the corresponding country as {@link Country}-instance</td>
	 * </table>
	 * 
	 * @return
	 */
	public static Map<String, Country> createCountryMap() {
		// ISO3166-ALPHA-2 codes
		String[] countryCodes = Locale.getISOCountries();

		// Key = CountryCode
		// Value = Country
		Map<String, Country> countries = new HashMap<String, Country>(
				countryCodes.length);

		for (String cc : countryCodes) {

			Locale.setDefault(Locale.US);
			String countryNameEnglish = new Locale(Locale.US.getLanguage(), cc)
					.getDisplayCountry();

			Locale.setDefault(Locale.GERMANY);
			String countryNameGerman = new Locale(Locale.GERMANY.getLanguage(),
					cc).getDisplayCountry();

			Country country = new Country(cc.toUpperCase(), countryNameEnglish,
					countryNameGerman);

			countries.put(cc, country);

		}

		if (logger.isInfoEnabled()) {
			logger.info(
					"Created a country-iso3166Code Map<String, Country> with {} entries.",
					countries.size());
		}

		return countries;

	}
}
