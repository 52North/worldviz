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
package org.n52.v3d.worldviz.extensions;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.triturus.vgis.VgGeomObject;
import org.n52.v3d.triturus.vgis.VgGeomObject2d;
import org.n52.v3d.triturus.vgis.VgPoint;

/**
 * Class to manage linearRing geometries (&quot;closed polylines /
 * lineStrings&quot; containing an area).
 * 
 * @author Christian Danowski
 *
 */
public abstract class VgLinearRing extends VgGeomObject2d {

	/**
	 * returns the polygon's i-th vertex. The constraint 0 &lt;= i &lt;
	 * <tt>this.numberOfCorners()</tt> must always hold; otherwise a
	 * <tt>T3dException</tt> will be thrown.
	 * 
	 * @return vertex
	 * @throws T3dException
	 */
	abstract public VgPoint getVertex(int i) throws T3dException;

	/**
	 * returns the number of vertices.
	 * 
	 * @return Number of vertices
	 */
	abstract public int getNumberOfVertices();

	/**
	 * returns the polygon area referring to the assigned coordinate reference
	 * system.<br />
	 * <br />
	 * 
	 * @return Area value
	 * @see VgGeomObject#getSRS
	 */
	public double area() {

		return this.sumUpAreaXY()[0];
	}

	/**
	 * returns the circumference of the polygon referring to the assigned
	 * coordinate reference system.<br />
	 * <br />
	 * 
	 * @return Circumference value
	 * @see VgGeomObject#getSRS
	 */
	public double circumference() {
		return this.sumUpAreaXY()[1];
	}

	private double[] sumUpAreaXY() {
		int N = this.getNumberOfVertices();
		double A = 0., C = 0.;

		if (N >= 3) {
			double dx, dy, dz, sx;
			VgPoint pt1, pt2;

			for (int i = 0; i < N - 1; i++) {
				pt1 = this.getVertex(i);
				pt2 = this.getVertex(i + 1);
				dx = pt2.getX() - pt1.getX();
				dy = pt2.getY() - pt1.getY();
				dz = pt2.getZ() - pt1.getZ();
				sx = pt1.getX() + pt2.getX();
				A += sx * dy;
				C += Math.sqrt(dx * dx + dy * dy);
			}

			pt1 = this.getVertex(N - 1);
			pt2 = this.getVertex(0);
			dx = pt2.getX() - pt1.getX();
			dy = pt2.getY() - pt1.getY();
			sx = pt1.getX() + pt2.getX();
			A += sx * dy;
			C += Math.sqrt(dx * dx + dy * dy);
		} else {
			if (N == 2)
				C = this.getVertex(0).distanceXY(this.getVertex(1));
		}

		double[] ret = new double[2];
		ret[0] = A;
		ret[1] = C;
		return ret;
	}

	public String toString() {
		String str = "[";
		if (this.getNumberOfVertices() > 0) {
			for (int i = 0; i < this.getNumberOfVertices() - 1; i++) {
				str = str + this.getVertex(i).toString() + ", ";
			}
			str = str
					+ this.getVertex(this.getNumberOfVertices() - 1).toString();
		}
		return str + "]";
	}

}
