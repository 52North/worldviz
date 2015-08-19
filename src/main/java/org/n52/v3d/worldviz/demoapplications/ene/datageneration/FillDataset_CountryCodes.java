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
package org.n52.v3d.worldviz.demoapplications.ene.datageneration;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.n52.v3d.worldviz.dataaccess.importtools.Country;
import org.n52.v3d.worldviz.dataaccess.importtools.CountryIsoCodeMapGenerator;
import org.n52.v3d.worldviz.dataaccess.importtools.CsvImporter;
import org.n52.v3d.worldviz.helper.RelativePaths;
import noNamespace.DatasetDocument;
import noNamespace.DatasetDocument.Dataset.Entries;
import noNamespace.DatasetDocument.Dataset.Entries.Entry;

import org.apache.xmlbeans.XmlException;

/**
 * This class generates the Entries part of the 'CountryCodes.xml'-file.
 * <b>Note:</b> In this case more work had to be done. The csv-file does not
 * contain the german name of the countries. Thus, after transferring all the
 * values from the csv-file the xml-file needs to be complemented with the
 * german names. For this, the Java Locale object is used in order to add the
 * german name to each country.
 * 
 * @author Christian Danowski
 * 
 */
public class FillDataset_CountryCodes {

	public static void main(String[] args) {

		Map<String, Country> countryMap = CountryIsoCodeMapGenerator
				.createCountryMap();

		String xmlFileLocation = RelativePaths.COUNTRY_CODES_XML;
		String csvFileLocation = RelativePaths.COUNTRY_CODES_CSV;
		Character csvSeperator = ';';
		String[] csvHeaders = new String[] { "ISO2", "NAME", "ISO3" };
		String nullValue = "NULL";
		String zeroValue = "NULL";
		int numberOfInitLinesToSkip = 0;

		CsvImporter fillCountryCodes = new CsvImporter(xmlFileLocation,
				csvFileLocation, csvSeperator, csvHeaders, nullValue,
				zeroValue, numberOfInitLinesToSkip);
		try {
			fillCountryCodes.fillDatasetEntries();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DatasetDocument countryCodesDataset;
		try {
			countryCodesDataset = DatasetDocument.Factory.parse(new File(
					xmlFileLocation));

			addGermanName(countryCodesDataset, countryMap);

			System.out.println("SUCCESS!");
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Finished!");

	}

	private static void addGermanName(DatasetDocument countryCodesDataset,
			Map<String, Country> countryMap) throws IOException {

		List<String> listOfFailedCountries = new ArrayList<String>();

		Entries countryCodeEntries = countryCodesDataset.getDataset()
				.getEntries();

		Entry[] entryArray = countryCodeEntries.getEntryArray();

		for (Entry entry : entryArray) {
			String[] valueArray = entry.getValueArray();
			String iso2Code = valueArray[0];

			if (countryMap.containsKey(iso2Code)) {
				Country country = countryMap.get(iso2Code);
				entry.addValue(country.getNameGerman());
			} else
				listOfFailedCountries.add(iso2Code);

		}

		countryCodeEntries.setEntryArray(entryArray);
		countryCodesDataset.save(new File(
				"data\\earth\\CountryCodes_Copy.xml"));

		System.out.println(countryCodesDataset.toString());

		System.out.println("Number of unmatched countries: "
				+ listOfFailedCountries.size());
	}

}
