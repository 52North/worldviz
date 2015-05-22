package org.n52.v3d.worldviz.demoapplications.ene.datageneration;

import java.io.FileNotFoundException;

import org.n52.v3d.worldviz.dataaccess.importtools.CsvImporter;
import org.n52.v3d.worldviz.helper.RelativePaths;

public class FillDataset_ElectricityGeneration {

	public static void main(String[] args) {

		String xmlFileLocation = RelativePaths.ELECTRICITY_GENERATION_XML;
		String csvFileLocation = RelativePaths.ELECTRICITY_GENERATION_CSV;
		Character csvSeperator = ';';
		String[] csvHeaders = new String[] { "Kürzel", "1970", "1980", "1990",
				"2000", "2007", "2008", "2009", "2010", "2011", "Anteil in %" };
		String nullValue = "NULL";
		String zeroValue = "NODATA";
		int numberOfInitLinesToSkip = 1;

		CsvImporter fillCO2Emissions = new CsvImporter(xmlFileLocation,
				csvFileLocation, csvSeperator, csvHeaders, nullValue,
				zeroValue, numberOfInitLinesToSkip);
		try {
			fillCO2Emissions.fillDatasetEntries();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
