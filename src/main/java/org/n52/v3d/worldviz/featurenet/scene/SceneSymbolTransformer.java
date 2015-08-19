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
package org.n52.v3d.worldviz.featurenet.scene;

import org.n52.v3d.triturus.vgis.VgPoint;

/**
 * This interface provides necessary information to place a symbol as a
 * connection between two points. These points represent a from-to-vector
 * relationship. The implementing class computes the necessary rotation (as
 * angles for x-, y- and z-axis) and translation (as mid-point between the two
 * given points) parameters for a connection between those points.<br/>
 * <br/>
 * Please note that any symbol in a Virtual Reality scene description like X3D
 * is directed with respect to the y-axis of the scene coordinate system
 * (meaning that a height-parameter of a symbol like a cylinder will extrude the
 * cylinder in y-direction!).
 * 
 * @author Christian Danowski
 *
 */
public interface SceneSymbolTransformer {

	/**
	 * Returns the length of the vector between the points 'from' and 'to'
	 * 
	 * @return
	 */
	public double getLengthFromTo();

	/**
	 * Returns the rotation of a symbol with respect to the x-axis (pointing to
	 * the right) of the scene-coordinate system.
	 * 
	 * @return
	 */
	public double getAngleX();

	/**
	 * Returns the rotation of a symbol with respect to the y-axis (pointing
	 * upwards; equivalent to the height axis) of the scene-coordinate system.
	 * 
	 * @return
	 */
	public double getAngleY();

	/**
	 * Returns the rotation of a symbol with respect to the z-axis (pointing
	 * towards the user outside of the screen) of the scene-coordinate system.
	 * 
	 * @return
	 */
	public double getAngleZ();

	/**
	 * Returns the mid-point between the points 'from' and 'to' defined in the
	 * constructor.
	 * 
	 * @return
	 */
	public VgPoint getMidPoint();

}
