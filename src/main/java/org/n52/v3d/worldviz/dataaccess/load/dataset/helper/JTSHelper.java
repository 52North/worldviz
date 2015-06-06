package org.n52.v3d.worldviz.dataaccess.load.dataset.helper;

import java.util.Iterator;
import java.util.List;

import org.n52.v3d.worldviz.exception.ShapeGeometryNotFoundException;

import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

/**
 * Provides static methods to work with JTS (Java Topology Suite) geometry
 * 
 * @author Christian Danowski
 * 
 */
public class JTSHelper {

	/**
	 * This methods iterates through all <code>SimpleFeature</code> objects to find one
	 * specific feature that has a certain attribute-value-combination. <br />
	 * Note: The algorithm expects that a <b>certain attribute-value-combination is
	 * unique for all features</b>. Thus it returns the first multipolygonal
	 * geometry of the first feature that matches the
	 * attribute-value-combination.
	 * 
	 * @param attributeName
	 *            the name of the feature's attribute
	 * @param attributeValue
	 *            the value of the feature's attribute
	 * @param jtsSimpleFeatures
	 *            a collection of JTS <code>SimpleFeature</code> objects (only <code>MultiPolygon</code> features
	 *            are processed)
	 * @return
	 * @throws ShapeGeometryNotFoundException
	 */
	public static MultiPolygon getMultiPolygonForAttribute(
			String attributeName, String attributeValue,
			List<SimpleFeature> jtsSimpleFeatures)
			throws ShapeGeometryNotFoundException {

		MultiPolygon mPolygon = null;

		Iterator<SimpleFeature> sfIterator = jtsSimpleFeatures.iterator();

		while (sfIterator.hasNext()) {

			SimpleFeature nextFeature = (SimpleFeature) sfIterator.next();

			if (nextFeature.getAttribute(attributeName).equals(attributeValue)) {
				
				Geometry geometry = getGeometryFromFeature(nextFeature);

				if (geometry instanceof MultiPolygon) {
					// the geometry for the attribute-value-combination has been
					// found
					mPolygon = (MultiPolygon) geometry;
					break;
				}
			}

		}

		if (mPolygon == null) {
			// no geometry could be found
			
			// only display as an error message 
			// catch geometry=null later (in class XmlCountryCodeDataset.java)!
			System.out.println("WARNING: No geometry could be determined for the 'attribute-value'-combination '"
							+ attributeName + "-" + attributeValue + "'!");
			
			
//			throw new ShapeGeometryNotFoundException(
//					"No geometry could be determined for the 'attribute-value'-combination '"
//							+ attributeName + "-" + attributeValue + "'!");
		}

		return mPolygon;
	}

	private static Geometry getGeometryFromFeature(SimpleFeature nextFeature) {
		GeometryAttribute defaultGeometryProperty = nextFeature
				.getDefaultGeometryProperty();

		// Multipolygon?!?
		Geometry geometry = (Geometry) nextFeature
				.getAttribute(defaultGeometryProperty.getName());
		return geometry;
	}
	
	/*
	 * Further future methods:
	 */
	
	// getPolygonFromMultipolygon?

	// getCoordinates

	// getLinerRings (outer Boundary for linear visualization)

}
