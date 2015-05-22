package org.n52.v3d.worldviz.demoapplications.ene.datageneration;

import java.io.FileNotFoundException;

import org.n52.v3d.worldviz.dataaccess.importtools.CsvImporter;
import org.n52.v3d.worldviz.helper.RelativePaths;

public class FillDataset_ElectricityConsumption {

	public static void main(String[] args) {
		String xmlFileLocation = RelativePaths.ELECTRICITY_CONSUMPTION_XML;
		String csvFileLocation = RelativePaths.ELECTRICITY_CONSUMPTION_CSV;
		Character csvSeperator = ';';
		String[] csvHeaders = new String[] { "Kürzel", "2000", "2005", "2006",
				"2007", "2008", "2009" };
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
