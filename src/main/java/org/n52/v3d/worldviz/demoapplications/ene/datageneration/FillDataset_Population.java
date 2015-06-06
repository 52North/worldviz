package org.n52.v3d.worldviz.demoapplications.ene.datageneration;
import java.io.FileNotFoundException;

import org.n52.v3d.worldviz.dataaccess.importtools.CsvImporter;
import org.n52.v3d.worldviz.helper.RelativePaths;

/**
 * This class generates the Entries part of the 'Population.xml'-file.
 * @author Christian Danowski
 *
 */
public class FillDataset_Population {

	public static void main(String[] args) {

		String xmlFileLocation = RelativePaths.POPULATION_XML;
		String csvFileLocation = RelativePaths.POPULATION_CSV;
		Character csvSeperator = ',';
		String[] csvHeaders = new String[] { "ISO 2 Code", "2000", "2001",
				"2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009",
				"2010" };
		String nullValue = "NULL";
		String zeroValue = "NODATA";
		int numberOfInitLinesToSkip = 4;

		CsvImporter fillPopulation = new CsvImporter(
				xmlFileLocation, csvFileLocation, csvSeperator, csvHeaders,
				nullValue, zeroValue, numberOfInitLinesToSkip);
		try {
			fillPopulation.fillDatasetEntries();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
