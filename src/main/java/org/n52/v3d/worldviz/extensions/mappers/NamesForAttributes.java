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
package org.n52.v3d.worldviz.extensions.mappers;

import org.n52.v3d.triturus.vgis.VgAttrFeature;

/**
 * This class contains String values which represent certain names for
 * attributes that are added to {@link VgAttrFeature}-objects during a mapping
 * step. <br/>
 * <br/>
 * For instance when a color shall be mapped to an {@link VgAttrFeature} -object
 * using the mapper {@link MpValue2ColoredAttrFeature}, then a new attribute
 * must be added to store the color. The name of that attribute is stored in
 * this class.
 * 
 * @author Christian Danowski
 *
 */
public final class NamesForAttributes {

	/**
	 * 
	 */
	public static String attributeNameForColor = "ColorForVisualization";
	public static String attributeNameForExtrusion = "ExtrusionHeightForVisualization";

}
