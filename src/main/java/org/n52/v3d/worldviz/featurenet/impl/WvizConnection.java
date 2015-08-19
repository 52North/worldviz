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
package org.n52.v3d.worldviz.featurenet.impl;

import org.n52.v3d.worldviz.featurenet.VgRelation;

import org.n52.v3d.triturus.vgis.VgFeature;

/**
 * A <tt>WvizConnection</tt> describes an undirected relation between two geo-objects ("features").
 * Note that such a connection might consist of an edge-weight.
 * TODO: So far, only scalar-valued edge-weights have been implemented.
 * 
 * @author Benno Schmidt
 */
public class WvizConnection implements VgRelation {
	
	private VgFeature from, to;
	private boolean hasWeight = false;
	private double weight;

	/**
	 * Constructor for an unweighted connection.
	 * 
	 * @param from One of the connected geo-objects
	 * @param to The other one of the connected geo-objects
	 */
	public WvizConnection(VgFeature from, VgFeature to) {
		this.from = from;
		this.to = to;
		this.hasWeight = false;
	}

	/**
	 * Constructor for a weighted connection.
	 * 
	 * @param from One of the connected geo-objects
	 * @param to The other one of the connected geo-objects
	 * @param weight Scalar value giving the edge weight
	 */
	public WvizConnection(VgFeature from, VgFeature to, double weight) {
		this.from = from;
		this.to = to;
		this.hasWeight = true;
		this.weight = weight;
	}

	public void setFrom(VgFeature from) {
		this.from = from;
	}

	public void setTo(VgFeature to) {
		this.to = to;
	}

	public VgFeature getFrom() {
		return from;
	}

	public VgFeature getTo() {
		return to;
	}

	public boolean isDirected() {
		return false;
	}
		 
	public Object getValue() {
		if (! this.hasWeight)
			return null;
		else
			return new Double(this.weight);
	}
	
	public String toString() {
		if (! this.hasWeight)
			return "[" + this.from + " <-> " + this.to + "]";
		else
			return "[" + this.from + " <-> " + this.to + ", weight = " + this.weight + "]";				
	}
}
