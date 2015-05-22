package org.n52.v3d.worldviz.demoapplications.ene.datageneration;

import java.io.FileNotFoundException;

import org.n52.v3d.worldviz.dataaccess.importtools.CsvImporter;
import org.n52.v3d.worldviz.helper.RelativePaths;

public class FillDataset_ElectricityProductionFromSpecificRessource {

	public static void main(String[] args) {
		String xmlFileLocation = RelativePaths.ELECTRICITY_PRODUCTION_FROM_SPECIFIC_RESSOURCE_XML;
		String csvFileLocation = RelativePaths.ELECTRICITY_PRODUCTION_FROM_SPECIFIC_RESSOURCE_CSV;
		Character csvSeperator = ';';
		String[] csvHeaders = new String[] { "L�nderk�rzel", "Kohle", "�l",
				"Gas", "Fossil gesamt", "Nuklear", "Wasser", "Geo Thermal",
				"Photovoltaic", "Sonne Thermal", "Wind", "Wasserstr�mung",
				"Erneuerbar gesamt", "Andere(Biomasse+Abfall+andere)", "Gesamt" };
		String nullValue = "-";
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
