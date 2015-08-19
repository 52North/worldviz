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

import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.gisimplm.GmPolygon;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.triturus.vgis.VgPolygon;

/**
 * This helper-class provides only one static method to extract a Bounding Box
 * out of a XmlDataset.
 * 
 * @author Christian Danowski
 * 
 */
public class BoundingBoxExtractor {

	/**
	 * Extracts the BBOX information from the XmlDataset and creates a polygon.
	 * 
	 * @param entryValue
	 *            the string that holds coordinates like: "-180,-90,180,90"
	 *            (lower left corner, upper right corner)
	 * @param crs
	 *            the geodetic coordinate reference system
	 * @return a VgPolygon that represents the bounding box of an XmlDataset.
	 * @throws Exception 
	 */
	public static VgPolygon createPolygonFromBoundingBox(String entryValue,
			String crs) throws Exception {
		VgPolygon polygon = new GmPolygon();
		polygon.setSRS(crs);

		// the entryValue looks like this: -180,-90,180,90
		String[] coords = entryValue.split(",");
		if (coords.length != 4) {
			throw new Exception(
					"The string '"
							+ entryValue
							+ "' ist not a comma-separated list of BBOX coordinates and thus cannot be interpreted!");
		}
		// coords[0] = leftX
		// coords[1] = lowerY
		// coords[2] = rightX
		// coords[3] = upperY

		VgPoint lowerLeft = new GmPoint(Double.valueOf(coords[0]),
				Double.valueOf(coords[1]), 0);
		lowerLeft.setSRS(crs);
		VgPoint upperLeft = new GmPoint(Double.valueOf(coords[0]),
				Double.valueOf(coords[3]), 0);
		upperLeft.setSRS(crs);
		VgPoint upperRight = new GmPoint(Double.valueOf(coords[2]),
				Double.valueOf(coords[3]), 0);
		upperRight.setSRS(crs);
		VgPoint lowerRight = new GmPoint(Double.valueOf(coords[2]),
				Double.valueOf(coords[1]), 0);
		lowerRight.setSRS(crs);

		((GmPolygon) polygon).addVertex(lowerLeft);
		((GmPolygon) polygon).addVertex(upperLeft);
		((GmPolygon) polygon).addVertex(upperRight);
		((GmPolygon) polygon).addVertex(lowerRight);
		((GmPolygon) polygon).addVertex(lowerLeft);

		return polygon;
	}

}
