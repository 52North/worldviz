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
import java.io.FileNotFoundException;

import org.n52.v3d.worldviz.dataaccess.importtools.CsvImporter;
import org.n52.v3d.worldviz.helper.RelativePaths;

/**
 * This class generates the Entries part of the 'CarbonEmissions.xml'-file.
 * 
 * @author Christian Danowski
 * 
 */
public class FillDataset_IndiaStateNames_withoutTelangana {

	public static void main(String[] args) {
		String xmlFileLocation = RelativePaths.INDIA_STATE_NAMES_WITHOUT_TELANGANA_XML;
		String csvFileLocation = RelativePaths.INDIA_STATE_NAMES_WITHOUT_TELANGANA_CSV;
		Character csvSeperator = ',';
		String[] csvHeaders = new String[] { "NAME"};
		String nullValue = "NULL";
		String zeroValue = "NODATA";
		int numberOfInitLinesToSkip = 0;

		CsvImporter fillIndiaStates_withoutTelangana = new CsvImporter(xmlFileLocation,
				csvFileLocation, csvSeperator, csvHeaders, nullValue,
				zeroValue, numberOfInitLinesToSkip);
		try {
			fillIndiaStates_withoutTelangana.fillDatasetEntries();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
