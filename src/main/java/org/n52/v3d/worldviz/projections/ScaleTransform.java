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

import org.n52.v3d.triturus.core.T3dNotYetImplException;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgGeomObject;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.worldviz.extensions.VgLinearRing;
import org.n52.v3d.worldviz.extensions.VgMultiPolygon;
import org.n52.v3d.worldviz.extensions.VgPolygon;

// TODO: This class could be migrated to the 52N Triturus core package.

/**
 * Scaling transformation.
 * 
 * @author Benno Schmidt
 */
public class ScaleTransform implements CoordinateTransform
{
	private T3dVector mScale;

	/**
	 * Constructor. This constructor allows to define non-uniform scalings.
	 * 
	 * @param scale Scaling vector
	 */
	public ScaleTransform(T3dVector scale) 
	{
		mScale = scale;
	}

	/**
	 * Constructor. For <tt>sx != sy</tt> and <tt>sx != sz</tt>, a non-uniform 
	 * scaling will be defined.
	 * 
	 * @param scale Scaling vector
	 */
	public ScaleTransform(double sx, double sy, double sz) 
	{
		mScale = new T3dVector(sx, sy, sz);
	}

	/**
	 * Constructor for uniform scalings.
	 * 
	 * @param scale Scaling factor
	 */
	public ScaleTransform(double factor) 
	{
		mScale = new T3dVector(factor, factor, factor);
	}
	
	public T3dVector transform(VgPoint loc) 
	{
		return new T3dVector(
			loc.getX() * mScale.getX(),
			loc.getY() * mScale.getY(), 
			loc.getZ() * mScale.getZ());
	}

	public T3dVector transform(T3dVector pnt) 
	{
		return new T3dVector(
				pnt.getX() * mScale.getX(),
				pnt.getY() * mScale.getY(), 
				pnt.getZ() * mScale.getZ());
	}

	public VgGeomObject transform(VgGeomObject geom) {
		// TODO Auto-generated method stub
		throw new T3dNotYetImplException();
	}

	public VgMultiPolygon transform(VgMultiPolygon multiPolygon) {
		// TODO Auto-generated method stub
		throw new T3dNotYetImplException();
	}

	public VgPolygon transform(VgPolygon polygon) {
		// TODO Auto-generated method stub
		throw new T3dNotYetImplException();
	}

	public VgLinearRing transform(VgLinearRing linearRing) {
		// TODO Auto-generated method stub
		throw new T3dNotYetImplException();
	}
}
