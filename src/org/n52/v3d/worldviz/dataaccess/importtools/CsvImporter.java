package org.n52.v3d.worldviz.dataaccess.importtools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import noNamespace.DatasetDocument;
import noNamespace.DatasetDocument.Dataset.Entries;
import noNamespace.DatasetDocument.Dataset.Entries.Entry;

import org.apache.xmlbeans.XmlException;
import org.n52.v3d.worldviz.helper.FileHelper;

import com.csvreader.CsvReader;

/**
 * Implementation of the interface 'Importer'. It uses any CSV-file that
 * contains all relevant data for the specific XML-file.
 * 
 * @see Importer
 * @author Christian Danowski
 * 
 */
public class CsvImporter implements Importer {

	private String xmlFileLocation;
	private String csvFileLocation;
	private String[] csvHeaders;
	private Character csvSeperator;
	/**
	 * If the csvFile uses some sort of NULL-value (e.g. "NULL", "null") to
	 * indicate non existing values, then this value shall be stored here.
	 */
	private String nullValue;
	/**
	 * The zero-value shall be used to replace the NULL-value.
	 */
	private String noDataValue;
	private int numberOfInitLinesToSkip;

	/**
	 * Constructor with all important parameters.
	 * 
	 * @param xmlFileLocation
	 *            the file location of the existing xml file that is valid for
	 *            the 'Dataset.xsd'
	 * @param csvFileLocation
	 *            the file location of the csv file from which the values shall
	 *            be extracted and written to the xml file
	 * @param csvSeperator
	 *            the column separator of the csv file
	 * @param csvHeaders
	 *            the column headers for <b>ALL</b> columns of the csv file that
	 *            shall be extracted. These headers must be in corresponding
	 *            order to the properties of the 'TableStructure'-element of the
	 *            xml file
	 * @param nullValue
	 *            if the csvFile uses some sort of NULL-value (e.g. "NULL",
	 *            "null") to indicate non existing values, then this value shall
	 *            be stored here
	 * 
	 * @param noDataValue
	 *            the no-data-value shall be used to replace the nullValue
	 * @param numberOfInitLinesToSkip
	 *            if the csv file contains some initial lines that represent no
	 *            important information; e.g. if the file starts with 4 lines of
	 *            metadata, then these 4 lines must be skipped
	 */
	public CsvImporter(String xmlFileLocation, String csvFileLocation,
			Character csvSeperator, String[] csvHeaders, String nullValue,
			String noDataValue, int numberOfInitLinesToSkip) {
		super();
		this.xmlFileLocation = xmlFileLocation;
		this.csvFileLocation = csvFileLocation;
		this.csvSeperator = csvSeperator;
		this.nullValue = nullValue;
		this.noDataValue = noDataValue;
		this.csvHeaders = csvHeaders;
		this.numberOfInitLinesToSkip = numberOfInitLinesToSkip;
	}

	protected String getZeroValue() {
		return noDataValue;
	}

	protected int getNumberOfInitLinesToSkip() {
		return numberOfInitLinesToSkip;
	}

	protected String getNullValue() {
		return nullValue;
	}

	protected void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

	protected Character getCsvSeperator() {
		return csvSeperator;
	}

	protected void setCsvSeperator(Character csvSeperator) {
		this.csvSeperator = csvSeperator;
	}

	protected String getXmlFileLocation() {
		return xmlFileLocation;
	}

	protected void setXmlFileLocation(String xmlFileLocation) {
		this.xmlFileLocation = xmlFileLocation;
	}

	protected String getCsvFileLocation() {
		return csvFileLocation;
	}

	protected void setCsvFileLocation(String csvFileLocation) {
		this.csvFileLocation = csvFileLocation;
	}

	protected String[] getCsvHeaders() {
		return csvHeaders;
	}

	protected void setCsvHeaders(String[] csvHeaders) {
		this.csvHeaders = csvHeaders;
	}

	@Override
	public void fillDatasetEntries() throws FileNotFoundException {
		// the files should already exist
		FileHelper.filesExist(new File(this.csvFileLocation), new File(
				this.xmlFileLocation));

		// prepare xmlFile and CsvReader
		File datasetFile = new File(this.xmlFileLocation);
		DatasetDocument dataset = null;
		CsvReader csvReader = null;
		try {

			csvReader = new CsvReader(this.csvFileLocation, this.csvSeperator);

			// skip initial lines, that contain metadata
			for (int i = 0; i < this.numberOfInitLinesToSkip; i++) {
				// note: the method csvReader.skipLine() may not work on any
				// csv-file; thus the method readRecord() is used
				csvReader.readRecord();
			}

			// read the headers of each column of the csv file
			csvReader.readHeaders();

			// create Map from all single entries of the csv-file
			Map<String, DatasetEntry> datasetEntries = readEntries(csvReader);

			// load existing xmlDocument
			dataset = DatasetDocument.Factory.parse(datasetFile);

			// create new list of Entries
			createNewEntries(dataset, datasetEntries);

			// System.out.println(dataset.toString());

			// save changes to datasetFile
			dataset.save(datasetFile);

		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (csvReader != null)
				csvReader.close();

			System.out.println("\nSuccess!");
		}

	}

	/**
	 * Reads the whole csv-file and extracts each single Entry(= each data line)
	 * of the csv-file.
	 * 
	 * @param csvReader
	 *            the csv reader
	 * @return a Map that contains the Entry and it's key-value.
	 * @throws IOException
	 */
	private Map<String, DatasetEntry> readEntries(
			CsvReader csvReader) throws IOException {

		Map<String, DatasetEntry> datasetEntries = new HashMap<String, DatasetEntry>();

		// read each entry of the csv file
		while (csvReader.readRecord()) {
			// TODO further checks, if the actual line is usable (if anything
			// unexpected is read, then the line shall be ignored)

			String[] values = getCsvValues(csvReader);

			if (!values[0].isEmpty()) {
				DatasetEntry datasetEntry = new DatasetEntry(
						this.csvHeaders, values);

				datasetEntries.put(datasetEntry.getKeyAttributeValue(),
						datasetEntry);
			}
		}
		return datasetEntries;
	}

	/**
	 * Sorts the Entries according to their key-value and creates the
	 * XML-Entries-part using xmlBeans.
	 * 
	 * @param dataset
	 *            the xmlDocument for which a new Entries-part will be generated
	 * @param csvEntries
	 *            the map that holds the entries from the csv file
	 * @throws IOException
	 */
	private void createNewEntries(DatasetDocument dataset,
			Map<String, DatasetEntry> csvEntries)
			throws IOException {

		Set<String> keySet = csvEntries.keySet();
		List<String> sortedKeyList = new ArrayList<String>(keySet);

		Collections.sort(sortedKeyList);

		Entries xmlEntries = Entries.Factory.newInstance();

		addNewEntries(xmlEntries, csvEntries, sortedKeyList);

		dataset.getDataset().setEntries(xmlEntries);

	}

	/**
	 * Adds each entry from the csv file to the Entries of the xml file
	 * 
	 * @param entries
	 *            the xml Entries
	 * @param csvEntries
	 *            the entries extracted from the csv file
	 * @param sortedKeyList
	 *            a list that holds the sorted keyValues. Thus the entries will
	 *            be generated in alphabetically sorted order.
	 * @throws IOException
	 */
	private void addNewEntries(Entries entries,
			Map<String, DatasetEntry> csvEntries,
			List<String> sortedKeyList) throws IOException {

		for (String key : sortedKeyList) {
			Entry newEntry = entries.addNewEntry();
			String[] values = csvEntries.get(key).getValues();
			newEntry.setValueArray(values);
		}

	}

	/**
	 * From the current csv line the values corresponding to the csvHeaders will
	 * be extracted. If the value is equal to the specified NULL-value, it will
	 * be replaced by the specified ZERO-value
	 * 
	 * @param csvReader
	 *            the csvReader
	 * @return a String[] that holds only the values of the corresponding
	 *         csvHeaders of the current csv line
	 * @throws IOException
	 */
	private String[] getCsvValues(CsvReader csvReader) throws IOException {

		String[] values = new String[this.csvHeaders.length];

		for (int i = 0; i < values.length; i++) {
			String csvValue = csvReader.get(this.csvHeaders[i]);
			if (csvValue.equals(nullValue))
				csvValue = this.noDataValue;
			values[i] = csvValue;
		}

		return values;
	}

}
