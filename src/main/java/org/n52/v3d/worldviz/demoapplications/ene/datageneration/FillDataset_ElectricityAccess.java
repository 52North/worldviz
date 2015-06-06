package org.n52.v3d.worldviz.demoapplications.ene.datageneration;

import java.io.FileNotFoundException;

import org.n52.v3d.worldviz.dataaccess.importtools.CsvImporter;
import org.n52.v3d.worldviz.helper.RelativePaths;

public class FillDataset_ElectricityAccess {

	public static void main(String[] args) {
		String xmlFileLocation = RelativePaths.ELECTRICITY_ACCESS_XML;
		String csvFileLocation = RelativePaths.ELECTRICITY_ACCESS_CSV;
		Character csvSeperator = ';';
		String[] csvHeaders = new String[] {
				"ISO 3166-2 code",
				"MAP DATA Access to Electricity 2000, millions of people estimated",
				"Access to Electricity 2000, percentage of people estimated" };
		String nullValue = "NULL";
		String zeroValue = "NODATA";
		int numberOfInitLinesToSkip = 0;

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
