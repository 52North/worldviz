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
package org.n52.v3d.worldviz.featurenet;

import java.util.Collection;

import org.n52.v3d.triturus.vgis.VgFeature;

/**
 * The VgFeatureNet interface allows to operate on sets of interrelated geo-objects ("features" in OGC-jargon).
 * 
 * The kind of relations between geo-objects will depend on your application scenario. Prominent examples are:
 * Flows of trade between regions, flight connections between airports, or the relation "same official language",
 * which would connect Australia and the United States.
 * 
 * From a mathematical perspective, a VgFeatureNet describes a graph structure consisting of nodes and edges.
 * It is left to the concrete application, whether a VgFeatureNet implementation describes directed or undirected
 * graphs and whether weights will be given for the edges.
 * 
 * However, VgFeatureNet structures allow to put a strong focus on the relations between spatial features (instead 
 * of deriving the relations from the features' attribute values).
 * 
 * @author Benno Schmidt
 */
abstract public class VgFeatureNet {

	 // TODO A "connection map" would be a visualization of a feature net.

	 /**
	 * Gets the feature net's geo-objects ("features").
	 * 
	 * @return Set of geo-objects (graph nodes)
	 */
	abstract public Collection<VgFeature> getFeatures();
	
	/**
	 * Gets the relations between the geo-objects ("features") that are held in the feature net.
	 * 
	 * @return Set of geo-object relations (graph edges)
	 */
	abstract public Collection<VgRelation> getRelations();
	
	public String toString() 
	{
		int numberOfFeatures = 0;
		if (this.getFeatures() != null)
			numberOfFeatures = this.getFeatures().size();
		int numberOfRelations = 0;
		if (this.getRelations() != null)
			numberOfRelations = this.getRelations().size();
		return "[" +
				"(#" + numberOfFeatures + " features), " +
				"(#" + numberOfRelations + " relations)]";
	}
}
