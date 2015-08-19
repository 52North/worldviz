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
package org.n52.v3d.worldviz.projections;

import java.util.ArrayList;
import java.util.List;

import org.n52.v3d.worldviz.extensions.GmLinearRing;
import org.n52.v3d.worldviz.extensions.GmMultiPolygon;
import org.n52.v3d.worldviz.extensions.GmPolygon;
import org.n52.v3d.worldviz.extensions.VgLinearRing;
import org.n52.v3d.worldviz.extensions.VgMultiPolygon;
import org.n52.v3d.worldviz.extensions.VgPolygon;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.vgis.VgGeomObject;
import org.n52.v3d.triturus.vgis.VgPoint;

public class Wgs84ToPetersTransform {

	public static VgMultiPolygon transformToPeters(
			VgMultiPolygon multiPolygonWGS84) throws Exception {
		if (!multiPolygonWGS84.getSRS().equalsIgnoreCase(
				VgGeomObject.SRSLatLonWgs84)) {
			throw new T3dException(
					"The geo reference system of the polygon is not 'EPSG:4326' (WGS84)!");
		}

		int numberOfGeometries = multiPolygonWGS84.getNumberOfGeometries();

		GmMultiPolygon buckmFullerMultiPolygon = new GmMultiPolygon();

		for (int i = 0; i < numberOfGeometries; i++) {
			VgPolygon vgPolygon = (VgPolygon) multiPolygonWGS84.getGeometry(i);
			buckmFullerMultiPolygon.addPolygon(transformToPeters(vgPolygon));
		}

		return buckmFullerMultiPolygon;
	}

	public static VgPolygon transformToPeters(VgPolygon polygonWGS84)
			throws Exception {
		// if (!polygonWGS84.getSRS()
		// .equalsIgnoreCase(VgGeomObject.SRSLatLonWgs84)) {
		// throw new T3dException(
		// "The geo reference system of the polygon is not 'EPSG:4326' (WGS84)!");
		// }

		VgLinearRing outerBoundaryUntransformed = polygonWGS84
				.getOuterBoundary();

		VgLinearRing outerBoundaryTransformed = transformToPeters(outerBoundaryUntransformed);

		// process inner holes
		int numberOfHoles = polygonWGS84.getNumberOfHoles();
		List<VgLinearRing> holesTransformed = new ArrayList<VgLinearRing>(
				numberOfHoles);

		for (int i = 0; i < numberOfHoles; i++) {
			holesTransformed.add(transformToPeters((polygonWGS84.getHole(i))));
		}

		GmPolygon buckmFullerPolygon = new GmPolygon(outerBoundaryTransformed,
				holesTransformed);

		return buckmFullerPolygon;
	}

	public static VgLinearRing transformToPeters(VgLinearRing linearRing)
			throws T3dException, Exception {

		int numberOfVertices = linearRing.getNumberOfVertices();

		GmLinearRing linearRingTransformed = new GmLinearRing();

		for (int i = 0; i < numberOfVertices; i++) {
			linearRingTransformed.addVertex(transformToPeters(linearRing
					.getVertex(i)));
		}

		return linearRing;
	}

	public static VgPoint transformToPeters(VgPoint pointWGS84)
			throws Exception {

		VgPoint point2D = latLon2GP(pointWGS84);

		GmPoint newPointBuckmFuller = new GmPoint(point2D.getX(),
				point2D.getY(), 0);

		return newPointBuckmFuller;
	}

	private static VgPoint latLon2GP(VgPoint pIn) {
		VgPoint pOut = new GmPoint();
		double mSqrt2 = Math.sqrt(2.0);

		if (!pIn.getSRS().equalsIgnoreCase(VgGeomObject.SRSLatLonWgs84)) {

			// throw new
			// T3dSRSException("Tried to process illegal point coordinate.");

		}

		double lat = pIn.getY();

		double lon = pIn.getX();

		double R = 6371.;

		pOut.setX(R * Math.PI * lon / (180. * mSqrt2));

		pOut.setY(R * mSqrt2 * Math.sin(lat * Math.PI / 180.));

		pOut.setZ(pIn.getZ());

		pOut.setSRS(VgGeomObject.SRSNone); // provisorische Setzung

		return pOut;
	}

}
