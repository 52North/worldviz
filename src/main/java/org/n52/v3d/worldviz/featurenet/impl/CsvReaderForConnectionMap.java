package org.n52.v3d.worldviz.featurenet.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.n52.v3d.triturus.vgis.VgFeature;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.worldviz.dataaccess.load.DatasetLoader;
import org.n52.v3d.worldviz.dataaccess.load.dataset.XmlDataset;
import org.n52.v3d.worldviz.featurenet.VgRelation;
import org.n52.v3d.worldviz.helper.RelativePaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csvreader.CsvReader;

/**
 * This CSV reader is a special implementation for parsing a FeatureNet from a
 * CSV source file with a special structure. The structure looks like:</br></br>
 * 
 * <table border='1'>
 * <tr>
 * <th>CountryCode</th>
 * <th>DE</th>
 * <th>FR</th>
 * <th>GB</th>
 * <th>US</th>
 * </tr>
 * <tr>
 * <td>DE</td>
 * <td>0</td>
 * <td>1</td>
 * <td>0</td>
 * <td>1</td>
 * </tr>
 * <tr>
 * <td>FR</td>
 * <td>1</td>
 * <td>0</td>
 * <td>1</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>GB</td>
 * <td>1</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>US</td>
 * <td>1</td>
 * <td>1</td>
 * <td>1</td>
 * <td>0</td>
 * </tr>
 * </table>
 * 
 * </br> As you might guess, the shown structure resembles a data matrix, which
 * have either 0 or 1 as cell values. The topmost row and the leftmost column
 * represent countries as ISO3166 country codes. Each cell represents a possible
 * connection between the countries (here value '1' marks a connection). A
 * possible weight of the connection might be set via any value != 0, e.g. using
 * values from 1 - 10. </br></br>
 * 
 * For the country(code) in a row this matrix shows it's connection-targets via
 * the columns. For instance Germany's (DE) targets are France (FR) and USA
 * (US).
 * 
 * @author Christian Danowski
 *
 */
public class CsvReaderForConnectionMap {

	private final String zeroValue = "0";

	Logger logger = LoggerFactory.getLogger(CsvReaderForConnectionMap.class);

	private final String countryNameColumnHeader = "CountryCode";

	private final String labelPropertyName = "name";

	private final String pathToCountriesPointFile = RelativePaths.COUNTRIES_POINT_XML;

	/**
	 * this header refers to the source file
	 * worldviz/data/earth/CountriesPoint.xml
	 */
	private final String countriesPointIsoCodeHeader = "ISO 3166-2 code";
	/**
	 * this header refers to the source file
	 * worldviz/data/earth/CountriesPoint.xml
	 */
	private final String countriesPointNameHeader = "Country name";
	/**
	 * this header refers to the source file
	 * worldviz/data/earth/CountriesPoint.xml
	 */
	private final String countriesPointLongitudeHeader = "Longitude";
	/**
	 * this header refers to the source file
	 * worldviz/data/earth/CountriesPoint.xml
	 */
	private final String countriesPointLatitudeHeader = "Latitude";

	private Map<String, VgFeature> countryCodeSceneNodeMap;

	/**
	 * reads a feature net given in CSV format from a file.
	 *
	 * @param csvFilePath
	 *            File path and name
	 * @return Feature-net
	 */
	public WvizUniversalFeatureNet readFromFile(String csvFilePath,
			char csvSeperator) {
		// load dataset with reference point for each country
		loadCountriesPointDataset();

		// empty featureNet, will be filled during next steps
		WvizUniversalFeatureNet featureNet = null;

		// parse csv-file --> load connections for each country of a row
		// (Map<countryCode,List<Connection+Weight>)
		// weight must be taken care of
		// maybe some consistency checks
		try {

			if (logger.isDebugEnabled())
				logger.debug(
						"Trying to parse the CSV-file {} to create a FeatureNet from it.",
						csvFilePath);

			CsvReader csvReader = new CsvReader(csvFilePath, csvSeperator);

			// parse the first row = headers of the matrix
			csvReader.readHeaders();

			int numberOfHeaders = csvReader.getHeaderCount();

			// set up collections for the features and the relations
			Collection<VgFeature> featureNodes = new ArrayList<VgFeature>();
			Collection<VgRelation> relations = new ArrayList<VgRelation>();

			// parse each row
			while (csvReader.readRecord()) {
				// this if the country FROM which any connection starts.
				String fromNodeCountryCode = csvReader
						.get(this.countryNameColumnHeader);

				// add fromFeatureNode to the list of featureNodes
				VgFeature fromFeatureNode = this.countryCodeSceneNodeMap
						.get(fromNodeCountryCode);
				featureNodes.add(fromFeatureNode);

				for (int indexDataColumn = 1; indexDataColumn < numberOfHeaders-1; indexDataColumn++) {
					String nextDataEntry = csvReader.get(indexDataColumn);

					// if nextDataEntry is != 0 then a new connection has been
					// found!
					// thus, if nextDataEntry equals 0, this matrix cell can be
					// skipped
					// note that nextDataEntry is still a String-object!
					if (nextDataEntry.equalsIgnoreCase(this.zeroValue))
						continue;

					else {
						double connectionWeight = Double
								.parseDouble(nextDataEntry);

						// now link that connection weight to the corresponding
						// node!

						String toNodeCountryCode = csvReader
								.getHeader(indexDataColumn);
						VgFeature toNodeFeature = this.countryCodeSceneNodeMap
								.get(toNodeCountryCode);

						// set up relation
						VgRelation fromToRelation = new WvizFlow(
								fromFeatureNode, toNodeFeature,
								connectionWeight);
						relations.add(fromToRelation);

						if (logger.isDebugEnabled())
							logger.debug("New Relation constructed: {}",
									fromToRelation);
					}
				}// end for-loop
			}// end while

			// --> construct FeatureNet
			// 1. nodes (real name) with coordinates from country codes
			// 2. Relations
			featureNet = new WvizUniversalFeatureNet(featureNodes, relations);

		} catch (FileNotFoundException e) {

			if (logger.isErrorEnabled())
				logger.error(
						"An error (FileNotFound-Exception) occured during parsing the CSV-file {}.",
						csvFilePath, e);
		} catch (IOException e) {
			if (logger.isErrorEnabled())
				logger.error(
						"An IO-Exception occured during parsing the CSV-file {}.",
						csvFilePath, e);
		}

		if (featureNet == null) {
			if (logger.isWarnEnabled())
				logger.warn("No FeatureNet could be created! NULL will be returned then.");
		}

		return featureNet;
	}

	private void loadCountriesPointDataset() {

		DatasetLoader countriesPointLoader = new DatasetLoader(
				this.pathToCountriesPointFile);

		try {
			XmlDataset countriesPointDataset = countriesPointLoader
					.loadDataset();

			List<VgAttrFeature> features = countriesPointDataset.getFeatures();

			// transform this list of features into a map with
			// key=ISO3166-countryCode, value=feature
			this.countryCodeSceneNodeMap = createMapFromFeatureList(features);

			if (this.countryCodeSceneNodeMap == null
					|| this.countryCodeSceneNodeMap.size() == 0) {
				if (logger.isWarnEnabled())
					logger.warn(
							"Loading the CountriesPoint-file at {} failed.",
							this.pathToCountriesPointFile);
			}

		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error("Failed to load the dataset {}.",
						this.pathToCountriesPointFile, e);
		}

	}

	private Map<String, VgFeature> createMapFromFeatureList(
			List<VgAttrFeature> features) {

		Map<String, VgFeature> countryCodeSceneNodeMap = new HashMap<String, VgFeature>(
				features.size());

		for (VgAttrFeature feature : features) {
			String isoCountryCode = (String) feature
					.getAttributeValue(countriesPointIsoCodeHeader);
			String countryName = (String) feature
					.getAttributeValue(countriesPointNameHeader);
			double longitude = Double.parseDouble((String) feature
					.getAttributeValue(countriesPointLongitudeHeader));
			double latitude = Double.parseDouble((String) feature
					.getAttributeValue(countriesPointLatitudeHeader));

			VgPoint referencePoint = new GmPoint(longitude, latitude, 0);

			// create VgFeature-object
			VgFeature node = new GmAttrFeature();

			// set the value that shall represent the label of the node
			((GmAttrFeature) node).addAttribute(labelPropertyName, "String",
					countryName);
			((GmAttrFeature) node).setGeometry(referencePoint);
			;

			countryCodeSceneNodeMap.put(isoCountryCode, node);
		}

		return countryCodeSceneNodeMap;
	}

}
