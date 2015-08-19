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

import java.util.List;

import org.n52.v3d.worldviz.dataaccess.load.dataset.helper.Unit;

import org.n52.v3d.triturus.vgis.VgAttrFeature;

/**
 * This interface is used for the java-representation of ENE-Datasets.
 * 
 * @author Christian Danowski
 * 
 */
public interface XmlDataset {

	/**
	 * Each ENE-dataset contains at least one title.
	 * 
	 * @return all titles
	 */
	public String[] getTitles();

	/**
	 * Each ENE-dataset contains at least one description of it's content.
	 * 
	 * @return all descriptions
	 */
	public String[] getDescriptions();

	/**
	 * The unit-element consists of a <i>title</i> and a <i>code</i> to specify
	 * some unit (e.g. gigagram per year).
	 * 
	 * @return the unit
	 */
	public Unit getUnit();

	/**
	 * The PublicReference-element may store information about a public person
	 * or organization from which the source data of the ENE-XML-file was
	 * gathered.
	 * 
	 * @return a reference to the data source
	 */
	public String getPublicReference();

	/**
	 * The MetadataReference-element stores a reference to the metadata file of
	 * the dataset. Note: It may be empty, if no public reference has been
	 * declared in the dataset!
	 * 
	 * @return a reference to the metadata file of the dataset
	 */
	public String getMetadataReference();

	/**
	 * Each Entry-element of the XML-dataset is interpreted as a feature with a
	 * georeferenced geometry and attributes. So each property becomes an
	 * attribute of the feature; exception: the property that contains a
	 * GeoReference-element is used for the geometry of the feature.
	 * 
	 * @return a list of all features (one feature per Entry)
	 */
	public List<VgAttrFeature> getFeatures();

}
