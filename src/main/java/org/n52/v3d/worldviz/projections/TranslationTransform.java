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

//TODO: This class could be migrated to the 52N Triturus core package.

/**
 * Translation transformation ("coordinate offset").
 * 
 * @author Benno Schmidt
 */
public class TranslationTransform implements CoordinateTransform
{
	private T3dVector mOffset;
	
	public TranslationTransform(T3dVector offset) 
	{
		mOffset = offset;
	}
	
	public T3dVector transform(VgPoint loc) 
	{
		return new T3dVector(
			loc.getX() + mOffset.getX(),
			loc.getY() + mOffset.getY(), 
			loc.getZ() + mOffset.getZ());
	}

	public T3dVector transform(T3dVector pnt) 
	{
		return new T3dVector(
				pnt.getX() + mOffset.getX(),
				pnt.getY() + mOffset.getY(), 
				pnt.getZ() + mOffset.getZ());
	}

	public VgGeomObject transform(VgGeomObject geom) {
		throw new T3dNotYetImplException();
	}

	public VgMultiPolygon transform(VgMultiPolygon multiPolygon) {
		throw new T3dNotYetImplException();
	}

	public VgPolygon transform(VgPolygon polygon) {
		throw new T3dNotYetImplException();
	}

	public VgLinearRing transform(VgLinearRing linearRing) {
		throw new T3dNotYetImplException();
	}
}
