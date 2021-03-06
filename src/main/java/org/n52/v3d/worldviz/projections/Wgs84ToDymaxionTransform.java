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
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.v3d.worldviz.projections;

import java.util.ArrayList;
import java.util.List;

import org.n52.v3d.worldviz.projections.dymaxion.Point2D;
import org.n52.v3d.worldviz.projections.dymaxion.PointLatLon;
import org.n52.v3d.worldviz.projections.dymaxion.Transformator;
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

/**
 * Offers static methods to transform Triturus-geometries from WGS84 to
 * Buckminster-Fuller's Dymaxion-projection.<br />
 * Note: Expects Triturus-geometries to have coordinates in the coordinate
 * reference system WGS84 (latitude, longitude)!
 * 
 * @author Christian Danowski
 * 
 */
public class Wgs84ToDymaxionTransform {

	public static VgMultiPolygon transformToBuckmFuller(
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
			buckmFullerMultiPolygon
					.addPolygon(transformToBuckmFuller(vgPolygon));
		}

		return buckmFullerMultiPolygon;
	}

	public static VgPolygon transformToBuckmFuller(VgPolygon polygonWGS84)
			throws Exception {
		// if (!polygonWGS84.getSRS()
		// .equalsIgnoreCase(VgGeomObject.SRSLatLonWgs84)) {
		// throw new T3dException(
		// "The geo reference system of the polygon is not 'EPSG:4326' (WGS84)!");
		// }

		VgLinearRing outerBoundaryUntransformed = polygonWGS84
				.getOuterBoundary();

		VgLinearRing outerBoundaryTransformed = transformToBuckmFuller(outerBoundaryUntransformed);

		// process inner holes
		int numberOfHoles = polygonWGS84.getNumberOfHoles();
		List<VgLinearRing> holesTransformed = new ArrayList<VgLinearRing>(
				numberOfHoles);

		for (int i = 0; i < numberOfHoles; i++) {
			holesTransformed.add(transformToBuckmFuller((polygonWGS84
					.getHole(i))));
		}

		GmPolygon buckmFullerPolygon = new GmPolygon(outerBoundaryTransformed,
				holesTransformed);

		return buckmFullerPolygon;
	}

	public static VgLinearRing transformToBuckmFuller(VgLinearRing linearRing)
			throws T3dException, Exception {

		int numberOfVertices = linearRing.getNumberOfVertices();

		GmLinearRing linearRingTransformed = new GmLinearRing();

		for (int i = 0; i < numberOfVertices; i++) {
			linearRingTransformed.addVertex(transformToBuckmFuller(linearRing
					.getVertex(i)));
		}

		return linearRing;
	}

	public static VgPoint transformToBuckmFuller(VgPoint pointWGS84)
			throws Exception {

		String srs = pointWGS84.getSRS();
		PointLatLon pointLatLon = new PointLatLon(pointWGS84.getY(),
				pointWGS84.getX());
		Transformator t = new Transformator();
		Point2D point2D = t.s2p(pointLatLon);

		GmPoint newPointBuckmFuller = new GmPoint(point2D.x, point2D.y, 0);

		newPointBuckmFuller.setSRS(srs);

		return newPointBuckmFuller;
	}

}
