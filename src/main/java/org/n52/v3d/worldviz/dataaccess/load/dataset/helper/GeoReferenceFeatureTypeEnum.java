package org.n52.v3d.worldviz.dataaccess.load.dataset.helper;

import org.n52.v3d.worldviz.dataaccess.load.DatasetLoader;

/**
 * This enumeration lists all possible feature types of XmlDataset. The type is determined by the <code>FeatureType</code>-attribute of the
	 * <code>GeoReference</code>-tag of a <code>Property</code>-element.
 * @author Christian Danowski
 * @see DatasetLoader
 *
 */
public enum GeoReferenceFeatureTypeEnum {

	Point, EsriShape, CountryCode, Bitmap

}
