package org.n52.v3d.worldviz.demoapplications.ene.datageneration;

import java.io.FileNotFoundException;

import org.n52.v3d.worldviz.dataaccess.importtools.CsvImporter;
import org.n52.v3d.worldviz.helper.RelativePaths;

public class FillDataset_NuclearAccidents {

	public static void main(String[] args) {
		String xmlFileLocation = RelativePaths.NUCLEAR_ACCIDENTS_XML;
		String csvFileLocation = RelativePaths.NUCLEAR_ACCIDENTS_CSV;
		Character csvSeperator = ';';
		String[] csvHeaders = new String[] {"ID","Longitude,Latitude", "Bundesstaat", "Jahr", "Verseuchung"};
		String nullValue = "NULL";
		String zeroValue = "NODATA";
		int numberOfInitLinesToSkip = 0;

		CsvImporter fillNuclearAccidents = new CsvImporter(xmlFileLocation,
				csvFileLocation, csvSeperator, csvHeaders, nullValue,
				zeroValue, numberOfInitLinesToSkip);
		try {
			fillNuclearAccidents.fillDatasetEntries();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
