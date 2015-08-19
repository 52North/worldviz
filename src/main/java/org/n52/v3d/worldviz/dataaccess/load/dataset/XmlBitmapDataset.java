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
package org.n52.v3d.worldviz.dataaccess.load.dataset;

import noNamespace.DatasetDocument;
import noNamespace.DatasetDocument.Dataset.TableStructure.Property;

import org.n52.v3d.triturus.gisimplm.GmAttrFeature;
import org.n52.v3d.triturus.vgis.VgAttrFeature;
import org.n52.v3d.triturus.vgis.VgPolygon;
import org.n52.v3d.worldviz.dataaccess.load.dataset.helper.BoundingBoxExtractor;

/**
 * Specialization of XmlDataset that is used for image-datasets (datasets that
 * represent an image-file that can be used as drape for any object in X3D).
 * Such datasets contain a georeference that represents the bounding box of the
 * image.
 * 
 * @author Christian Danowski
 * 
 */
public class XmlBitmapDataset extends AbstractXmlDataset {

	public XmlBitmapDataset(DatasetDocument doc) throws Exception {
		super(doc);
	}

	@Override
	protected void setGeometry(VgAttrFeature newFeature, Property property,
			String entryValue) throws Exception {

		if (logger.isDebugEnabled())
			logger.debug(
					"Creating a polygon-geometry from the feature property '{}' with value '{}'.",
					property.getTitle().getStringValue(), entryValue);

		// As this concerns images, the geographical bounding box shall be
		// expressed as a polygon.

		String crs = property.getGeoReference().getCRS().toString();

		VgPolygon polygon = BoundingBoxExtractor.createPolygonFromBoundingBox(
				entryValue, crs);

		((GmAttrFeature) newFeature).setGeometry(polygon);

	}

}
