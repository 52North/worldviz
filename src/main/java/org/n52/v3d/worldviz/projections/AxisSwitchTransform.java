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

import org.n52.v3d.triturus.core.T3dNotYetImplException;
import org.n52.v3d.triturus.gisimplm.GmPoint;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgGeomObject;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.worldviz.extensions.GmLinearRing;
import org.n52.v3d.worldviz.extensions.GmMultiPolygon;
import org.n52.v3d.worldviz.extensions.GmPolygon;
import org.n52.v3d.worldviz.extensions.VgLinearRing;
import org.n52.v3d.worldviz.extensions.VgMultiPolygon;
import org.n52.v3d.worldviz.extensions.VgPolygon;

//TODO: Check, if this class should be migrated to the 52N Triturus core package in the future.

/**
 * Transformation that simply switches the coordinate-axes in the following
 * manner:
 * <p />
 * <table border=1>
 * <tr>
 * <th>real-world coordinates</th>
 * <th></th>
 * <th>scene coordinates</th>
 * </tr>
 * <tr align=center>
 * <td>x</td>
 * <td>-&gt;</td>
 * <td>X</td>
 * </tr>
 * <tr align=center>
 * <td>y</td>
 * <td>-&gt;</td>
 * <td>-Z</td>
 * </tr>
 * <tr align=center>
 * <td>z</td>
 * <td>-&gt;</td>
 * <td>Y</td>
 * </tr>
 * </table>
 * <p />
 * Hence, this transformation maps typical real-world coordinate-axes
 * <tt>(x, y, z)</tt> to those "classical" coordinate-systems used in the field
 * of Computer Graphics <tt>(X, Y, Z)</tt>.
 * <p />
 * Note: Mostly, a scaling and translation transformation will be performed,
 * too, e.g. by applying a <tt>NormTransform</tt>.
 * 
 * @author Benno Schmidt
 *
 * @see TranslationTransform
 * @see ScaleTransform
 * @see NormTransform
 */
public class AxisSwitchTransform implements CoordinateTransform {

	public T3dVector transform(VgPoint loc) {
		double x = loc.getX();
		double y = loc.getZ();
		double z = -loc.getY();

		return new T3dVector(x, y, z);
	}

	public T3dVector transform(T3dVector pnt) {
		double x = pnt.getX();
		double y = pnt.getZ();
		double z = -pnt.getY();

		return new T3dVector(x, y, z);
	}

	public VgGeomObject transform(VgGeomObject geometry)
			throws T3dNotYetImplException {

		if (geometry instanceof VgPoint)
			return transformToVgPoint((VgPoint) geometry);

		else if (geometry instanceof VgPolygon)
			return transform((VgPolygon) geometry);
		
		else if (geometry instanceof VgMultiPolygon)
			return transform((VgMultiPolygon) geometry);
		
		else if (geometry instanceof VgLinearRing)
			return transform((VgLinearRing) geometry);

		else
			throw new T3dNotYetImplException("");

	}

	public VgMultiPolygon transform(VgMultiPolygon multiPolygon) {
		int numberOfGeometries = multiPolygon.getNumberOfGeometries();

		List<VgPolygon> polygons_transformed = new ArrayList<VgPolygon>(
				numberOfGeometries);

		for (int i = 0; i < numberOfGeometries; i++) {
			VgPolygon polygon_old = (VgPolygon) multiPolygon.getGeometry(i);

			VgPolygon polygon_transformed = this.transform(polygon_old);
			polygons_transformed.add(polygon_transformed);
		}

		return new GmMultiPolygon(polygons_transformed);
	}

	public VgPolygon transform(VgPolygon polygon) {
		VgLinearRing outerBoundary = polygon.getOuterBoundary();

		VgLinearRing outerBoundary_transformed = transform(outerBoundary);

		// inner holes
		int numberOfHoles = polygon.getNumberOfHoles();
		List<VgLinearRing> holes_transformed = new ArrayList<VgLinearRing>(
				numberOfHoles);

		for (int i = 0; i < numberOfHoles; i++) {
			VgLinearRing hole_old = polygon.getHole(i);

			VgLinearRing hole_transformed = this.transform(hole_old);
			holes_transformed.add(hole_transformed);
		}

		return new GmPolygon(outerBoundary_transformed, holes_transformed);
	}

	public VgLinearRing transform(VgLinearRing linearRing) {
		GmLinearRing linearRing_transformed = new GmLinearRing();

		int numberOfVertices = linearRing.getNumberOfVertices();

		for (int i = 0; i < numberOfVertices; i++) {
			VgPoint oldPoint = linearRing.getVertex(i);

			VgPoint point_transformed = this.transformToVgPoint(oldPoint);
			linearRing_transformed.addVertex(point_transformed);
		}

		return linearRing_transformed;
	}

	private VgPoint transformToVgPoint(VgPoint geometry) {
		T3dVector newVector = this.transform((VgPoint) geometry);

		double x = newVector.getX();
		double y = newVector.getY();
		double z = newVector.getZ();

		return new GmPoint(x, y, z);
	}

	/**
	 * Retransforms a point with scene coordinates to a point with real world
	 * coordinates.
	 * 
	 * <p />
	 * <table border=1>
	 * <tr>
	 * <th>scene coordinates</th>
	 * <th></th>
	 * <th>real-world coordinates</th>
	 * </tr>
	 * <tr align=center>
	 * <td>x</td>
	 * <td>-&gt;</td>
	 * <td>X</td>
	 * </tr>
	 * <tr align=center>
	 * <td>y</td>
	 * <td>-&gt;</td>
	 * <td>Z</td>
	 * </tr>
	 * <tr align=center>
	 * <td>z</td>
	 * <td>-&gt;</td>
	 * <td>-Y</td>
	 * </tr>
	 * </table>
	 * <p />
	 * 
	 * @param loc
	 * @return
	 */
	public T3dVector retransform(VgPoint loc) {
		double x = loc.getX();
		double y = -loc.getZ();
		double z = loc.getY();

		return new T3dVector(x, y, z);
	}

	/**
	 * Retransforms a point with scene coordinates to a point with real world
	 * coordinates.
	 * 
	 * <p />
	 * <table border=1>
	 * <tr>
	 * <th>scene coordinates</th>
	 * <th></th>
	 * <th>real-world coordinates</th>
	 * </tr>
	 * <tr align=center>
	 * <td>x</td>
	 * <td>-&gt;</td>
	 * <td>X</td>
	 * </tr>
	 * <tr align=center>
	 * <td>y</td>
	 * <td>-&gt;</td>
	 * <td>Z</td>
	 * </tr>
	 * <tr align=center>
	 * <td>z</td>
	 * <td>-&gt;</td>
	 * <td>-Y</td>
	 * </tr>
	 * </table>
	 * <p />
	 * 
	 * @param loc
	 * @return
	 */
	public T3dVector retransform(T3dVector pnt) {
		double x = pnt.getX();
		double y = -pnt.getZ();
		double z = pnt.getY();

		return new T3dVector(x, y, z);
	}
}
