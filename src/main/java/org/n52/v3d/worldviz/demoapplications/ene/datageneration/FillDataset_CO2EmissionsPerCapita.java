package org.n52.v3d.worldviz.demoapplications.ene.datageneration;

import java.io.FileNotFoundException;

import org.n52.v3d.worldviz.dataaccess.importtools.CsvImporter;
import org.n52.v3d.worldviz.helper.RelativePaths;

/**
 * This class generates the Entries part of the
 * 'CarbonEmissionsPerCapita.xml'-file.
 * 
 * @author Christian Danowski
 * 
 */
public class FillDataset_CO2EmissionsPerCapita {

	public static void main(String[] args) {
		String xmlFileLocation = RelativePaths.CARBON_EMISSIONS_PER_CAPITA_XML;
		String csvFileLocation = RelativePaths.CARBON_EMISSIONS_PER_CAPITA_CSV;
		Character csvSeperator = ',';
		String[] csvHeaders = new String[] { "ISO 2 Code", "1960", "1961",
				"1962", "1963", "1964", "1965", "1966", "1967", "1968", "1969",
				"1970", "1971", "1972", "1973", "1974", "1975", "1976", "1977",
				"1978", "1979", "1980", "1981", "1982", "1983", "1984", "1985",
				"1986", "1987", "1988", "1989", "1990", "1991", "1992", "1993",
				"1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001",
				"2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009",
				"2010" };
		String nullValue = "NULL";
		String noDataValue = "NODATA";
		int numberOfInitLinesToSkip = 4;

		CsvImporter fillCO2EmissionsPerCapita = new CsvImporter(
				xmlFileLocation, csvFileLocation, csvSeperator, csvHeaders,
				nullValue, noDataValue, numberOfInitLinesToSkip);
		try {
			fillCO2EmissionsPerCapita.fillDatasetEntries();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
