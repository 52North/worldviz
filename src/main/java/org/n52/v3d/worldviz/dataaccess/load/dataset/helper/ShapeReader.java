package org.n52.v3d.worldviz.dataaccess.load.dataset.helper;

import java.io.IOException;
import java.util.List;

import org.n52.v3d.worldviz.helper.RelativePaths;

import org.opengis.feature.simple.SimpleFeature;

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
public interface ShapeReader {

	/**
	 * Gets all simple features (only JTS (Java Topology Suite) MultiPolygons)
	 * from the default shape file that is referenced by the attribute
	 * {@link RelativePaths#COUNTRIES_SHAPE_ESRI_SHAPE}
	 * 
	 * @return a list of simple features
	 * @throws IOException 
	 */
	public List<SimpleFeature> getSimpleFeatureCollection() throws IOException;
	
}
