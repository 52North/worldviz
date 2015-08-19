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
package org.n52.v3d.worldviz.dataaccess.load.dataset;

import org.n52.v3d.worldviz.dataaccess.load.dataset.helper.BoundingBoxExtractor;
import noNamespace.DatasetDocument;
import noNamespace.DatasetDocument.Dataset.TableStructure.Property;

import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.n52.v3d.triturus.vgis.VgPolygon;

/**
 * Specialization of XmlDataset that is used for shape-datasets (dataset that
 * represent a shape-file that stores external geometries). Such datasets
 * contain a georeference that represents the bounding box of the shape file.</br>
 * <b>Note:</b> the feature's geometry is just the BBOX of all geometries of the shape-file.
 * 
 * @author Christian Danowski
 * 
 */
public class XmlEsriShapeDataset extends AbstractXmlDataset {

	public XmlEsriShapeDataset(DatasetDocument doc)
			throws Exception {
		super(doc);
	}

	@Override
	protected void setGeometry(VgAttrFeature newFeature, Property property,
			String entryValue) throws Exception {

		if (logger.isDebugEnabled())
			logger.debug(
					"Creating a polygon-geometry (BBOX) from the feature property '{}' with value '{}'.",
					property.getTitle().getStringValue(), entryValue);
		
		String crs = property.getGeoReference().getCRS().toString();

		VgPolygon polygon = BoundingBoxExtractor.createPolygonFromBoundingBox(
				entryValue, crs);

		((GmAttrFeature) newFeature).setGeometry(polygon);

	}

}
