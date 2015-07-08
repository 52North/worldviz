package org.n52.v3d.worldviz.dataaccess.load.dataset.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.n52.v3d.worldviz.helper.RelativePaths;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to extract the geometry of all MultiPolygon-features from a shape file.
 * Note: As of now (28.05.2014), only the geometry of the shape's features is
 * relevant to the ENE-project. Thus the thematic attributes provided by the
 * SimpleFeatures of the shape-file are not extracted. <br />
 * <br />
 * 
 * @author Christian Danowski
 * 
 */
public abstract class AbstractShapeReader implements ShapeReader {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private List<SimpleFeature> simpleFeatureCollection = null;

	protected String shapeFileLocation;

	public AbstractShapeReader() throws IOException {
		super();
	}

	/**
	 * Gets all simple features (only JTS (Java Topology Suite) MultiPolygons)
	 * from the default shape file that is referenced by the attribute
	 * {@link RelativePaths#COUNTRIES_SHAPE_ESRI_SHAPE}
	 * 
	 * @return a list of simple features
	 * @throws IOException
	 */
	public List<SimpleFeature> getSimpleFeatureCollection() throws IOException {

		if (this.simpleFeatureCollection == null)
			extractJtsSimpleFeaturesFromDefaultShape();

		return simpleFeatureCollection;
	}
	
	/**
	 * This method extracts all simple features from the declared shapeFile as
	 * JTS (Java Topology Suite) SimpleFeatures and assigns them to the variable
	 * simpleFeatureCollection.
	 * 
	 * @param shapeFilePath
	 *            the filePath to the shape file
	 * @throws IOException
	 */
	private void extractJtsSimpleFeaturesFromDefaultShape() throws IOException {

		if (logger.isDebugEnabled())
			logger.debug("Extracting all simple features from shapefile at {}",
					this.shapeFileLocation);
		
		List<SimpleFeature> jtsSimpleFeatures = new ArrayList<SimpleFeature>();

		FeatureCollection<?, ?> featureCollection = null;

		featureCollection = getFeatureCollectionFromShapeFile(shapeFileLocation);

		FeatureIterator<?> iterator = featureCollection.features();

		while (iterator.hasNext()) {
			SimpleFeature feature = (SimpleFeature) iterator.next();

			jtsSimpleFeatures.add(feature);

		}

		simpleFeatureCollection = jtsSimpleFeatures;

		iterator.close();
	}

	private FeatureCollection<?, ?> getFeatureCollectionFromShapeFile(
			String shapeFilePath) throws IOException {

		File file = new File(shapeFilePath);
		if (!file.exists())
			throw new FileNotFoundException(
					"The shape file could not be found! The supposed location was: "
							+ file.getAbsolutePath());

		Map<String, URL> map = createMapFromFilePath(file);
		DataStore dataStore = getDataStore(map);
		String typeName = dataStore.getTypeNames()[0];

		// FeatureSource<SimpleFeatureType, SimpleFeature>
		FeatureSource<?, ?> source = dataStore.getFeatureSource(typeName);

		FeatureCollection<?, ?> collection = source.getFeatures();

		dataStore.dispose();

		return collection;
	}

	private Map<String, URL> createMapFromFilePath(File file)
			throws MalformedURLException {
		Map<String, URL> map = new HashMap<String, URL>();
		map.put("url", file.toURI().toURL());
		return map;
	}

	private DataStore getDataStore(Map<String, URL> map) throws IOException {
		DataStore dataStore = DataStoreFinder.getDataStore(map);
		return dataStore;
	}

	

}
