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
package org.n52.v3d.worldviz.featurenet.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.n52.v3d.worldviz.featurenet.VgRelation;

import org.n52.v3d.triturus.vgis.VgFeature;

/**
 * Special feature net implementation to hold nets consisting of geo-objects and flows in between.
 * From the mathematical perspective, a <tt>WvizFlowNet</tt> corresponds to an edge-weighted directed graph structure.
 * 
 * @author Benno Schmidt
 */
public class WvizFlowNet extends WvizUniversalFeatureNet {
		
	/**
	 * Constructor.
	 * Note that relations that do not specify flows (undirected, unweighted relations) will be ignored. 
	 * 
	 * @param features Set of geo-objects (graph nodes) 
	 * @param relations Set of relations between geo-objects (directed, weighted graph edges)
	 */
	public WvizFlowNet(Collection<VgFeature> features, Collection<VgRelation> relations) {
		this.features = new ArrayList<VgFeature>();
		for (VgFeature f : features) {
			this.features.add(f);
		}
		this.relations = new ArrayList<VgRelation>();
		for (VgRelation r : relations) {
			if (r instanceof WvizFlow)
				this.relations.add(r);
		}
	}

	/**
	 * Constructor.
	 * Note that relations that do not specify flows (undirected, unweighted relations) will be ignored. 
	 * 
	 * @param features Set of geo-objects (graph nodes) 
	 * @param relations Set of relations between geo-objects (directed, weighted graph edges)
	 */
	public WvizFlowNet(VgFeature[] features, VgRelation[] relations) {
		this.features = new ArrayList<VgFeature>();
		for (VgFeature f : features) {
			this.features.add(f);
		}
		this.relations = new ArrayList<VgRelation>();
		for (VgRelation r : relations) {
			if (r instanceof WvizFlow)
				this.relations.add(r);
		}
	}
}
