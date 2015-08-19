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

import org.n52.v3d.triturus.core.T3dNotYetImplException;
import org.n52.v3d.triturus.t3dutil.T3dVector;
import org.n52.v3d.triturus.vgis.VgGeomObject;
import org.n52.v3d.triturus.vgis.VgPoint;
import org.n52.v3d.worldviz.extensions.VgLinearRing;
import org.n52.v3d.worldviz.extensions.VgMultiPolygon;
import org.n52.v3d.worldviz.extensions.VgPolygon;

//TODO: Check, if this class should be migrated to the 52N Triturus core package in the future.

/**
 * Rotation transformation.
 * 
 * Note: This method has not been implemented yet!
 */
public class RotationTransform implements CoordinateTransform
{
	// TODO This class has not been implemented yet...
	
	public T3dVector transform(VgPoint loc) 
	{
		throw new T3dNotYetImplException(); 
	}

	public T3dVector transform(T3dVector pnt) 
	{
		throw new T3dNotYetImplException(); 
	}

	@Override
	public VgGeomObject transform(VgGeomObject geom) {
		// TODO Auto-generated method stub
		throw new T3dNotYetImplException();
	}

	@Override
	public VgMultiPolygon transform(VgMultiPolygon multiPolygon) {
		// TODO Auto-generated method stub
		throw new T3dNotYetImplException();
	}

	@Override
	public VgPolygon transform(VgPolygon polygon) {
		// TODO Auto-generated method stub
		throw new T3dNotYetImplException();
	}

	@Override
	public VgLinearRing transform(VgLinearRing linearRing) {
		// TODO Auto-generated method stub
		throw new T3dNotYetImplException();
	}
}
