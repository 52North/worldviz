/**
 * Copyright (C) 2015-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *  - Apache License, version 2.0
 *  - Apache Software License, version 1.0
 *  - GNU Lesser General Public License, version 3
 *  - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *  - Common Development and Distribution License (CDDL), version 1.0.
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * icense version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.v3d.worldviz.dataaccess.load.dataset.helper;

import java.util.Iterator;
import java.util.List;

import org.n52.v3d.worldviz.exception.ShapeGeometryNotFoundException;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.simple.SimpleFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

/**
 * Provides static methods to work with JTS (Java Topology Suite) geometry
 * 
 * @author Christian Danowski
 * 
 */
public class JTSHelper {

	private static Logger logger = LoggerFactory.getLogger(JTSHelper.class);

	/**
	 * This methods iterates through all <code>SimpleFeature</code> objects to
	 * find one specific feature that has a certain attribute-value-combination. <br />
	 * Note: The algorithm expects that a <b>certain attribute-value-combination
	 * is unique for all features</b>. Thus it returns the first multipolygonal
	 * geometry of the first feature that matches the
	 * attribute-value-combination.
	 * 
	 * @param attributeName
	 *            the name of the feature's attribute
	 * @param attributeValue
	 *            the value of the feature's attribute
	 * @param jtsSimpleFeatures
	 *            a collection of JTS <code>SimpleFeature</code> objects (only
	 *            <code>MultiPolygon</code> features are processed)
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
			if (logger.isWarnEnabled())
				logger.warn(
						"No geometry could be determined for the 'attribute-value'-combination '{} - {}'!",
						attributeName, attributeValue);
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
